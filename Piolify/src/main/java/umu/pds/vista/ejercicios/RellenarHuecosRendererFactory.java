package umu.pds.vista.ejercicios;

import umu.pds.modelo.TipoEjercicio;
import umu.pds.utils.EjercicioRenderer;
import umu.pds.utils.RellenarHuecosRenderer;

/**
 * Clase que representa una fábrica de renderers para ejercicios de tipo Rellenar Huecos.
 * Extiende RendererFactory para crear instancias de RellenarHuecosRenderer.
 */
public class RellenarHuecosRendererFactory extends RendererFactory {
    
    /**
     * Constructor por defecto.
     * Inicializa la fábrica de renderers para ejercicios de tipo Rellenar Huecos.
     */
    @Override
    public EjercicioRenderer crearRenderer() {
        return new RellenarHuecosRenderer();
    }
    
    /**
     * Método que devuelve el tipo de ejercicio asociado a este renderer.
     * 
     * @return TipoEjercicio.COMPLETAR_HUECOS
     */
    @Override
    public TipoEjercicio getTipoEjercicio() {
        return TipoEjercicio.COMPLETAR_HUECOS;
    }
}
