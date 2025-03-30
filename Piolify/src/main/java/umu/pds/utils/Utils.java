package umu.pds.utils;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Utils {

    // JPanel con degradado de fondo
    public static class JPanelGradient extends JPanel {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private Color color1;
        private Color color2;

        // Constructor que recibe los colores como parámetros
        public JPanelGradient(Color color1, Color color2) {
            this.color1 = color1;
            this.color2 = color2;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            int width = getWidth();
            int height = getHeight();

            // Usar los colores proporcionados para el degradado
            GradientPaint gp = new GradientPaint(0, 0, color1, 0, height, color2); // Degradado vertical

            g2d.setPaint(gp);
            g2d.fillRect(0, 0, width, height);
        }
    }

    // JLabel para escribir texto con sombra
    public static class JLabelWithShadow extends JLabel {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private Color shadowColor;

        // Constructor que recibe el texto y el color de la sombra como parámetros
        public JLabelWithShadow(String text, Color shadowColor) {
            super(text);
            this.shadowColor = shadowColor;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();

            // Usar el color de la sombra proporcionado
            g2d.setColor(shadowColor);
            g2d.drawString(getText(), getInsets().left + 2, getInsets().top + getFontMetrics(getFont()).getAscent() + 2);

            // Dibuja el texto normal
            g2d.setColor(getForeground());
            g2d.drawString(getText(), getInsets().left, getInsets().top + getFontMetrics(getFont()).getAscent());

            g2d.dispose();
        }
    }

    public static ImageIcon createCircularIcon(BufferedImage original, int diameter) {
        if (original == null) {
            return new ImageIcon(); // o algún icono por defecto
        }

        // 1) Tomamos el tamaño mínimo de la imagen para hacer un cuadrado centrado
        int size = Math.min(original.getWidth(), original.getHeight());

        // 2) Calculamos coordenadas para recortar la parte central
        int x = (original.getWidth()  - size) / 2;
        int y = (original.getHeight() - size) / 2;

        // 3) Obtenemos el "subimage" cuadrado
        BufferedImage squareSubimage = original.getSubimage(x, y, size, size);

        // 4) Creamos un buffer ARGB de 'diameter' x 'diameter'
        BufferedImage circleBuffer = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = circleBuffer.createGraphics();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // 5) Hacemos el clip circular (0, 0, diameter, diameter)
            g2.setClip(new Ellipse2D.Float(0, 0, diameter, diameter));

            // 6) Dibujamos la subimagen cuadrada, redimensionándola a diameter x diameter
            g2.drawImage(squareSubimage, 0, 0, diameter, diameter, null);

        } finally {
            g2.dispose();
        }

        // 7) Retornamos el ImageIcon circular
        return new ImageIcon(circleBuffer);
    }



}
