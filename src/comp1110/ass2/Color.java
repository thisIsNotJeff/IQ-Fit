package comp1110.ass2;

/**
 * This enumeration type represents the 9 distinct color each
 * corresponding a distinct playing shape in IQFit game.
 *
 */

public enum Color {
    BLUE('B'), GREEN('G'), INDIGO('I'), LIMEGREEN('L'), NAVYBLUE('N'), PINK('P'), RED('R'), SKYBLUE('S'), YELLOW('Y'),
    blue('b'), green('g'), indigo('i'), limegreen('l'), navyblue('n'), pink('p'), red('r'), skyblue('s'), yellow('y');

    final private char value;

    Color(char value){ this.value=value;}

}
