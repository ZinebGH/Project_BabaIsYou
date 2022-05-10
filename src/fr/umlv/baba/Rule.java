package fr.umlv.baba;

import java.util.Objects;

import fr.umlv.words.WordTypes;

public class Rule {
	
    final Block first;
    final Block second;
    final Block third;

    
    /**
     * Constructor of a rule with her 3 block 
     * @param first is a block must be non NULL
     * @param second is a block must be non NULL
     * @param third is a block must be non NULL
     */
    public Rule(Block first, Block second, Block third) {
        this.first = Objects.requireNonNull(first);
        this.second = Objects.requireNonNull(second);
        this.third = Objects.requireNonNull(third);
    }

    
    /**
     * Check if a rule is valid or not with 3 conditions on her 3 blocks : first block has to be a type NOUN and not controllable,
     * the second has to be a type OPERATOR and the the last has to be a type PROPERTY or a NOUN not controllable.
     * @param first Block for NOUN
     * @param second Block for OPERATOR
     * @param third Block for NOUN or PROPERTY
     * @return true if the conditions are all valid, else false.
     */
    public static boolean isValid(Block first, Block second, Block third) {
        var a = first.isType(WordTypes.NOUN) && !first.isControllable();
        var b = second.isType(WordTypes.OPERATOR);
        var c = third.isType(WordTypes.PROPERTY) || (third.isType(WordTypes.NOUN) && !third.isControllable());
        return  a && b && c;
    }
}
