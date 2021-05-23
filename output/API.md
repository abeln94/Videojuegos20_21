# HobbitEngine V1

This file contains a description of how the json files need to be in order to be played

All the files need to be inside a folder, preferably in the same directory as the Engine jar. In order to load the game
simple pass the path to the folder to the jar (either relative or absolute, no ending '/' char). For example "
my_game_data" or "/users/home/my_game_data" or "C:\user\my_game_data". If no name is configured it will default
to `data`, which will try to load a game in a folder named data in the same directory as the jar.

## Files

The folder must contain 4 files in the data folder:

### properties.json

A json object with the following fields:

* `title`: An string. The name of the window. Default to "My game"
* `imageRatio`: An integer representing the ratio width/height of the images. Default to 1

* `fontFile`: The path of a font file inside the folder directory, for example "/other/font.ttf". Will be used to
  display the text in the game. Default to null (default font)
* `helpFile`: The path of a txt file inside the folder directory, for example "/other/help.txt". Will be shown in the
  help dialog. Default to null (no help)

* `musicPath`: The path of a generic music file. If it contains a '{}' that will be replaced with the music label (see
  locations/music). For example "/music/{}.wav". Default to "{}" (so the label is the path)
* `imagePath`: The path of a generic image file. If it contains a '{}' that will be replaced with the image label (see
  elements/image). For example "/images/{}.png". Default to "{}" (so the label is the path)

* `startScreen`: An image label for an image file (see properties/imagePath). Will be shown in the start screen. Null by
  default (
  no image)
* `startDescription`: A string. Will be shown in the description box when the start screen is shown. Defaults to "" (no
  description)
* `startMusic`: A music label for a music file (see properties/musicPath). Will be played in the game over screen. Null
  by default (no music)

* `winScreen`: An image label for an image file (see properties/imagePath). Will be shown in the win screen. Null by
  default (no image)
* `winDescription`: A string. Will be shown in the description box when the win screen is shown. Defaults to "" (no
  description)
* `winMusic`: A music label for a music file (see properties/musicPath). Will be played in the win screen. Null by
  default (no music)

* `gameOverScreen`: An image label for an image file (see properties/imagePath). Will be shown in the game over screen.
  Null by default (no image)
* `gameOverMusic`: A music label for a music file (see properties/musicPath). Will be played in the game over screen.
  Null by default (no music)

* `winItem`: A string with the id of an element (see element/id). The game will be considered 'completed' when the item
  with this id is inside the winLocation. Null by default (wich makes the game unwinnable)
* `winLocation`: A string with the id of an item (see element/id). The game will be considered 'completed' when the
  item '
  winItem' is inside the element with this id. Null by default (wich makes the game unwinnable)

Example:
data/properties.json

```json
{
  "title": "An empty game"
}
```

### items.json, locations.json, npcs.json

Each file must be a JSON list where each item is a JSON object with the following fields:

#### common

This fields are available in all three files:

* `id`: A string which internally identifies this element, used in other properties when you need to identify this
  element. Must be unique. If not configured a random id is assigned.
* `name`: A string which externally identifies this element. Used when describing this element to the player. All words
  in this string (which are not reserved words) can be specified in commands to select this element. For example "una
  puerta redonda" can be later selected with "mirar puerta", "romper la redonda" or "coger la puerta redonda". Empty by
  default (which makes it unselectable).

* `weight`: An int. Represents the weight of this element. An NPC can only pick and eat elements with smaller weight.
  Its weight is also added to the health when eated (substracted if you eat an npc instead). MAX_INT by default (
  unmovable).
* `location`: A string with the id of an element (see element/id). This element will be placed inside that specified
  element. Null by default (not inside anything).
* `location_wear`: A boolean. If location is an npc this element will be weared by the npc specified in the location
  instead. False by default.
* `hidden`: A JSON object where the key is one of 'DIG', 'BREAK' or 'PULL' and the value is an id of an element (see
  element/id)
  . When the player uses that action on this element, the 'hidden' element is placed on the same parent. (if the action
  is BREAK this element is also removed so it can be seen as a replacement). Empty by default

