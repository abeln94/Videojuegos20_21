//
// Created by abeln on 15/02/2021.
//

#include <map>
#include "Rooms.hpp"
#include "Window.hpp"

using namespace std;

/**
 * Rooms
 */
map<string, Room> rooms = {
        {"center", Room{
                .up = "up",
                .down = "down",
                .left = "left",
                .right = "right",
                .r = 255,
                .g = 255,
                .b = 255,
        }},
        {"up",     Room{
                .up = "down",
                .down = "center",
                .r = 255,
        }},
        {"down",   Room{
                .up = "center",
                .down = "up",
                .g = 255,
        }},
        {"left",   Room{
                .left = "right",
                .right = "center",
                .b = 255,
        }},
        {"right",  Room{
                .left = "center",
                .right = "left",
        }},
};

/**
 * Current room
 */
Room *current = &rooms["center"];

/**
 * Draws the current room
 * @param renderer the renderer
 */
void drawRoom() {
    SDL_SetRenderDrawColor(renderer, current->r, current->g, current->b, 255);
    SDL_RenderClear(renderer);
}

/**
 * Traverses the rooms
 * @param key
 */
void onKeyDown_Room(SDL_Keycode key) {
    string nextRoom;

    switch (key) {
        case SDLK_UP:
            nextRoom = current->up;
            break;
        case SDLK_DOWN:
            nextRoom = current->down;
            break;
        case SDLK_LEFT:
            nextRoom = current->left;
            break;
        case SDLK_RIGHT:
            nextRoom = current->right;
            break;
        default:
            nextRoom = "";
    }

    if (!nextRoom.empty()) {
        current = &rooms[nextRoom];
    }
}
