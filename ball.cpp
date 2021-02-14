#include "ball.h"
#include <SDL.h>
#include <cmath>

/**
 * Constructor for the ball
 * @param radius radius
 * @param xSpeed horizontal speed
 * @param ySpeed vertical speed
 */
Ball::Ball(int radius, int xSpeed, int ySpeed) : radius(radius), xSpeed(xSpeed), ySpeed(ySpeed) {}

/**
 * Renders the ball into the @param renderer
 */
void Ball::render(SDL_Renderer *renderer) const {
    // draw circle
    for (int i = 0; i <= radius; i++) {
        int dx = floor(sqrt((2.0 * radius * i) - (i * i)));

        //Cambia el color con el que se pinta la imagen
        SDL_SetRenderDrawColor(renderer, red, green, blue, alpha);
        int x1 = (xPos - dx); //si se cambia dx por i, sale un rombo
        int x2 = (xPos + dx); //si se cambia dx por i, sale un rombo
        int ya = yPos + i - radius;
        int yb = yPos - i + radius;
        //Pinta mitad superior
        SDL_RenderDrawLine(renderer, x1, ya, x2, ya);
        //Pinta mitad inferior
        SDL_RenderDrawLine(renderer, x1, yb, x2, yb);
    }
}

/**
 * Updates the ball internal properties
 * @param width window width
 * @param height window height
 */
void Ball::update(int width, int height) {
    // update pos
    xPos += xSpeed * xDir;
    yPos += ySpeed * yDir;

    if (xPos <= radius) xDir = 1;
    if (xPos >= width - radius) xDir = -1;

    if (yPos <= radius) yDir = 1;
    if (yPos >= height - radius) yDir = -1;

    // update color
    green += colorPos;

    if (green == 0xFF) colorPos = -1;
    if (green == 0x00) colorPos = 1;
}

