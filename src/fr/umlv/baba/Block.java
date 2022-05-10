package fr.umlv.baba;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import fr.umlv.words.WordTypes;
import fr.umlv.words.Words;

public class Block {
    private Image image;
    private final Words word;
    private final boolean controllable;
    public Block secondNoun = null;
    public ArrayList<Words> properties = new ArrayList<>();

    /**
     * Constructor of a Block : need a word (Words) and a boolean controllable to know the image associated.
     * @param word Words
     * @param controllable boolean true if he can move, else false
     */
    public Block(Words word, boolean controllable) {
        this.controllable = controllable;
        this.word = Objects.requireNonNull(word);
    }

    /**
     * Create a block by the character token given in the parameter. If isToken(token) not exist, an Exception is throw
     * If the new block with the word associated to token is a NOUN and the token is a lower case, the word is controllable, else not. 
     * @param token char 
     * @return new Block with the word associated to the token and the boolean for the controllable.
     */
    public static Block create(char token) {
        var words = Words.values();
        for (var word : words) {
            if (word.isToken(token)) {
                return new Block(word,word.isType(WordTypes.NOUN) && Character.isLowerCase(token));
            }
        }
        throw new IllegalArgumentException("Unknown token " + token);
    }
    /**
     * Draw all the objects in the block at the given position (x,y).
     * @param graphics2D Instance of Graphics2D object used to draw shapes on the screen.
     * @param x integer for the coordinate x
     * @param y integer for the coordinate y
     */
    public void render(Graphics2D graphics2D, int x, int y) {
        if (isType(WordTypes.BLANK)) {
            return;
        }
        if (image == null) {
            if (controllable) {
                image = Toolkit.getDefaultToolkit().getImage("res/images/" + word.name() + "_0.gif");
            } else {
                image = Toolkit.getDefaultToolkit().getImage("res/images/Text_" + word.name() + "_0.gif");
            }
        }
        if (secondNoun != null) {
            Image image2;
            image2 = Toolkit.getDefaultToolkit().getImage("res/images/" + secondNoun.word.name() + "_0.gif");
            graphics2D.drawImage(image2 ,x , y, null);
        }
        graphics2D.drawImage(image ,x , y, null);
    }
    
    /**
     * Update the properties of the second object in the block at the position of the object at each movement of them.
     * @param rules List<Rule> with all the rules in the map
     */
    public void updateSecondNounProp(List<Rule> rules) {
    	if (secondNoun == null) {
    		return; 
    	}
    	secondNoun.updateProperties(rules);
    }

    /**
     * Take the word in first of rules, select each third word of first in rules and add it to the list of properties of first. 
     * @param rules List<Rule> with all the rules in the map
     */
    public void updateProperties(List<Rule> rules) {
        properties.clear();
        rules.stream()
                .filter(e -> e.first.isName(word.name()))
                .map(e -> e.third.word)
                .forEach(word -> properties.add(word));
    }
    
    /**
     * Check if the property exist in the properties of the block where we are
     * @param property Words
     * @return true if the property given in argument exist in the list properties, else false 
     */
    public boolean hasProperty(Words property) {
        return this.properties.contains(property);
    }

    /**
     * Take the word in the block where we are and check if the name is equal to his name
     * @param name String
     * @return true if the name given in argument is the same, else false 
     */
    public boolean isName(String name) {
        return word.isName(name);
    }
    
    /**
     * Check if the type of the block where we are is the same to the argument type
     * @param type of the Word (NOUNS, OPERATOR, PROPERTY, BLANK)
     * @return true if the type given in argument is the same, else false
     */
    public boolean isType(WordTypes type) {
        return word.isType(type);
    }

    /**
     * @return true if the Noun in the block can move or not, else false
     */
    public boolean isControllable() {
        return controllable;
    }


}
