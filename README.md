# Operación Fuego de Quasar Challenge
➡ Enunciado del Challenge propuesto: https://drive.google.com/file/d/1fDfA9iiGRUkkqvPiQu4sum-yzYENsvme/view?usp=sharing

## Tecnologías y herramientas usadas
✔ Lenguaje: Java 11

✔ Framework: Spring y Spring Boot

✔ Clouds computing: AWS y Heroku

✔ Base de datos: MySQL en AWS y PostgreSQL en Heroku

✔ IDE: IntelliJ

✔ Principales librerías o dependencias: com.lemmingapex.trilateration,  io.springfox, junit, mockito

## Ramas del proyecto
✔ main: Rama con la configuración para desplegar en AWS y Local usando BD MySQL

✔ heroku: Rama para desplegar en Heroku usando BD PostgreSQL

## ¿Cómo ejecutar el programa?
Para ejecutar el servicio de manera directa en la nube, usar los EndPoints y documentación de Swagger o importar la colección de Postman que se indica en la solución

Para ejecutar el proyecto en maquina local:

✔ Descargar el código fuente de github, rama main

✔ Importar el proyecto en un IDE como proyecto maven

✔ En IntelliJ dar click en el boton run

✔ El proyecto usara los datos configurados de una BD que se encuentra en AWS

✔ Para probar el servicio usar los EndpPins descritos en la solucion

## Posición (coordenadas) que tienen los satélites  default del servicio
✔ Kenobi: [-500, -200]

✔ Skywalker: [100, -100]

✔ Sato: [500, 100]


