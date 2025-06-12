package umu.pds.modelo;

/**
 * Enum que define los diferentes tipos de logros disponibles en el sistema.
 * Cada tipo tiene un nombre, descripción, condición y emoji asociado.
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
    
    private final String nombre;
    private final String descripcion;
    private final String imagenPath;
    
    private static final String RECURSOS_PATH = "/Logros/";

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
     * Verifica si este logro se debe desbloquear basado en las estadísticas del usuario
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