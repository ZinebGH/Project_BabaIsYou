package fr.umlv.action;

import fr.umlv.baba.Block;
import fr.umlv.baba.Grid;
import fr.umlv.words.Words;
import fr.umlv.zen5.KeyboardKey;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;


public class Movement {
    private final Grid grid;
    private final ArrayList<int[]> arraypush = new ArrayList<>();

    /**
     * Constructor of Movement who take the grid in argument.
     * @param grid the map non NULL, where the movement will be used.
     */
    public Movement(Grid grid) {
        this.grid = Objects.requireNonNull(grid);
    }

    /**
     * Do an update on the rules and the properties in the grid, and try to see if the key can be used or not.
     * @param key KeyboardKey key is the key which the player used.
     * @return if in the grid we don't have one property YOU return false, else return move(key).
     */
    public boolean update(KeyboardKey key) {
        grid.findRules();
        grid.updateProperties();
        if (!grid.hasYou()) {
            return false;
        }
        return move(key);
    }

    /**
     * Renders the game objects.
     * @param graphics2D Instance of Graphics2D object used to draw shapes on the screen.
     * @param width of the Screen
     * @param height of the Screen
     */
    public void render(Graphics2D graphics2D, int width, int height) {
        grid.render(graphics2D, width, height);
    }

    /**
     * Check and give the new position of the object, from the direction of the key and the position of the object who is moving.
     * @param key type KeyboardKey, it is the key pressed by the player. 
     * @param curs is a list of 2 integers, represent the coordinate of the position of the object which is moving.
     * @return a list of 2 integers, it is the new available position of the object.
     */
    private int[] moveFromKey(KeyboardKey key, int[] curs) {
        int[] res = {curs[0], curs[1]};
        if (key == KeyboardKey.RIGHT && curs[0] + 1 < grid.getCols()) {
            res[0] += 1;
        }

        if (key == KeyboardKey.LEFT && curs[0] - 1 >= 0) {
            res[0] -= 1;
        }

        if (key == KeyboardKey.UP && curs[1] - 1 >= 0) {
            res[1] -= 1;
        }

        if (key == KeyboardKey.DOWN && curs[1] + 1 < grid.getRows()) {
            res[1] += 1;
        }
        return res;
    }

    /**
     * Find the first block with the name "BLANK".
     * @return a block who has the name "BLANK" or null if no blocks have a name "BLANK".
     */
    private Block findBlank() {
        for (var i = 0; i < grid.getCols(); i++) {
            for (var j = 0; j < grid.getRows(); j++) {
                if (grid.blocks[i][j].isName("BLANK")) {
                    return grid.blocks[i][j];
                }
            }
        }
        return null;
    }

    /**
     * Check if the block where we moved has a NOUN. 
     * @param block Block who moved.
     * @return the block we founded or a blank. 
     */
    private Block blankOrSecondNoun(Block block) {
        if (block.secondNoun != null) {
            return block.secondNoun;
        }
        return findBlank();
    }

    /**
     * Take the second block of the block. If the block has no properties or has 
     * DEFEAT or HOT for his property or the name of the block is "BLANK" or he is controllable, means we can't have two blocks in one.
     * @param block has the type Block.
     * @return the second block of block if the condition is false, else the block we give in argument.
     */
    private Block takeSecondNoun(Block block) {
        if ((block.properties.size() == 0 || block.hasProperty(Words.DEFEAT) || block.hasProperty(Words.HOT)) && !block.isName("BLANK") && block.isControllable()) {
            return block;
        }
        return block.secondNoun;
    }

    /**
     * Take two positions (the new and current), do a temporary block of the new position.
     * Give the block in the current position to the new and current take his second block (BLANK or secondNoun). 
     * Give a statement to the secondNoun of the new position of the block : if the name of the temporary block had "BLANK", 
     * the block in new and current position will not have a secondNoun. Else we take the secondNoun of the temporary block and give it to the
     * secondNoun of the block at the new position.
     * @param newcurs coordinates in a list of 2 integers.
     * @param cursPresent coordinates in a list of 2 integers.
     */
    private void moveTo(int[] newcurs, int[] cursPresent) {
        var block = grid.blocks[newcurs[0]][newcurs[1]];
        grid.blocks[newcurs[0]][newcurs[1]] = grid.blocks[cursPresent[0]][cursPresent[1]];
        grid.blocks[cursPresent[0]][cursPresent[1]] = blankOrSecondNoun(grid.blocks[cursPresent[0]][cursPresent[1]]);
        if (block.isName("BLANK")) {
            grid.blocks[newcurs[0]][newcurs[1]].secondNoun = grid.blocks[cursPresent[0]][cursPresent[1]].secondNoun = null;
        }

        if (block.properties.size() == 0 && block.isControllable()) {
            grid.blocks[newcurs[0]][newcurs[1]].secondNoun = block;
        }
    }

