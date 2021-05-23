# Proyecto de asignatura de Videojuegos

### Detalles

* Grupo 2
* Abel Naya Forcano (544125)
* Sandra Navarro Moreno (681138)

### Ejecución

Se proporciona un fichero jar para ser ejecutado mediante java. En caso de no tenerlo configurado para poder abrirse mediante doble click debería bastar con el comando `$java -jar HobbitMotor.jar`. En caso de que se reporten errores de heap space se puede añadir el parámetro `-Xmx1024m` o similar: `$java -Xmx1024m -jar HobbitMotor.jar`.

En total se proporcionan 3 juegos. Al ejecutar se pregunta cuál de ellos cargar. El juego principal es el hobbit.

### Descripción

El proyecto consiste en el desarrollo de un motor de juegos textuales por comandos, que en particular implementa una versión propia del videojuego El Hobbit.

La carpeta 'hobbit' consta de los datos del Hobbit, y se permite (y se anima) a cambiarlos para crear nuevas situaciones, personajes, elementos o incluso un juego totalmente nuevo. En las carpetas extra se encuentran otros dos juegos diferentes generados como prueba de concepto, que también se pueden editar. El minijuego 'extra-scape' utiliza características experimentales del motor.

En el fichero API.md se encuentra la documentación para modificar o crear tus propias variantes. En caso de error de configuración, al iniciar el juego se muestra una ventana de error con algo de información.

[Extra para desarrolladores: Si durante el juego pulsas F12 puedes teletransportarte a cualquier otro objeto. Y si pulsas shift+F12 se generará el código para crear un grafo con graphviz de todos los elementos y sus principales interacciones]