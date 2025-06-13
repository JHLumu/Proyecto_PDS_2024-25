package umu.pds.modelo;

/**
 * Enum que define los diferentes tipos de logros disponibles en el sistema.
 * Cada tipo tiene un nombre, descripción, condición y emoji asociado. 
 * <br> Se diferencian varias categorías:
 * <ul>
 * <li>Ejercicios completados: Número total de ejercicios que el usuario ha completado.</li>
 * <li>Racha diaria: Días consecutivos en el que el usuario ha completado una sesión.</li>
 * <li>Tiempo acumulado: Tiempo total (en segundos) que el usuario ha estado realizando sesiones de aprendizaje.</li>
 * <li>Cursos iniciados: Número de cursos que ha iniciado el usuario.</li>
 * 
 * </ul>
 */
public enum TipoLogro {
	
    PRIMER_EJERCICIO("Primer Paso", "Completa tu primer ejercicio", "trophy.png", 1),
    CINCO_EJERCICIOS("Aprendiz", "Completa 5 ejercicios", "trophy.png", 5),
    DIEZ_EJERCICIOS("Estudiante Dedicado", "Completa 10 ejercicios", "trophy.png", 10),
    
    RACHA_3_DIAS("Constante", "Mantén una racha de 3 días", "trophy.png", 3),
    RACHA_7_DIAS("Perseverante", "Mantén una racha de 7 días", "trophy.png", 7),
    RACHA_15_DIAS("Disciplinado", "Mantén una racha de 15 días", "trophy.png", 15),
    
    
    TIEMPO_30_MIN("Media Hora", "Estudia durante 30 minutos acumulados", "trophy.png", 1800), // 30 min en segundos
    TIEMPO_1_HORA("Una Hora", "Estudia durante 1 hora acumulada", "trophy.png", 3600), // 1 hora en segundos
    TIEMPO_5_HORAS("Cinco Horas", "Estudia durante 5 horas acumuladas", "trophy.png", 18000), // 5 horas en segundos
    TIEMPO_10_HORAS("Diez Horas", "Estudia durante 10 horas acumuladas", "trophy.png", 36000), // 10 horas en segundos
    
    PRIMER_CURSO("Explorador", "Comienza tu primer curso", "trophy.png", 1),
    TRES_CURSOS("Curioso", "Comienza 3 cursos diferentes", "trophy.png", 3),
    CINCO_CURSOS("Investigador", "Comienza 5 cursos diferentes", "trophy.png", 5);
    
	/**
	 * Nombre del logro.
	 */
    private final String nombre;
    
    /**
     * Descripción del logro.
     */
    private final String descripcion;
    
    /**
     * Ruta de la imagen del logro.
     */
    private final String imagenPath;
    
    /**
     * Carpeta que contiene las imágenes de los logros.
     */
    private static final String RECURSOS_PATH = "/Logros/";

    /**
     * Número de condiciones necesarias para desbloquear el logro. Dependiendo de la categoría del logro, tendrá una interpretación u otra.
     *  <ul>
	 * <li>Ejercicios completados: Número total de ejercicios que el usuario debe completar.</li>
	 * <li>Racha diaria: Días consecutivos en el que el usuario ha completado al menos una sesión.</li>
	 * <li>Tiempo acumulado: Tiempo total (en segundos) que el usuario debe tener en sus sesiones de aprendizaje.</li>
	 * <li>Cursos iniciados: Número de cursos que el usuario debe iniciar.</li>
	 * 
	 * </ul>
     */
    private final int condicion;
    
    TipoLogro(String nombre, String descripcion, String imagenPath, int condicion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagenPath = imagenPath;
        this.condicion = condicion;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public String getImagePath() {
        return RECURSOS_PATH + imagenPath;
    }
    
    public int getCondicion() {
        return condicion;
    }
    
    /**
     * Método que verifica si este logro se debe desbloquear basado en las estadísticas del usuario.
     * @param estadisticas Instancia {@link Estadisticas} asociada al usuario.
     * @param cursosComenzados número de cursos comenzados por el usuario.
     * @return {@code true} si el logro cumple las condiciones, {@code false} en caso contrario.
     */
    public boolean seCumpleCondicion(Estadisticas estadisticas, int cursosComenzados) {
        if (estadisticas == null) return false;
        
        switch (this) {
            case PRIMER_EJERCICIO:
            case CINCO_EJERCICIOS:
            case DIEZ_EJERCICIOS:
                return estadisticas.getTotalEjerciciosCompletados() >= this.condicion;
                
            case RACHA_3_DIAS:
            case RACHA_7_DIAS:
            case RACHA_15_DIAS:
                return estadisticas.getRachaDias() >= this.condicion || 
                       estadisticas.getMejorRacha() >= this.condicion;
                
            case TIEMPO_30_MIN:
            case TIEMPO_1_HORA:
            case TIEMPO_5_HORAS:
            case TIEMPO_10_HORAS:
                return estadisticas.getTiempoTotal() >= this.condicion;
                
            case PRIMER_CURSO:
            case TRES_CURSOS:
            case CINCO_CURSOS:
                return cursosComenzados >= this.condicion;
                
            default:
                return false;
        }
    }
}