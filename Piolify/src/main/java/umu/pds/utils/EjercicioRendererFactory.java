package umu.pds.utils;

import umu.pds.modelo.TipoEjercicio;

public abstract class EjercicioRendererFactory {
    
    
    /** Método estático para obtener el renderer según el tipo de ejercicio.
     * 
     * @param tipo Tipo de ejercicio para el que se desea obtener la fábrica de renderers.
     * @return Una instancia de EjercicioRenderer correspondiente al tipo de ejercicio.
     */
    public static EjercicioRenderer crearRenderer(TipoEjercicio tipo) {
    	String className = "umu.pds.utils." + tipo.getClase();
        try {
            Class<?> clazz = Class.forName(className);
            if (EjercicioRenderer.class.isAssignableFrom(clazz)) {
                return (EjercicioRenderer) clazz.getDeclaredConstructor().newInstance();
            } else {
                throw new IllegalArgumentException("Clase " + className + " no es un EjercicioRenderer válido.");
            }
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("No se encontró la clase para el tipo: " + tipo, e);
        } catch (Exception e) {
            throw new RuntimeException("Error al instanciar la clase para el tipo: " + tipo, e);
        }
    }
}

