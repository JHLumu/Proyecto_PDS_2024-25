# Piolify - Casos de Uso

## 1. Registrar Usuario

### Actor Principal
Usuario.

### Descripción
El usuario se registra para tener acceso completo a la plataforma.

### Diagrama:

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


## 3. Seleccionar un curso

* **Actor**: Usuario
* **Descripción**: El usuario visualiza el catálogo de cursos (o tipos de contenido) disponibles y selecciona el que desea realizar.

## 4. Elegir estrategia de aprendizaje

* **Actor**: Usuario
* **Descripción**: Antes de iniciar el curso, el usuario decide cuál estrategia utilizar (por ejemplo, secuencial, aleatoria, repetición espaciada o adaptativa).

## 5. Iniciar Sesion de Aprendizaje de un curso

* **Actor**: Usuario
* **Descripción**: La aplicación inicia el curso mostrando una serie de ejercicios cuyo orden depende de la estrategia elegida, permitiendo al usuario interactuar con cada elemento.

## 6. Interactuar con ejercicios/preguntas

* **Actor**: Usuario
* **Descripción**: Durante el curso, el usuario responde o interactúa con los distintos tipos de ejercicios (Completar Huecos, Corregir Código, Traducir Código, Completar Huecos o Opción Múltiple), según el dominio y tipo de contenido.

## 7. Guardar progreso del curso

* **Actor**: Usuario
* **Descripción**: Permite al usuario guardar el estado actual del curso para poder pausarlo y reanudarlo posteriormente desde el mismo punto.

## 8. Registrar estadísticas de Usuario

* **Actor**: Sistema
* **Descripción**: La aplicación registra datos de uso (tiempo de estudio, racha de días, etc.) durante una sesión de aprendizaje del usuario.

## 9. Consultar estadísticas de Usuario

* **Actor**: Usuario:
* **DescripcióN**: El usuario accede a sus estadísticas de uso.

## 10. Crear un curso

* **Actor**: Usuario (o creador de contenido)
* **Descripción**: Se permite la creación de nuevos cursos definiéndolos (por ejemplo, mediante ficheros JSON o YAML), facilitando la extensión a nuevos dominios.

## 11. Compartir curso

* **Actor**: Usuario
* **Descripción**: El usuario puede compartir un curso creado con otros usuarios.

## 12. Instalar curso de otro usuario.

* **Actor**: Usuario
* **Descripción**: El usuario puede instalar un curso que le ha compartido otro usuario.

## 13. Añadir Amigo

* **Actor**: Usuario
* **Descripción**: El usuario puede enviar una petición de amistad a otro usuario, y este puede aceptarlo o rechazarlo.


## 14. Eliminar Amigo
* **Actor**: Usuario
* **Descripción**: El usuario puede eliminar la amistad que tiene con un usuario.

## 15. Obtener Logro
* **Actor**: Sistema
* **Descripción**: El usuario obtiene un logro en función de una acción que haya realizado o debido a estadísticas de uso alcanzadas. El sistema puede lanzar este caso de uso dado a algún evento.