    /**
  	 * 
     * @param prec Block.
     * @param first_secondNoun Block.
     * @return first_secondNoun if he is not null, else if return a block of a blank or return null.
     */
    private Block updateFirstElem(Block prec, Block first_secondNoun) {
        if (first_secondNoun != null) {
            return first_secondNoun;
        }
        else if (prec.properties.size() == 0 || prec.hasProperty(Words.DEFEAT) || prec.hasProperty(Words.HOT) &&  prec.isControllable() ) {
            return findBlank();
        }
        return null;
    }

    /**
     * Reverse the direction of key who is given in parameter.
     * @param key KeyboardKey.
     * @return the inverse of the key.
     */
    private KeyboardKey keyboardReverseDir(KeyboardKey key) {
        if (key == KeyboardKey.RIGHT) {
            return KeyboardKey.LEFT;
        }
        if (key == KeyboardKey.LEFT) {
            return KeyboardKey.RIGHT;
        }
        if (key == KeyboardKey.UP) {
            return KeyboardKey.DOWN;
        }
        return KeyboardKey.UP;
    }

    /**
     * Take a block and his position, to see if he can be pushed.
     * @param block verify the condition with this block.
     * @param curs coordinate of the precedent block (for the property SINK).
     * @return true or false depending of all the condition given.
     */
    private boolean isPushable(Block block, int[] curs) {
        return (block.isName("BLANK") || (block.properties.size() == 0 && block.isControllable()) || (block.hasProperty(Words.DEFEAT)
                || block.hasProperty(Words.HOT)) && block.isControllable() || (block.hasProperty(Words.SINK) && grid.blocks[curs[0]][curs[1]].isControllable()));
    }

    private void arraySize() {
        if (arraypush.size() < 3) {
            arraypush.clear();
        }
    }

   /**
    * Add in our array "arraypush" all the elements that can be pushed and after move.
    * @param key direction of the movement.
    * @param curs the coordinate (x, y) of the pushable block. 
    * @param you coordinate in a list of 2 integers, take all the elements with the property YOU on the same direction. 
    */
    private void pushBlock(KeyboardKey key, int[] curs, int[] you) {
        int[] newcurs = {curs[0], curs[1]};
        addYouPush(key, curs, you);
        while (isPush(grid.blocks[newcurs[0]][newcurs[1]])) {
            arraypush.add(newcurs);
            newcurs = moveFromKey(key, curs);
            var block = grid.blocks[newcurs[0]][newcurs[1]];
            if (isPushable(block, curs)) {
                arraypush.add(newcurs);
            }
            else if (newcurs[0] == curs[0] && newcurs[1] == curs[1] || (!block.hasProperty(Words.PUSH) && block.isControllable())) {
                arraypush.clear();
                return;
            }
            curs = newcurs;
        }
        
        arraySize();
    }

    /**
     * 
     * @param block verify the condition on this block.
     * @return true if the noun have the property PUSH with all the condition to push, else false.
     */
    private boolean isPush(Block block) {
        return (block.hasProperty(Words.PUSH) || !block.isControllable()) && !block.isName("BLANK");
    }

    /**
     * Add all the Nouns who have the property YOU and are in the same lines or columns as the first
     * moving NOUN with that property.
     * @param key KeyboardKey of the direction.
     * @param curs coordinate in a list of 2 integers.
     * @param you coordinate in a list of 2 integers, take all the elements with the property YOU on the same direction.
     */
    private void addYouPush(KeyboardKey key, int[] curs, int[] you) {
        int[] newyou = {you[0], you[1]};
        if (isPush(grid.blocks[curs[0]][curs[1]])) {
            while (grid.blocks[newyou[0]][newyou[1]].hasProperty(Words.YOU) && grid.blocks[newyou[0]][newyou[1]].isControllable()) {
                arraypush.add(newyou);
                newyou = moveFromKey(keyboardReverseDir(key), you);
                if (newyou[0] == you[0] && newyou[1] == you[1]) {
                    return;
                }
                you = newyou;
            }
            Collections.reverse(arraypush);
        }
    }

    /**
     * 
     * @param block Block of where we are.
     * @return true if the block or his secondNoun have a property DEFEAT or HOT  or no properties or the block is controllable.
     */
    private boolean defeatHotSecondNoun(Block block) {
        if (block.secondNoun != null) {
            return (block.secondNoun.hasProperty(Words.DEFEAT) || block.secondNoun.hasProperty(Words.HOT) ) && block.secondNoun.properties.size() != 0;
        }
        return (block.hasProperty(Words.DEFEAT) || block.hasProperty(Words.HOT)) && block.properties.size() != 0 && block.isControllable();
    }

