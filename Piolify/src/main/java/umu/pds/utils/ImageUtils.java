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

/**
 * Clase utilitaria para operaciones relacionadas con imágenes.
 * Incluye escalado de imágenes, creación de iconos circulares y carga de imágenes desde diferentes fuentes.
 */
public class ImageUtils {

	/**
	 * Escala una imagen a un tamaño específico manteniendo la calidad.
	 * Utiliza interpolación bicúbica para mejorar la calidad de la imagen escalada.
	 * 
	 * @param imagen La imagen a escalar.
	 * @param ancho El ancho deseado de la imagen escalada.
	 * @param alto El alto deseado de la imagen escalada.
	 * @return BufferedImage con la imagen escalada.
	 */
	public static BufferedImage escalarImagenCalidad(Image imagen, int ancho, int alto) {
	    if (imagen == null) return null;
	    BufferedImage bufferedImagen;
	    if (imagen instanceof BufferedImage) {
	        bufferedImagen = (BufferedImage) imagen;
	    } else {
	        bufferedImagen = new BufferedImage(imagen.getWidth(null), imagen.getHeight(null), BufferedImage.TYPE_INT_ARGB);
	        Graphics2D g = bufferedImagen.createGraphics();
	        g.drawImage(imagen, 0, 0, null);
	        g.dispose();
	    }
	    
	    if (bufferedImagen.getWidth() == ancho && bufferedImagen.getHeight() == alto) {
	        return bufferedImagen;
	    }
	    
	    BufferedImage resultado = bufferedImagen;
	    int anchoActual = bufferedImagen.getWidth();
	    int altoActual = bufferedImagen.getHeight();
	    
	    while (anchoActual != ancho || altoActual != alto) {
	        int nuevoAncho, nuevoAlto;
	        
	        if (anchoActual > ancho) {
	            nuevoAncho = Math.max(ancho, anchoActual / 2);
	        } else if (anchoActual < ancho) {
	            nuevoAncho = Math.min(ancho, anchoActual * 2);
	        } else {
	            nuevoAncho = ancho;
	        }
	        
	        if (altoActual > alto) {
	            nuevoAlto = Math.max(alto, altoActual / 2);
	        } else if (altoActual < alto) {
	            nuevoAlto = Math.min(alto, altoActual * 2);
	        } else {
	            nuevoAlto = alto;
	        }
	        
	        BufferedImage temp = new BufferedImage(nuevoAncho, nuevoAlto, BufferedImage.TYPE_INT_ARGB);
	        Graphics2D g2d = temp.createGraphics();
	        
	        // renderizado en alta calidad
	        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
	        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
	        
	        g2d.drawImage(resultado, 0, 0, nuevoAncho, nuevoAlto, null);
	        g2d.dispose();
	        
	        resultado = temp;
	        anchoActual = nuevoAncho;
	        altoActual = nuevoAlto;
	    }
	    
	    return resultado;
	}
	
	/**
	 * Crea un icono circular a partir de una imagen.
	 * Si la imagen es nula, utiliza una imagen por defecto.
	 * 
	 * @param img La imagen a convertir en icono circular.
	 * @param diameter El diámetro del icono circular.
	 * @return ImageIcon con la imagen circular.
	 */
	public static ImageIcon createCircularIcon(Image img, int diameter) {
	    if (img == null) {
	        return createCircularIcon(getImagenPorDefecto(), diameter);
	    }

	    try {
	        BufferedImage bufferedImage;
	        if (img instanceof BufferedImage) {
	            bufferedImage = (BufferedImage) img;
	        } else {
	            bufferedImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
	            Graphics2D g = bufferedImage.createGraphics();
	            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
	            g.drawImage(img, 0, 0, null);
	            g.dispose();
	        }

	        int originalWidth = bufferedImage.getWidth();
	        int originalHeight = bufferedImage.getHeight();
	        int size = Math.min(originalWidth, originalHeight);
	        
	        int x = (originalWidth - size) / 2;
	        int y = (originalHeight - size) / 2;
	        
	        BufferedImage imagenCuadrada = bufferedImage.getSubimage(x, y, size, size);
	        BufferedImage imagenEscalada = escalarImagenCalidad(imagenCuadrada, diameter, diameter);
	        BufferedImage imagenCircular = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);
	        Graphics2D g2d = imagenCircular.createGraphics();
	        
	        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);    
	        g2d.setClip(new Ellipse2D.Float(0, 0, diameter, diameter));
	        g2d.drawImage(imagenEscalada, 0, 0, null);
	        g2d.dispose();

	        return new ImageIcon(imagenCircular);
	        
	    } catch (Exception e) {
	        System.err.println("Error creando icono circular: " + e.getMessage());
	        e.printStackTrace();
	        // En caso de error, intentar con imagen por defecto
	        if (img != getImagenPorDefecto()) {
	            return createCircularIcon(getImagenPorDefecto(), diameter);
	        }
	        return null;
	    }
	}
    
	/**
	 * Escala una imagen a un ancho específico manteniendo la proporción.
	 * Si la imagen es nula, utiliza una imagen por defecto.
	 * 
	 * @param imagePath Ruta de la imagen a escalar.
	 * @param width Ancho deseado de la imagen escalada.
	 * @return ImageIcon con la imagen escalada.
	 */
    public static ImageIcon escalarImagen(String imagePath, int width) {
        try {
            Image imagen = cargarImagen(imagePath);
            if (imagen == null) {
                return null;
            }
            
            // Calcular alto manteniendo proporción
            int originalWidth = imagen.getWidth(null);
            int originalHeight = imagen.getHeight(null);
            
            if (originalWidth <= 0 || originalHeight <= 0) {
                return null;
            }
            
            int height = (originalHeight * width) / originalWidth;
            BufferedImage imagenEscalada = escalarImagenCalidad(imagen, width, height);
            
            return new ImageIcon(imagenEscalada);
            
        } catch (Exception e) {
            System.err.println("Error al escalar la imagen: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
	/**
	 * Carga una imagen desde una ruta local, URL o recurso del classpath.
	 * Si la ruta es nula o vacía, devuelve una imagen por defecto.
	 * 
	 * @param ruta Ruta de la imagen a cargar.
	 * @return Image cargada desde la ruta especificada o imagen por defecto en caso de error.
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
	 * Obtiene una imagen por defecto para usar cuando no se proporciona una imagen válida.
	 * Utiliza un recurso del classpath llamado "fotoUser.png".
	 * 
	 * @return Image por defecto.
	 */
    public static Image getImagenPorDefecto() {
        return new ImageIcon(ImageUtils.class.getResource("/fotoUser.png")).getImage();
    }

	/**
	 * Verifica si una imagen es válida intentando cargarla desde la ruta especificada.
	 * Si la imagen no se puede cargar, devuelve false.
	 * @param ruta Ruta de la imagen a validar.
	 * @return true si la imagen es válida, false en caso contrario.
	 * */
    public static boolean esImagenValida(String ruta) {
        try {
            BufferedImage img = ImageIO.read(new File(ruta));
            return img != null;
        } catch (Exception e) {
            return false;
        }
    }



}
