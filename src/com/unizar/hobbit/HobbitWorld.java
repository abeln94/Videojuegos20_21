package com.unizar.hobbit;

import com.unizar.game.Game;
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
        elements.add(new EastBlackRiver());
        elements.add(new GreenForest());
        elements.add(new PlaceOfBlackSpider());
        elements.add(new ElvishClearing());
        elements.add(new Elvenkings());
        elements.add(new CellarOfWine());
        elements.add(new RedDoorRoom());
        elements.add(new Forestriver());
        elements.add(new LongLake());
        elements.add(new LakeTown());
        elements.add(new BleakBarrenLand());
        elements.add(new RuinsTownDale());
        elements.add(new Ravenhill());
        elements.add(new Sidedoor());
        elements.add(new SmoothPassage());
        elements.add(new EmptyPlace());
        elements.add(new NarrowDangerousPath());
        elements.add(new ForestGate());
        elements.add(new DragonsHalls());
        elements.add(new FastRiver());
        elements.add(new Forest());
        elements.add(new ForestGate());
        elements.add(new Forestriver());
        elements.add(new ForestRoad_1());
        elements.add(new ForestRoad_2());
        elements.add(new FrontGate());
        elements.add(new LonelyMountain());
        elements.add(new NarrowDangerousPath());
        elements.add(new StrongRiver());
        elements.add(new Waterfall());

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
        elements.add(new SpiderWeb());
        elements.add(new SpiderWeb_Broken());
        elements.add(new MagicDoor());
        elements.add(new RedDoor());
        elements.add(new LargeTrapDoor());
        elements.add(new Barrel());
        elements.add(new Wine());
        elements.add(new RedKey());
        elements.add(new StrongPortcullis());
        elements.add(new Bow());
        elements.add(new Arrow());
        elements.add(new SideDoorLonelyMountain());
        elements.add(new TheValuableTreasure());
        elements.add(new RunningRiver());

        // npcs
        elements.add(new Gandalf());
        elements.add(new Thorin());
        elements.add(new HideousTroll());
        elements.add(new ViciousTroll());
        elements.add(new Elrond());
        elements.add(new NastyGoblin());
        elements.add(new Gollum());
        elements.add(new Butler());
        elements.add(new Bardo());
        elements.add(new RedGoldenDragon());

    }

    @Override
    public boolean playerWon(Game game) {
        return false;//game.findElementByClassName(StartChest.class).elements.contains(game.findElementByClassName(Gold.class));
    }
}