#### items

Specific properties only available for elements in the items.json file:

* `openable`: One of 'OPENED', 'CLOSED' or 'LOCKED' which represents the state of this openable item. Null by default,
  which makes this item not-openable.

* `lockedWith`: A string with the id of an element (see element/id). The specified element is the one required to unlock
  this item. Null by default, which makes this item unlockable if defined as locked, or unable to lock if defined as
  opened or closed (see item/openable).

* `language`: A string representing the language of the description in this item (see item/description). Null by
  default (all npcs can understand it).

* `description`: A string which will be returned to an npc wich 'examines' this item if understands the language it is
  written (see item/language). Null by default which returns a default description instead (it's name and what it
  contains).

* `makesInvisible`: A boolean. If true and an npc wears this it will be 'invisible' to other npcs. False by default.

Example:
data/items.json

```json
[
  {
    "id": "ring",
    "name": "el anillo único",
    "weight": 1,
    "location": "chest",
    "makesInvisible": true
  },
  {
    "id": "chest",
    "name": "el cofre de madera",
    "openable": "CLOSED",
    "location": "room"
  }
]
```

#### locations

Specific properties only available for elements in the locations.json file:

* `image`: An image label for an image file (see properties/imagePath). Will be shown when the player is inside this
  location. If the string contains the char '|' the label before will be used on day, and the label after on night. Null
  by default (
  no image)
* `music`: A music label for a music file (see properties/musicPath). Will be played when the player is inside this
  location. If the string contains the char '|' the label before will be used on day, and the label after on night. Null
  by default (
  no music)

* `description`: a string wich will be appended after the name of this locations when describing it. Words in this
  string won't be used to select this element (as opposed to name). Empty string by default.
