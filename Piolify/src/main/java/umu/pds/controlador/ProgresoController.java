package umu.pds.controlador;

import java.awt.Component;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import umu.pds.modelo.Bloque;
import umu.pds.modelo.Curso;
import umu.pds.modelo.Ejercicio;
import umu.pds.modelo.Estrategia;
import umu.pds.modelo.ProgresoBloque;
import umu.pds.servicios.ServicioProgreso;
import umu.pds.vista.PioEjerciciosConProgreso;

/**
 * Controlador para gestionar el progreso del usuario en cursos y bloques de ejercicios.
 * Facilita la interacción entre la interfaz de usuario y el modelo de datos relacionado con el progreso.
 */
public class ProgresoController {
    private Piolify controlador;

    private ServicioProgreso servicioProgreso;
    
    /**
     * Constructor del controlador de progreso.
     * @param pio La instancia principal del controlador de la aplicación (Piolify).
     */
    public ProgresoController (Piolify pio) {
    	this.controlador = pio;
    	this.servicioProgreso  = new ServicioProgreso();
    }

    /**
     * Inicia o continúa la interacción con un curso, guiando al usuario a través de la
     * selección de bloques y estrategias.
     * Este método centraliza la lógica que antes estaba en Biblioteca.
     * @param curso El curso a iniciar o continuar.
     * @param parentComponent El componente padre para los diálogos, utilizado para centrar las ventanas emergentes.
     */
    public void iniciarOContinuarCurso(Curso curso, Component parentComponent) {
        // Verifica si el curso está completamente terminado para el usuario actual.
        boolean cursoCompletado = servicioProgreso.isCursoCompletado(controlador.getUsuarioActual(), curso);
        
        if (cursoCompletado) {
            // Si el curso está completado, se maneja la opción de repetirlo.
            manejarCursoCompletado(curso, parentComponent);
        } else {
            // Si no está completado, se muestra el selector de bloques para continuar o iniciar.
            mostrarSelectorBloques(curso, parentComponent);
        }
    }

