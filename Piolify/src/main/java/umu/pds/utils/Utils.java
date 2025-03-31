package umu.pds.utils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

import javax.swing.ImageIcon;

public class Utils {


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
    
    public static ImageIcon escalarImagen(String imagePath, int width) {
        try {
            ImageIcon originalIcon;
            
            // Determina si es un recurso o una ruta de archivo
            if (imagePath.startsWith("/")) {
                // Es un recurso del classpath
                URL imageUrl = Utils.class.getResource(imagePath);
                if (imageUrl == null) {
                    System.err.println("No se pudo encontrar el recurso: " + imagePath);
                    return null;
                }
                originalIcon = new ImageIcon(imageUrl);
            } else {
                // Es una ruta de archivo
                File file = new File(imagePath);
                if (!file.exists()) {
                    System.err.println("No se pudo encontrar el archivo: " + imagePath);
                    return null;
                }
                originalIcon = new ImageIcon(imagePath);
            }
            
            // Obtener dimensiones originales
            int originalWidth = originalIcon.getIconWidth();
            int originalHeight = originalIcon.getIconHeight();
            
            // Si la imagen es inválida o ya tiene el tamaño deseado
            if (originalWidth <= 0 || originalHeight <= 0 || originalWidth == width) {
                return originalIcon;
            }
            
            // Calcular el nuevo alto manteniendo la proporción
            int height = (originalHeight * width) / originalWidth;
            
            // Escalar la imagen
            Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            
            // Crear un nuevo ImageIcon con la imagen escalada
            return new ImageIcon(scaledImage);
        } catch (Exception e) {
            System.err.println("Error al escalar la imagen: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }



}
