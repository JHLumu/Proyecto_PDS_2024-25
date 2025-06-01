package umu.pds.vista.elementos;

import java.awt.Cursor;

import javax.swing.JButton;

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
