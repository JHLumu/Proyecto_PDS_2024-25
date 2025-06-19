package umu.pds.controlador;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import umu.pds.modelo.Bloque;
import umu.pds.modelo.Curso;
import umu.pds.modelo.Ejercicio;
import umu.pds.modelo.EstrategiaFactory;
import umu.pds.modelo.TipoEstrategia;
import umu.pds.modelo.Usuario;
import umu.pds.servicios.CursoService;
import umu.pds.servicios.ServicioImportacion;
import umu.pds.servicios.ServicioImportacion.ResultadoImportacion;
import umu.pds.servicios.importacion.ImportacionException;

public class ImportacionControllerTest {

	private ServicioImportacion servicioImportacion;
	
	private CursoService cursoService;
	
	private ImportacionController importacionController;
	
	private Usuario usuario;
	
	
	private String rutaArchivo;
	
	@BeforeEach
	  void setUp() {
		rutaArchivo = "curso_ejemplo.json";
		usuario = mock(Usuario.class);
		servicioImportacion = mock(ServicioImportacion.class);
		cursoService = mock(CursoService.class);
		importacionController = new ImportacionController(servicioImportacion, cursoService);
		
    }
	
	static List<Arguments> casosBasicos() {
		
		Curso curso1 = mock(Curso.class);
		Curso curso2 = mock(Curso.class);
		Bloque bloque = mock(Bloque.class); 
		Ejercicio ejercicio = mock(Ejercicio.class);
		when(curso1.getTitulo()).thenReturn("Mock");
		when(curso2.getTitulo()).thenReturn("Mock");
		when(bloque.getEjercicios()).thenReturn(List.of(ejercicio));
		when(curso1.getBloques()).thenReturn(List.of(bloque));
		when(curso2.getBloques()).thenReturn(List.of(bloque));
		return List.of(
				Arguments.of(true, Collections.emptyList()),
				Arguments.of(true, List.of(curso1)),
				Arguments.of(true, List.of(curso1, curso2)),
				Arguments.of(false, Collections.emptyList()),
				Arguments.of(false, List.of(curso1)),
				Arguments.of(false, List.of(curso1, curso2))
				);
		
		
	}
	
	static List<Exception> casosExcepcion() {
		return List.of (new ImportacionException(null,null), new RuntimeException());
	}
	
	private ResultadoImportacion inicializarResultadoImportacion(boolean res, List<Curso> cursos) {
		ResultadoImportacion resultado = mock(ResultadoImportacion.class);
		when(resultado.fueExitoso()).thenReturn(res);
		when(resultado.getCursos()).thenReturn(cursos);
		when(resultado.getCantidadImportada()).thenReturn(cursos.size());
		when(resultado.getFormatoUtilizado()).thenReturn("json");
		return resultado;
	}
	
	
	@ParameterizedTest
	@MethodSource("casosBasicos")
	void testImportarCursosDesdeArchivo(boolean res, List<Curso> cursos) throws ImportacionException {
		ResultadoImportacion resultado = inicializarResultadoImportacion(res, cursos);
		when(servicioImportacion.importarDesdeArchivo(rutaArchivo)).thenReturn(resultado);
		when(usuario.getBiblioteca()).thenReturn(Collections.emptyList());
		
		assertEquals(res, importacionController.importarCursosDesdeArchivo(rutaArchivo, usuario));
		verify(servicioImportacion).importarDesdeArchivo(rutaArchivo);
		verifyNoMoreInteractions(servicioImportacion, cursoService);
	}

	
	@ParameterizedTest
	@MethodSource("casosBasicos")
	void testImportarCursoDesdeStream(boolean res, List<Curso> cursos) throws ImportacionException {
		ResultadoImportacion resultado = inicializarResultadoImportacion(res, cursos);
		InputStream stream = mock(InputStream.class);
		when(servicioImportacion.importarDesdeStream(stream, resultado.getFormatoUtilizado())).thenReturn(resultado);
		when(usuario.getBiblioteca()).thenReturn(cursos);
		
		assertEquals(res, importacionController.importarCursosDesdeStream(stream, resultado.getFormatoUtilizado(), usuario));
		verify(servicioImportacion).importarDesdeStream(stream, resultado.getFormatoUtilizado());
		verifyNoMoreInteractions(servicioImportacion, cursoService);
	}

	
	
	@ParameterizedTest
	@MethodSource("casosExcepcion")
	void testImportarCursoDesdeArchivoThrowException(Exception e){
		
		try {
			when(servicioImportacion.importarDesdeArchivo(rutaArchivo)).thenThrow(e);
			assertFalse(importacionController.importarCursosDesdeArchivo(rutaArchivo, usuario));
			verify(servicioImportacion).importarDesdeArchivo(rutaArchivo);
			verifyNoMoreInteractions(servicioImportacion, cursoService);
		} catch (Exception e1) {}
				
	}
	
	@ParameterizedTest
	@MethodSource("casosExcepcion")
	void testImportarCursoDesdeStreamThrowException(Exception e){
		
		try {
			InputStream stream = mock(InputStream.class);
			when(servicioImportacion.importarDesdeStream(stream, "json")).thenThrow(e);
			assertFalse(importacionController.importarCursosDesdeStream(stream, "json", usuario));
			verify(servicioImportacion).importarDesdeStream(stream, "json");
			verifyNoMoreInteractions(servicioImportacion, cursoService);
		} catch (Exception e1) {}
				
	}
	
	@Test
	void testGetFormatosSoportados() {

		String[] formatos = {"json", "yaml"};
		when(servicioImportacion.getFormatosSoportados()).thenReturn(formatos);
		assertArrayEquals(formatos, importacionController.getFormatosSoportados());
	}
	

	
	static List<Arguments> casosSoportaFormato(){
		return List.of(
				Arguments.of(true, "json"),
				Arguments.of(true, "yaml"),
				Arguments.of(false, "csv")
				);
	}
	
	@ParameterizedTest
	@MethodSource("casosSoportaFormato")
	void testSoportaFormato(boolean res, String extension) {
		when(servicioImportacion.soportaFormato(extension)).thenReturn(res);
		assertEquals(res, importacionController.soportaFormato(extension));
	}
	
	@Test
	void testGetTiposEstrategiasDefinidas() {
		List<TipoEstrategia> estrategias = importacionController.getTiposEstrategiasDefinidas();
		assertNotNull(estrategias);
		assertFalse(estrategias.isEmpty());
	}

	
	
}
