package umu.pds.modelo;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("COMPLETAR_HUECOS")
public class EjercicioRellenarHuecos extends Ejercicio {
	
	public EjercicioRellenarHuecos() {
		super();
	}

	public EjercicioRellenarHuecos(String contenido, String respuesta) {
		super(contenido, respuesta);
	}
	
	@Override
	public void renderEjercicio() {
		System.out.println("Completa el siguiente texto:");
		System.out.println();
		
		String contenido = getContenido();
		// Reemplazar patrones de huecos con espacios en blanco para mostrar
		String textoParaMostrar = contenido.replaceAll("\\{[^}]*\\}", "_____")
		                                   .replaceAll("\\[[^\\]]*\\]", "_____")
		                                   .replaceAll("_{3,}", "_____");
		
		System.out.println(textoParaMostrar);
		System.out.println();
		System.out.println("Escribe las respuestas separadas por comas:");
	}

	@Override
	public boolean validarRespuesta(String respuestaUsuario) {
		if (respuestaUsuario == null || respuestaUsuario.trim().isEmpty()) {
			return false;
		}
		
		String respuestaCorrecta = getRespuesta();
		if (respuestaCorrecta == null || respuestaCorrecta.trim().isEmpty()) {
			return false;
		}
		
		// Normalizar respuestas para comparación
		String respuestaNormalizada = normalizarRespuesta(respuestaUsuario);
		String correctaNormalizada = normalizarRespuesta(respuestaCorrecta);
		
		// Comparación exacta primero
		if (respuestaNormalizada.equals(correctaNormalizada)) {
			return true;
		}
		
		// Comparación por partes si las respuestas contienen separadores
		List<String> partesUsuario = Arrays.asList(respuestaNormalizada.split("\\|"));
		List<String> partesCorrectas = Arrays.asList(correctaNormalizada.split("\\|"));
		
		// Si tienen diferente número de partes, es incorrecto
		if (partesUsuario.size() != partesCorrectas.size()) {
			return false;
		}
		
		// Verificar cada parte
		for (int i = 0; i < partesUsuario.size(); i++) {
			String parteUsuario = partesUsuario.get(i).trim();
			String parteCorrecta = partesCorrectas.get(i).trim();
			
			// Permitir múltiples respuestas correctas separadas por comas
			if (parteCorrecta.contains(",")) {
				String[] opcionesCorrectas = parteCorrecta.split(",");
				boolean algunaCorrecta = false;
				for (String opcion : opcionesCorrectas) {
					if (parteUsuario.equalsIgnoreCase(opcion.trim())) {
						algunaCorrecta = true;
						break;
					}
				}
				if (!algunaCorrecta) {
					return false;
				}
			} else {
				// Comparación simple
				if (!parteUsuario.equalsIgnoreCase(parteCorrecta)) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	/**
	 * Normaliza una respuesta eliminando espacios extra y convirtiendo a minúsculas
	 */
	private String normalizarRespuesta(String respuesta) {
		if (respuesta == null) return "";
		
		return respuesta.trim()
		               .replaceAll("\\s+", " ")  // Múltiples espacios a uno solo
		               .toLowerCase();
	}
	
	/**
	 * Extrae los huecos del contenido del ejercicio
	 * @return Lista de huecos encontrados en el texto
	 */
	public List<String> extraerHuecos() {
		String contenido = getContenido();
		if (contenido == null) return Arrays.asList();
		
		Pattern pattern = Pattern.compile("\\{([^}]*)\\}|\\[([^\\]]*)\\]|_{3,}");
		Matcher matcher = pattern.matcher(contenido);
		
		return matcher.results()
		             .map(match -> {
		                 String grupo1 = match.group(1);
		                 String grupo2 = match.group(2);
		                 if (grupo1 != null) return grupo1;
		                 if (grupo2 != null) return grupo2;
		                 return "_____";
		             })
		             .toList();
	}
	
	/**
	 * Cuenta el número de huecos en el ejercicio
	 * @return Número de huecos encontrados
	 */
	public int contarHuecos() {
		return extraerHuecos().size();
	}
	
	/**
	 * Obtiene el texto del ejercicio sin los marcadores de huecos
	 * @return Texto limpio para mostrar
	 */
	public String getTextoParaMostrar() {
		String contenido = getContenido();
		if (contenido == null) return "";
		
		return contenido.replaceAll("\\{[^}]*\\}", "_____")
		               .replaceAll("\\[[^\\]]*\\]", "_____")
		               .replaceAll("_{3,}", "_____");
	}
	
	/**
	 * Verifica si el ejercicio tiene un formato válido
	 * @return true si el ejercicio es válido
	 */
	public boolean esValido() {
		if (getContenido() == null || getContenido().trim().isEmpty()) {
			return false;
		}
		
		if (getRespuesta() == null || getRespuesta().trim().isEmpty()) {
			return false;
		}
		
		int numHuecos = contarHuecos();
		if (numHuecos == 0) {
			return false;
		}
		
		// Verificar que el número de respuestas coincida con el número de huecos
		String[] respuestas = getRespuesta().split("\\|");
		return respuestas.length == numHuecos;
	}
	
	/**
	 * Obtiene las respuestas como lista
	 * @return Lista de respuestas esperadas
	 */
	public List<String> getRespuestasComoLista() {
		String respuesta = getRespuesta();
		if (respuesta == null || respuesta.trim().isEmpty()) {
			return Arrays.asList();
		}
		
		return Arrays.asList(respuesta.split("\\|"))
		             .stream()
		             .map(String::trim)
		             .toList();
	}
	
	/**
	 * Establece las respuestas desde una lista
	 * @param respuestas Lista de respuestas
	 */
	public void setRespuestasDesdeList(List<String> respuestas) {
		if (respuestas == null || respuestas.isEmpty()) {
			setRespuesta("");
			return;
		}
		
		String respuestaCombinada = String.join("|", respuestas);
		setRespuesta(respuestaCombinada);
	}

	@Override
	public TipoEjercicio getTipo() {
		return TipoEjercicio.COMPLETAR_HUECOS;
	}
}
