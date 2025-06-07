package umu.pds.vista.ejercicios;

import umu.pds.modelo.TipoEjercicio;
import umu.pds.utils.EjercicioRenderer;
import umu.pds.utils.RellenarHuecosRenderer;

public class RellenarHuecosRendererFactory extends RendererFactory {
    
    @Override
    public EjercicioRenderer crearRenderer() {
        return new RellenarHuecosRenderer();
    }
    
    @Override
    public TipoEjercicio getTipoEjercicio() {
        return TipoEjercicio.COMPLETAR_HUECOS;
    }
}
