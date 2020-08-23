package comp1110.ass2;

public class Puzzle {
    final private Direction direction;
    final private Color color;

    /**
     * Constructor.
     * @param direction the orinatation of this puzzle piece
     * @param color the color used to represent this puzzle piece
     */
    Puzzle(Direction direction, Color color){
        this.direction=direction;
        this.color=color;
    }


    /** @return the orientation of this puzzle piece */
    public Direction getDirection(){return direction;}

    /** @return the color of this puzzle piece */
    public Color getColor(){return color;}


}
