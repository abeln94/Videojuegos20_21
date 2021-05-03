package com.unizar.game.commands;

import com.unizar.game.elements.Element;
import com.unizar.game.elements.Item;
import com.unizar.game.elements.Location;
import com.unizar.game.elements.NPC;
import com.unizar.hobbit.items.GoldenRing;
import com.unizar.hobbit.items.RedKey;
import com.unizar.hobbit.npcs.Bardo;

import java.util.*;

/**
 * Select the next action for an npc.
 */
public class Behaviour {
    final int tam = 12;

    //parametros que se tienen en cuenta a la hora de tomar una decision
    private NPC lastAttackedBy = null;
    private int id = -1;
    private boolean autonomo = false;
    private boolean inmortal = false;
    private boolean puedeDormir = false;
    private boolean puedeTP = false;
    private boolean puedeLeer = false;
    private boolean puedeMatarAJugador = false;
    private List<Element> lugares = new ArrayList<>();
    private boolean dormido = false;
    private boolean primerEncuentroJugador = false;
    public List<String> saludos = new ArrayList<>();
    public List<String> frases = new ArrayList<>();
    private int cansancio = 0;
    public Class<Location> sitioTP = null;
    public Class<Item> elementoAbrir = null;
    public Class<Item> elementoLeer = null;
    private String arma = null;
    private String orden = null;
    private Element posicionNPC = null;
    private boolean noche = false;
    private Set<Element> inventarioNPC = new HashSet<>();
    private String name = null;

    //necesitamos a Bilbo para tener los siguientes datos
    private Set<Element> inventarioJugador = new HashSet<>();


    //matriz de pesos
    //0 (Atacar NPC) 1 (Atacar jug)	2 (Seguir jug) 3 (Ir a)	4 (Morir) 5 (Dormir) 6 (Teletransportar) 7 (Hablar)	8 (Dar)	9 (Leer) 10( Matar)	11 (Abrir)
    List<Integer> pesos = Arrays.asList(2, 2, 3, 3, 3, 1, 2, 1, 2, 2, 3, 2);

    public int nextAction(NPC npc, NPC jugador, boolean night) {
        inventarioJugador = jugador.elements;
        lastAttackedBy = npc.lastAttackedBy;
        autonomo = npc.autonomo;
        inmortal = npc.inmortal;
        puedeDormir = npc.puedeDormir;
        puedeTP = npc.puedeTP;
        puedeLeer = npc.puedeLeer;
        puedeMatarAJugador = npc.puedeMatarAJugador;
        dormido = npc.dormido;
        primerEncuentroJugador = npc.primerEncuentroJugador;
        frases = npc.frases;
        saludos = npc.saludos;
        sitioTP = npc.sitioTP;
        elementoAbrir = npc.elementoAbrir;
        elementoLeer = npc.elementoLeer;
        arma = npc.arma;
        orden = npc.orden;
        noche = night;
        inventarioNPC = npc.elements;
        name = npc.name;
        id = npc.id;

        //se calcula el array general
        List<Boolean> general = generalArray(jugador);
        //se calcula el array de personaje
        List<Boolean> pj = pjArray();
        //si hay algún 2, se devuelve el 2, sino...
        List<Integer> integerForm = productBooltoInt(general, pj);
        if(integerForm.contains(2)){
            //se devuelve la accion del 2
            return indexMax(integerForm);
        }
        else{ //solo 0 y 1
            //se multiplican con el de pesos
            List<Integer> withPesos = productInt(integerForm, pesos);
            //se genera el aleatorio y se multiplican
            List<Integer> finArray = randomArray(withPesos);
            //se elige el valor más alto
            return indexMax(finArray);
        }
    }

    private List<Boolean> generalArray(NPC jugador)
    {
        List<Boolean> general = new ArrayList<Boolean>();
        //0 ARMA != NULL && ORDEN=KILL
        general.add(arma != null && orden != null);

        //1 ARMA != NULL
        general.add(arma != null);

        //2 !AUTONOMO
        general.add(!autonomo);

        //3 AUTONOMO
        general.add(autonomo);

        //4 !INMORTAL && ATAQUE != NULL
        general.add(!inmortal && lastAttackedBy != null);

        //5 PUEDE_DORMIR
        general.add(!puedeDormir);

        //6 TP && SITIO != NULL
        general.add(!puedeTP && sitioTP != null);

        //7 FRASES != NULL
        general.add(frases != null);

        //8 INVENTARIO_NPC != NULL
        general.add(inventarioNPC != null);

        //9 PUEDE_LEER && ORDEN==LEER && INVENTARIO contains el_leer
        general.add(puedeLeer && orden != null && inventarioNPC.contains(elementoLeer));

        //10 MATAR_JUGADOR &&  ATAQUE.quien == JUGADOR
        general.add(puedeMatarAJugador && lastAttackedBy.equals(jugador));

        //11 EL_ABRIR != NULL
        general.add(elementoAbrir != null);
        return general;
    }

