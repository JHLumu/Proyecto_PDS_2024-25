package umu.pds.controlador;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import umu.pds.modelo.Usuario;
import umu.pds.vista.Login;
import umu.pds.vista.Principal;

/**
 * Clase principal de la aplicación. Se encarga de la coordinación
 * de los controladores, la autenticación de los usuarios y de la 
 * notificación a observadores registrados.
 */
public class Piolify {
    
	/**
	 * Instancia {@link Usuario} actualmente autenticado en la aplicación.
	 */
	private Usuario usuarioActual;
	
	/**
	 * Instancia {@link UsuarioController}, controlador encargado de operaciones 
	 * relacionadas con el usuario.
	 */
    private final UsuarioController usuarioController;
    
    /**
	 * Instancia {@link ImportacionController}, controlador encargado de la importación de cursos.
	 */
    private final ImportacionController importacionController;
    
    /**
	 * Instancia única de {@link Piolify} (Singleton).
	 */
    private static Piolify unicaInstancia = null;
    
    /**
     * Lista utilizada para avisar a observadores cuando cambia el usuario actual autenticado.
     */
    private List<Runnable> notificarCambiosUsuario = new ArrayList<>();
    
	/**
	 * Controlador por defecto. Inicializa los controladores {@link UsuarioController} y {@link ImportacionController}.
	 */
    public Piolify() {
        this.usuarioController = new UsuarioController(this);
        this.importacionController = new ImportacionController();
        
    }
    
	/**
	 * Constructor para inyección de dependencias.
	 * @param usuarioController Controlador de usuario
	 * @param importacionController Controlador de importación
	 */
	public Piolify(UsuarioController usuarioController, ImportacionController importacionController) {
		this.usuarioController = usuarioController;
		this.importacionController = importacionController;
	}
    
	/**
	 * Método estático para obtener la instancia única de Piolify (patrón Singleton).
	 * @return Instancia única de Piolify
	 */
	public static Piolify getUnicaInstancia() {
		if (unicaInstancia == null) {
			unicaInstancia = new Piolify();
		}
		return unicaInstancia;
	}
	
	/**
	 * Método para obtener el controlador de usuario.
	 * @return Controlador de usuario
	 */
	public UsuarioController getUsuarioController() {
		return usuarioController;
	}
    
	/**
	 * Método para obtener el controlador de importación.
	 * @return Controlador de importación
	 */
	public ImportacionController getImportacionController() {
		return this.importacionController;
	}

	/**
	 * Método para añadir un observador que se notificará de cambios en el usuario actual.
	 * @param callback Runnable que se ejecutará al notificar cambios
	 */
    public void añadirObservador(Runnable callback) {
    	notificarCambiosUsuario.add(callback);
    }
    
	/**
	 * Método para eliminar un observador que ya no se notificará de cambios en el usuario actual.
	 * @param callback Runnable que se eliminará de la lista de observadores
	 */
    public void borrarObservador(Runnable callback) {
    	notificarCambiosUsuario.remove(callback);
    }

	/**
	 * Método para notificar a todos los observadores de cambios en el usuario actual.
	 */
    public void notificarCambiosUsuario() {
    	notificarCambiosUsuario.forEach(Runnable::run);
    }
    
	/**
	 * Método para establecer el usuario actual.
	 * @param usuario Usuario a establecer como actual
	 */
    public void setUsuarioActual(Usuario usuario) {
    	this.usuarioActual = usuario;
    }
    
	/**
	 * Método para obtener el usuario actual.
	 * @return Usuario actual
	 * @throws RuntimeException si no hay usuario autenticado
	 */
	public Usuario getUsuarioActual() {
		if (usuarioActual == null) {
			throw new RuntimeException("No hay usuario autenticado");
		}
		return usuarioActual;
	}


	/**
	 * Método para manejar el registro exitoso de un usuario.
	 * Abre la ventana de inicio de sesión de manera fluida.
	 */
	public void registroExitoso() {
	    SwingUtilities.invokeLater(() -> {
	        Login login = new Login();
	        login.getFrame().setVisible(true);
	    });
	}
	
	/**
	 * Método para manejar el inicio de sesión exitoso.
	 * Abre la ventana principal de la aplicación.
	 */
	public void loginExitoso() {
		Principal ventanaPrincipal = new Principal();
        ventanaPrincipal.setVisible(true);
	}
	
	
	
}