#include "ball.h"
#include <iostream>
#include <cstdlib>
#include <string>
#include <SDL.h>

ball::ball(int x, int y, int radius, int x_speed, int y_speed) : x(x), y(y), radius(radius), x_speed(x_speed), y_speed(y_speed) {}

void ball::render(SDL_Renderer* renderer, int r, int g, int b, int a)
{
	for (double i = 0; i <= radius; i++)
	{
		double dx = floor(sqrt((2.0 * radius * i) - (i * i)));

		//Cambia el color con el que se pinta la imagen
		SDL_SetRenderDrawColor(renderer, r, g, b, a);
		double x1 = (this->x - dx); //si se cambia dx por i, sale un rombo
		double x2 = (this->x + dx); //si se cambia dx por i, sale un rombo
		double ya = this->y + i - radius;
		double yb = this->y - i + radius;
		//Pinta mitad superior
		SDL_RenderDrawLine(renderer, x1, ya, x2, ya);
		//Pinta mitad inferior
		SDL_RenderDrawLine(renderer, x1, yb, x2, yb);
	}
}

void ball::update(int h, int w, int radius)
{
	x += x_speed;
	y += y_speed;

	if (x <= 20 || x >= h - radius)
		x_speed *= -1;

	if (y <= 20 || y >= w - radius)
		y_speed *= -1;
}

