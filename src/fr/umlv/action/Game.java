package fr.umlv.action;

import fr.umlv.zen5.ApplicationContext;
import fr.umlv.zen5.Event;

import java.awt.*;
import java.util.Objects;

public class Game {
    private final Movement move;
    private boolean win = false;

    /**
     * Constructor of Game 
     * @param move Movement 
     */
    public Game(Movement move) {
        this.move = Objects.requireNonNull(move);
    }

    /**
     * run the game and while win is false, clear the window and draw the game.
     * @param context the application context.
     */
    public void run(ApplicationContext context) {

        var width = (int)context.getScreenInfo().getWidth();
        var height = (int)context.getScreenInfo().getHeight();
        while (!win) {
            context.renderFrame(graphics2D -> {
                var event = context.pollOrWaitEvent(300);
                win = update(event);
                graphics2D.clearRect(0, 0, width , height);
                render(graphics2D, width, height);
            });
        }
    }


    /**
     * Process keyboard events. If the process is valid, the key of the event is checking in the method update of move
     * @param event Current events.
     * @return true if the update of move return true, else false.
     */
    private boolean update(Event event) {
        if (event != null) {
            var action = event.getAction();
            if (action == Event.Action.KEY_PRESSED) {
                return move.update(event.getKey());
            }
        }
        return false;
    }

    /**
     * Renders the game objects.
     * @param graphics2D Instance of Graphics2D object used to draw shapes on the screen.
     * @param width of the Screen
     * @param height of the Screen
     */
    private void render(Graphics2D graphics2D, int width, int height) {
        move.render(graphics2D, width, height);
    }
}
