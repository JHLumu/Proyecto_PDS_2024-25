# Piolify - Casos de Uso

## Índice
- [1. Registrar Usuario](#1-registrar-usuario)
- [2. Iniciar Sesión Usuario](#2-iniciar-sesión-usuario)
- [3. Seleccionar un curso](#3-seleccionar-un-curso)
- [4. Elegir estrategia de aprendizaje](#4-elegir-estrategia-de-aprendizaje)
- [5. Iniciar Sesión de Aprendizaje](#5-iniciar-sesion-de-aprendizaje-de-un-curso)
- [6. Interactuar con ejercicios](#6-interactuar-con-ejerciciospreguntas)
- [7. Guardar progreso del curso](#7-guardar-progreso-del-curso)
- [8. Registrar estadísticas](#8-registrar-estadísticas-de-usuario)
- [9. Consultar estadísticas](#9-consultar-estadísticas-de-usuario)
- [10. Crear un curso](#10-crear-un-curso)
- [11. Compartir curso](#11-compartir-curso)
- [12. Instalar curso](#12-instalar-curso-de-otro-usuario)
- [13. Añadir Amigo](#13-añadir-amigo) Revisad los casos de uso y tratad de "compactarlos". De hecho, fijaros que hay casos de uso que tenéis que realmente no tienen funcionalidad, eso es una pista de que posiblemente no sea correcto y deba agregarse a otro caso de uso más grande.
- [14. Eliminar Amigo](#14-eliminar-amigo)
- [15. Obtener Logro](#15-obtener-logro)
  
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

## 3. Seleccionar un curso

* **Actor**: Usuario
* **Descripción**: El usuario visualiza el catálogo de cursos (o tipos de contenido) disponibles y selecciona el que desea realizar. Antes de iniciar el curso, el usuario decide cuál estrategia utilizar (por ejemplo, secuencial, aleatoria, repetición espaciada o adaptativa). Una vez iniciado el curso, se muestra una serie de ejercicios cuyo orden depende de la estrategia elegida, permitiendo al usuario interactuar con los distintos tipos de ejercicios (Completar Huecos, Corregir Código, Traducir Código o Opción Múltiple), según el dominio y tipo de contenido. Durante la sesión, la aplicación registra datos de uso (tiempo de esutido, racha de días, etc...)

---

## 4. Guardar progreso del curso

* **Actor**: Usuario
* **Descripción**: Permite al usuario guardar el estado actual del curso para poder pausarlo y reanudarlo posteriormente desde el mismo punto.

---


## 5. Consultar estadísticas de Usuario

* **Actor**: Usuario:
* **DescripcióN**: El usuario accede a sus estadísticas de uso.

---

## 6. Crear un curso

* **Actor**: Usuario (o creador de contenido)
* **Descripción**: Se permite la creación de nuevos cursos definiéndolos (por ejemplo, mediante ficheros JSON o YAML), facilitando la extensión a nuevos dominios.

---

## 7. Compartir e Instalar curso

* **Actor**: Usuario
* **Descripción**: El usuario puede compartir un curso creado con otros usuarios, que pueden añadirlo a su biblioteca interna.

---

## 8. Añadir Amigo

* **Actor**: Usuario
* **Descripción**: El usuario puede enviar una petición de amistad a otro usuario, y este puede aceptarlo o rechazarlo.

---

## 9. Eliminar Amigo
* **Actor**: Usuario
* **Descripción**: El usuario puede eliminar la amistad que tiene con un usuario.

---

## 10. Obtener Logro
* **Actor**: Sistema
* **Descripción**: El usuario obtiene un logro en función de una acción que haya realizado o debido a estadísticas de uso alcanzadas. El sistema puede lanzar este caso de uso dado a algún evento.



