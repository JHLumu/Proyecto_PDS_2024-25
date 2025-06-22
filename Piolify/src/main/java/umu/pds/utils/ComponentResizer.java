package umu.pds.utils;
import javax.swing.*;

import umu.pds.vista.elementos.BaseRoundedFrame;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Clase que permite redimensionar un JFrame con esquinas redondeadas.
 * Detecta el cursor en las esquinas y bordes del frame para permitir el redimensionamiento.
 */
public class ComponentResizer extends MouseAdapter {
	private final JFrame frame;
    private final int cornerRadius;
    private static final int BORDER_DRAG_THICKNESS = 8;
    private Point initialClick;
    private Rectangle startBounds;
    private int currentCursor;

    /**
     * Constructor que inicializa el redimensionador para un JFrame con esquinas redondeadas.
     * @param frame El JFrame al que se le aplicará el redimensionamiento.
     * @param cornerRadius El radio de las esquinas redondeadas del JFrame.
     */
    public ComponentResizer(JFrame frame, int cornerRadius) {
        this.frame = frame;
        this.cornerRadius = cornerRadius;
        
        // Verificar si debe aplicar la funcionalidad
        if (!shouldSkipResizer()) {
            setupGlassPane();
        }
    }

    /**
     * Verifica si el resizer debe ser omitido.
     * Si el frame es una instancia de BaseRoundedFrame y está en modo diseño, se omite.
     * @return true si se debe omitir el resizer, false en caso contrario.
     */
    private boolean shouldSkipResizer() {
        if (frame instanceof BaseRoundedFrame) {
            BaseRoundedFrame brf = (BaseRoundedFrame) frame;
            return brf.isDesignMode();
        }
        return false;
    }

    /**
     * Configura el glass pane del JFrame para detectar los eventos de mouse.
     * El glass pane permite interceptar los eventos de mouse y redimensionar el frame.
     */
    private void setupGlassPane() {
        JComponent glassPane = new JComponent() {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public boolean contains(int x, int y) {
                return x < BORDER_DRAG_THICKNESS 
                    || x > getWidth() - BORDER_DRAG_THICKNESS 
                    || y < BORDER_DRAG_THICKNESS 
                    || y > getHeight() - BORDER_DRAG_THICKNESS;
            }
        };
        
        glassPane.addMouseListener(this);
        glassPane.addMouseMotionListener(this);
        glassPane.setOpaque(false);
        frame.setGlassPane(glassPane);
        glassPane.setVisible(true);
    }