* `exits`: a JSON object whose keys are one direction (one of 'NORTH', 'NORTHEAST', 'SOUTH', 'NORTHWEST', 'EAST', '
  SOUTHEAST', 'WEST', 'SOUTHWEST', 'UP' or 'DOWN') and whose value are another JSON object with:
    - `location`: A string with the id of a location (see element/id). The npc will be moved to the specified location
      when going in that direction. Null by default which kill the npc.
    - `item`: A string with the id of an item (see element/id). The item is considered to be between this and the other
      location, and must be opened in order to an npc to travel in this direction. Null by default which means there is
      no item so you can travel freely.

Example:
data/locations.json

```json
[
  {
    "id": "room",
    "image": "roomDay|roomNight",
    "exits": {
      "SOUTH": {
        "location": "exterior",
        "item": "door"
      }
    },
    "music": "calm",
    "name": "la sala inicial"
  },
  {
    "id": "exterior",
    "image": "sky|stars",
    "exits": {
      "SOUTH": {
        "item": "door",
        "location": "room"
      },
      "EAST": {
        "location": "exterior"
      },
      "WEST": {
        "location": "exterior"
      }
    },
    "music": "birds|danger",
    "name": "el mundo exterior",
    "description": " lleno de peligros"
  }
]
```

#### npcs

Note: One and only one of the npcs defined must contain the word 'Player' in its id (see element/id). This npc will be
considered the player.

Specific properties only available for elements in the locations.json file:

* `strength`: An int. Represents the strength of this npc, used in the damage calculation. 1 by default.
* `health`: An int. Represents the health of this npc. The npc is killed when it reaches 0 or negative. 10 by default.

* `languages`: An array of strings representing the languages this npc understand (see item/language). Empty by default.

* `allowedLocations`: An array (empty by default) where each element is a string with the id of a location (see
  element/id). Represents the locations that this npc is allowed to navigate to with the 'GO' command. If not defined (
  and forbiddenLocations is not defined either) the npc is allowed to traverse anywhere.
* `forbiddenLocations`: An array where each element is a string with the id of a location (see element/id). Represents
  the locations that this npc is not allowed to navigate to with the 'GO' command. If not defined (and allowedLocations
  is not defined either) the npc is allowed to traverse anywhere. If both properties are defined this takes precedence.

* `canFollowOrders`: A boolean. If true the player can use the 'ASK' command to tell this npc to do something. False by
  default.

* `sleepAt`: 'night' or 'day', which makes this npc be asleep at that time of day. Null by default (no sleep).


* `attackPlayerWeight`: Weight of the attack player behaviour (see npc/pacificTurns). Used when randomly picking an
  action for the IA. If pacificTurns is defined this is 1 by default, 0 otherwise.
* `pacificTurns`: An int. Makes this npc not attack the player until the specified turns have passed (while both are on
  the same location). 0 by default. (attack as soon as it is seen)

* `attackItemWeight`: Weight of the attack items behaviour (see npc/attackItems). Used when randomly picking an action
  for the IA. If attackItems is defined this is 1 by default, 0 otherwise.
* `attackItems`: An array where each element is a string with the id of an element (see element/id). This npc will
  attack any npc which contains at least one of this elements. Empty by default.

* `followWeight`: Weight of the follow npcs behaviour (see npc/followNPCs). Used when randomly picking an action for the
  IA. If followNPCs is defined this is 1 by default, 0 otherwise.
* `followNPCs`: An array where each element is a string with the id of an npc (see element/id). Makes this npc try to
  follow one of them. Empty by default.

* `navigateWeight`: Weight of the navigation behaviour (see npc/allowedLocations and npc/forbiddenLocations). Used when
  randomly picking an action for the IA. If allowedLocations or forbiddenLocations are defined this is 1 by default, 0
  otherwise.

* `talkWeight`: Weight of the talk behaviour (see npc/talkPlayer). Used when randomly picking an action for the IA. If
  talkPlayer is defined this is 1 by default, 0 otherwise.
* `talkPlayer`: An array (empty by default) where each element is an object with the following properties:
    - `sentence`: A string. Will be said to the player. ("..." by default)
    - `turns`: An int. Represents the number of turns to pass until the sentence is allowed to be said. 1 by default. (
      always available when the player is in the same location).

* `giveWeight`: Weight of the give items behaviour (see npc/giveItems). Used when randomly picking an action for the IA.
  If giveItems is defined this is 1 by default, 0 otherwise.
* `giveItems`: An array where each element is a string with the id of an item (see element/id). The npc will give one of
  this items to the player (unless the item is somewhere else).

* `openWeight`: Weight of the open behaviour, which makes this npc try to open a random item. Used when randomly picking
  an action for the IA. 0 by default.
* `pickWeight`: Weight of the pick behaviour, which makes this npc try to pick a random item. Used when randomly picking
  an action for the IA. 0 by default.


* `allies`: An array where each element is a string with the id of an npc (see element/id). This npc won't attack nor
  teleport any of those npcs (unless they attack first). Empty by default.

* `moveNPCsTo`: A string with the id of an element (see element/id). When this npc is on the same location as another
  one, will teleport that other npc to this element (unless it is an allie). Null by default (no teleport).

Example:
data/npcs.json

```json
[
  {
    "id": "thePlayer",
    "name": "Jugador",
    "weight": 10,
    "location": "room",
    "strength": 3,
    "health": 20
  },
  {
    "id": "enemy",
    "name": "El enemigo",
    "weight": 50,
    "location": "exterior",
    "strength": 3,
    "health": 34,
    "sleepAt": "day",
    "pacificTurns": 3,
    "talkPlayer": [
      {
        "turns": 1,
        "sentence": "No me caes bien"
      },
      {
        "turns": 3,
        "sentence": "Como no te vayas me voy a enfadar"
      }
    ],
    "attackPlayerWeight": 3,
    "talkWeight": 10
  }
]
```

### Experimental

This api is highly experimental and not ready to use, but added as a proof-of-concept. Documentation is not available.

The engine uses the javascript interpreter to allow running arbitrary code. The scripts contains a variable called '
game' with the main class, and another called 'storage' where you can store data between executions. The following two
additional parameters are:

* In an element (either an item, location or npc) you can add a field named `code` with the javascript code, that will
  run in each turn for that object. A variable called 'element' refers to the current object.
* In the properties file, you can add a field named `code` with the javascript code, that will run after the user enters
  a command. A variable called 'input' refers to the user input command.