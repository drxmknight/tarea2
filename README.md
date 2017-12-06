# Tarea 2: Sistemas Distribuidos 2017-2
- Profesor: Raúl Monge Anwandter
- Ayudante: Rodolfo Castillo Mateluna
- Estudiantes:
  - Eduardo Hales Jiménez 201003001-k
  - Mario Hazard Valdés 201004502-1
  
## Problema
Crear procesos que participen en el sistema de Semáforo Distribuidos basado en el Algoritmo de Suzuki-Kasami, implementado con funciones remotas (RMI en Java) el paso de un Token, como objeto.
Cada uno de los semáforos tiene un estado:
  - Rojo: El proceso tiene el token y está en sección crítica.
  - Amarillo: El proceso debe entrar en sección crítica, pero no posee el token.
  - Verde: El proceso no está en sección crtica, ni debe empezarla. Independiente si tiene el token.

## Estrategia

  - Básicamente, se aprovecha que cada proceso tiene su propio ID para utilizarlo en el nombre dentro de RMI. Con esto, al tener N (número total de procesos), cada proceso conoce la cantidad de procesos para conectarse.
  - Cada proceso tiene un arreglo, que contiene la cantidad de veces que un proceso ha solicitado el token. Cuando un proceso debe entrar en la sección crítica, envia una solicitud con su ID y la vez que desea entrar (las veces que ha entrado + 1), si dicho valor es mayor que los anteriores, el resto de los procesos actualiza el valor en su arreglo y el proceso que tiene el token lo agrega a la cola, en caso de estar ocupado.
  - Cuando el proceso que posee el token está ocioso y recibe una solicitud, o recibio una y terminó la sección crítica, envía el token al primer proceso en la cola. Lo hace ejecutando el método remoto donde se asigna el token que propio, como el token del proceso remoto, y luego cambia el boolean de poseer el token a false.
  - El algoritmo termina cuando todos los elementos del arreglo de enteros del token, son distintos de cero, cambiando un boolean propio en cada proceso. 

## Ejecución

```
#compilar todo
make

#correr rmiregistry
make run-rmiregistry

#correr un proceso
make args="<id> <n> <initialDelay> <bearer>" run-process
```

Por ejemplo, para correr el programa con un total de 3 semáforos, se debe ejecutar 3 procesos luego de los primeros pasos.
Donde el segundo proceso parte con el token. Es importante considerar que solo un proceso parte con el token, por lo que los restantes deben tener el cuarto campo en "*false*". 
> Se compila todo y luego se corre rmi-registry.
```
make
make run-rmiregistry
```
> Se inicia un primer proceso, sin el token.
```
make args="0 3 100 false" run-process
```
> Se corre el segundo proceso, que tiene el token.
```
make args="1 3 100 true" run-process
```
> Se corre el resto de los procesos.
```
make args="2 3 100 false" run-process
```
  
