package com.unizar.game;

abstract public class Room {
    abstract public void onEnter();
    abstract public boolean onCommand(String command);
}
