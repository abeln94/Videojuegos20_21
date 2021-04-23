package com.unizar.hobbit;

import com.unizar.game.Game;
import com.unizar.game.World;
import com.unizar.game.commands.Word;
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


        // rooms
        add(new StartLocation())
                .with(new Bilbo_Player()) // player
                .with(add(new Gandalf())
                        .with(new Map()))
                .with(new Thorin())
                .with(new StartChest());

        add(new EmptyLand());

        add(new TrollsClearing())
                .with(add(new HideousTroll())
                        .with(new LargeKey()))
                .with(new ViciousTroll());

        add(new HiddenPath());
        add(new TrollsCave())
                .with(new Sword())
                .with(new Rope());

        add(new Rivendell())
                .with(new Elrond());

        add(new DangerousPath());
        add(new DeadlyPath());

        add(new NarrowPath_1());
        add(new NarrowPath_2());
        add(new NarrowPath_3());
        add(new NarrowPath_4());
        add(new NarrowPath_5());
        add(new NarrowPath_6());
        add(new NarrowPath_7());

        add(new SteepPath_1());
        add(new SteepPath_2());
        add(new SteepPath_3());

        add(new MistyValley_1());
        add(new MistyValley_2())
                .with(new GoldenKey());

        add(new DimValley());
        add(new DryCave())
                .with(new NastyGoblin());

        add(new GoblinDungeon())
                .with(add(new Sand())
                        .withHidden(Word.Action.DIG, add(new TrapDoor())
                                .withHidden(Word.Action.BREAK, new SmallKey())));

        add(new DarkWidingPassage_1());
        add(new DarkWidingPassage_2());
        add(new DarkWidingPassage_3());
        add(new DarkWidingPassage_4());
        add(new DarkWidingPassage_5());
        add(new DarkWidingPassage_6())
                .with(new GoldenRing())
                .with(new Gollum());
        add(new DarkWidingPassage_7());
        add(new DarkWidingPassage_8());
        add(new DarkWidingPassage_9());

        add(new DeadlyPassage());
        add(new GoblinsGate());
        add(new OutsideGoblinsGate());
        add(new TrelessOpening());
        add(new BeornsHouse())
                .with(add(new Cupboard())
                        .with(new Food.CupboardFood()));

        add(new Mirkwood());
        add(new BewitchedPlace());
        add(new WestBlackRiver())
                .with(new Gold());

        // doors
        elements.add(new GreenDoor());
        elements.add(new RockDoor());
        elements.add(new GoblinDoor());
        elements.add(new GoblinWindow());
        elements.add(new GoblinGate());

        add(new Food.ElrondFood()); // obtained via Elrond
    }

    @Override
    public boolean playerWon(Game game) {
        return game.findElementByClassName(StartChest.class).elements.contains(game.findElementByClassName(Gold.class));
    }
}
