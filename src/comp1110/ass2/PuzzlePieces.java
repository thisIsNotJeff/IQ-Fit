package comp1110.ass2;

/**
 * @author Boyang Gao, Yuxuan Hu, Qinrui Cheng
 */

public class PuzzlePieces {
    public Color color;
    public int row;
    public int column;
    public Direction direction;

    /**
     * Constructor.
     * @param direction the orientation of this puzzle piece
     * @param color the color used to represent this puzzle piece
     * @param row the row position of this puzzle is placed
     * @param column the column position of this puzzle is placed
     */
    public PuzzlePieces(Direction direction, Color color, int row, int column){
        this.direction=direction;
        this.color=color;
        this.row=row;
        this.column=column;
    }

    /**
     * @return the orientation of this puzzle piece placed
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * @return the color of this puzzle piece
     */
    public Color getColor() {
        return color;
    }

    public String toString() {
        String color = Character.toString(this.color.value);
        String direction = Character.toString(this.direction.value);
        String s = color + this.column + this.row + direction;
        return s;
    }

}


