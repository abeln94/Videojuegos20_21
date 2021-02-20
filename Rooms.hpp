//
// Created by abeln on 15/02/2021.
//

#ifndef VIDEOJUEGOS20_21_ROOMS_HPP
#define VIDEOJUEGOS20_21_ROOMS_HPP

#include <SDL.h>
#include <SDL_ttf.h>
#include <string>

struct Room {
    std::string up;
    std::string down;
    std::string left;
    std::string right;

    int r = 0, g = 0, b = 0;
};

void drawRoom();

void onKeyDown_Room(SDL_Keycode key);

#endif //VIDEOJUEGOS20_21_ROOMS_HPP
