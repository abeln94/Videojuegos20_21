package com.unizar.hobbit;

import com.unizar.game.World;
import com.unizar.hobbit.items.GreenDoor;
import com.unizar.hobbit.items.Map;
import com.unizar.hobbit.items.StartChest;
import com.unizar.hobbit.npcs.Bilbo_Player;
import com.unizar.hobbit.npcs.Gandalf;
import com.unizar.hobbit.npcs.Thorin;
import com.unizar.hobbit.rooms.EastLocation;
import com.unizar.hobbit.rooms.StartLocation;

/**
 * This should be better with annotations, but from now lets use it explicitly
 */
public class HobbitWorld extends World {

    public HobbitWorld() {
        // properties
        properties = new HobbitProperties();

        // player
        elements.add(new Bilbo_Player());

        // rooms
        elements.add(new StartLocation());
        elements.add(new EastLocation());

        // items
        elements.add(new Map());
        elements.add(new StartChest());
        elements.add(new GreenDoor());

        // npcs
        elements.add(new Gandalf());
        elements.add(new Thorin());


        // objectives
        requiredObjective("Prueba a 'abrir la puerta'", game -> game.findElementByClassName(GreenDoor.class).opened == Boolean.TRUE);
        requiredObjective("Ve a explorar el resto del mundo", game -> !(game.getPlayer().location instanceof StartLocation));
        optionalObjective(game -> game.findElementByClassName(StartChest.class).opened == Boolean.TRUE);
//        optionalObjective(game -> game.getPlayer().elements.contains());

    }

}
