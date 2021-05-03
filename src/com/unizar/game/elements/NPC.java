package com.unizar.game.elements;

import com.unizar.Utils;
import com.unizar.game.commands.Command;
import com.unizar.game.commands.Result;
import com.unizar.game.commands.Word;
import com.unizar.game.commands.Behaviour;
import com.unizar.hobbit.npcs.Bilbo_Player;
import com.unizar.hobbit.npcs.Thorin;
import com.unizar.hobbit.rooms.GoblinDungeon;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A generic NPC
 */
abstract public class NPC extends Element {

    Behaviour behaviour = new Behaviour();
    /**
     * List of parameters that de machine use to select the next action of the npc
     */
    public int id = 0;
    public NPC lastAttackedBy = null;
    public boolean autonomo = false;
    public boolean inmortal = false;
    public boolean puedeDormir = false;
    public boolean puedeTP = false;
    public boolean puedeLeer = false;
    public boolean puedeMatarAJugador = false;
    public List<Class<Location>> lugares = new ArrayList<>();
    public boolean dormido = false;
    public boolean primerEncuentroJugador = false;
    public List<String> saludos = new ArrayList<>();
    public List<String> frases = new ArrayList<>();
    public Class<Location> sitioTP = null;
    public Class<Item> elementoAbrir = null;
    public Class<Item> elementoLeer = null;
    public String arma = null;
    public String orden = null;
    //inventario de npc son sus elements

    public NPC(String name) {
        super(name);
    }

    @Override
    public String getDescription(NPC npc) {
        String prefix = ": " + " Lleva";

        return this + Utils.joinList("", prefix, prefix, elements.stream().map(e -> e.getDescription(npc)).collect(Collectors.toList()));
    }

    /**
     * When this npc hears something
     *
     * @param message what was heard
     */
    public void hear(String message) {
        System.out.println("[" + this + "]> " + message);
    }


    /**
     * When another npc asks something to this npc (a command to execute)
     *
     * @param npc     who asked
     * @param message what was asked
     */
    public void ask(NPC npc, String message) { orden = message; }

    @Override
    public void act() {
        //se pasa este NPC y el game.getPlayer()
        int intAction = behaviour.nextAction(this, game.getPlayer(), game.world.night);
        Result result = null;
        switch (intAction){
            case 0: //Atacar NPC
                Command parse = game.parser.parse(orden);
                if (parse.parseError) {
                    // bad command
                    this.hear(this + " te responde: Como has dicho?");
                    return;
                }
                // execute
                result = game.engine.execute(this, parse);
                break;
            case 1: //Atacar jug
                result = game.engine.execute(this, Command.act(Word.Action.KILL, game.getPlayer()));
                break;
            case 2: //Seguir jug
                result = game.engine.execute(this, Command.act(Word.Action.FOLLOW, game.getPlayer()));
                break;
            case 3: //Ir a
                result = game.engine.execute(this, Command.go(Utils.pickRandom(Word.Direction.values()))); //TODO: limitar a las salas a las que pueden ir según su lista
                break;
            case 4: //Morir
                Result.done(this.name + " muere.");
                break;
            case 5: //Dormir
                dormido = true;
                break;
            case 6: //Teletransportar
                getLocation().notifyNPCs(this, this + " dice: Secuestraos");
                game.getPlayer().moveTo(game.findElementByClassName(sitioTP));
                game.findElementByClassName(Thorin.class).moveTo(game.findElementByClassName(GoblinDungeon.class));
                break;
            case 7: //Hablar
                if(primerEncuentroJugador){
                    int num = (int) (Math.random()*(saludos.size() - 1));
                    getLocation().notifyNPCs(this, this + " dice: " + saludos.get(num));
                    primerEncuentroJugador = false;
                }
                else{
                    int num = (int) (Math.random()*(frases.size() - 1));
                    getLocation().notifyNPCs(this, this + " dice: " + frases.get(num));
                }
                break;
            case 8: //Dar
                Element f = null;
                for (Iterator<Element> it = elements.iterator(); it.hasNext(); ) {
                    f = it.next();
                }
                result = game.engine.execute(this, Command.act(Word.Action.GIVE, f, game.findElementByClassName(Bilbo_Player.class)));
                break;
            case 9: //Leer
                getLocation().notifyNPCs(this, this + " dice: Ve hacia el este desde el Gran Lago para llegar a Ciudad del lago");
                break;
            case 10: //Matar
                result = game.engine.execute(this, Command.act(Word.Action.KILL, game.getPlayer()));
                alive = false;
                break;
            case 11: //Abrir TODO:
                result = game.engine.execute(this, Command.act(Word.Action.OPEN, game.findElementByClassName(elementoAbrir), game.findElementByClassName(Bilbo_Player.class)));
                break;
            //Por defecto
            case -1:
            default:
                result = game.engine.execute(this, Command.simple(Word.Action.WAIT));
                break;
        }
        //System.out.println(this + ": " + result);
        if (!result.done) {
            this.hear(this + " te responde: No puedo hacer eso");
        } else {
            hear(result.output);
        }
    }

    @Override
    public void init() {
        super.init();
    }
}
