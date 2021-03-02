package com.unizar.game;

abstract public class Room {
    abstract public void onEnter();
    abstract public String onCommand(String command);
}
