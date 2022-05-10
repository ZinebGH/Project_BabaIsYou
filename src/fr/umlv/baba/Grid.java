package fr.umlv.baba;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import fr.umlv.words.Words;


public class Grid {
    private final int rows; 
    private final int cols; 
    public Block[][] blocks; //List of the blocks in the grid with the length of rows and cols 
    private final List<Rule> rules = new ArrayList<>();

    /**
     * Constructor of grid constituate with a list of blocks (type Block) and have a size rows*cols
     * @param rows number of row in the file txt
     * @param cols number of col in the file txt
     */
    public Grid(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        blocks = new Block[cols][rows];
    }

    /**
     * Create the list of blocks with the file name given in parameter, if the file name not exist : send exception to the main.
     * @param fileName
     * @return the new grid create
     * @throws IOException
     */
    public static Grid create(String fileName) throws IOException {
        var lines = Files.readAllLines(Path.of(fileName));
        var rows = lines.size();
        var cols = lines.get(0).length();
        var grid = new Grid(rows, cols);

        for (var i = 0; i < cols; i++) {
            for (var j = 0; j < rows; j++) {
                grid.blocks[i][j] = Block.create(lines.get(j).charAt(i));
            }
        }
        return grid;
    }

    
    /**
     * Check if a Noun in all the block of blocks with the Property YOU exist in the grid 
     * @return true if the Noun exist or false if not
     */
    public boolean hasYou() {
    	boolean res = false;
        for (var i = 0; i < cols; i++) {
            for (var j = 0; j < rows; j++) {
                if (blocks[i][j].hasProperty(Words.YOU) && !blocks[i][j].isControllable()) {
                    res = true;
                }
                if (blocks[i][j].hasProperty(Words.YOU) && blocks[i][j].isControllable()) {
                	res = true;
                }
            }
        }
        return res;
    }



    /**
     * Renders the Grid with all the objects in it.
     * @param graphics2D Instance of Graphics2D object used to draw shapes on the screen.
     * @param width of the screen use to draw the shapes
     * @param height of the screen use to draw the shapes
     */
    public void render(Graphics2D graphics2D, int width, int height) {
        for (var i = 0; i < cols; i++) {
            for (var j = 0; j < rows; j++) {
                var block = blocks[i][j];
                int blockSize = 24;
                int x = (i * blockSize) + (width - blockSize * cols) / 2;
                int y = (j * blockSize) + (height - blockSize * rows) / 2;
                block.render(graphics2D, x, y);
            }
        }
    }
    

    /**
     * Find all vertical and horizontal rules
     */
    public void findRules() {
        rules.clear();
        findHVRules(cols, rows);
        findHVRules(rows, cols);
    }

    /**
     * Find all the rules Vertical or Horizontal in the Grid
     * @param colrow Look from cols to rows
     * @param rowcol Look from rows to cols
     */
    private void findHVRules(int colrow, int rowcol) {
        Block first;
        Block second;
        Block third;
        for (int i = 0; i < colrow; i++) {
            for (int j = 0; j <= rowcol - 3; j++) {
                if (colrow == cols) {
                    first = blocks[i][j];
                    second = blocks[i][j + 1];
                    third = blocks[i][j + 2];
                }
                else {
                    first = blocks[j][i];
                    second = blocks[j + 1][i];
                    third = blocks[j + 2][i];
                }
                addValidRules(first, second, third);
            }
        }
    }

    /**
     * Add the parameters in a valid rule : 1st must be a NOUN, the second an OPERATOR and the last a PROPERTY or NOUN
     * @param first Block Which has the type NOUN 
     * @param second Block Which has the type OPERATOR
     * @param third Block Which has the type PROPERTY or NOUN
     */
    private void addValidRules(Block first, Block second, Block third) {
        if (Rule.isValid(first, second, third)) {
            rules.add(new Rule(first, second, third));
        }
    }

    /**
     * Updates all properties of all the character who are controllable, in our list of Block
     */
    public void updateProperties() {
        for (var i = 0; i < cols; i++) {
            for (var j = 0; j < rows; j++) {
                blocks[i][j].updateProperties(rules);
                blocks[i][j].updateSecondNounProp(rules);
            }
        }
    }

    /**
     * Recovers number of columns
     * @return cols number of col in the file txt
     */
    public int getCols() {
        return cols;
    }

    /**
     * Recovers number of rows
     * @return rows number of row in the file txt
     */
    public int getRows() {
        return rows;
    }
}
