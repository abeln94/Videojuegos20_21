#pragma once
#include <SDL.h>

class ball
{
private:
	int x;
	int y;
	int radius;
	int x_speed;
	int y_speed;

public:
	ball(int x, int y, int radius, int x_speed, int y_speed);
	void render(SDL_Renderer* renderer, int r, int g, int b, int a);
	void update(int h, int w, int radius);
};

