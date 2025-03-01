# Modelo de Dominio de Piolify

A continuación se presenta el diagrama de clases que representa el modelo de dominio de nuestra aplicación:

```mermaid
%%{ init: {"theme": "neutral"} }%%
classDiagram
    direction LR
    
    class Usuario {
        +String nombre
        +String email
        +String contraseña
        +agregarAmigo(Usuario)
    }
    
    class Curso {
        +String titulo
        +String descripcion
        +String dificultad
        +String autor
        +importar(String json)
        +exportar() String
    }
    
    class Bloque {
        +String titulo
        +String descripcion
    }
    
    class Ejercicio {
        +String contenido
        +int dificultad
        +Object respuestaCorrecta
        +evaluar(respuesta)
        +mostrar()
    }
    
    class TipoEjercicio {
        <<enumeration>>
        OPCION_MULTIPLE
        COMPLETAR_HUECOS
        FLASHCARD
        TRADUCCION_CODIGO
        CORREGIR_CODIGO
    }
    
    class Progreso {
        +Date ultimoAcceso
        +int puntuacion
        +List ejerciciosCompletados
        +guardarProgreso()
        +cargarProgreso()
        +calcularPorcentajeCompletado()
    }
    
    class SesionAprendizaje {
        +DateTime fechaInicio
        +DateTime fechaFin
        +int ejerciciosCompletados
        +int aciertos
        +iniciarSesion()
        +finalizarSesion()
        +siguienteEjercicio()
    }
    
    class EstrategiaAprendizaje {
        +obtenerSiguienteEjercicio()
        +inicializar(List ejercicios)
    }
    
    class TipoEstrategia {
        <<enumeration>>
        SECUENCIAL
        REPETICION_ESPACIADA
        ALEATORIA
        ADAPTATIVA
    }
    
    class Estadisticas {
        +int tiempoTotal
        +int diasRacha
        +int mejorRacha
        +int ejerciciosCompletados
        +double precision
        +actualizarRacha()
        +registrarActividad()
    }
    
    class Amistad {
        +Date fechaCreacion
        +String estado
        +enviarSolicitud()
        +aceptarSolicitud()
        +rechazarSolicitud()
    }
    
    class ResultadoEjercicio {
        +DateTime fecha
        +Object contenidoRespuesta
        +boolean esCorrecta
        +int tiempoRespuesta
        +String mensajeFeedback
        +String sugerenciaMejora
        +String materialAdicional
        +generarFeedback()
    }
    
    class Logro {
        +String nombre
        +String descripcion
        +String imagen
        +Date fechaObtencion
        +verificarCondicion(Usuario)
    }
    
    Usuario "1" --> "0..*" Progreso : tiene
    Usuario "1" --> "1" Estadisticas : posee
    Usuario "1" --> "0..*" Amistad : inicia
    Usuario "1" --> "0..*" Amistad : recibe
    Usuario "1" --> "0..*" SesionAprendizaje : realiza
    Usuario "1" --> "0..*" Logro : obtiene
    
    Curso "1" --> "1..*" Bloque : contiene
    Bloque "1" --> "1..*" Ejercicio : incluye
    
    Ejercicio "1" --> "1" TipoEjercicio : es de
    Ejercicio "1" --> "0..*" ResultadoEjercicio : genera
    
    EstrategiaAprendizaje "1" --> "1" TipoEstrategia : es de
    
    Progreso "1" --> "1" Curso : asociado a
    SesionAprendizaje "1" --> "1" EstrategiaAprendizaje : utiliza
    SesionAprendizaje "1" --> "1" Progreso : actualiza
    SesionAprendizaje "1" --> "0..*" ResultadoEjercicio : registra
    
    ResultadoEjercicio "0..*" --> "1" Usuario : pertenece a
