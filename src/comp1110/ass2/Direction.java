package comp1110.ass2;

/**
 * This enumeration type specifies the four general directions that
 * individual piece may be place (North, South, West and East).
 *
 */

public enum Direction {

    NORTH('N'), SOUTH('S'), EAST('E'), WEST('W');

    public char value;

    Direction(char value) {
        this.value = value;
    }

}
