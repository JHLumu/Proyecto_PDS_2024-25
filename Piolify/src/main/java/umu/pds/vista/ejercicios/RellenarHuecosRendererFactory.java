package umu.pds.vista.ejercicios;

import umu.pds.modelo.TipoEjercicio;

public class RellenarHuecosRendererFactory extends RendererAbstractFactory {
    
    @Override
    public EjercicioRenderer crearRenderer() {
        return new RellenarHuecosRenderer();
    }
    
    @Override
    public TipoEjercicio getTipoEjercicio() {
        return TipoEjercicio.COMPLETAR_HUECOS;
    }
}
