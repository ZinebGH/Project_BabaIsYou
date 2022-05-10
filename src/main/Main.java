package main;

import fr.umlv.action.Game;
import fr.umlv.action.Movement;
import fr.umlv.baba.Grid;
import fr.umlv.zen5.Application;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException {
    	
        Application.run(Color.BLACK, context -> {
        	if (args.length > 0) {
        		//Open a level choosen by the player.
        		if (args[0].equals("--level")) {
        			Grid grid = null;
                    try {
                        grid = Grid.create("res/levels/" + args[1]);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Movement move = new Movement(grid);
                    var game = new Game(move);
                    game.run(context);
                    context.exit(0);
        		}
        		//Open all levels one by one after a win.
        		if (args[0].equals("--levels")) {
        			File repertory = new File("res/" + args[1]);
	            	String[] liste = repertory.list();
	            	Arrays.sort(liste);
	            	if (liste != null) {
	            		for (int i = 0; i < liste.length; i++) {
	                        System.out.println(liste[i]);
	                        Grid grid = null;
	                        try {
	                            grid = Grid.create("res/" + args[1] + "/" + liste[i]);
	                        } catch (IOException e) {
	                            e.printStackTrace();
	                        }
	                        Movement move = new Movement(grid);
	                        var game = new Game(move);
	                        game.run(context);
	            		}
	            	}
        		}
        		//Not finished
        		if (args[0].equals("--execute")) {
        			System.out.println(args[0]);
        		}
        	}
        });
    }
}
