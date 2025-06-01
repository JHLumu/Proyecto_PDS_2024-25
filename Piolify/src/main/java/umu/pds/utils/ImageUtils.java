package umu.pds.utils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ImageUtils {


    public static ImageIcon createCircularIcon(Image img, int diameter) {
        if (img == null) return null;

        // Convertir a BufferedImage si no lo es
        BufferedImage original;
        if (img instanceof BufferedImage) {
            original = (BufferedImage) img;
        } else {
            original = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = original.createGraphics();
            g2.drawImage(img, 0, 0, null);
            g2.dispose();
        }

        // Crear imagen circular
        BufferedImage circleBuffer = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = circleBuffer.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setClip(new Ellipse2D.Float(0, 0, diameter, diameter));
        g2.drawImage(original, 0, 0, diameter, diameter, null);
        g2.dispose();

        return new ImageIcon(circleBuffer);
    }
    
    public static ImageIcon escalarImagen(String imagePath, int width) {
        try {
            ImageIcon originalIcon;
            
            // Determina si es un recurso o una ruta de archivo
            if (imagePath.startsWith("/")) {
                // Es un recurso del classpath
                URL imageUrl = ImageUtils.class.getResource(imagePath);
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
    
    /**
     * Carga una imagen desde una ruta local, URL o recurso del classpath.
     */
    public static Image cargarImagen(String ruta) {
        try {
            if (ruta == null || ruta.isEmpty()) {
                return getImagenPorDefecto();
            } else if (ruta.startsWith("http://") || ruta.startsWith("https://")) {
                return ImageIO.read(new URL(ruta));
            } else if (ruta.startsWith("/") && ImageUtils.class.getResource(ruta) != null) {
                return new ImageIcon(ImageUtils.class.getResource(ruta)).getImage();
            } else {
                return ImageIO.read(new File(ruta));
            }
        } catch (Exception e) {
            System.err.println("Error al cargar imagen: " + e.getMessage());
            return getImagenPorDefecto();
        }
    }

    /**
     * Imagen por defecto en caso de error.
     */
    public static Image getImagenPorDefecto() {
        return new ImageIcon(ImageUtils.class.getResource("/fotoUser.png")).getImage();
    }


    /**
     * Valida si una ruta contiene una imagen válida.
     */
    public static boolean esImagenValida(String ruta) {
        try {
            BufferedImage img = ImageIO.read(new File(ruta));
            return img != null;
        } catch (Exception e) {
            return false;
        }
    }



}
