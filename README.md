# Proyecto de asignatura de Videojuegos

### Detalles

* Grupo 2
* Abel Naya Forcano (544125)
* Sandra Navarro Moreno (681138)

### Ejecución

Se proporciona un fichero jar para ser ejecutado mediante java. En caso de no tenerlo configurado para poder abrirse mediante doble click debería bastar con el comando `$java -jar ElHobbit.jar`. En caso de que se reporten errores de heap space se puede añadir el parámetro `-Xmx1024m` o similar: `$java -Xmx1024m -jar ElHobbit.jar`.

Se puede pasar un párametro al jar con el nombre de la carpeta de los datos, que por defecto es 'data': `$java -jar ElHobbit.jar otrosdatos` cargará el juego de la carpeta 'otrosdatos'.

En total se proporcionan 3 juegos. Por defecto se ejecuta 'El Hobbit' dentro de la carpeta 'data', para jugar a los otros dos basta especificar el nombre de la carpeta: `$java -jar ElHobbit.jar extra-labyrinth` y `$java -jar ElHobbit.jar extra-scape`

### Descripción

El proyecto consiste en el desarrollo de un motor de juegos textuales por comandos, que implementa una versión propia del videojuego El Hobbit.

La carpeta 'data' consta de los datos del Hobbit, y se permite (y se anima) a cambiarlos para crear nuevas situaciones, personajes, elementos o incluso un juego totalmente nuevo. En las carpetas extra se encuentran otros dos juegos diferentes generados como prueba de concepto. En particular extra-scape utiliza características experimentales del motor.

En el fichero API.md se encuentra la documentación para modificar o crear tus propias variantes. En caso de error de configuración, al iniciar el juego se muestra una ventana de error con algo de información.

[Extra para desarrolladores: Si durante el juego pulsas F12 puedes teletransportarte a cualquier otro objeto. Y si pulsas shift+F12 se generará el código para crear un grafo con graphviz de todos los elementos y sus principales interacciones]