    /**
     * Muestra una ventana de diálogo para que el usuario seleccione un bloque de ejercicios dentro de un curso.
     * También muestra el progreso individual de cada bloque y el progreso general del curso.
     * @param curso El curso del cual se mostrarán los bloques.
     * @param parentComponent El componente padre para los diálogos.
     */
    private void mostrarSelectorBloques(Curso curso, Component parentComponent) {
        List<Bloque> bloques = curso.getBloques();
        
        // Comprueba si el curso tiene bloques asociados.
        if (bloques == null || bloques.isEmpty()) {
            JOptionPane.showMessageDialog(parentComponent,
                "El curso \"" + curso.getTitulo() + "\" no tiene bloques de ejercicios.",
                "Sin ejercicios", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Prepara las opciones para el selector de bloques, incluyendo el estado de progreso de cada uno.
        String[] opcionesBloques = bloques.stream()
            .map(bloque -> {
                ProgresoBloque progreso = servicioProgreso.obtenerProgreso(controlador.getUsuarioActual(), bloque);
                if (progreso == null) {
                    return bloque.getTitulo() + " (No iniciado)";
                } else if (progreso.isCompletado()) {
                    return bloque.getTitulo() + " (✓ Completado)";
                } else {
                    return bloque.getTitulo() + String.format(" (%.1f%% completado)", 
                        progreso.getPorcentajeCompletado());
                }
            })
            .toArray(String[]::new);

        // Prepara el mensaje a mostrar en el diálogo, incluyendo el progreso general del curso.
        String mensaje = String.format(
            "Curso: %s\nProgreso general: %.1f%%\n\nSelecciona el bloque que quieres estudiar:",
            curso.getTitulo(),
            servicioProgreso.calcularPorcentajeCurso(controlador.getUsuarioActual(), curso)
        );

        // Muestra el diálogo de selección de bloques.
        String seleccionBloque = (String) JOptionPane.showInputDialog(
            parentComponent, mensaje, "Elegir Bloque",
            JOptionPane.QUESTION_MESSAGE, null, opcionesBloques, opcionesBloques[0]
        );

        // Si el usuario cancela la selección, se sale del método.
        if (seleccionBloque == null) return;

        // Encuentra el índice del bloque seleccionado.
        int indiceSeleccionado = -1;
        for (int i = 0; i < opcionesBloques.length; i++) {
            if (opcionesBloques[i].equals(seleccionBloque)) {
                indiceSeleccionado = i;
                break;
            }
        }
        
        // Si se seleccionó un bloque válido, se procede a manejar su selección.
        if (indiceSeleccionado >= 0) {
            Bloque bloqueSeleccionado = bloques.get(indiceSeleccionado);
            manejarSeleccionBloque(bloqueSeleccionado, parentComponent);
        }
    }

    /**
     * Gestiona la acción a realizar una vez que un bloque ha sido seleccionado por el usuario.
     * Determina si el bloque es nuevo, está en progreso o ya está completado y llama al método apropiado.
     * @param bloque El bloque de ejercicios seleccionado.
     * @param parentComponent El componente padre para los diálogos.
     */
    private void manejarSeleccionBloque(Bloque bloque, Component parentComponent) {
        // Obtiene el progreso del bloque para el usuario actual.
        ProgresoBloque progreso = servicioProgreso.obtenerProgreso(controlador.getUsuarioActual(), bloque);
        
        if (progreso == null) {
            // Si no hay progreso, el bloque se considera nuevo.
            iniciarBloqueNuevo(bloque, parentComponent);
        } else if (progreso.isCompletado()) {
            // Si el bloque está completado, se maneja la opción de repetirlo.
            manejarBloqueCompletado(bloque, parentComponent);
        } else {
            // Si el bloque está en progreso, se ofrecen opciones para continuar, cambiar estrategia o reiniciar.
            manejarBloqueEnProgreso(bloque, progreso, parentComponent);
        }
    }

    /**
     * Maneja la situación de un bloque que está en progreso, ofreciendo al usuario opciones como
     * continuar, cambiar de estrategia o reiniciar el bloque.
     * @param bloque El bloque de ejercicios en progreso.
     * @param progreso El objeto ProgresoBloque que representa el estado actual del progreso.
     * @param parentComponent El componente padre para los diálogos.
     */
    private void manejarBloqueEnProgreso(Bloque bloque, ProgresoBloque progreso, Component parentComponent) {
        String estrategiaActual = progreso.getEstrategiaUtilizada().toString();
        
        // Construye el mensaje a mostrar en el diálogo de opciones.
        String mensaje = String.format(
            "Bloque: \"%s\"\n" +
            "Progreso: %.1f%% (%d/%d ejercicios)\n" +
            "Estrategia actual: %s\n\n" +
            "¿Qué deseas hacer?",
            bloque.getTitulo(),
            progreso.getPorcentajeCompletado(),
            progreso.getEjerciciosCompletados(),
            bloque.getEjercicios().size(),
            estrategiaActual
        );
        
        String[] opciones = { "Continuar donde lo dejé", "Cambiar estrategia y continuar", "Reiniciar este bloque", "Cancelar" };
        
        // Muestra el diálogo con las opciones.
        int seleccion = JOptionPane.showOptionDialog(
            parentComponent, mensaje, "Bloque en Progreso", 
            JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]
        );
        
        // Realiza la acción según la selección del usuario.
        switch (seleccion) {
            case 0: // Continuar donde lo dejé
                continuarBloque(bloque, progreso, parentComponent);
                break;
            case 1: // Cambiar estrategia y continuar
                cambiarEstrategiaYContinuar(bloque, progreso, parentComponent);
                break;
            case 2: // Reiniciar este bloque
                reiniciarBloque(bloque, parentComponent);
                break;
            default: // Cancelar o cerrar el diálogo
                return;
        }
    }

    /**
     * Inicia un nuevo bloque de ejercicios para el usuario, solicitando primero la selección de una estrategia.
     * Crea un nuevo registro de progreso y abre la ventana de ejercicios.
     * @param bloque El bloque de ejercicios a iniciar.
     * @param parentComponent El componente padre para los diálogos.
     */
    private void iniciarBloqueNuevo(Bloque bloque, Component parentComponent) {
        // Muestra el selector de estrategias. Si se cancela, no se hace nada.
        Estrategia estrategia = mostrarSelectorEstrategia(parentComponent);
        if (estrategia == null) return;
        
        // Inicia el progreso para el bloque con la estrategia seleccionada.
        ProgresoBloque progresoBloque = servicioProgreso.iniciarProgreso(
        		controlador.getUsuarioActual(), bloque, estrategia.getTipoEstrategia()
        );
        
        // Abre la ventana de ejercicios con el nuevo progreso.
        abrirVentanaEjercicios(bloque, estrategia, progresoBloque, parentComponent);
    }

    /**
     * Continúa un bloque de ejercicios que ya había sido iniciado, utilizando la estrategia previamente seleccionada.
     * Abre la ventana de ejercicios con el progreso existente.
     * @param bloque El bloque de ejercicios a continuar.
     * @param progreso El objeto ProgresoBloque que contiene el estado actual del progreso.
     * @param parentComponent El componente padre para los diálogos.
     */
    private void continuarBloque(Bloque bloque, ProgresoBloque progreso, Component parentComponent) {
        // Obtiene la estrategia actual basada en el tipo almacenado en el progreso.
        String estrategiaActual = progreso.getEstrategiaUtilizada().toString();
        Estrategia estrategia = controlador.getImportacionController().getEstrategia(estrategiaActual);
        // Abre la ventana de ejercicios.
        abrirVentanaEjercicios(bloque, estrategia, progreso, parentComponent);
    }
    
    /**
     * Permite al usuario cambiar la estrategia de aprendizaje para un bloque en progreso y luego continuar.
     * La nueva estrategia se guarda en el progreso del bloque.
     * @param bloque El bloque de ejercicios cuyo progreso se va a modificar.
     * @param progreso El objeto ProgresoBloque que representa el estado actual del progreso.
     * @param parentComponent El componente padre para los diálogos.
     */
    private void cambiarEstrategiaYContinuar(Bloque bloque, ProgresoBloque progreso, Component parentComponent) {
        // Muestra el selector de estrategias para elegir una nueva.
        Estrategia nuevaEstrategia = mostrarSelectorEstrategia(parentComponent);
        if (nuevaEstrategia == null) return;
        // Actualiza la estrategia en el objeto de progreso y la guarda.
        progreso.setEstrategiaUtilizada(nuevaEstrategia.getTipoEstrategia());
        servicioProgreso.guardarProgreso(progreso);
        // Abre la ventana de ejercicios con la nueva estrategia.
        abrirVentanaEjercicios(bloque, nuevaEstrategia, progreso, parentComponent);
    }

    /**
     * Reinicia por completo el progreso de un bloque de ejercicios para el usuario actual.
     * Esto implica eliminar el progreso anterior y comenzar uno nuevo con una estrategia seleccionada.
     * @param bloque El bloque de ejercicios a reiniciar.
     * @param parentComponent El componente padre para los diálogos.
     */
    private void reiniciarBloque(Bloque bloque, Component parentComponent) {
        // Solicita al usuario seleccionar una nueva estrategia para el bloque reiniciado.
        Estrategia estrategia = mostrarSelectorEstrategia(parentComponent);
        if (estrategia == null) return;
        // Elimina el progreso existente para el bloque.
        servicioProgreso.eliminarProgreso(controlador.getUsuarioActual(), bloque);
        // Inicia un nuevo progreso con la estrategia elegida.
        ProgresoBloque nuevoProgreso = servicioProgreso.iniciarProgreso(
        		controlador.getUsuarioActual(), bloque, estrategia.getTipoEstrategia()
        );
        // Abre la ventana de ejercicios con el bloque y el nuevo progreso.
        abrirVentanaEjercicios(bloque, estrategia, nuevoProgreso, parentComponent);
    }

    /**
     * Maneja la situación en la que un bloque de ejercicios ya ha sido completado.
     * Pregunta al usuario si desea repetirlo y, en caso afirmativo, lo reinicia.
     * @param bloque El bloque de ejercicios que ya está completado.
     * @param parentComponent El componente padre para los diálogos.
     */
    private void manejarBloqueCompletado(Bloque bloque, Component parentComponent) {
        // Pregunta al usuario si desea repetir el bloque.
        int respuesta = JOptionPane.showConfirmDialog(parentComponent,
            "El bloque \"" + bloque.getTitulo() + "\" ya está completado.\n¿Deseas repetirlo?", 
            "Bloque Completado", JOptionPane.YES_NO_OPTION);
            
        if (respuesta == JOptionPane.YES_OPTION) {
            // Si la respuesta es afirmativa, reinicia el bloque.
            reiniciarBloque(bloque, parentComponent);
        }
    }

    /**
     * Maneja la situación en la que un curso completo ha sido finalizado por el usuario.
     * Ofrece la opción de reiniciar el curso, lo que implica eliminar el progreso de todos sus bloques.
     * @param curso El curso que ya está completado.
     * @param parentComponent El componente padre para los diálogos.
     */
    private void manejarCursoCompletado(Curso curso, Component parentComponent) {
		// Pregunta al usuario si desea repetir el curso.
		int respuesta = JOptionPane.showConfirmDialog(parentComponent,
				"\"" + curso.getTitulo() + "\" ya está completado.\n¿Deseas repetirlo?", "Curso Completado",
				JOptionPane.YES_NO_OPTION);

		if (respuesta == JOptionPane.YES_OPTION) {
			// Si la respuesta es afirmativa, elimina el progreso de cada bloque del curso.
			for (Bloque bloque : curso.getBloques()) {
				servicioProgreso.eliminarProgreso(controlador.getUsuarioActual(), bloque);
			}
			// Después de reiniciar, vuelve a mostrar el selector de bloques para que el usuario pueda empezar de nuevo.
			mostrarSelectorBloques(curso, parentComponent);
		}
	}

    /**
     * Muestra un diálogo para que el usuario seleccione una estrategia de aprendizaje.
     * Las estrategias disponibles se obtienen del controlador de importación.
     * @param parentComponent El componente padre para los diálogos.
     * @return La estrategia seleccionada por el usuario, o null si el usuario cancela la selección.
     */
    private Estrategia mostrarSelectorEstrategia(Component parentComponent) {
        // Obtiene los nombres de las estrategias definidas para mostrarlas como opciones.
        String[] opciones = controlador.getImportacionController().getTiposEstrategiasDefinidas().stream().map(t -> t.toString()).toArray(String[]::new);
        // Muestra el diálogo de selección de estrategia.
        String seleccion = (String) JOptionPane.showInputDialog(parentComponent,
                "Selecciona la estrategia de aprendizaje para la sesión", "Seleccionar Estrategia",
                JOptionPane.INFORMATION_MESSAGE, null, opciones, opciones[0]);
        // Devuelve la estrategia correspondiente a la selección o null si se cancela.
        return (seleccion == null) ? null : controlador.getImportacionController().getEstrategia(seleccion);
    }
    
    /**
     * Abre la ventana principal de ejercicios (PioEjerciciosConProgreso) para que el usuario comience a practicar.
     * Los ejercicios se ordenan según la estrategia seleccionada.
     * @param bloque El bloque de ejercicios a mostrar.
     * @param estrategia La estrategia de aprendizaje que se aplicará para ordenar los ejercicios.
     * @param progreso El objeto ProgresoBloque que se utilizará para registrar y mostrar el progreso.
     * @param parentComponent El componente padre para los diálogos.
     */
    private void abrirVentanaEjercicios(Bloque bloque, Estrategia estrategia, ProgresoBloque progreso, Component parentComponent) {
        try {
            // Verifica si el bloque contiene ejercicios.
            if (bloque.getEjercicios() == null || bloque.getEjercicios().isEmpty()) {
                JOptionPane.showMessageDialog(parentComponent,
                    "El bloque \"" + bloque.getTitulo() + "\" no tiene ejercicios.",
                    "Sin ejercicios", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Ordena los ejercicios del bloque según la estrategia seleccionada.
            List<Ejercicio> ejercicios = estrategia.ordenarEjercicios(bloque.getEjercicios());
            
            // Abre la ventana de ejercicios en el hilo de despacho de eventos de Swing.
            SwingUtilities.invokeLater(() -> {
                PioEjerciciosConProgreso ventanaEjercicios = new PioEjerciciosConProgreso(
                    ejercicios, progreso, servicioProgreso
                );
                ventanaEjercicios.setVisible(true);
            });
            
        } catch (Exception e) {
            // Muestra un mensaje de error si ocurre un problema al abrir la ventana de ejercicios.
            JOptionPane.showMessageDialog(parentComponent,
                "Error al abrir los ejercicios: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
