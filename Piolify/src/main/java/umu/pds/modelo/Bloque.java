package umu.pds.modelo;

import java.util.Collections;
import java.util.List;

public class Bloque {
	
	private String titulo;
	private String descripcion;
	private List<Ejercicio> listaEjercicios;
	
	
	public String getTitulo() {
		return titulo;
	}
	
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	public String getDescripcion() {
		return descripcion;
	}
	
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public List<Ejercicio> getListaEjercicios() {
		return Collections.unmodifiableList(listaEjercicios);
	}
	
	public void setListaEjercicios(List<Ejercicio> listaEjercicios) {
		this.listaEjercicios = listaEjercicios;
	}
	
}
