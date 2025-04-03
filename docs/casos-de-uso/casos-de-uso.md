# Piolify - Casos de Uso

## Índice
- [1. Registrar Usuario](#1-registrar-usuario)
- [2. Iniciar Sesión Usuario](#2-iniciar-sesión-usuario)
- [3. Seleccionar un curso](#3-realizar-un-curso)
- [4. Consultar Estadísticas y Progresos de Cursos](#4-consultar-estadísticas-y-progreso-de-los-cursos)
- [5. Crear Curso](#5-crear-un-curso)
- [6. Instalar un curso](#6-instalar-curso)
- [7. Enviar Petición de Amistad](#7-enviar-peticion-de-amistad)
- [8. Gestionar Peticiones de Amistad](#8-gestionar-peticiones-de-amistad)
- [9. Obtener Logro](#9-obtener-logro)
  
---


## 1. Registrar Usuario

### Actor Principal
Usuario.

### Descripción
El usuario se registra para tener acceso completo a la plataforma.

### Flujo Básico

1. El usuario inicia el proceso de registro.
2. El usuario introduce los datos necesarios (Nombre, Nombre de Usuario, Correo Electrónico, Contraseña) en la pantalla de registro.
3. El sistema comprueba los datos introducidos por el usuario.
4. El sistema valida los datos ingresados y registra al usuario, mostrando un mensaje de confirmación.

### Flujo Alternativo

#### 3a. Los datos introducidos por el usuario no son correctos.
    
1. El sistema muestra un mensaje de error informando los datos que son incorrectos.
2. Si el usuario decide volver a introducir las credenciales, el flujo vuelve al paso 2 del flujo básico.

### Postcondiciones

* El usuario queda registrado en el sistema.

---

## 2. Iniciar Sesión Usuario

### Actor Principal
Usuario.


### Descripción
El usuario inicia sesión para acceder a la plataforma con su cuenta.

### Precondiciones
* El usuario debe estar registrado en el sistema.

### Flujo Básico

1. El usuario accede al proceso de inicio de sesión.
2. El usuario introduce las credenciales necesarias (Nombre de Usuario/Correo Electrónico y Contraseña).
3. El sistema comprueba los datos introducidos.
4. El sistema autentica al usuario y le permite el acceso a la plataforma.

### Flujo Alternativo

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

### Actor Principal
Estudiante.

### Descripción
El estudiante accede a sus estadísticas (tiempo de estudio, racha de días, etc) y al progreso realizado en los cursos (porcentaje de completado de cada curso).

### Precondiciones
* El estudiante debe estar registrado en el sistema y haber iniciado sesión.

### Flujo Básico
1. El estudiante accede a la sección de estadísticas definida en el sistema.
2. El sistema muestra al estudiante una lista de los cursos en los que ha realizado al menos una sesión y un resumen de las estadísticas generales del estudiante: Tiempo de estudio en total, rachas de días máximo, porcentaje de aciertos total, ejercicios completados en total.
3. El estudiante selecciona de la lista un curso que ha realizado.
4. El sistema muestra estadísticas específicas del estudiante en dicho curso: Tiempo de estudio dedicado al curso, racha de días máximo en el curso, porcentaje de aciertos en el curso, ejercicios completados del curso.


---

## 5. Crear un curso

* **Actor**: Creador
* **Descripción**:El creador puede crear un curso proporcionando un nombre, descripción, dificultad esperada de todo el curso y de manera opcional una imagen. La definición del curso se realiza a través de un fichero estructurado en formato JSON o YAML. EL curso puede dividirse en varios bloques a los que se les puede asignar tambien un nombre y descripción a cada uno. Dentro de cada bloque el creador puede añadir una secuencia ordenada de ejercicios. Para poder introducir un ejercicio, el creador debe especificar su contenido según el tipo de ejercicio escogido (Completar Huecos, Corregir Código, Traducir Código u Opción Múltiple), junto a una o varias respuestas correcta. Adicionalmente se le puede añadir recursos complementarios (imagenes, videos, enlaces).

---

## 6. Instalar curso

### Actor Principal
Estudiante.

### Descripción
Un estudiante puede instalar un curso a su biblioteca interna adjuntando al sistema el archivo JSON/YAML del curso. Una vez se haya adjuntado, la aplicación muestra el curso y el estudiante puede acceder a él en cualquier momento.

### Precondiciones

* El estudiante debe estar registrado en el sistema y haber iniciado sesión.
* El archivo JSON/YAML del curso debe mantener la estructura esperada por el sistema.

### Flujo Básico

1. El estudiante accede a su biblioteca interna de cursos.
2. El sistema muestra la biblioteca interna del estudiante.
3. El estudiante escoge la opción de añadir un nuevo curso.
4. El estudiante selecciona el archivo JSON/YAML del curso que desea instalar.
5. El sistema verifica la estructura del archivo JSON/YAML.
6. El sistema registra el curso en la biblioteca interna del estudiante y le notifica que se ha añadido correctamente.
7. El sistema muestra en la biblioteca interna del estudiante el curso añadido, pudiendo acceder desde ese momento.

### Flujo Alternativo

#### 4a. El sistema comprueba que el archivo seleccionado no cumple con la estructura esperada.
1. El sistema notifica al estudiante que no se ha podido añadir el curso debido a que no cumple la estructura que el sistema espera.
2. El sistema muestra una opción al estudiante de visualizar la estructura esperada en el archivo JSON/YAML, y otra opción para reintentar la instalación del curso.
3. Si el estudiante escoge la segunda opción, se vuelve al paso 3 del flujo básico.

### Postcondiciones
* El curso añadido por el estudiante debe almacenarse en el sistema.

---

## 7. Enviar Peticion de Amistad

### Actor Principal
Usuario.

### Descripción
El usuario puede enviar una petición de amistad a otro usuario, y este puede aceptarlo o rechazarlo.

### Precondiciones
* Tanto el usuario que envía la petición como el que la recibe deben estar registrados en el sistema.
* El usuario emisor debe haber iniciado sesión en el sistema.

### Flujo Básico
1. El usuario accede a la opción de añadir amigos, dentro de la sección social definida dentro del sistema.
2. El sistema ofrece al usuario la búsqueda de usuarios registrados del sistema.
3. El usuario introduce el nombre del usuario al que desea envíar la petición y lo escoge.
4. El sistema muestra la opción de añadir amigo al usuario, y el usuario la selecciona.
5. El sistema notifica al emisor que la petición de amistad se ha enviado correctamente, y al receptor que ha recibido una nueva petición de amistad.
---

### Flujo Alternativo

#### 4a. El usuario receptor al que el emisor quiere enviar la petición ya esta añadido como amigo.
1. El sistema notifica al usuario emisor que el usuario receptor ya está añadido como amigo.
2. El sistema accede a la sección de amigos del sistema.

### Postcondiciones
* La petición de amistad debe quedar registrada en el sistema.
* El estado de la petición de amistad debe ser "Pendiente".


## 8. Gestionar Peticiones de Amistad

### Actor Principal
Usuario.

### Descripción
El usuario puede ver las solicitudes de amistad pendientes que ha recibido y decidir si las acepta o las rechaza.

### Precondiciones
* El usuarrio debe estar registrado en el sistema y haber iniciado sesión.

### Flujo Básico
1. El usuario accede a la opción de ver peticiones de amistad, dentro de la sección social definida dentro del sistema.
2. El sistema muestra una lista de peticiones de amistad que ha enviado y recibido el usuario, con su estado correspondiente.
3. El usuario selecciona la petición de amistad que desea gestionar, pudiendo realizar las siguientes acciones:
   * Si la petición de amistad ha sido enviada por el usuario, puede cancelar la petición de amistad.
   * Si la petición de amistad ha sido recibida, puede aceptar o cancelar la petición de amistad.
4. El usuario realiza la accion que desea sobre la petición de amistad escogida.
5. El sistema solicita al usuario una confirmación sobre la realización de la acción.
6. El usuario confirma la acción al sistema, notificando el sistema que la acción se ha realizado correctamente.

### Flujo Alternativo

#### 6a. La acción que se realiza es aceptar una petición de amistad.
1. La petición de amistad pasa del estado "Pendiente" a "Aceptada" y se elimina de la lista de peticiones de amistad tanto del usuario emisor como del usuario receptor.
2. El sistema registra la amistad entre ambos usuarios.
3. El sistema notifica al usuario emisor que la petición de amistad realizada al usuario receptor se ha aceptado.

##### 6b. La acción que se realiza es rechazar una petición de amistad.
1. La petición de amistad pasa del estado "Pendiente" a "Rechazada" y se elimina de la lista de peticiones de amistad tanto del usuario emisor como del usuario receptor.
2. El sistema notifica al usuario emisor que la petición de amistad realizada al usuario receptor se ha cancelado.

#### 6c. La acción que se realizada es cancelar una petición de amistad.
1. La petición de amistad se elimina de la lista de peticiones tanto del usuario emisor como del usuario receptor.

### Postcondiciones
* El sistema debe reflejar la lista de peticiones de amistad actualizadas tanto del usuario emisor como del usuario receptor.
* El sistema debe actualizar la lista de amigos tanto del usuario emisor como del usuario receptor.
---

## 9. Obtener Logro
* **Actor**: Sistema
* **Descripción**: El usuario obtiene un logro en función de una acción que haya realizado o debido a estadísticas de uso alcanzadas. El sistema puede lanzar este caso de uso dado a algún evento.



