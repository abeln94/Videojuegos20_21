package com.unizar.hobbit;

import com.unizar.game.World;
import com.unizar.hobbit.items.Gold;
import com.unizar.hobbit.items.*;
import com.unizar.hobbit.npcs.*;
import com.unizar.hobbit.rooms.*;

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
        elements.add(new EmptyLand());
        elements.add(new TrollsClearing());
        elements.add(new HiddenPath());
        elements.add(new TrollsCave());
        elements.add(new Rivendell());
        elements.add(new DangerousPath());
        elements.add(new DeadlyPath());
        elements.add(new NarrowPath_1());
        elements.add(new NarrowPath_2());
        elements.add(new NarrowPath_3());
        elements.add(new NarrowPath_4());
        elements.add(new NarrowPath_5());
        elements.add(new NarrowPath_6());
        elements.add(new NarrowPath_7());
        elements.add(new SteepPath_1());
        elements.add(new SteepPath_2());
        elements.add(new SteepPath_3());
        elements.add(new MistyValley_1());
        elements.add(new MistyValley_2());
        elements.add(new DimValley());

        // items
        elements.add(new Map());
        elements.add(new StartChest());
        elements.add(new GreenDoor());
        elements.add(new LargeKey());
        elements.add(new RockDoor());
        elements.add(new Sword());
        elements.add(new Rope());
        elements.add(new Food());
        elements.add(new GoldenKey());
        elements.add(new Gold());

        // npcs
        elements.add(new Gandalf());
        elements.add(new Thorin());
        elements.add(new HideousTroll());
        elements.add(new ViciousTroll());
        elements.add(new Elrond());


        // objectives
        requiredObjective("Prueba a 'abrir la puerta'", game -> game.findElementByClassName(GreenDoor.class).opened == Boolean.TRUE);
        requiredObjective("Ve a explorar el resto del mundo", game -> !(game.getPlayer().location instanceof StartLocation));
        requiredObjective("Guarda el oro en el cofre", game ->
                game.findElementByClassName(StartChest.class).elements.contains(game.findElementByClassName(Gold.class))
        );
        optionalObjective(game -> game.findElementByClassName(Gandalf.class).elements.contains(game.findElementByClassName(Gold.class)));
//        optionalObjective(game -> game.getPlayer().elements.contains());

    }

}
