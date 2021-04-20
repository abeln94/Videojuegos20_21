package com.unizar.hobbit;

import com.unizar.game.World;
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
        elements.add(new DryCave());
        elements.add(new GoblinDungeon());
        elements.add(new DarkWidingPassage_1());
        elements.add(new DarkWidingPassage_2());
        elements.add(new DarkWidingPassage_3());
        elements.add(new DarkWidingPassage_4());
        elements.add(new DarkWidingPassage_5());
        elements.add(new DarkWidingPassage_6());
        elements.add(new DarkWidingPassage_7());
        elements.add(new DarkWidingPassage_8());
        elements.add(new DarkWidingPassage_9());
        elements.add(new DeadlyPassage());
        elements.add(new GoblinsGate());
        elements.add(new OutsideGoblinsGate());
        elements.add(new TrelessOpening());
        elements.add(new BeornsHouse());
        elements.add(new Mirkwood());
        elements.add(new BewitchedPlace());
        elements.add(new WestBlackRiver());

        // items
        elements.add(new Map());
        elements.add(new StartChest());
        elements.add(new GreenDoor());
        elements.add(new LargeKey());
        elements.add(new RockDoor());
        elements.add(new Sword());
        elements.add(new Rope());
        elements.add(new Food.CupboardFood());
        elements.add(new Food.ElrondFood());
        elements.add(new GoldenKey());
        elements.add(new GoblinDoor());
        elements.add(new GoblinWindow());
        elements.add(new Sand());
        elements.add(new TrapDoor());
        elements.add(new SmallKey());
        elements.add(new GoldenRing());
        elements.add(new Gold());
        elements.add(new GoblinGate());
        elements.add(new Cupboard());

        // npcs
        elements.add(new Gandalf());
        elements.add(new Thorin());
        elements.add(new HideousTroll());
        elements.add(new ViciousTroll());
        elements.add(new Elrond());
        elements.add(new NastyGoblin());
        elements.add(new Gollum());


        // objectives
//        requiredObjective("Prueba a 'abrir la puerta'", game -> game.findElementByClassName(GreenDoor.class).openable == Boolean.TRUE);
//        requiredObjective("Ve a explorar el resto del mundo", game -> !(game.getPlayer().getLocation() instanceof StartLocation));
//        requiredObjective("Guarda el oro en el cofre", game ->
//                game.findElementByClassName(StartChest.class).elements.contains(game.findElementByClassName(Gold.class))
//        );
//        optionalObjective(game -> game.findElementByClassName(Gandalf.class).elements.contains(game.findElementByClassName(Gold.class)));
//        optionalObjective(game -> game.getPlayer().elements.contains());

    }

}
