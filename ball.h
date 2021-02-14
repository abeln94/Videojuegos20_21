#pragma once

#include <SDL.h>

/**
 * A ball that moves and bounces
 */
class Ball {
private:
    // parameters
    int radius;
    int xSpeed;
    int ySpeed;

    // local variables
    int xPos = 0;
    int yPos = 0;
    int xDir = 1;
    int yDir = 1;
    int red = 0xFF, green = 0xFF, blue = 0xFF, alpha = 0xFF;
    int colorPos = -1;

public:
    Ball(int radius, int xSpeed, int ySpeed);

    void render(SDL_Renderer *renderer) const;

    void update(int width, int height);
};

