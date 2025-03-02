# Casos de uso

1. **Seleccionar un curso**  
   - **Actor:** Usuario  
   - **Descripción:** El usuario visualiza el catálogo de cursos (o tipos de contenido) disponibles y selecciona el que desea realizar.

2. **Elegir estrategia de aprendizaje**  
   - **Actor:** Usuario  
   - **Descripción:** Antes de iniciar el curso, el usuario decide cuál estrategia utilizar (por ejemplo, secuencial, aleatoria, o repetición espaciada).

3. **Iniciar y realizar un curso**  
   - **Actor:** Usuario  
   - **Descripción:** La aplicación inicia el curso mostrando tarjetas o ejercicios de forma secuencial según la estrategia elegida, permitiendo al usuario interactuar con cada elemento (responder preguntas o visualizar contenido en el caso de flashcards).

4. **Interactuar con ejercicios/preguntas**  
   - **Actor:** Usuario  
   - **Descripción:** Durante el curso, el usuario responde o interactúa con los distintos tipos de preguntas (preguntas tipo test, completar huecos, traducir, etc.), según el dominio y tipo de contenido.

5. **Guardar y reanudar el progreso del curso**  
   - **Actor:** Usuario  
   - **Descripción:** Permite al usuario guardar el estado actual del curso para poder pausarlo y reanudarlo posteriormente desde el mismo punto.

6. **Registrar y consultar estadísticas de uso**  
   - **Actor:** Sistema (con información para el usuario)  
   - **Descripción:** La aplicación registra datos de uso (tiempo de estudio, racha de días, etc.) y permite al usuario consultar estas estadísticas.

7. **Crear un curso**  
   - **Actor:** Usuario (o creador de contenido)  
   - **Descripción:** Se permite la creación de nuevos cursos definiéndolos (por ejemplo, mediante ficheros JSON o YAML), facilitando la extensión a nuevos dominios.

8. **Compartir e instalar cursos**  
   - **Actor:** Usuario  
   - **Descripción:** El usuario puede compartir un curso creado con otros usuarios y, a su vez, instalar nuevos cursos en su biblioteca interna.

9. **Extender el tipo de preguntas**  
   - **Actor:** (Generalmente, desarrolladores o administradores)  
   - **Descripción:** El sistema debe permitir añadir nuevos tipos de preguntas de forma sencilla para ampliar los dominios de aplicación. (Este caso de uso está relacionado con la extensibilidad del sistema).

