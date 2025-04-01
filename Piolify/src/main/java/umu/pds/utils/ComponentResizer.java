package umu.pds.utils;
import javax.swing.*;

import umu.pds.vista.elementos.BaseRoundedFrame;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class ComponentResizer extends MouseAdapter {
	private final JFrame frame;
    private final int cornerRadius;
    private static final int BORDER_DRAG_THICKNESS = 8;
    private Point initialClick;
    private Rectangle startBounds;
    private int currentCursor;

    public ComponentResizer(JFrame frame, int cornerRadius) {
        this.frame = frame;
        this.cornerRadius = cornerRadius;
        
        // Verificar si debe aplicar la funcionalidad
        if (!shouldSkipResizer()) {
            setupGlassPane();
        }
    }

    private boolean shouldSkipResizer() {
        if (frame instanceof BaseRoundedFrame) {
            BaseRoundedFrame brf = (BaseRoundedFrame) frame;
            return brf.isDesignMode();
        }
        return false;
    }

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

    @Override
    public void mousePressed(MouseEvent e) {
        initialClick = e.getLocationOnScreen();
        startBounds = frame.getBounds();
    }

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

    @Override
    public void mouseReleased(MouseEvent e) {
        initialClick = null;
        startBounds = null;
    }
}