## Solución:
Para determinar las coordenadas (Posición) del emisor conociendo la distancia a sus tres satélites de referencia se usó la técnica geométrica llamada  [trilateración](https://es.wikipedia.org/wiki/Trilateraci%C3%B3n)

### Arquitectura
El proyecto está dividido en cuatro capas principales:

✔ Controller: Es la capa que contiene los EndPoins y recibe las peticiones  

✔ Service: Es la capa encargada de hacer toda la lógica del servicio, y es la que comunica el controller con el repository
  
✔ Repository: Es la capa encargada de cominicarse y hacer persistencia con la base de datos

✔ Model: Es la capa que contiene los modelos y los entities del servicio

### Documentacion Swagger y colección Postman para importar
✔ Swagger en Heroku: https://quasar-operation-challenge.herokuapp.com/swagger-ui.html

✔ Swagger en AWS: http://ec2-18-228-173-196.sa-east-1.compute.amazonaws.com:8080/swagger-ui.html

✔ Colección Postman para importar: https://drive.google.com/file/d/1v4oxPD4VvKDfi5AWB_9EA8kZxdg2Xtbl/view?usp=sharing

### Servicios - EndPoints
#### ✔ POST /topscret
Recibe los mensajes parciales y la distancia de la nave espacial a cada uno de los satélites y retorna la posición y el mensaje de la nave

https://quasar-operation-challenge.herokuapp.com/topsecret

http://ec2-18-228-173-196.sa-east-1.compute.amazonaws.com:8080/topsecret
##### Request body

      {
          "satellites": [
              {
                  "name": "kenobi",
                  "distance": 110.0,
                  "message": ["este", "", "", "mensaje", ""]
              },
              {
                  "name": "skywalker",
                  "distance": 115.5,
                  "message": ["", "es", "", "", "secreto"]
              },
              {
                  "name": "sato",
                  "distance": 142.7,
                  "message": ["este", "", "un", "", ""]
              }
          ]
      }

##### Response Body (200)

      {
          "position": {
              "x": -42.95642775426958,
              "y": -64.39708806171568
          },
          "message": "este es un mensaje secreto "
      }

##### Response Body (404)

      {
          ERROR: Descripción del error
      }

#### ✔ POST /topsecret_split/{satelliteName}
Recibe uno a uno la información de los satélites (nombre, distancia y mensaje) a almacenar. Si el nombre del satélite enviado ya existe, sus datos se actualizan

https://quasar-operation-challenge.herokuapp.com/topsecret_split/{satelliteName}

http://ec2-18-228-173-196.sa-east-1.compute.amazonaws.com:8080/topsecret_split/{satelliteName}
##### Request body

       {
          "distance": 110.0,
          "message": ["este", "", "", "mensaje", ""]
       }

##### Response Body (200)

      {
          Satellite information saved/updated successfully
      }

##### Response Body (404)

      {
          ERROR: Descripción del error
      }
      
#### ✔ GET /topsecret_split
Retorna la posición y el mensaje de la nave espacial usando información previamente almacenada, se requiere un mínimo de tres satélites almacenados para calcular la posición de la nave espacial

https://quasar-operation-challenge.herokuapp.com/topsecret_split

http://ec2-18-228-173-196.sa-east-1.compute.amazonaws.com:8080/topsecret_split


##### Response Body (200)

      {
          "position": {
              "x": -42.95642775426958,
              "y": -64.39708806171568
          },
          "message": "este es un mensaje secreto "
      }
      
##### Response Body (404)

      {
          ERROR: Descripción del error
      }
      
      
#### ✔ (EXTRA) -> GET /topsecret_split_find_all
Retorna la información de los satélites previamente almacenados (nombre, distancia y mensaje)

https://quasar-operation-challenge.herokuapp.com/topsecret_split_find_all

http://ec2-18-228-173-196.sa-east-1.compute.amazonaws.com:8080/topsecret_split_find_all


##### Response Body (200)
```
   {
       "satellites": [
           {
               "name": "kenobii",
               "distance": 110.0,
               "message": ["este", "", "", "mensaje", ""]
           },
           {
               "name": "skywalker",
               "distance": 115.5,
               "message": ["", "es", "", "", "secreto"]
           },
           {
               "name": "sato",
               "distance": 142.7,
               "message": ["este", "", "un", "", ""]
           }
       ]
   }
 ```     
##### Response Body (404)

      {
          ERROR: Descripción del error
      }

#### ✔ (EXTRA) -> DELETE /topsecret_split_delete/{satelliteName}
Elimina la información de un satélite previamente almacenado, este borrado es por nombre

https://quasar-operation-challenge.herokuapp.com/topsecret_split_delete/{satelliteName}

http://ec2-18-228-173-196.sa-east-1.compute.amazonaws.com:8080/topsecret_split_delete/{satelliteName}


##### Response Body (200)

```
  {
      Satellite information deleted successfully
  }
```  

##### Response Body (404)
```
   {
       ERROR: Descripción del error
   }
```

### Pruebas unitarias
El proyecto cuenta con pruebas unitarias de los services y el controller, estas pruebas se hicieron usando Junit y Mockito

### Buenas practicas 
✔ Se aplicaron los principios SOLID:

● Single responsibility: Cada clase y cada método cumple con responsabilidades únicas dentro del programa, cada metodo es pequeño con una unica funcionalidad

● Open/closed: Las clases y métodos están creados de tal manera que puedan extenderse y usarse en diferentes lugares sin necesidad de modificar los mismos. Ejemplo, los servicios LocationService y MessageService

● Liskov Substitution: Aunque en este programa no se tiene herencia, se debe tener en cuenta este principio si en algún momento se crean subtipos de alguna clase

● Interface Segregation: Cada servicio del programa cuenta con su propia interfaz segregando sus métodos particulares

● Dependency Inversion: La creación de instancias en el programa se esta haciendo mediante inyección de dependencias, inyectando la interfaz y no la implementación. En dicha inyección de dependencias se usa el patrón creacional Singlenton

✔ Principios o filosofía Clean code:

● Los nombres son importantes: Los nombres de las clases, métodos, variables, parámetros se hicieron de manera que describieran la funcionalidad que representan
 
● Regla del boy scout: Aunque esta regla no aplica porque es un código creado de cero, se dejo el código lo más limpio posible
 
● Ser el verdadero autor del código: Las funciones o métodos creados en el programa son pequeñas y de fácil entendimiento

● Comentar solamente lo necesario: Se comento unicamente los métodos que tienen la lógica principal del servicio

● Tratamiento de errores: Se hicieron las respectivas validaciones para tratar los posibles errores y si hay algún error no esperado se maneja con la Exceepcion ResponseStatusException respondiendo un código 404

● Test limpios: El funcionamiento del código de los métodos principales del servicio fue validado mediante pruebas unitarias

