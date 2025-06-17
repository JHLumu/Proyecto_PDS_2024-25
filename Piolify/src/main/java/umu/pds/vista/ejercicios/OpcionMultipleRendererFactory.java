package umu.pds.vista.ejercicios;

import umu.pds.modelo.TipoEjercicio;
import umu.pds.utils.EjercicioRenderer;
import umu.pds.utils.OpcionMultipleRenderer;

/* Clase que representa una fábrica de renderers para ejercicios de tipo Opción Múltiple.
 */
public class OpcionMultipleRendererFactory extends RendererFactory {
    
    /**
     * {@inheritDoc}
     */
    @Override
    public EjercicioRenderer crearRenderer() {
        return new OpcionMultipleRenderer();
    }
    
    /**
     * {@inheritDoc}
     * 
     * @return {@code TipoEjercicio.OPCION_MULTIPLE}
     */
    @Override
    public TipoEjercicio getTipoEjercicio() {
        return TipoEjercicio.OPCION_MULTIPLE;
    }
}