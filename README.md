Universidad TI (proyecto evaluación Módulo 6)
=============================================

Modelo de Datos:


URL publica pruebas	:  https://universidad-ti-production.up.railway.app/login

Este proyecto llamado UNIVERSIDAD-TI ha sido desarrollado conforme a los conocimientos adquiridos en el Módulo 6 de nuestro bootcamp y 
siguiendo las instrucciones de la evaluación (SpringEduManager).
Bauticé el proyecto como UNIVERSIDAD-TI, dado que al instalar las versiones de ayuda en la misma ruta donde instalo todos los proyectos, 
para no distraerme con los nombres parecidos, le puse un nombre muy diferente. 
Estuve haciendo paralelismos con las versiones de apoyo, por lo mismo que, recogí muchos aspectos de ellas 
(en especial de la versión senior).

La aplicación dispone de dos entornos de trabajo diferenciados: un Portal Administrativo, 
destinado a la gestión institucional, y un Portal del Estudiante, orientado a la consulta de información personal y académica.
Ambos entornos están protegidos mediante un mecanismo de autenticación y autorización basado en roles.

Seguridad y control de acceso
=============================
Universidad TI incorpora un sistema de seguridad basado en Spring Security, que administra la autenticación de usuarios y la autorización de operaciones según el rol asignado.
Las credenciales se almacenan en la base de datos, utilizando BCrypt para proteger las contraseñas.
Una vez autenticado, cada usuario es dirigido automáticamente al entorno que le corresponde:
•	ADMIN: acceso a las funciones de administración académica. 
•	ESTUDIANTE: acceso a su información personal, académica y operaciones de autoservicio autorizadas. 

La aplicación también protege sus servicios REST, diferenciando las operaciones administrativas de aquellas destinadas al estudiante autenticado.
Reglas de negocio

ROL Estudiante:
---------------
1)	El estudiante puede inscribirse en un curso una sola vez en el período, y su inscripción quedará registrada en la tabla estudiante_seccion.
2)	El estudiante puede desistir de su inscripción (eliminación), siempre y cuando no tenga ya registrada una nota en la tabla notas, asociada a dicha sección.

ROL Administrador:
-----------------
1)	El administrador puede ingresar nuevos estudiantes (tabla estudiante).
2)	El administrador puede eliminar a un estudiante (campo vigente=false).
Al eliminar al estudiante, los registros de estudiante_seccion y notas pertenecientes al estudiante, quedan eliminados físicamente.
3)	El administrador puede agregar y eliminar cursos (vigente=false).
4)	El administrador puede agregar secciones (tabla sección).
5)	El administrador puede eliminar secciones (tabla sección) siempre y cuando no existan notas asociadas a la sección en la tabla notas. (la eliminación de sección es mediante un delete del registro).
6)	El administrador puede registrar notas en la tabla de notas con toda libertad.
7)	La aplicación solo calculará el campo promedio de la tabla estudiante_seccion cuando se complete la cantidad de notas correspondiente a dicha sección (campo total_notas). Es decir, que cuando el administrador ingrese la última nota, se dará la situación descrita anteriormente.

APENDICE 1:  ARQUITECTURA Y TECNOLOGÍAS UTILIZADAS
---------------------------------------------------
Universidad TI ha sido desarrollada utilizando tecnologías del ecosistema Java, con una arquitectura organizada en capas que facilita la separación de responsabilidades.
Tecnología	Utilización en el proyecto
Java 21	Lenguaje principal de programación
Spring Boot	Estructura y ejecución de la aplicación
Spring MVC	Controladores web y servicios REST
Spring Data JPA	Persistencia y acceso a datos
Spring Security	Autenticación y autorización
Thymeleaf	Generación de las interfaces web dinámicas
HTML y CSS	Presentación visual de los portales
MySQL	Almacenamiento de información académica y usuarios
Maven	Administración de dependencias y construcción del proyecto

APENDICE 2: INVENTARIO ENDPOINTS DE REST
----------------------------------------
URL base: http://localhost:8080

1. Gestión de estudiantes
   
Método	Endpoint	Descripción
GET	/api/estudiantes	Lista todos los estudiantes.
GET	/api/estudiantes/curso/{codigoCurso}	Lista estudiantes vigentes inscritos en un curso.
GET	/api/estudiantes/{id}/carga	Consulta la carga académica de un estudiante.
GET	/api/estudiantes/{id}/notas	Consulta los cursos y calificaciones de un estudiante.
POST	/api/estudiantes	Registra un estudiante y crea su usuario.
PUT	/api/estudiantes/{id}	Actualiza los datos personales del estudiante.
DELETE	/api/estudiantes/{id}	Da de baja al estudiante.

2. Gestión de cursos

Método	Endpoint	Descripción
GET	/api/cursos	Lista los cursos registrados.
POST	/api/cursos	Registra un nuevo curso.
PUT	/api/cursos/{id}	Actualiza el nombre y la descripción de un curso.
DELETE	/api/cursos/{id}	Da de baja un curso.

3. Gestión de secciones

Método	Endpoint	Descripción
GET	/api/secciones	Lista las secciones académicas.
POST	/api/secciones	Crea una sección para un curso.
PUT	/api/secciones/{id}	Modifica una sección sin notas registradas.
DELETE	/api/secciones/{id}	Elimina una sección sin notas registradas.
		
4. Gestión de inscripciones
   
Método	Endpoint	Descripción
GET	/api/inscripciones	Lista las inscripciones registradas.
GET	/api/inscripciones/estudiante/{id}	Consulta las inscripciones de un estudiante.
GET	/api/inscripciones/seccion/{id}	Consulta los inscritos en una sección.
POST	/api/inscripciones	Inscribe un estudiante en una sección.
DELETE	/api/inscripciones/{id}	Retira una inscripción que no tenga notas.

5. Gestión de calificaciones
 
Método	Endpoint	Descripción
GET	/api/notas	Lista las calificaciones registradas.
GET	/api/notas/inscripcion/{id}	Consulta las notas de una inscripción.
POST	/api/notas	Registra una calificación individual.
POST	/api/notas/seccion/{id}	Registra una evaluación para todos los inscritos en una sección.

6. Servicios personales del estudiante

Estos endpoints utilizan la identidad del usuario autenticado. Por lo tanto, el estudiante no necesita proporcionar su ID.
Método	Endpoint	Descripción
GET	/api/mi-cuenta	Consulta los datos personales del estudiante.
GET	/api/mi-cuenta/carga	Consulta su carga académica.
GET	/api/mi-cuenta/notas	Consulta sus calificaciones y promedios.
GET	/api/mi-cuenta/plan	Consulta su plan curricular.
GET	/api/mi-cuenta/secciones-disponibles	Consulta las secciones disponibles para inscripción.
POST	/api/mi-cuenta/inscripciones/{idSeccion}	Inscribe al estudiante autenticado en una sección.
DELETE	/api/mi-cuenta/inscripciones/{idInscripcion}	Retira una inscripción propia, si está permitido.
________________________________________
Resumen cuantitativo
Módulo	Endpoints
Estudiantes	7
Cursos	4
Secciones	4
Inscripciones	5
Calificaciones	4
Mi cuenta	7
Total	31
Seguridad de los servicios
Grupo de endpoints	Rol autorizado
/api/estudiantes/**	ADMIN
/api/cursos/**	ADMIN
/api/secciones/**	ADMIN
/api/inscripciones/**	ADMIN
/api/notas/**	ADMIN
/api/mi-cuenta/**	ESTUDIANTE




