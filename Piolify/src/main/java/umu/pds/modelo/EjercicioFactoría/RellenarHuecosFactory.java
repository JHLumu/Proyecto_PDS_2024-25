package umu.pds.modelo.EjercicioFactoría;

import umu.pds.modelo.Ejercicio;
import umu.pds.modelo.EjercicioRellenarHuecos;
import umu.pds.modelo.TipoEjercicio;

public class RellenarHuecosFactory extends EjercicioAbstractFactory {
    
    @Override
    public Ejercicio crearEjercicio() {
        return new EjercicioRellenarHuecos();
    }
    
    @Override
    public Ejercicio crearEjercicio(String contenido, String respuesta) {
        return new EjercicioRellenarHuecos(contenido, respuesta);
    }
    
    @Override
    public TipoEjercicio getTipoEjercicio() {
        return TipoEjercicio.COMPLETAR_HUECOS;
    }
}
