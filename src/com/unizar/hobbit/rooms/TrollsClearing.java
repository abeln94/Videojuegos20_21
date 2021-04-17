package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;
import com.unizar.hobbit.items.LargeKey;
import com.unizar.hobbit.npcs.Bilbo_Player;
import com.unizar.hobbit.npcs.HideousTroll;
import com.unizar.hobbit.npcs.ViciousTroll;

public class TrollsClearing extends Location {
    public TrollsClearing() {
        super("el claro de los trolls", "TrollsClearingNight"); //noche
    }

    @Override
    public void init() {
        elements.add(game.findElementByClassName(HideousTroll.class));
        elements.add(game.findElementByClassName(ViciousTroll.class));

        exits.put(Word.Direction.WEST, Utils.Pair.of(game.findElementByClassName(EmptyLand.class), null));
        exits.put(Word.Direction.SOUTHWEST, Utils.Pair.of(game.findElementByClassName(EmptyLand.class), null));
        exits.put(Word.Direction.NORTH, Utils.Pair.of(game.findElementByClassName(HiddenPath.class), null));
        exits.put(Word.Direction.SOUTHEAST, Utils.Pair.of(game.findElementByClassName(Rivendell.class), null));
        super.init();
    }

    @Override
    public void act() {
        System.out.println(game.world.time);
        if(game.world.time < 10){ //noche
            name = "el claro de los trolls";
            image = "TrollsClearingNight";
        }
        else{ //dia
            name = "el claro de los trolls con dos trolls de piedra";
            image = "TrollsClearingDay";
            if(elements.contains(game.findElementByClassName(HideousTroll.class)))
                elements.remove(game.findElementByClassName(HideousTroll.class));
            if(elements.contains(game.findElementByClassName(ViciousTroll.class)))
                elements.remove(game.findElementByClassName(ViciousTroll.class));
            if(!elements.contains(game.findElementByClassName(LargeKey.class)) && !game.findElementByClassName(Bilbo_Player.class).elements.contains(game.findElementByClassName(LargeKey.class)))
                elements.add(game.findElementByClassName(LargeKey.class));
        }
    }
}
