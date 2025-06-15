package umu.pds.vista.elementos;

import java.awt.Cursor;

import javax.swing.JButton;

/**
 * Clase que representa un botón personalizado en la interfaz de usuario de Piolify.
 * Extiende JButton para aplicar estilos y comportamientos específicos.
 */
public class PioButton extends JButton{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	PioButton(){
		super();
		setBorderPainted(false);
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}

	public PioButton(String string) {
		super(string);
		setBorderPainted(false);
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}

}
