# Modelo de Dominio de Piolify

A continuación se presenta el diagrama de clases que representa el modelo de dominio de nuestra aplicación:

```mermaid

%%{ init: {"theme": "neutral"} }%%
classDiagram
    EstrategiaAprendizaje <|-- Secuencial
    EstrategiaAprendizaje <|-- Aleatoria
    EstrategiaAprendizaje <|-- Repeticion_Espaciada
    Ejercicio <|-- Opcion_Multiple
    Ejercicio <|-- Completar_Huecos
    Ejercicio <|-- Flashcard


    direction LR
    
    class Usuario {
        +nombre
        +email
        +contraseña
        +genero
        +imagen
    }
    
    class Curso {
        +titulo
        +descripcion
        +dificultad
        +autor
    }
    
    class Bloque {
        +titulo
        +descripcion
    }
    
    class Ejercicio {
        +contenido
        +dificultad
        +respuestaCorrecta
    }
    
    class Progreso {
        +ultimoAcceso
        +puntuacion
        +ejerciciosCompletados

    }
    
    class SesionAprendizaje {
        +fechaInicio
        +fechaFin
        +aciertos
    }
    
    class EstrategiaAprendizaje {

    }
    
    class Estadisticas {
        +tiempoTotal
        +diasRacha
        +mejorRacha
        +ejerciciosCompletados
        +precision

    }


    class Amistad {
        +fechaCreacion
        +estado
    }
    
    class ResultadoEjercicio {
        +fecha
        +contenidoRespuesta
        +tiempoRespuesta
    }
    

    class Logro {
        +nombre
        +descripcion
        +imagen
        +fechaObtencion

    }
    
  
    Usuario "1" --> "0..*" Amistad : inicia
    Usuario "1" --> "0..*" Amistad : recibe
    Usuario "1" --> "0..*" Logro : obtiene
    Usuario "0..1" --> "1*" Curso : crea
    Curso "1" --> "1..*" Bloque : contiene
    Bloque "1" --> "1..*" Ejercicio : incluye
    
    Ejercicio "0..*" <-- "1" ResultadoEjercicio : asociado a
    Estadisticas "0..*" <-- "1" Logro : genera

    Progreso "1" --> "1" Curso : asociado a
    SesionAprendizaje "1" --> "1" EstrategiaAprendizaje : utiliza
    SesionAprendizaje "1" --> "1" Progreso : actualiza
    SesionAprendizaje "1" --> "0..*" ResultadoEjercicio : registra
    Usuario "1" --> "0..*" SesionAprendizaje : realiza
    Usuario "1" --> "0..*" Progreso : tiene
    Usuario "1" --> "1" Estadisticas : posee
    Usuario "0..*" <-- "1" ResultadoEjercicio : pertenece a 
  

