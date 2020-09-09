package comp1110.ass2;

public class puzzlePieces {
    public Direction direction;
    public Color color;
    public int row;
    public int column;

    /**
     * Constructor.
     * @param direction the orientation of this puzzle piece
     * @param color the color used to represent this puzzle piece
     * @param row the row position of this puzzle is placed
     * @param column the column position of this puzzle is placed
     */
    public puzzlePieces(Direction direction, Color color, int row, int column){
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

//    /**
//     * @return the position of this puzzle piece
//     */
//    public int[][] getPosition() {
//        return position;
//    }






}


