package umu.pds.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import umu.pds.persistencia.CursoDAO;

public class CatalogoCursosTest {

	private CatalogoCursos catalogo;
	
	private CursoDAO cursoDAO;
	
	private Map<Long, Curso> cursos;
	
	 @BeforeEach
	    void setUp() throws MalformedURLException {
		 	cursos = new HashMap<Long, Curso>();
		 	cursoDAO = mock(CursoDAO.class);
	        
	        Curso curso1 = new Curso();
	        Curso curso2 = new Curso();
	        
	        curso1.setTitulo("Curso 1");
	        curso1.setDescripcion("curso 1");
	        curso1.setId((long) 1);
	        
	        curso2.setTitulo("Curso 2");
	        curso2.setDescripcion("curso 2");
	        curso2.setId((long) 2);

	        when(cursoDAO.recuperarTodosCursos())
	            .thenReturn(Arrays.asList(curso1, curso2));
	            
	        catalogo = new CatalogoCursos(cursoDAO, cursos);
	    }
	 
	 @Test
	    void testGetInstancia() {
		 	CatalogoCursos instancia1 = CatalogoCursos.getInstancia();
		 	CatalogoCursos instancia2 = CatalogoCursos.getInstancia();
	        
	        assertNotNull(instancia1);
	        assertSame(instancia1, instancia2);
	        
	    }
	 
	 @Test
	    void testNuevoCurso() {
	        Curso curso3 = new Curso();
	        curso3.setTitulo("Curso 3");
	        curso3.setDescripcion("curso 3");
	        curso3.setId((long) 3);
	        this.catalogo.nuevoCurso(curso3);
	        
	        Optional<Curso> cursoRecuperado = catalogo.obtenerCursoPorID(curso3.getId());
	        assertTrue(cursoRecuperado.isPresent());
	        assertEquals(curso3, cursoRecuperado.get());
	    }
	 
	 @Test
	 void testExisteCurso() {
		 assertTrue(this.catalogo.existeCurso((long) 1));
		 assertTrue(this.catalogo.existeCurso((long) 2));
		 assertFalse(this.catalogo.existeCurso((long) 3));
	 }
	 
	 @Test
	 void testObtenerCursoPorId() {
		 Optional<Curso> curso1 = catalogo.obtenerCursoPorID((long) 1);
		 Optional<Curso> curso2 = catalogo.obtenerCursoPorID((long) 2);
		 Optional<Curso> curso3 = catalogo.obtenerCursoPorID((long) 3);
	     assertTrue(curso1.isPresent());
	     assertTrue(curso2.isPresent());
	     assertFalse(curso3.isPresent());
	     assertEquals(cursos.get((long) 1), curso1.get());
	     assertEquals(cursos.get((long) 2), curso2.get());
	 }
	
	 @Test
	 void testCargarCatalogo() {
		 //cargarCatalogo se ha comprobado indirectamente, mediante las pruebas ya definidas.
	 }

}
