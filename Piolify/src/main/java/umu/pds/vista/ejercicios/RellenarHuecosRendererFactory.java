package umu.pds.vista.ejercicios;

import umu.pds.modelo.TipoEjercicio;
import umu.pds.utils.EjercicioRenderer;
import umu.pds.utils.RellenarHuecosRenderer;

/**
 * Clase que representa una fábrica de renderers para ejercicios de tipo Rellenar Huecos.
 */
public class RellenarHuecosRendererFactory extends RendererFactory {
    
    /**
     * {@inheritDoc}
     */
    @Override
    public EjercicioRenderer crearRenderer() {
        return new RellenarHuecosRenderer();
    }
    
    /**
     * {@inheritDoc}
     * @return {@code TipoEjercicio.COMPLETAR_HUECOS}
     */
    @Override
    public TipoEjercicio getTipoEjercicio() {
        return TipoEjercicio.COMPLETAR_HUECOS;
    }
}