    /**
     * 
     * Update the list of block with the help of an Array "arraypush"
     */
    private void updatePush() {
        var prec = grid.blocks[arraypush.get(0)[0]][arraypush.get(0)[1]];
        var first_secondNoun = prec.secondNoun;

        for (int k = 1; k < arraypush.size(); k++) {
            var block = grid.blocks[arraypush.get(k)[0]][arraypush.get(k)[1]];
            if (block.hasProperty(Words.SINK) && block.isControllable() && prec.isControllable()) {
                prec = findBlank();
                block = findBlank();
            }
            if (defeatHotSecondNoun(block) && prec.isControllable() && prec.hasProperty(Words.YOU)) {
                prec = blankOrSecondNoun(block);
            }
            grid.blocks[arraypush.get(k)[0]][arraypush.get(k)[1]] = prec;
            grid.blocks[arraypush.get(k)[0]][arraypush.get(k)[1]].secondNoun = takeSecondNoun(block);
            prec = block;
        }
        grid.blocks[arraypush.get(0)[0]][arraypush.get(0)[1]] = updateFirstElem(prec, first_secondNoun);
    }

    /**
     * Create a new array with all the controllable NOUNS who have the property YOU.
     * @param key Keyboard key
     * @return an ArrayList of all the coordinate (in a list of 2 integers) of the blocks with a controllable NOUN.
     */
    private ArrayList<int[]> arrayYou(KeyboardKey key) {
        ArrayList<int[]> arrayyou = new ArrayList<>();
        for (var i = 0; i < grid.getCols(); i++) {
            for (var j = 0; j < grid.getRows(); j++) {
                if (grid.blocks[i][j].hasProperty(Words.YOU) && grid.blocks[i][j].isControllable()) {
                    arrayyou.add(new int[]{i, j});
                }
            }
        }
        if (key == KeyboardKey.RIGHT || key == KeyboardKey.DOWN) {
            Collections.reverse(arrayyou);
        }
        return arrayyou;
    }

    /**
     * Check if the block has the property HOT and the block is controllable  
     * and the block don't have the property push and the block at the position before this one is controllable and has the property MELT 
     * OR
     * if the block has the property DEFEAT and he is controllable and has at least 1 property.
     * @param block Block of where we are.
     * @param you list of 2 integers, position of the block before block.
     * @return true if condition are valid, else false.
     */
    private boolean isHotMeltOrDefeat(Block block, int[] you){
        if (block.hasProperty(Words.DEFEAT) && block.isControllable() && block.properties.size() != 0) {
            return true;
        }
        return block.hasProperty(Words.HOT) && block.isControllable() && !block.hasProperty(Words.PUSH) && grid.blocks[you[0]][you[1]].isControllable()
                && grid.blocks[you[0]][you[1]].hasProperty(Words.MELT);
    }

    
    /**
     * Movement of nouns with properties YOU and PUSH, and also action for NOUNS with properties SINK or
     * HOT or DEFEAT.
     * @param key keyboard key the direction of the movement
     * @param you coordinate, in a list of 2 integers, of the block with the property YOU.
     * @return true if the block has the property win, else false.
     */
    private boolean moveAux(KeyboardKey key, int[] you) {
        int[] curs = moveFromKey(key, you);
        var block = grid.blocks[curs[0]][curs[1]];
        if (block.isName("BLANK") || (block.properties.size() == 0 && block.isControllable())) {
            moveTo(curs, you);
        }
        pushBlock(key, curs, you);
        if (grid.blocks[curs[0]][curs[1]].hasProperty(Words.WIN) && grid.blocks[curs[0]][curs[1]].isControllable()) {
            return true;
        }
        if (block.hasProperty(Words.SINK) && block.isControllable()) {
            grid.blocks[curs[0]][curs[1]] = findBlank();
            grid.blocks[you[0]][you[1]] = blankOrSecondNoun(grid.blocks[you[0]][you[1]]);
        }
        if (isHotMeltOrDefeat(block, you)) {
            grid.blocks[you[0]][you[1]] = findBlank();
        }
        return false;
    }

    /**
     * Move in function of key and the coordinate (x,y) from the list arrayYou who has all the coordinate of the blocks with the property YOU.
     * @param key keyboard key the direction of the movement.
     * @return boolean true if the player wins or false for other case.
     *
     */
    private boolean move(KeyboardKey key) {
        ArrayList<int[]> arrayyou = arrayYou(key);
        for (int[] you : arrayyou) {
            if (moveAux(key, you)) {
                return true;
            }
        }
        if (arraypush.size() > 2) {
            updatePush();
            arraypush.clear();
        }
        return false;
    }
}
