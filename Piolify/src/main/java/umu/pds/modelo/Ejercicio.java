package umu.pds.modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class Ejercicio {
	private final String id;
	private final LocalDateTime fechaCreacion;
	private int dificultad;
	private String contenido;
	private String respuesta;
	
	public Ejercicio() {
		this.id = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
		this.fechaCreacion = LocalDateTime.now();
		this.dificultad = 5;
	}
	
	public Ejercicio(String contenido, String respuesta) {
		this.id = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
		this.fechaCreacion = LocalDateTime.now();
		this.dificultad = 5;
		this.contenido = contenido;
		this.respuesta = respuesta;
	}
	
	public String getId() {
		return this.id;
	}
	
	public LocalDateTime getFechaCreacion(){
		return this.fechaCreacion;
	}
	
	public int getDificultad() {
		return this.dificultad;
	}
	
	public void setDificultad(int dificultad) {
		this.dificultad = dificultad;
	}
	
	public String getContenido() {
		return this.contenido;
	}
	
	public String getRespuesta() {
		return this.respuesta;
	}
	
	public void setContenido(String contenido) {
		this.contenido = contenido;
	}
	
	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}
	
	public abstract void renderEjercicio();
	
	public abstract void validarRespuesta();
	

}
