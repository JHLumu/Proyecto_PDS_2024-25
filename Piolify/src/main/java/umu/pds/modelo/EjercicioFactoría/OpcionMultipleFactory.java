package umu.pds.modelo.EjercicioFactoría;

import umu.pds.modelo.Ejercicio;
import umu.pds.modelo.EjercicioOpcionMultiple;
import umu.pds.modelo.TipoEjercicio;

public class OpcionMultipleFactory extends EjercicioAbstractFactory {
    
    @Override
    public Ejercicio crearEjercicio() {
        return new EjercicioOpcionMultiple();
    }
    
    @Override
    public Ejercicio crearEjercicio(String contenido, String respuesta) {
        return new EjercicioOpcionMultiple(contenido, respuesta);
    }
    
    @Override
    public TipoEjercicio getTipoEjercicio() {
        return TipoEjercicio.OPCION_MULTIPLE;
    }
}
