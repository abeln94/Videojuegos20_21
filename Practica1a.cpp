#include <SDL.h>
#include "Window.hpp"
#include "Rooms.hpp"


int main(int argv, char** args) {

    // init
    initWindow();

    // main loop
    bool running = true;
    while (running) {

        // draw
        drawRoom();

        // update
        displayWindow();

        // check event
        SDL_Event event;
        if (SDL_PollEvent(&event)) {
            switch (event.type) {
                case SDL_QUIT:
                    // close window -> exit
                    running = false;
                    break;
                case SDL_KEYDOWN: {
                    // keydown -> manage
                    SDL_Keycode key = event.key.keysym.sym;
                    switch (key) {
                        case SDLK_ESCAPE:
                            // ESC -> exit
                            running = false;
                            break;
                        default:
                            // other key -> room manager
                            onKeyDown_Room(key);
                    }
                }
            }
        }
    }

    // exit
    return 0;
}