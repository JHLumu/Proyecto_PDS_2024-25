package umu.pds.vista.elementos;

import javax.swing.*;

import umu.pds.utils.ComponentResizer;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Clase base para crear un JFrame con bordes redondeados y una barra de título personalizada.
 * Permite personalizar colores y comportamientos en modo diseño y en tiempo de ejecución.
 */
public class BaseRoundedFrame extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static final int CORNER_RADIUS = 30;
    private boolean designMode = false;
    protected JPanel titleBar;
    protected JPanel contentPanel;
    private JButton closeButton;
    private Color backgroundColor = PioColores.BLANCO;
    private Color titleBarColor = PioColores.BLANCO;
    private Color closeButtonColor = PioColores.NEGRO;

    // Constructor para WindowBuilder
    public BaseRoundedFrame() {
        this.designMode = true;
        initComponents();
    }

    // Constructor normal
    public BaseRoundedFrame(String title) {
        super(title);
        initComponents();
        if (!designMode) {
            applyRuntimeFeatures();
        }
        
        // Cada vez que el frame cambie de tamaño, recalculamos la shape:
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                setShape(new RoundRectangle2D.Double(
                    0, 0,
                    getWidth(), getHeight(),
                    CORNER_RADIUS, CORNER_RADIUS
                ));
            }
        });
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            protected void paintComponent(Graphics g) {
                if (!designMode) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                                      RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setColor(backgroundColor);
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 
                                    CORNER_RADIUS, CORNER_RADIUS);
                    g2d.dispose();
                }
            }
        };
        
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setContentPane(mainPanel);

        // Barra de título
        titleBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        titleBar.setOpaque(false);
        titleBar.setBackground(titleBarColor);
        
        closeButton = new JButton("×");
        configureCloseButton();
        titleBar.add(closeButton);

        // Panel de contenido
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);

        mainPanel.add(titleBar, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        setupDragListeners();
    }

    private void applyRuntimeFeatures() {
        setUndecorated(true);
        new ComponentResizer(this, CORNER_RADIUS);
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 
                                           CORNER_RADIUS, CORNER_RADIUS));
    }

    private void configureCloseButton() {
        closeButton.setFont(new Font("Arial", Font.BOLD, 18));
        closeButton.setForeground(closeButtonColor);
        closeButton.setContentAreaFilled(false);
        closeButton.setBorderPainted(false);
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(e -> dispose());
    }

    private void setupDragListeners() {
        titleBar.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
            }
            
            public void mouseReleased(MouseEvent e) {
                getContentPane().setCursor(Cursor.getDefaultCursor());
            }
        });

        titleBar.addMouseMotionListener(new MouseMotionAdapter() {
            private Point startPoint;
            
            public void mouseDragged(MouseEvent e) {
                if (startPoint == null) startPoint = e.getLocationOnScreen();
                Point current = e.getLocationOnScreen();
                setLocation(getX() + (current.x - startPoint.x), 
                          getY() + (current.y - startPoint.y));
                startPoint = current;
            }
        });
    }

    // Métodos para cambiar colores
    public void setBackgroundColor(Color color) {
        backgroundColor = color;
        repaint();
    }

    public void setTitleBarColor(Color color) {
        titleBarColor = color;
        titleBar.setBackground(color);
        titleBar.setOpaque(color.getAlpha() > 0);
        repaint();
    }

    public void setCloseButtonColor(Color color) {
        closeButtonColor = color;
        closeButton.setForeground(color);
    }

    public boolean isDesignMode() {
        return designMode;
    }
}