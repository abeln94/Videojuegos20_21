#include "Window.hpp"
#include <iostream>

using namespace std;

// window  properties
static const int WIDTH = 640;
static const int HEIGHT = 480;

// private variables
SDL_Window *win = nullptr;

// public variables
/**
 * The renderer object
 */
SDL_Renderer *renderer = nullptr;

/**
 * To exit on error
 */
void raiseError() {
    cerr << SDL_GetError() << endl;
    raise(-1);
}

/**
 * Initialize window
 */
void initWindow() {
    //initialize the SDL library
    if (SDL_Init(SDL_INIT_VIDEO) != 0) raiseError();

    // create window
    win = SDL_CreateWindow("Practica1", SDL_WINDOWPOS_CENTERED, SDL_WINDOWPOS_CENTERED, WIDTH, HEIGHT, SDL_WINDOW_RESIZABLE);
    if (win == nullptr) raiseError();

    //create alpha 2D rendering context for alpha window
    renderer = SDL_CreateRenderer(win, -1, SDL_RENDERER_ACCELERATED | SDL_RENDERER_PRESENTVSYNC);
    if (renderer == nullptr) raiseError();
}



/**
 * Updates the window with the current renderer
 */
void displayWindow() {
    SDL_RenderPresent(renderer);
}