    /**
     * Métodos de MouseListener y MouseMotionListener para manejar los eventos de mouse.
     * Estos métodos permiten detectar el movimiento del mouse y redimensionar el frame.
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        int width = frame.getWidth();
        int height = frame.getHeight();

        boolean top = y < BORDER_DRAG_THICKNESS;
        boolean bottom = y > height - BORDER_DRAG_THICKNESS;
        boolean left = x < BORDER_DRAG_THICKNESS;
        boolean right = x > width - BORDER_DRAG_THICKNESS;

        int cursorType = Cursor.DEFAULT_CURSOR;
        if (top && left) cursorType = Cursor.NW_RESIZE_CURSOR;
        else if (top && right) cursorType = Cursor.NE_RESIZE_CURSOR;
        else if (bottom && left) cursorType = Cursor.SW_RESIZE_CURSOR;
        else if (bottom && right) cursorType = Cursor.SE_RESIZE_CURSOR;
        else if (left) cursorType = Cursor.W_RESIZE_CURSOR;
        else if (right) cursorType = Cursor.E_RESIZE_CURSOR;
        else if (top) cursorType = Cursor.N_RESIZE_CURSOR;
        else if (bottom) cursorType = Cursor.S_RESIZE_CURSOR;

        frame.getGlassPane().setCursor(Cursor.getPredefinedCursor(cursorType));
        currentCursor = cursorType;
    }

    /**
     * Método que se llama cuando se presiona el mouse.
     * Guarda la posición inicial del mouse y los límites actuales del frame.
     * @param e Evento de mouse.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        initialClick = e.getLocationOnScreen();
        startBounds = frame.getBounds();
    }

    /**
     * Método que se llama cuando se arrastra el mouse.
     * Calcula el nuevo tamaño y posición del frame basado en el movimiento del mouse.
     * @param e Evento de mouse.
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        if (initialClick == null || startBounds == null) return;

        Point current = e.getLocationOnScreen();
        int dx = current.x - initialClick.x;
        int dy = current.y - initialClick.y;

        Rectangle newBounds = new Rectangle(startBounds);
        updateBounds(newBounds, dx, dy);
        enforceMinimumSize(newBounds);
        
        frame.setBounds(newBounds);
        updateFrameShape();
    }

    /**
     * Actualiza los límites del frame según el cursor actual y el movimiento del mouse.
     * @param bounds Los límites actuales del frame.
     * @param dx Cambio en la posición horizontal.
     * @param dy Cambio en la posición vertical.
     */
    private void updateBounds(Rectangle bounds, int dx, int dy) {
        switch (currentCursor) {
            case Cursor.NW_RESIZE_CURSOR:
                bounds.setBounds(startBounds.x + dx, startBounds.y + dy, 
                               startBounds.width - dx, startBounds.height - dy);
                break;
            case Cursor.NE_RESIZE_CURSOR:
                bounds.setSize(startBounds.width + dx, startBounds.height - dy);
                break;
            case Cursor.SW_RESIZE_CURSOR:
                bounds.setBounds(startBounds.x + dx, startBounds.y, 
                               startBounds.width - dx, startBounds.height + dy);
                break;
            case Cursor.SE_RESIZE_CURSOR:
                bounds.setSize(startBounds.width + dx, startBounds.height + dy);
                break;
            case Cursor.W_RESIZE_CURSOR:
                bounds.setBounds(startBounds.x + dx, startBounds.y, 
                               startBounds.width - dx, startBounds.height);
                break;
            case Cursor.E_RESIZE_CURSOR:
                bounds.width += dx;
                break;
            case Cursor.N_RESIZE_CURSOR:
                bounds.setBounds(startBounds.x, startBounds.y + dy, 
                               startBounds.width, startBounds.height - dy);
                break;
            case Cursor.S_RESIZE_CURSOR:
                bounds.height += dy;
                break;
        }
    }

    /**
     * Método que asegura que el tamaño del frame no sea menor al tamaño mínimo definido.
     * Ajusta los límites si son menores que el tamaño mínimo.
     * @param bounds Los límites actuales del frame.
     */
      
    private void enforceMinimumSize(Rectangle bounds) {
        // Usar el mínimo tamaño definido en el frame, no el calculado
        Dimension frameMinSize = frame.getMinimumSize();
        
        if (bounds.width < frameMinSize.width) {
            bounds.width = frameMinSize.width;
            if (currentCursor == Cursor.W_RESIZE_CURSOR 
                || currentCursor == Cursor.NW_RESIZE_CURSOR 
                || currentCursor == Cursor.SW_RESIZE_CURSOR) {
                bounds.x = startBounds.x + startBounds.width - frameMinSize.width;
            }
        }
        
        if (bounds.height < frameMinSize.height) {
            bounds.height = frameMinSize.height;
            if (currentCursor == Cursor.N_RESIZE_CURSOR 
                || currentCursor == Cursor.NW_RESIZE_CURSOR 
                || currentCursor == Cursor.NE_RESIZE_CURSOR) {
                bounds.y = startBounds.y + startBounds.height - frameMinSize.height;
            }
        }
    }

    private void updateFrameShape() {
        SwingUtilities.invokeLater(() -> {
            frame.setShape(new RoundRectangle2D.Double(0, 0, 
                frame.getWidth(), 
                frame.getHeight(), 
                cornerRadius, 
                cornerRadius));
            frame.repaint();
        });
    }

    /**
     * Método que se llama cuando se suelta el mouse.
     * Limpia las variables de estado.
     * @param e Evento de mouse.
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        initialClick = null;
        startBounds = null;
    }
}