    private List<Boolean> pjArray(){
        if(id == 2)
            return butlerArray();
        if(id == 5)
            return gollumArray();
        if(id == 7)
            return dragonArray();
        if(id >= 20 && id <= 29)
            return trollArray();
        return Arrays.asList(true, true, true, true, true, true, true, true, true, true, true, true);
    }

    private List<Integer> productBooltoInt(List<Boolean> g, List<Boolean> pj){
        List<Integer> prodInt = new ArrayList<Integer>();
        for(int i = 0; i < g.size(); i++){
            boolean first = g.get(i);
            boolean second = pj.get(i);
            boolean res = first && second;
            if(i == 4 || i == 10){
                prodInt.add(res ? 2 : 0);
            }
            prodInt.add(res ? 1 : 0);
        }
        return prodInt;
    }

    private List<Integer> productInt(List<Integer> g, List<Integer> pj){
        List<Integer> prodInt = new ArrayList<Integer>();
        for(int i = 0; i < g.size(); i++){
            int first = g.get(i);
            int second = pj.get(i);
            int res = first * second;
            prodInt.add(res);
        }
        return prodInt;
    }

    private List<Integer> randomArray(List<Integer> g){
        List<Integer> prodInt = new ArrayList<Integer>();
        for(int i = 0; i < g.size(); i++){
            int num = (int) (Math.random()*9 + 1);
            prodInt.add(num);
        }
        return prodInt;
    }

    private int indexMax(List<Integer> g){
        int max = 0;
        int pos = 0;
        for(int i = 0; i < g.size(); i++){
            if(g.get(i) > max){
                max = g.get(i);
                pos = i;
            }
        }
        if(max == 0)
            return -1;
        return pos;
    }

    //Funciones propias
    private List<Boolean> butlerArray(){
        List<Boolean> pj = new ArrayList<Boolean>();
        //los que no tienen restricciones extra se ponen a true, para que primen el resto
        pj.add(true);
        //1 !DORMIDO && INV_J contains Llave Roja
        pj.add(!dormido && inventarioJugador.contains(RedKey.class));
        //2-11
        pj.add(true);
        pj.add(true);
        pj.add(true);
        pj.add(true);
        pj.add(true);
        pj.add(true);
        pj.add(true);
        pj.add(true);
        pj.add(true);
        pj.add(true);
        return pj;
    }

    private List<Boolean> gollumArray(){
        List<Boolean> pj = new ArrayList<Boolean>();
        //los que no tienen restricciones extra se ponen a true, para que primen el resto
        pj.add(true);
        //1 && INV_J contains Anillo
        pj.add(inventarioJugador.contains(GoldenRing.class));
        //2-11
        pj.add(true);
        pj.add(true);
        pj.add(true);
        pj.add(true);
        pj.add(true);
        pj.add(true);
        pj.add(true);
        pj.add(true);
        pj.add(true);
        pj.add(true);
        return pj;
    }

    private List<Boolean> dragonArray(){
        List<Boolean> pj = new ArrayList<Boolean>();
        //los que no tienen restricciones extra se ponen a true, para que primen el resto
        pj.add(true);
        //1 && P_NPC == HALL
        pj.add(!dormido && inventarioJugador.contains(RedKey.class));
        //2,3
        pj.add(true);
        pj.add(true);
        //4 && ATAQUE == Bardo
        pj.add(lastAttackedBy.equals(Bardo.class));
        //5-11
        pj.add(true);
        pj.add(true);
        pj.add(true);
        pj.add(true);
        pj.add(true);
        pj.add(true);
        pj.add(true);
        return pj;
    }

    private List<Boolean> trollArray(){
        List<Boolean> pj = new ArrayList<Boolean>();
        //los que no tienen restricciones extra se ponen a true, para que primen el resto
        pj.add(true);
        //1 && NOCHE && !PRIMER_ENCUENTRO
        pj.add(noche && !primerEncuentroJugador);
        //2-6
        pj.add(true);
        pj.add(true);
        pj.add(true);
        pj.add(true);
        pj.add(true);
        //7 &&NOCHE
        pj.add(noche);
        //8-11
        pj.add(true);
        pj.add(true);
        pj.add(true);
        pj.add(true);
        return pj;
    }
}
