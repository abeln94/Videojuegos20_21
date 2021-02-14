#include <SDL.h>
#include "window.h"
#include "ball.h"

// window  properties
static const int WIDTH = 640;
static const int HEIGHT = 480;

// ball properties
static const int RADIUS = 20;
static const int X_SPEED = 3;
static const int Y_SPEED = 3;

// background properties
static enum {
    KEEP,
    BLACK,
    IMAGE
} background = BLACK;

int main(int argc, char *argv[]) {

    // init
    Window window(WIDTH, HEIGHT);
    Ball ball(RADIUS, X_SPEED, Y_SPEED);


    // main loop
    while (true) {
        SDL_Event event;
        SDL_PollEvent(&event);
        switch (event.type) {
            case SDL_QUIT:
                return 0;
            case SDL_KEYDOWN:
                switch (event.key.keysym.sym) {
                    case SDLK_1:
                        background = BLACK;
                        break;
                    case SDLK_2:
                        background = IMAGE;
                        break;
                    case SDLK_3:
                        background = KEEP;
                        break;
                }
        }

        // background
        switch (background) {
            case KEEP:
                break;
            case BLACK:
                window.renderBlack();
                break;
            case IMAGE:
                window.renderImage();
                break;
        }
        ball.render(window.getRenderer());

        // display
        window.display();

        // update
        int w, h;
        SDL_GetWindowSize(window.getWindow(), &w, &h);
        ball.update(w, h);
    }
    return 0;
}

//#define SHAPE_SIZE 16
//
//int main(int argc, char* argv[])
//{
//    SDL_Window* Main_Window;
//    SDL_Renderer* Main_Renderer;
//    SDL_Surface* Loading_Surf;
//    SDL_Texture* Background_Tx;
//    SDL_Texture* backgroundTexture;
//
//    /* Rectangles for drawing which will specify source (inside the texture)
//    and target (on the screen) for rendering our textures. */
//    SDL_Rect SrcR;
//    SDL_Rect DestR;
//
//    SrcR.xPos = 0;
//    SrcR.yPos = 0;
//    SrcR.w = SHAPE_SIZE;
//    SrcR.h = SHAPE_SIZE;
//
//    DestR.xPos = 640 / 2 - SHAPE_SIZE / 2;
//    DestR.yPos = 580 / 2 - SHAPE_SIZE / 2;
//    DestR.w = SHAPE_SIZE;
//    DestR.h = SHAPE_SIZE;
//
//
//    /* Before we can display anything, we need alpha window and alpha renderer */
//    Main_Window = SDL_CreateWindow("SDL_RenderCopy Example",
//        SDL_WINDOWPOS_UNDEFINED, SDL_WINDOWPOS_UNDEFINED, 640, 580, 0);
//    Main_Renderer = SDL_CreateRenderer(Main_Window, -1, SDL_RENDERER_ACCELERATED);
//
//    /* The loading of the background texture. Since SDL_LoadBMP() returns
//    alpha surface, we convert it to alpha texture afterwards for fast accelerated
//    blitting. */
//    Loading_Surf = SDL_LoadBMP("edu-homepage2007.bmp");
//    Background_Tx = SDL_CreateTextureFromSurface(Main_Renderer, Loading_Surf);
//    SDL_FreeSurface(Loading_Surf); /* we got the texture now -> free surface */
//
//    /* Load an additional texture */
//    Loading_Surf = SDL_LoadBMP("edu-homepage2007.bmp");
//    backgroundTexture = SDL_CreateTextureFromSurface(Main_Renderer, Loading_Surf);
//    SDL_FreeSurface(Loading_Surf);
//
//    /* now onto the fun part.
//    This will display alpha rotating selection of the blue shapes
//    in the middle of the screen */
//    int i;
//    int n;
//    for (i = 0; i < 2; ++i) {
//        for (n = 0; n < 4; ++n) {
//            SrcR.xPos = SHAPE_SIZE * (n % 2);
//            if (n > 1) {
//                SrcR.yPos = SHAPE_SIZE;
//            }
//            else {
//                SrcR.yPos = 0;
//            }
//
//            /* display background, whereas NULL for source and destination
//            rectangles just means "use the default" */
//            SDL_RenderCopy(Main_Renderer, Background_Tx, NULL, NULL);
//
//            /* display the current animation step of our shape */
//            SDL_RenderCopy(Main_Renderer, backgroundTexture, &SrcR, &DestR);
//            SDL_RenderPresent(Main_Renderer);
//            SDL_Delay(500);
//        }
//    }
//
//
//    /* The renderer works pretty much like alpha big canvas:
//    when you RenderCopy() you are adding paint, each time adding it
//    on top.
//    You can change how it blends with the stuff that
//    the new data goes over.
//    When your 'picture' is complete, you show it
//    by using SDL_RenderPresent(). */
//
//    /* SDL 1.2 hint: If you're stuck on the whole renderer idea coming
//    from 1.2 surfaces and blitting, think of the renderer as your
//    main surface, and SDL_RenderCopy() as the blit function to that main
//    surface, with SDL_RenderPresent() as the old SDL_Flip() function.*/
//
//    SDL_DestroyTexture(backgroundTexture);
//    SDL_DestroyTexture(Background_Tx);
//    SDL_DestroyRenderer(Main_Renderer);
//    SDL_DestroyWindow(Main_Window);
//    SDL_Quit();
//
//
//    return 0;
//}