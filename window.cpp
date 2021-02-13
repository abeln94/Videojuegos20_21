#include "window.h"
#include <iostream>

bool window::init()
{
	if (SDL_Init(SDL_INIT_VIDEO) != 0) //initialize the SDL library
	{
		printError();
		return false;
	}

	win = SDL_CreateWindow("Practica1", SDL_WINDOWPOS_CENTERED, SDL_WINDOWPOS_CENTERED, width, height, SDL_WINDOW_RESIZABLE); //se crea la ventana

	if (win == nullptr)
	{
		printError();
		return false;
	}

	renderer = SDL_CreateRenderer(win, -1, SDL_RENDERER_ACCELERATED | SDL_RENDERER_PRESENTVSYNC); //create a 2D rendering context for a window

	if (renderer == nullptr)
	{
		printError();
		return false;
	}

	return true;
}

window::window(int width, int height) : width(width), height(height), closed(!init()) { }

SDL_Renderer* window::getRenderer()
{
	return renderer;
}

SDL_Window* window::getWindow()
{
	return win;
}


bool window::poll_events()
{
	SDL_Event event;
	return SDL_PollEvent(&event) && (event.type == SDL_QUIT);
}

void window::render()
{
	SDL_RenderPresent(renderer);
	SDL_SetRenderDrawColor(renderer, 0x00, 0x00, 0x00, 255);
	SDL_RenderClear(renderer); //si se comenta se convierte en un salvapantallas bonito
}

void window::printError() 
{
	std::cerr << SDL_GetError() << std::endl;
}

//background fuera
void window::render2()
{
	SDL_RenderPresent(renderer);
	SDL_Surface* background = SDL_LoadBMP("edu-homepage2007.bmp");
	SDL_Texture* BlueShapes = SDL_CreateTextureFromSurface(renderer, background);
	SDL_FreeSurface(background);
	SDL_RenderCopy(renderer, BlueShapes, NULL, NULL);
	SDL_RenderPresent(renderer);

}