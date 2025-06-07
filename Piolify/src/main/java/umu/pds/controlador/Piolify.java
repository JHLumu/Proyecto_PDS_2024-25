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
    
    // patrón observer
    private List<Runnable> notificarCambiosUsuario = new ArrayList<>();
    
    public Piolify() {
        this.usuarioController = new UsuarioController(this);
        this.importacionController = new ImportacionController();
    }
    
	public static Piolify getUnicaInstancia() {
		if (unicaInstancia == null) {
			unicaInstancia = new Piolify();
		}
		return unicaInstancia;
	}
	
	public UsuarioController getUsuarioController() {
		return usuarioController;
	}
    
	public ImportacionController getImportacionController() {
		return this.importacionController;
	}
	
    public void añadirObservador(Runnable callback) {
    	notificarCambiosUsuario.add(callback);
    }
    
    public void borrarObservador(Runnable callback) {
    	notificarCambiosUsuario.remove(callback);
    }

    void notificarCambiosUsuario() {
    	notificarCambiosUsuario.forEach(Runnable::run);
    }
    
    public void setUsuarioActual(Usuario usuario) {
    	this.usuarioActual = usuario;
    }
    
	public Usuario getUsuarioActual() {
		if (usuarioActual == null) {
			throw new RuntimeException("No hay usuario autenticado");
		}
		return usuarioActual;
	}

	public void registroExitoso() {
        Login login = new Login();
        login.getFrame().setVisible(true);
	}
	
	public void loginExitoso() {
		Principal ventanaPrincipal = new Principal();
        ventanaPrincipal.setVisible(true);
	}
	
	
	
}