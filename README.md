# Proyecto de asignatura de Videojuegos

### Detalles

* Grupo 2
* Abel Naya Forcano (544125)
* Sandra Navarro Moreno (681138)

### Ejecución

Se proporciona un fichero jar para ser ejecutado mediante java. En caso de no tenerlo configurado para poder abrirse
mediante doble click debería bastar con el comando `$java -jar ElHobbit.jar`.

### Descripción

El proyecto consiste en el desarrollo de un motor de juegos textuales por comandos, que implementa una versión propia
del videojuego El Hobbit.

La carpeta 'data' consta de los datos del Hobbit, y se permite (y se anima) a cambiarlos para crear nuevas situaciones,
personajes, elementos o incluso un juego totalmente nuevo.

Lamentablemente la documentación no está aún disponible, pero se puede ver como está hecho el Hobbit para entender la
mayoría de parámetros. En caso de error de configuración el juego muestra una ventana de error con algo de información.

Se puede pasar un parametro al jar con el nombre de la carpeta de los datos, que por defecto es '
data': `java -jar ElHobbit.jar otrosdatos` cargará el juego de la carpeta 'otrosdatos'.

[Extra para desarrolladores: Si durante el juego pulsas F12 puedes teletransportarte a cualquier otro objeto. Y si pulsas shift+F12 se generará el código para crear un grafo con graphviz de todos los elementos y sus principales interacciones]