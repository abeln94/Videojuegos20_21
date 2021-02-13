#pragma once
#include <SDL.h>

class window
{
private:
	SDL_Window* win = nullptr;
	SDL_Renderer* renderer = nullptr;

	int width = 640;
	int height = 480;
	bool closed = false;
	bool init();
	void printError();
public:
	window(int width, int height);
	SDL_Renderer* getRenderer();
	SDL_Window* getWindow();
	bool poll_events();
	void render();
	void render2();
};

