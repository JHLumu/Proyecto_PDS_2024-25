# Piolify - Casos de Uso

## Índice
- [1. Registrar Usuario](#1-registrar-usuario)
- [2. Iniciar Sesión Usuario](#2-iniciar-sesión-usuario)
- [3. Modificar Información Personal](#3-modificar-información-personal)
- [4. Cambiar contraseña](#4-cambiar-contraseña)
- [5. Realizar un curso](#5-realizar-un-curso)
- [6. Consultar estadísticas y logros](#6-consultar-estadísticas-y-logros)
- [7. Importar curso](#7-importar-curso)
- [8. Enviar Petición de Amistad](#8-enviar-peticion-de-amistad)
- [9. Gestionar Peticiones de Amistad](#9-gestionar-peticiones-de-amistad)
- [10. Obtener Logro](#10-obtener-logro)
  
---


## 1. Registrar Usuario

### Actor Principal
Usuario.

### Descripción
El usuario se registra para tener acceso completo a la plataforma.

### Flujo Básico

1. El usuario inicia el proceso de registro.
2. El usuario introduce los datos necesarios (Nombre, Nombre de Usuario, Correo Electrónico, Contraseña) en la pantalla de regi

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

#### 3a. Los datos introducidos por el usuario no son correctos
    
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

#### 3a. Las credenciales introducidas por el usuario son incorrectas
1. El sistema muestra un mensaje de error.
2. Si el usuario decide volver a introducir las credenciales, el flujo vuelve al paso 2 del flujo básico.

### Postcondiciones
* El usuario queda autenticado en el sistema y tiene acceso completo a la plataforma y a sus datos.

---

## 3. Modificar Información Personal

### Actor Principal
Usuario.

### Descripción
El usuario puede actualizar su información personal.

### Precondiciones
* El usuario debe estar registrado en el sistema y haber iniciado sesión.

### Flujo Básico
1. El usuario accede a la información de su perfil.
2. El sistema muestra al usuario su información registrada en el sistema ( Nombre, Apellidos, Email, Género).
3. El sistema permite al usuario modificar su información personal (Nombre, Apellidos).
4. El usuario actualiza su información personal.
5. El usuario notifica al sistema que quiere guardar los cambios.
6. El sistema actualiza la información del usuario registrada en el sistema y notifica del éxito de los cambios.

### Flujo Alternativo

#### 5a. El usuario introduce información no válida.
1. El sistema notifica al usuario de que la información introducida no es válida.

#### 6a. El sistema no ha podido actualizar la información del usuario
1. El sistema notifica al usuario del fracaso a la hora de aplicar los cambios.

### Postcondiciones
* El sistema debe actualizar la información personal del usuario
* El sistema debe reflejar la información personal actualizada del usuario.

---

## 4. Cambiar contraseña

### Actor Principal
Usuario.

### Descripción
El usuario cambia su contraseña actual por una nueva, verificando el sistema si el que realiza el cambio es el propietario de la cuenta.

### Precondiciones
* El usuario debe estar registrado en el sistema y haber iniciado sesión.

### Flujo Básico
1. El usuario accede a la información de su perfil.
2. El sistema solicita al usuario la contraseña actual y la contraseña nueva dos veces (como método de confirmación de que esa es la nueva contraseña que quiere).
3. El usuario introduce la contraseña actual y la contraseña nueva.
4. El usuario notifica al sistema que quiere aplicar el cambio.
5. El sistema verifica que la contraseña actual introducida por el usuario coincide con la almacenada en el sistema.
6. El sistema verifica que la contraseña nueva y la confirmación son idénticas.
7. El sistema actualiza la contraseña almacenada en el sistema, notificando al usuario del éxito del cambio.

### Flujo Alternativo

#### 4a. El usuario no introduce toda la información solicitada por el sistema
  1. El sistema notifica al usuario que no ha introducido toda la información necesaria para el cambio de contraseña.

#### 5a. La contraseña actual introducida es incorrecta.
  1. El sistema notifica al usuario de que la contraseña actual es incorrecta.

#### 5a. La contraseña nueva y la confiramción de contraseña nueva son distintas.
  1. El sistema notifica al usuario de que las contraseñas nuevas introducidas no coinciden.

---

## 5. Realizar un curso

### Actor Principal
Estudiante.

### Descripción.
El estudiante escoge uno de los cursos disponibles en el catálogo de cursos que desea realizar.

### Precondiciones
* El estudiante debe estar registrado en el sistema y haber iniciado sesión.

### Flujo Básico
1. El estudiante accede a la sección de cursos del sistema.
2. El sistema muestra la biblioteca interna del estudiante, con una lista de los cursos actuales que dispone.
3. El estudiante selecciona el curso que desea realizar.
4. El sistema solicita al estudiante que escoja la estrategia de aprendizaje que desea para la sesión de aprendizaje, mostrando las tres opciones con una breve descripción:
   * Estrategia secuencial.
   * Estrategia de repetición espaciada.
   * Estrategia adaptativa.
5. El usuario escoge la estrategia de aprendizaje que desee utilizar, registrando la elección el sistema.
6. El sistema muestra la lista de bloques del curso, pudiendo seleccionar el estudiante aquellos bloques que estén desbloqueados (bloques que ya haya completado, bloques que se hayan desbloqueado por completar los anteriores).
7. El estudiante selecciona el bloque que desee realizar.
8. El sistema inicia la sesión de aprendizaje mostrando una secuencia de ejercicios cuyo orden depende de la estrategia de aprendizaje elegida.
9. El estudiante responde cada uno de estos ejercicios, registrando el sistema varias estadísticas y el progreso realizado en la sesión (tiempo de estudio, porcentaje de aciertos y ejercicios completados). Para cada ejercicio que responde, si el estudiante acierta, el sistema le notifica que la respuesta es correcta. En caso contrario, muestra una respuesta correcta esperada.
10. El estudiante termina de responder la secuencia de ejercicios, notificando el sistema que ha completado el bloque y volviendo a la biblioteca interna de cursos.

### Flujo Alternativo

#### 7a. El estudiante escoge un bloque de aprendizaje inacabado
1. El sistema recupera el punto en el que el estudiante estuvo antes de salirse de la sesión.
2. El sistema continúa la sesión de aprendizaje desde ese punto.

#### 9a. El estudiante se sale de la sesión de aprendizaje sin terminar la secuencia de ejercicios
1. El sistema guarda el estado actual de la sesión de aprendizaje y del curso.
2. El estudiante regresa a la biblioteca interna de loscursos, pudiendo reanudar la sesión de aprendizaje desde el mismo punto.

### Postcondiciones

* El sistema debe actualizar las estadísticas generales y específicas del estudiante.
* El sistema debe actualizar la lista de bloques de aprendizaje, desbloqueando bloques de aprendizaje si es necesario y si se ha completado un bloque.

---

## 6. Consultar estadísticas y logros

### Actor Principal
Estudiante.

### Descripción
El estudiante accede a sus estadísticas (tiempo de estudio, racha de días, etc), al progreso realizado en los cursos (porcentaje de completado de cada curso) y a los logros que ha obtenido.

### Precondiciones
* El estudiante debe estar registrado en el sistema y haber iniciado sesión.

### Flujo Básico
1. El estudiante accede a la sección de estadísticas definida en el sistema.
2. El sistema muestra al estudiante:
   * Una lista de los cursos en los que ha realizado al menos una sesión y un resumen de las estadísticas generales del estudiante: Tiempo de estudio en total, rachas de días máximo, porcentaje de aciertos total, ejercicios completados en total.
   * Una lista de logros que ha obtenido el usuario.
3. El estudiante selecciona de la lista un curso que ha realizado.
4. El sistema muestra estadísticas específicas del estudiante en dicho curso: Tiempo de estudio dedicado al curso, racha de días máximo en el curso, porcentaje de aciertos en el curso, ejercicios completados del curso.


---

## 7. Importar curso

### Actor Principal
Estudiante.

### Descripción
Un estudiante puede instalar un curso a su biblioteca interna adjuntando al sistema el archivo JSON/YAML del curso. Una vez se haya adjuntado, la aplicación muestra el curso y el estudiante puede acceder a él en cualquier momento.

### Precondiciones

* El estudiante debe estar registrado en el sistema y haber iniciado sesión.

### Flujo Básico

1. El estudiante accede a la sección de cursos de la aplicación.
2. El sistema muestra una opción para importar un curso.
3. El estudiante escoge la opción de importar un curso.
4. El estudiante selecciona el archivo JSON/YAML del curso que desea instalar.
5. El sistema verifica la estructura del archivo JSON/YAML.
6. El sistema registra el curso en la biblioteca interna del estudiante y le notifica que se ha añadido correctamente.
7. El sistema muestra en la biblioteca interna del estudiante el curso añadido, pudiendo acceder desde ese momento.

### Flujo Alternativo

#### 4a. El sistema comprueba que el archivo seleccionado no cumple con la estructura esperada
1. El sistema notifica al estudiante que no se ha podido añadir el curso debido a que no cumple la estructura que el sistema espera.
2. El sistema muestra una opción al estudiante de visualizar la estructura esperada en el archivo JSON/YAML, y otra opción para reintentar la instalación del curso.
3. Si el estudiante escoge la segunda opción, se vuelve al paso 3 del flujo básico.

### Postcondiciones
* El curso añadido por el estudiante debe almacenarse en el sistema.

---

## 8. Enviar Peticion de Amistad

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

### Flujo Alternativo

#### 4a. El usuario receptor al que el emisor quiere enviar la petición ya esta añadido como amigo
1. El sistema notifica al usuario emisor que el usuario receptor ya está añadido como amigo.
2. El sistema accede a la sección de amigos del sistema.

### Postcondiciones
* La petición de amistad debe quedar registrada en el sistema.
* El estado de la petición de amistad debe ser "Pendiente".


## 9. Gestionar Peticiones de Amistad

### Actor Principal
Usuario.

### Descripción
El usuario puede ver las solicitudes de amistad pendientes que ha recibido y decidir si las acepta o las rechaza.

### Precondiciones
* El usuario debe estar registrado en el sistema y haber iniciado sesión.

### Flujo Básico
1. El usuario accede a la opción de ver peticiones de amistad, dentro de la sección social definida dentro del sistema.
2. El sistema muestra una lista de peticiones de amistad que ha enviado y recibido el usuario, con su estado correspondiente.
3. El usuario selecciona la petición de amistad que desea gestionar, pudiendo realizar las siguientes acciones:
   * Si la petición de amistad ha sido enviada por el usuario, puede cancelar la petición de amistad.
   * Si la petición de amistad ha sido recibida, puede aceptar o cancelar la petición de amistad.
4. El usuario realiza la acción que desea sobre la petición de amistad escogida.
5. El sistema solicita al usuario una confirmación sobre la realización de la acción.
6. El usuario confirma la acción al sistema, notificando el sistema que la acción se ha realizado correctamente.

### Flujo Alternativo

#### 6a. La acción que se realiza es aceptar una petición de amistad
1. La petición de amistad pasa del estado "Pendiente" a "Aceptada" y se elimina de la lista de peticiones de amistad tanto del usuario emisor como del usuario receptor.
2. El sistema registra la amistad entre ambos usuarios.
3. El sistema notifica al usuario emisor que la petición de amistad realizada al usuario receptor se ha aceptado.

#### 6b. La acción que se realiza es rechazar una petición de amistad
1. La petición de amistad pasa del estado "Pendiente" a "Rechazada" y se elimina de la lista de peticiones de amistad tanto del usuario emisor como del usuario receptor.
2. El sistema notifica al usuario emisor que la petición de amistad realizada al usuario receptor se ha rechazado.

#### 6c. La acción que se realizada es cancelar una petición de amistad
1. La petición de amistad se elimina de la lista de peticiones tanto del usuario emisor como del usuario receptor.

### Postcondiciones
* El sistema debe reflejar la lista de peticiones de amistad actualizadas tanto del usuario emisor como del usuario receptor.
* El sistema debe actualizar la lista de amigos tanto del usuario emisor como del usuario receptor.
---

## 10. Obtener Logro

### Actor principal
Sistema.

### Descripción
El sistema verifica y otorga logros al usuario cuando este accede al panel de estadísticas. Durante esta consulta, el sistema evalúa las condiciones de todos los logros disponibles y otorga automáticamente aquellos que el usuario haya cumplido pero aún no posea.

### Precondiciones
* El usuario debe estar registrado y autenticado en el sistema
* El usuario accede al panel de estadísticas
* Existen logros definidos en el catálogo del sistema
* El usuario ha realizado acciones o alcanzado estadísticas que pueden generar logros

### Flujo Básico
1. El usuario accede al panel de estadísticas
2. El sistema obtiene las estadísticas actuales del usuario
3. El sistema recupera la lista de todos los logros disponibles en el catálogo
4. El sistema recupera los logros que el usuario ya posee
5. Para cada logro no poseído, el sistema evalúa si se cumplen sus condiciones específicas
6. Si se cumplen las condiciones de un logro, el sistema lo registra en el perfil del usuario
7. El sistema presenta el panel de estadísticas con todos los logros (incluyendo los recién obtenidos)

### Flujo Alternativo
#### 5a. No se cumplen condiciones para nuevos logros
1. El sistema registra que no hay nuevos logros para otorgar
2. Se presenta el panel con los logros existentes únicament

### Postcondiciones
* Los nuevos logros obtenidos se han registrado en el perfil del usuario
* El panel de estadísticas muestra todos los logros del usuario (anteriores y nuevos)
* Se ha registrado la fecha de obtención de los nuevos logros


