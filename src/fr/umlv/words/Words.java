package fr.umlv.words;


public enum Words {
    //BLANK
    BLANK(WordTypes.BLANK,'_'),

    // NOUNS
    BABA(WordTypes.NOUN,'B'),
    ROCK(WordTypes.NOUN,'R'),
    WALL(WordTypes.NOUN,'W'),
    FLAG(WordTypes.NOUN,'F'),
    SKULL(WordTypes.NOUN,'K'),
    WATER(WordTypes.NOUN,'E'),
    LAVA(WordTypes.NOUN,'L'),
    FLOWER(WordTypes.NOUN,'o'),
    WORLD(WordTypes.NOUN,'D'),

    // OPERATORS
    IS(WordTypes.OPERATOR,'i'),
    //HAS(WordTypes.OPERATOR,'i'),
    //AND(WordTypes.OPERATOR,'a'),


    // PROPERTIES
    YOU(WordTypes.PROPERTY,'Y'),
    WIN(WordTypes.PROPERTY,'!'),
    STOP(WordTypes.PROPERTY,'S'),
    PUSH(WordTypes.PROPERTY,'P'),
    MELT(WordTypes.PROPERTY,'M'),
    HOT(WordTypes.PROPERTY,'H'),
    DEFEAT(WordTypes.PROPERTY,'X'),
    SINK(WordTypes.PROPERTY,'@'),
    CURE(WordTypes.PROPERTY,'C');

    private final char token;
    private final WordTypes type;
    
    /**
     * Define a word with his type and his token
     * @param type is type WordsTypes
     * @param token is a char
     */
    Words(WordTypes type, char token) {
        this.type = type;
        this.token = token;
    }

    /**
     * Take a name and check if the name is the same as the name of the word 
     * @param name String
     * @return true if the name is similar to the name of the word, else false
     */
    public boolean isName(String name) {
        return this.name().equals(name);
    }

    /**
     * Take a type and check if the type is the same as the type of the word 
     * @param name String
     * @return true if the type is similar to the type of the word, else false
     */
    public boolean isType(WordTypes type) {
        return this.type.equals(type);
    }

    /**
     * Take a token and check if the token is the same as the token of the word 
     * @param token char
     * @return true if the token is similar to the token of the word, else false
     */
    public boolean isToken(char token) {
        return this.token == token || (type.equals(WordTypes.NOUN) && Character.toLowerCase(this.token) == token);
    }
}
