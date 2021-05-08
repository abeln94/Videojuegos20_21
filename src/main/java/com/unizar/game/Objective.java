package com.unizar.game;

import java.io.Serializable;

/**
 * A serializable function
 */
public abstract class Objective implements Serializable {
    public abstract boolean isCompleted();
}
