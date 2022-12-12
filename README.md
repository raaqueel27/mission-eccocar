# Prueba Eccocar
## _API Rest para la Gestión de Misiones de Star Wars_ 
El API consume los datos de la API externa: https://swapi.dev/documentation y gestiona misiones espaciales.

## Documentación  
La documentación del API se genera gracias a la herramienta **Swagger** y se puede encontrar en el enlace 
http://localhost:8080/swagger-ui.html/

## Base de Datos
Como base de datos usamos **H2** que es una base de datos relacional escrita en **Java** que funciona como una base de datos en memoria.
Se podrá encontrar en el enlace: 
http://localhost:8080/h2-console

## Endpoints
#### _Get All_
```sh
[GET] /missions
```
Lista todas las misiones.

#### _Get All By Captains_
```sh
[GET] /missions/captains/{captainsId}
```
Lista todas las misiones que tengan presente a los capitanes indicados.
Se deben pasar como parámetro las ID de los capitanes.

#### _Create Mission_
```sh
[POST] /missions
```
Crea una misión. Se deben indicar los siguientes datos:
 - Fecha de inicio de la misión
 - Nave que se va a usar en la misión
 - Capitanes que van a ir a la misión
 - Tripulación (Número de tripulantes adicionales)
 - Planetas que hay que recorrer en la misión
 
 **Ejemplo de JSON:**
 ```json
 {
  "captains": [
    "https://swapi.dev/api/people/1/"
  ],
  "crew": 0,
  "planets": [
    "https://swapi.dev/api/planets/1/"
  ],
  "starship": "https://swapi.dev/api/starships/12/",
  "startDate": "2022-12-10T16:44:51.039Z"
}
```
