#include "window.h"
#include <iostream>

/**
 * Constructor
 * @param width initial width of the window
 * @param height initial height of the window
 */
Window::Window(int width, int height) {

    //initialize the SDL library
    if (SDL_Init(SDL_INIT_VIDEO) != 0) raiseError();

    // create window
    win = SDL_CreateWindow("Practica1", SDL_WINDOWPOS_CENTERED, SDL_WINDOWPOS_CENTERED, width, height, SDL_WINDOW_RESIZABLE);
    if (win == nullptr) raiseError();

    //create alpha 2D rendering context for alpha window
    renderer = SDL_CreateRenderer(win, -1, SDL_RENDERER_ACCELERATED | SDL_RENDERER_PRESENTVSYNC);
    if (renderer == nullptr) raiseError();

    // load bmpSurface
    SDL_Surface *bmpSurface = SDL_LoadBMP("edu-homepage2007.bmp");
    backgroundTexture = SDL_CreateTextureFromSurface(renderer, bmpSurface);
    SDL_FreeSurface(bmpSurface);
}

/**
 * To exit on error
 */
void Window::raiseError() {
    std::cerr << SDL_GetError() << std::endl;
    raise(-1);
}

/**
 * @return the window renderer
 */
SDL_Renderer *Window::getRenderer() {
    return renderer;
}

/**
 * @return the window object
 */
SDL_Window *Window::getWindow() {
    return win;
}

/**
 * Paints all the window black
 */
void Window::renderBlack() {
    SDL_SetRenderDrawColor(renderer, 0x00, 0x00, 0x00, 255);
    SDL_RenderClear(renderer); //si se comenta se convierte en un salvapantallas bonito
}

/**
 * Paints the image to the window
 */
void Window::renderImage() {
    SDL_RenderCopy(renderer, backgroundTexture, nullptr, nullptr);
}

/**
 * Updates the window with the current renderer
 */
void Window::display() {
    SDL_RenderPresent(renderer);
}
