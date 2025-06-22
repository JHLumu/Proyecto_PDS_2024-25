package umu.pds.servicios;

import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import umu.pds.controlador.Piolify;
import umu.pds.modelo.Bloque;
import umu.pds.modelo.Curso;
import umu.pds.modelo.Ejercicio;
import umu.pds.modelo.Estadisticas;
import umu.pds.modelo.SesionAprendizaje;
import umu.pds.modelo.Usuario;
import umu.pds.persistencia.FactoriaDAO;
import umu.pds.persistencia.JPAFactoriaDAO;
import umu.pds.persistencia.SesionAprendizajeDAO;
import umu.pds.persistencia.UsuarioDAO;


/**
 * Clase de apoyo para los controladores, utilizado para la gestión 
 * de estadísticas de los usuarios.
 */
public class ServicioEstadisticas {
	
	/**
	 * Instancia {@link SesionAprendizajeDAO} para la persistencia de estadísticas de un usuario.
	 */
	private final SesionAprendizajeDAO sesionDAO;
	/**
	 * Instancia {@link UsuarioDAO} para la persistencia de un usuario.
	 */
	private final UsuarioDAO usuarioDAO;


	/**
	 * Constructor por defecto que inicializa las instancias de DAOs utilizando la
	 * fábrica de DAOs. Utiliza la implementación JPAFactoriaDAO.
	 */
	public ServicioEstadisticas() {
		FactoriaDAO factoria = FactoriaDAO.getInstancia(JPAFactoriaDAO.class.getName());
		this.sesionDAO = factoria.getSesionAprendizajeDAO();
		this.usuarioDAO = factoria.getUsuarioDAO();
	}
	
	/**
	 * Constructor que permite inyectar dependencias de forma manual, útil para
	 * pruebas unitarias.
	 * 
	 * @param sesionDAO  Instancia {@link SesionAprendizajeDAO} para la persistencia
	 *                   de estadísticas de un usuario.
	 * @param usuarioDAO Instancia {@link UsuarioDAO} para la persistencia de un
	 *                   usuario.
	 */
	public ServicioEstadisticas(SesionAprendizajeDAO sesionDAO, UsuarioDAO usuarioDAO) {
		this.sesionDAO = sesionDAO;
		this.usuarioDAO = usuarioDAO;
	}

	/**
	 * Método que inicia una nueva sesión de aprendizaje para un usuario.
	 * @param usuario Instancia {@link Usuario}, que inicia la sesión.
	 * @param curso Instancia {@link Curso}, del que se iniciará la sesión.
	 * @param ejercicio Instancia {@link Ejercicio}. Primer ejercicio de la sesión.
	 * @return Instancia {@link SesionAprendizaje}.
	 */
	public SesionAprendizaje iniciarSesion(Usuario usuario, Curso curso, Ejercicio ejercicio) {
		SesionAprendizaje sesion = new SesionAprendizaje(usuario, curso, ejercicio);
		sesionDAO.guardarSesion(sesion);
		return sesion;
	}

	/**
	 * Método que finaliza una sesión y actualiza las estadísticas del usuario, persistiendo la actualización.
	 * @param sesion Instancia {@link SesionAprendizaje} a finalizar.
	 */
	public void finalizarSesion(SesionAprendizaje sesion) {
		sesion.finalizarSesion();
		sesionDAO.actualizarSesion(sesion);
		actualizarEstadisticasUsuario(sesion.getUsuario());
	}

	/**
	 * Método que registra un acierto en la sesión actual, actualizando la sesión y persistiéndolo.
	 * @param sesion Instancia {@link SesionAprendizaje} en curso.
	 */
	public void registrarAcierto(SesionAprendizaje sesion) {
		sesion.registrarAcierto();
		sesionDAO.actualizarSesion(sesion);
	}

	/**
	 * Método que registra un fallo en la sesión actual, actualizando la sesión y persistiéndolo.
	 * @param sesion Instancia {@link SesionAprendizaje} en curso.
	 */
	public void registrarFallo(SesionAprendizaje sesion) {
		sesion.registrarFallo();
		sesionDAO.actualizarSesion(sesion);
	}

