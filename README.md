# Capsula de Cloud computing

Sistema de Gestión de Cursos en GCP (Google Cloud Platform)

Desarrollar una API REST utilizando Spring Boot que permita el Alta y Consulta de cursos. La
solución debe integrar Google Cloud Storage para la gestión de archivos PDF y Google Cloud SQL para
la persistencia de datos.

### Requisitos Funcionales:

1. Gestión de Cursos:
    * Permitir a los profesores crear y eliminar cursos, incluyendo los siguientes detalles: Nombre,
      Categoría, Descripción, Link y Lista de Documentos PDF. ---> ***[OK]***
    * Los documentos PDF se almacenan en Google Cloud Storage. ---> ***[OK]***

2. Consulta de Cursos:
    * Permitir a los alumnos obtener el listado de todos los cursos, filtrando por Categoría.
      ---> ***[OK]***
    * Permitir a los alumnos descargar el listado de archivos PDF correspondiente a un determinado
      curso, filtrando por Nombre. ---> ***[OK]***

### Requisitos Técnicos:

1. Utilizar Spring Boot para desarrollar la aplicación. ---> ***[OK]***

2. Utilizar base de datos relacional (Google Cloud SQL). ---> ***[OK]***

3. Integrar Google Cloud Storage para el almacenamiento y recuperación de los archivos PDF.
   ---> ***[OK]***

4. Desplegar la API usando cualquiera de los engines aprendidos en el curso (GAP, GCE o GKE).
   ---> ***[OK]***

5. Autenticación: Utilizar Oauth 2.0 o JWT integrado con GCP como medio de autenticación y
   autorización en los servicios. ---> ***[EN CURSO ...]***

# Casos de uso - Demostración

La aplicación, denominada **course-management**, está desplegada en Google Cloud.

Los servicios expuestos están disponibles en
su [Swagger](https://cloud-computing-capsule-acc.rj.r.appspot.com/api/swagger-ui/index.html).

## Creación de Cursos

Para crear cursos, puedes utilizar el siguiente endpoint:

```
POST https://cloud-computing-capsule-acc.rj.r.appspot.com/api/courses
```

El cuerpo de la solicitud debe ser un JSON con los detalles del curso:

```json
{
  "name": "Curso 1",
  "description": "Primer curso",
  "category": "Categoria 1",
  "link": "www.cursos.com/curso1"
}
```

## Agregar Documentos a Cursos

Puedes agregar archivos PDF a la lista de documentos de un curso utilizando este endpoint:

```
POST https://cloud-computing-capsule-acc.rj.r.appspot.com/api/courses/document/upload
```

La solicitud debe incluir un cuerpo en formato form-data, con los archivos PDF que deseas agregar y
el nombre del curso al que pertenecen:

```
curl --location 'https://cloud-computing-capsule-acc.rj.r.appspot.com/api/courses/document/upload' \
--form 'files=@".../archivo.pdf"' \
--form 'course="Curso 1"'
```

## Consultas

Puedes realizar consultas para obtener información sobre los cursos. Por ejemplo, puedes filtrar
cursos por categoría:

```
GET https://cloud-computing-capsule-acc.rj.r.appspot.com/api/courses?category=Categoria 1
```

Al utilizar el anterior endpoint y ver la información completa de un curso, podras descargar un
archivo PDF específico de alguno de los cursos existentes, utilizando el siguiente endpoint:

```
GET https://cloud-computing-capsule-acc.rj.r.appspot.com/api/courses/document/download?course=Curso
1&filename=Primer PDF Curso 1.pdf
```

## Eliminación de Información

Puedes borrar archivos PDF individuales de la lista de documentos de un curso. Esto eliminará tanto
los registros de la base de datos como los archivos del Storage de Google:

```
DELETE https://cloud-computing-capsule-acc.rj.r.appspot.com/api/courses/document/remove?course=Curso
1&filename=turno.pdf
```

También puedes eliminar un curso completo, lo que eliminará todos los documentos PDF relacionados
del Storage de Google:

```
DELETE https://cloud-computing-capsule-acc.rj.r.appspot.com/api/courses?name=Curso 1
```

# Despliegue en Google Cloud Platform (GCP) con App Engine

La aplicación, denominada **course-management**, está desplegada en Google Cloud a través de App
Engine, los pasos realizados fueron:

1. ***Preparación del entorno de desarrollo:*** Asegúrate de tener tu aplicación Java lista para ser
   desplegada, incluyendo la compilación y empaquetado en un archivo JAR o WAR.

2. ***Configuración de Google Cloud SDK:*** Instala y configura el Google Cloud SDK en tu máquina
   local, autenticándote en tu cuenta de Google Cloud y seleccionando el proyecto correcto.

3. ***Configuración del archivo app.yaml:*** Define la configuración de tu aplicación en el archivo
   app.yaml, especificando detalles como el tipo de entorno, la versión de Java y otras opciones de
   configuración.

4. ***Ejecución del comando gcloud app deploy:*** Navega hasta el directorio raíz de tu proyecto y
   ejecuta el comando ***"gcloud app deploy"*** en tu terminal para iniciar el proceso de despliegue
   en App Engine.

5. ***Espera a que termine el despliegue:*** Google Cloud desplegará tu aplicación en sus servidores
   y realizará todas las configuraciones necesarias. Este proceso puede tardar algunos minutos.

6. ***Verificación del despliegue:*** Una vez completado el despliegue, verifica que tu aplicación
   esté funcionando correctamente accediendo a la URL proporcionada por Google Cloud para tu
   aplicación desplegada.
