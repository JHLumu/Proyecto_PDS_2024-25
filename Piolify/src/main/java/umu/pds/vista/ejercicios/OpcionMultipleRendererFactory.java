package umu.pds.vista.ejercicios;

import umu.pds.modelo.TipoEjercicio;
import umu.pds.utils.EjercicioRenderer;
import umu.pds.utils.OpcionMultipleRenderer;

/* * Clase que representa una fábrica de renderers para ejercicios de tipo Opción Múltiple.
 * Extiende RendererFactory para crear instancias de OpcionMultipleRenderer.
 */
public class OpcionMultipleRendererFactory extends RendererFactory {
    
    /**
     * Constructor por defecto.
     * Inicializa la fábrica de renderers para ejercicios de tipo Opción Múltiple.
     */
    @Override
    public EjercicioRenderer crearRenderer() {
        return new OpcionMultipleRenderer();
    }
    
    /**
     * Método que devuelve el tipo de ejercicio asociado a este renderer.
     * 
     * @return TipoEjercicio.OPCION_MULTIPLE
     */
    @Override
    public TipoEjercicio getTipoEjercicio() {
        return TipoEjercicio.OPCION_MULTIPLE;
    }
}