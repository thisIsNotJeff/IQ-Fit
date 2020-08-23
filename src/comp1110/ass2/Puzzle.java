package comp1110.ass2;

public class Puzzle {
    final private Direction direction;
    final private Color color;
    public int[][] position = new int[10][5];

    /**
     * Constructor.
     * @param direction the orinatation of this puzzle piece
     * @param color the color used to represent this puzzle piece
     */
    Puzzle(Direction direction, Color color, int[][] position){
        this.direction=direction;
        this.color=color;
        this.position=position;
    }


    /** @return the orientation of this puzzle piece placed */
    public Direction getDirection(){return direction;}

    /** @return the color of this puzzle piece */
    public Color getColor(){return color;}

    /** @return the position of this puzzle piece */
    public int[][] getPosition(){return position;}

}