	/**
	 * Método que actualiza las estadísticas generales de un usuario basándose en todas sus
	 * sesiones de aprendizaje completadas.
	 * @param usuario Instancia {@link Usuario} cuyas estadísticas serán actualizadas.
	 */
	public void actualizarEstadisticasUsuario(Usuario usuario) {
		List<SesionAprendizaje> todasLasSesiones = sesionDAO.buscarSesionesPorUsuario(usuario);

		// Asegurarnos de que el usuario y sus estadísticas estén correctamente enlazados
		Usuario usuarioActualizado = null;
		try {
			usuarioActualizado = usuarioDAO.recuperarUsuario(usuario.getId());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		Estadisticas stats = usuarioActualizado.getEstadisticas();

		// Si no tiene estadísticas, crear nuevas
		if (stats == null) {
			stats = usuarioDAO.recuperarEstadisticas(usuario.getId());
			if (stats == null) {
				stats = new Estadisticas();
				stats.setUsuario(usuarioActualizado);
				usuarioActualizado.setEstadisticas(stats);
			}
		}

		// Calcular estadísticas generales
		int tiempoTotal = todasLasSesiones.stream().filter(SesionAprendizaje::isCompletada)
				.mapToInt(SesionAprendizaje::getTiempoTotal).sum();

		int totalEjercicios = todasLasSesiones.stream().filter(SesionAprendizaje::isCompletada)
				.mapToInt(SesionAprendizaje::getEjerciciosCompletados).sum();

		int totalAciertos = todasLasSesiones.stream().filter(SesionAprendizaje::isCompletada)
				.mapToInt(SesionAprendizaje::getAciertos).sum();

		int totalFallos = todasLasSesiones.stream().filter(SesionAprendizaje::isCompletada)
				.mapToInt(SesionAprendizaje::getFallos).sum();

		// Calcular rachas
		int rachaActual = calcularRachaActual(todasLasSesiones);
		int mejorRacha = calcularMejorRacha(todasLasSesiones);

		// Calcular precisión
		double precision = 0.0;
		if (totalAciertos + totalFallos > 0) {
			precision = (double) totalAciertos / (totalAciertos + totalFallos) * 100.0;
		}

		// Actualizar estadísticas - MANTENER TIEMPO EN SEGUNDOS
		stats.setTiempoTotal(tiempoTotal); // Mantener en segundos para mayor precisión
		stats.setTotalEjerciciosCompletados(totalEjercicios);
		stats.setRachaDias(rachaActual);
		stats.setMejorRacha(mejorRacha);
		stats.setPrecision(precision);

		// Guardar usuario actualizado
		usuarioDAO.modificarUsuario(usuarioActualizado);
		Piolify.getUnicaInstancia().setUsuarioActual(usuarioActualizado);
	}

	/**
	 * Método que calcula la racha actual de días consecutivos con actividad.
	 * @param sesiones Lista de instancias {@link SesionAprendizaje} de un usuario.
	 */
	private int calcularRachaActual(List<SesionAprendizaje> sesiones) {
		if (sesiones.isEmpty())
			return 0;

		// Obtener días únicos con actividad, ordenados descendentemente
		List<LocalDate> diasConActividad = sesiones.stream().filter(SesionAprendizaje::isCompletada)
				.map(s -> s.getFechaInicio().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()).distinct()
				.sorted((d1, d2) -> d2.compareTo(d1)) // Orden descendente
				.collect(Collectors.toList());

		if (diasConActividad.isEmpty())
			return 0;

		LocalDate hoy = LocalDate.now();
		LocalDate ultimoDiaActividad = diasConActividad.get(0);

		// Si el último día de actividad no es hoy ni ayer, la racha se rompió
		if (ultimoDiaActividad.isBefore(hoy.minusDays(1))) {
			return 0;
		}

		int racha = 0;
		LocalDate diaEsperado = hoy;

		// Si no hay actividad hoy, empezar desde ayer
		if (!ultimoDiaActividad.equals(hoy)) {
			diaEsperado = hoy.minusDays(1);
		}

		// Contar días consecutivos
		for (LocalDate dia : diasConActividad) {
			if (dia.equals(diaEsperado)) {
				racha++;
				diaEsperado = diaEsperado.minusDays(1);
			} else {
				break;
			}
		}

		return racha;
	}

	/**
	 * Método que calcula la mejor racha histórica de días consecutivos con actividad.
	 * @param sesiones Lista de instancias {@link SesionAprendizaje} de un usuario.
	 * @return máxima racha histórica registrada.
	 */
	private int calcularMejorRacha(List<SesionAprendizaje> sesiones) {
		if (sesiones.isEmpty())
			return 0;

		List<LocalDate> diasConActividad = sesiones.stream().filter(SesionAprendizaje::isCompletada)
				.map(s -> s.getFechaInicio().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()).distinct()
				.sorted().collect(Collectors.toList());

		if (diasConActividad.isEmpty())
			return 0;

		int mejorRacha = 1;
		int rachaActual = 1;

		for (int i = 1; i < diasConActividad.size(); i++) {
			LocalDate diaAnterior = diasConActividad.get(i - 1);
			LocalDate diaActual = diasConActividad.get(i);

			if (diaActual.equals(diaAnterior.plusDays(1))) {
				rachaActual++;
				mejorRacha = Math.max(mejorRacha, rachaActual);
			} else {
				rachaActual = 1;
			}
		}

		return mejorRacha;
	}

	/**
	 * Método que obtiene estadísticas específicas de un curso para un usuario
	 * @param usuario Instancia {@link Usuario} del que se obtienen las estadísticas de un curso.
	 * @param curso Instancia {@link Curso},
	 * @return Instancia {@link EstadisticasCurso} con tiempo, precisión, total de ejercicios completados y porcentaje de ejercicios completados.
	 */
	public EstadisticasCurso obtenerEstadisticasCurso(Usuario usuario, Curso curso) {
		List<SesionAprendizaje> sesionesCurso = sesionDAO.buscarSesionesPorUsuarioYCurso(usuario, curso);

		int tiempoTotal = sesionesCurso.stream().filter(SesionAprendizaje::isCompletada)
				.mapToInt(SesionAprendizaje::getTiempoTotal).sum();

		int ejerciciosCompletados = sesionesCurso.stream().filter(SesionAprendizaje::isCompletada)
				.mapToInt(SesionAprendizaje::getEjerciciosCompletados).sum();

		int aciertos = sesionesCurso.stream().filter(SesionAprendizaje::isCompletada)
				.mapToInt(SesionAprendizaje::getAciertos).sum();

		int fallos = sesionesCurso.stream().filter(SesionAprendizaje::isCompletada)
				.mapToInt(SesionAprendizaje::getFallos).sum();

		double precision = 0.0;
		if (aciertos + fallos > 0) {
			precision = (double) aciertos / (aciertos + fallos) * 100.0;
		}

		int totalEjerciciosCurso = 0;
		if (curso.getBloques() != null) {
			for (Bloque bloque : curso.getBloques()) {
				if (bloque.getEjercicios() != null) {
					totalEjerciciosCurso += bloque.getEjercicios().size();
				}
			}
		}

		double porcentajeCompletado = 0.0;
		if (totalEjerciciosCurso > 0) {
			// Usar ejercicios únicos completados, no repeticiones
			porcentajeCompletado = Math.min(100.0, (double) ejerciciosCompletados / totalEjerciciosCurso * 100.0);
		}

		return new EstadisticasCurso(curso, tiempoTotal / 60, // Minutos
				tiempoTotal, ejerciciosCompletados, precision, porcentajeCompletado);
	}

	/**
	 * Método que obtiene todas las sesiones de un usuario.
	 * @param usuario Instancia {@link Usuario} del que se obtienen las sesiones.
	 * @return Lista de todas las instancias {@link SesionAprendizaje} asociadas al usuario.
	 * 
	 */
	public List<SesionAprendizaje> obtenerSesionesUsuario(Usuario usuario) {
		return sesionDAO.buscarSesionesPorUsuario(usuario);
	}

	/**
	 * Método que obtiene estadísticas de todos los cursos en los que el usuario ha tenido
	 * actividad.
	 * @param usuario Instancia {@link Usuario} del que se recuperan todas las estadísticas de cursos.
	 * @return Lista de instancias {@link EstadisticasCurso}, cada uno asociada a un curso del usuario.
	 */
	public List<EstadisticasCurso> obtenerEstadisticasTodosCursos(Usuario usuario) {
		List<SesionAprendizaje> todasLasSesiones = sesionDAO.buscarSesionesPorUsuario(usuario);

		Map<Curso, List<SesionAprendizaje>> sesionesPorCurso = todasLasSesiones.stream()
				.filter(SesionAprendizaje::isCompletada).collect(Collectors.groupingBy(SesionAprendizaje::getCurso));

		return sesionesPorCurso.keySet().stream().map(curso -> obtenerEstadisticasCurso(usuario, curso))
				.collect(Collectors.toList());
	}

	/**
	 * Clase interna para encapsular estadísticas específicas de un curso. Contiene, para un curso asociado: <ul>
	 * <li> Tiempo total de actividad del usuario en minutos. </li>
	 * <li> Tiempo total de actividad del usuario en segundos. </li>
	 * <li> Número de ejercicios completados. </li>
	 * <li> Porcentaje de aciertos (precisión). </li>
	 * <li> Porcentaje del curso completado. </li>
	 * </ul>
	 */
	public static class EstadisticasCurso {
		private final Curso curso;
		private final int tiempoTotalMinutos;
		private final int tiempoTotalSegundos;
		private final int ejerciciosCompletados;
		private final double precision;
		private final double porcentajeCompletado;

		
		public EstadisticasCurso(Curso curso, int tiempoTotalMinutos, int tiempoTotalSegundos,
				int ejerciciosCompletados, double precision, double porcentajeCompletado) {
			this.curso = curso;
			this.tiempoTotalMinutos = tiempoTotalMinutos;
			this.tiempoTotalSegundos = tiempoTotalSegundos;
			this.ejerciciosCompletados = ejerciciosCompletados;
			this.precision = precision;
			this.porcentajeCompletado = porcentajeCompletado;
		}

		public Curso getCurso() {
			return curso;
		}

		public int getTiempoTotalMinutos() {
			return tiempoTotalMinutos;
		}

		public int getTiempoTotalSegundos() {
			return tiempoTotalSegundos;
		}

		public int getEjerciciosCompletados() {
			return ejerciciosCompletados;
		}

		public double getPrecision() {
			return precision;
		}

		public double getPorcentajeCompletado() {
			return porcentajeCompletado;
		}
	}
}