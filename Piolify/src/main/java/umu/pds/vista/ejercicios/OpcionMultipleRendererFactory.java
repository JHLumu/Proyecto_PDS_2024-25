package umu.pds.vista.ejercicios;

import umu.pds.modelo.TipoEjercicio;
import umu.pds.utils.EjercicioRenderer;
import umu.pds.utils.OpcionMultipleRenderer;

public class OpcionMultipleRendererFactory extends RendererFactory {
    
    @Override
    public EjercicioRenderer crearRenderer() {
        return new OpcionMultipleRenderer();
    }
    
    @Override
    public TipoEjercicio getTipoEjercicio() {
        return TipoEjercicio.OPCION_MULTIPLE;
    }
}