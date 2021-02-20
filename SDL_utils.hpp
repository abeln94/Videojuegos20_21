//
// Created by abeln on 16/02/2021.
//

#ifndef VIDEOJUEGOS20_21_SDL_UTILS_HPP
#define VIDEOJUEGOS20_21_SDL_UTILS_HPP

#include "SDL.h"
#include "SDL_ttf.h"


void get_text_and_rect(SDL_Renderer *renderer, int x, int y, char *text,
                       TTF_Font *font, SDL_Texture **texture, SDL_Rect *rect);

#endif //VIDEOJUEGOS20_21_SDL_UTILS_HPP
