#pragma once

#include <SDL.h>

/**
 * A window object (contains the renderer too)
 */
class Window {
private:
    // fields
    SDL_Window *win = nullptr;
    SDL_Renderer *renderer = nullptr;
    SDL_Texture *backgroundTexture = nullptr;

    // utils
    static void raiseError();

public:
    Window(int width, int height);

    SDL_Renderer *getRenderer();

    SDL_Window *getWindow();

    void display();

    void renderImage();

    void renderBlack();
};

