package umu.pds.modelo;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "progresos_curso")
public class ProgresoCurso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Usuario usuario;

    @ManyToOne
    private Curso curso;

    private int bloqueActual; // Índice o id del bloque actual
    private int ejercicioActual; // Índice o id del ejercicio actual

    @Enumerated(EnumType.STRING)
    private TipoEstrategia estrategia; // Estrategia elegida

    private Date fechaInicio;
    private Date fechaUltimoAcceso;

    /**
     * Constructor por defecto para crear un progreso de curso
     */
	public ProgresoCurso() {
	}
	
}