package umu.pds.vista.ejercicios;

import umu.pds.modelo.TipoEjercicio;

public class OpcionMultipleRendererFactory extends RendererAbstractFactory {
    
    @Override
    public EjercicioRenderer crearRenderer() {
        return new OpcionMultipleRenderer();
    }
    
    @Override
    public TipoEjercicio getTipoEjercicio() {
        return TipoEjercicio.OPCION_MULTIPLE;
    }
}