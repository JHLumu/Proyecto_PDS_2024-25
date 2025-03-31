# Piolify - Casos de Uso

## Índice
- [1. Registrar Usuario](#1-registrar-usuario)
- [2. Iniciar Sesión Usuario](#2-iniciar-sesión-usuario)
- [3. Seleccionar un curso](#3-realizar-un-curso)
- [4. Consultar Estadísticas y Progresos de Cursos](#4-consultar-estadísticas-y-progreso-de-los-cursos)
- [5. Crear Curso](#5-crear-un-curso)
- [6. Instalar un curso](#6-instalar-un-curso)
- [7. Añadir Amigo](#7-añadir-amigo)
- [8. Eliminar Amigo](#8-eliminar-amigo)
- [9. Obtener Logro](#15-obtener-logro)
  
---


## 1. Registrar Usuario

### Actor Principal
Usuario.

### Descripción
El usuario se registra para tener acceso completo a la plataforma.

### Flujo Básico:

1. El usuario inicia el proceso de registro.
2. El usuario introduce los datos necesarios (Nombre, Nombre de Usuario, Correo Electrónico, Contraseña) en la pantalla de registro.
3. El sistema comprueba los datos introducidos por el usuario.
4. El sistema valida los datos ingresados y registra al usuario, mostrando un mensaje de confirmación.

### Flujo Alternativo:

#### 3a. Los datos introducidos por el usuario no son correctos.
    
1. El sistema muestra un mensaje de error informando los datos que son incorrectos.
2. Si el usuario decide volver a introducir las credenciales, el flujo vuelve al paso 2 del flujo básico.

### Postcondiciones:

* El usuario queda registrado en el sistema.

---

## 2. Iniciar Sesión Usuario

### Actor Principal
Usuario.


### Descripción
El usuario inicia sesión para acceder a la plataforma con su cuenta.

### Precondiciones
* El usuario debe estar registrado en el sistema.

### Flujo Básico:

1. El usuario accede al proceso de inicio de sesión.
2. El usuario introduce las credenciales necesarias (Nombre de Usuario/Correo Electrónico y Contraseña).
3. El sistema comprueba los datos introducidos.
4. El sistema autentica al usuario y le permite el acceso a la plataforma.

### Flujo Alternativo:

#### 3a. Las credenciales introducidas por el usuario son incorrectas.
1. El sistema muestra un mensaje de error.
2. Si el usuario decide volver a introducir las credenciales, el flujo vuelve al paso 2 del flujo básico.

### Postcondiciones
* El usuario queda autenticado en el sistema y tiene acceso completo a la plataforma.

---

## 3. Realizar un curso
* **Actor**: Estudiante
* **Descripciön**: El estudiante escoge uno de los cursos disponibles en el catálogo de cursos que desea realizar. Antes de iniciar el curso, decide cuál estrategia utilizar (secuencial, aleatoria, repetición espaciada o adaptativa). Una vez iniciado el curso, se muestra una serie de ejercicios cuyo orden depende de la estrategia elegida, permitiendo al usuario interactuar con los distintos tipos de ejercicios (Completar Huecos, Corregir Código, Traducir Código u Opción Múltiple), según el dominio y tipo de contenido. Durante la sesión, el sistema registra datos de uso del estudiante (tiempo de estudio, racha de días, etc...) y el progreso que realiza durante la realización del curso, permitiendo al usuario guardar el estado actual del curso para poder pausarlo y reanudarlo posteriormente desde el mismo punto.

---

## 4. Consultar estadísticas y progreso de los cursos

* **Actor**: Estudiante
* **Descripción**: El estudiante accede a sus estadísticas (tiempo de estudio, racha de días, etc) y al progreso realizado en los cursos (porcentaje de completado de cada curso).

---

## 5. Crear un curso

* **Actor**: Creador
* **Descripción**:El creador puede crear un curso proporcionando un nombre, descripción, dificultad esperada de todo el curso y de manera opcional una imagen. La definición del curso se realiza a través de un fichero estructurado en formato JSON o YAML. EL curso puede dividirse en varios bloques a los que se les puede asignar tambien un nombre y descripción a cada uno. Dentro de cada bloque el creador puede añadir una secuencia ordenada de ejercicios. Para poder introducir un ejercicio, el creador debe especificar su contenido según el tipo de ejercicio escogido (Completar Huecos, Corregir Código, Traducir Código u Opción Múltiple), junto a una o varias respuestas correcta. Adicionalmente se le puede añadir recursos complementarios (imagenes, videos, enlaces).

---

## 6. Instalar curso

* **Actor**: Estudiante
* **Descripción**: Un estudiante puede instalar un curso a su biblioteca interna adjuntando al sistema el archivo JSON/YAML del curso. Una vez se haya adjuntado, la aplicación muestra el curso y el estudiante puede acceder a él en cualquier momento.

---

## 7. Añadir Amigo

* **Actor**: Estudiante/Creador
* **Descripción**: El usuario puede enviar una petición de amistad a otro usuario, y este puede aceptarlo o rechazarlo.

---

## 8. Eliminar Amigo
* **Actor**: Estudiante/Creador
* **Descripción**: El usuario puede eliminar la amistad que tiene con un usuario.

---

## 9. Obtener Logro
* **Actor**: Sistema
* **Descripción**: El usuario obtiene un logro en función de una acción que haya realizado o debido a estadísticas de uso alcanzadas. El sistema puede lanzar este caso de uso dado a algún evento.



