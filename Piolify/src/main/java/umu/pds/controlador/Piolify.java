package umu.pds.controlador;

import java.util.ArrayList;
import java.util.List;

import umu.pds.modelo.Usuario;
import umu.pds.vista.Login;
import umu.pds.vista.Principal;


public class Piolify {
    
	private Usuario usuarioActual;
    private final UsuarioController usuarioController;
    private final ImportacionController importacionController;
    private static Piolify unicaInstancia = null;
    
    private List<Runnable> notificarCambiosUsuario = new ArrayList<>();
    
	
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
	 * Abre la ventana de inicio de sesión.
	 */
	public void registroExitoso() {
        Login login = new Login();
        login.getFrame().setVisible(true);
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