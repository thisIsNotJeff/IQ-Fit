package comp1110.ass2.gui;

import comp1110.ass2.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.nio.file.Paths;
import java.util.*;

/**
 * @author Yuxuan Hu completed task7, Boyang Gao wrote task8. Yuxuan Hu made some changes in Boyang's code
 * because there were conflict between task7 and task8
 */

public class Board extends Application {

    private static final int BOARD_WIDTH = 933;
    private static final int BOARD_HEIGHT = 700;
    private final Group root = new Group();
    private static Group hints = new Group();
    //number of piece showing on screen, show the first puzzle piece in beginning
    final int[] current = {0};
    /*the flip state of each piece
    use picture in path1 when equals to 1,use piece in path2 when equals to 2*/
    final int[] current_f = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
    final int[] current_r = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    final int[] on_board = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}; //1 if the piece is on the board
    String challengeString = "";
    // FIXME Task 7: Implement a basic playable Fix Game in JavaFX that only allows pieces to be placed in valid places
    void basic() {
        int[] picture = new int[10];
        String[] path1 = new String[10];
        String[] path2 = new String[10];
        //path2 is the flipped version of path1
        path1[0] = "file:src/comp1110/ass2/gui/assets/B1.png";
        path1[1] = "file:src/comp1110/ass2/gui/assets/G1.png";
        path1[2] = "file:src/comp1110/ass2/gui/assets/I1.png";
        path1[3] = "file:src/comp1110/ass2/gui/assets/L1.png";
        path1[4] = "file:src/comp1110/ass2/gui/assets/N1.png";
        path1[5] = "file:src/comp1110/ass2/gui/assets/O1.png";
        path1[6] = "file:src/comp1110/ass2/gui/assets/P1.png";
        path1[7] = "file:src/comp1110/ass2/gui/assets/R1.png";
        path1[8] = "file:src/comp1110/ass2/gui/assets/S1.png";
        path1[9] = "file:src/comp1110/ass2/gui/assets/Y1.png";
        path2[0] = "file:src/comp1110/ass2/gui/assets/B2.png";
        path2[1] = "file:src/comp1110/ass2/gui/assets/G2.png";
        path2[2] = "file:src/comp1110/ass2/gui/assets/I2.png";
        path2[3] = "file:src/comp1110/ass2/gui/assets/L2.png";
        path2[4] = "file:src/comp1110/ass2/gui/assets/N2.png";
        path2[5] = "file:src/comp1110/ass2/gui/assets/O2.png";
        path2[6] = "file:src/comp1110/ass2/gui/assets/P2.png";
        path2[7] = "file:src/comp1110/ass2/gui/assets/R2.png";
        path2[8] = "file:src/comp1110/ass2/gui/assets/S2.png";
        path2[9] = "file:src/comp1110/ass2/gui/assets/Y2.png";
        String[] color = {"  blue   ", "  green  ", "  indigo ", "limegreen", " navyblue", "  orange ",
                          "   pink  ", "   red   ", " skyblue ", "  yellow "};
        Image image = new Image("file:src/comp1110/ass2/gui/assets/board.png");
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitWidth(700);
        imageView.setFitHeight(370);
        root.getChildren().add(imageView);
        int[][] occupationArray = new int[10][5];
        for(int i = 0; i< 10; i++) {
            for(int j = 0; j< 5; j++) {
                occupationArray[i][j] = 0;
            }
        }
        class DraggablePiece extends ImageView {
            double mouseX0, mouseX1, mouseX2;
            double mouseY0, mouseY1, mouseY2;
            double movementX;
            double movementY;
            int number; //number of this piece
            int col;
            int row;

            DraggablePiece(int number) {
                this.number = number;
                setOnMousePressed(event -> {      // mouse press indicates begin of drag
                    mouseX1 = event.getSceneX();
                    mouseY1 = event.getSceneY();
                    mouseX0 = mouseX1;
                    mouseY0 = mouseY1;
                    Color[] color1 = {Color.blue, Color.green, Color.indigo, Color.limegreen, Color.navyblue,
                            Color.orange, Color.pink, Color.red, Color.skyblue, Color.yellow};
                    Color[] color2 = {Color.BLUE, Color.GREEN, Color.INDIGO, Color.LIMEGREEN, Color.NAVYBLUE,
                            Color.ORANGE, Color.PINK, Color.RED, Color.SKYBLUE, Color.YELLOW};
                    Color c;
                    Direction d;
                    if(current_r[number] == 0) d = Direction.NORTH;
                    else if(current_r[number] == 1) d = Direction.EAST;
                    else if(current_r[number] == 2) d = Direction.SOUTH;
                    else d = Direction.WEST;
                    if(current_f[number] == 1) c = color1[number];
                    else c = color2[number];
                    if(on_board[number] == 1){
                        PuzzlePieces p = new PuzzlePieces(d, c, row, col);
                        GameBoard.removePiece(p, occupationArray);
                    }
                });
                setOnMouseDragged(event -> {      // mouse is being dragged
                    //  can be legally moved.
                    movementX = event.getSceneX() - mouseX1;
                    movementY = event.getSceneY() - mouseY1;
                    setLayoutX(getLayoutX() + movementX);
                    setLayoutY(getLayoutY() + movementY);
                    mouseX1 = event.getSceneX();
                    mouseY1 = event.getSceneY();
                    //event.consume();
                });
                setOnMouseReleased(event -> {     // drag is complete
                    mouseX2 = event.getSceneX();
                    mouseY2 = event.getSceneY();
                    //on_board[current[0]] = 1;
                    adjust();
                    for(int i = 0; i < 10; i++) {
                        if((current[0] != number)&&(on_board[number] == 0)) root.getChildren().remove(this);
                    }
                });
            }
            void adjust() {
                double biasX = 0;
                double biasY = 0;
                double ltX;
                double ltY;
                Color[] color1 = {Color.blue, Color.green, Color.indigo, Color.limegreen, Color.navyblue,
                        Color.orange, Color.pink, Color.red, Color.skyblue, Color.yellow};
                Color[] color2 = {Color.BLUE, Color.GREEN, Color.INDIGO, Color.LIMEGREEN, Color.NAVYBLUE,
                        Color.ORANGE, Color.PINK, Color.RED, Color.SKYBLUE, Color.YELLOW};
                Color c;
                Direction d;
                if(current_r[number] == 0) d = Direction.NORTH;
                else if(current_r[number] == 1) d = Direction.EAST;
                else if(current_r[number] == 2) d = Direction.SOUTH;
                else d = Direction.WEST;
                if(current_f[number] == 1) c = color1[number];
                else c = color2[number];
                if ((current_r[number] == 0) || (current_r[number] == 2)) {
                    biasX = mouseX2 - getLayoutX() + 15;
                    biasY = mouseY2 - getLayoutY() + 15;
                    ltX = mouseX2 - biasX;
                    ltY = mouseY2 - biasY;
                    if((ltX >= 20)&&(ltX <= 590)&&(ltY >= 0)&&(ltY <= 270)) on_board[number] = 1;
                    if ((ltX >= 20) && (ltX < 80)) {setLayoutX(50); col = 0;}
                    else if ((ltX >= 80) && (ltX < 140)) {setLayoutX(110); col = 1;}
                    else if ((ltX >= 140) && (ltX < 200)) {setLayoutX(170); col = 2;}
                    else if ((ltX >= 200) && (ltX < 260)) {setLayoutX(230); col = 3;}
                    else if ((ltX >= 260) && (ltX < 320)) {setLayoutX(290); col = 4;}
                    else if ((ltX >= 320) && (ltX < 380)) {setLayoutX(350); col = 5;}
                    else if ((ltX >= 380) && (ltX < 440)) {setLayoutX(410); col = 6;}
                    else if ((ltX >= 440) && (ltX < 500)) {setLayoutX(470); col = 7;}
                    else if ((ltX >= 500) && (ltX < 560)) {setLayoutX(530); col = 8;}
                    else if ((ltX >= 560) && (ltX < 620)) {setLayoutX(590); col = 9;}
                    else {
                        if(on_board[number] == 1){
                            PuzzlePieces p = new PuzzlePieces(d, c, row, col);
                            GameBoard.removePiece(p, occupationArray);
                        }
                        on_board[number] = 0;
                    }

                    if ((ltY >= 0) && (ltY < 60)) {setLayoutY(30); row = 0;}
                    else if ((ltY >= 60) && (ltY < 120)) {setLayoutY(90); row = 1;}
                    else if ((ltY >= 120) && (ltY < 180)) {setLayoutY(150); row = 2;}
                    else if ((ltY >= 180) && (ltY < 240)) {setLayoutY(210); row = 3;}
                    else if ((ltY >= 240) && (ltY < 300)) {setLayoutY(270); row = 4;}
                    else {
                        if(on_board[number] == 1){
                            PuzzlePieces p = new PuzzlePieces(d, c, row, col);
                            GameBoard.removePiece(p, occupationArray);
                        }
                        on_board[number] = 0;
                    }
                }
                else {
                    if ((number >= 1) && (number <= 4)) {
                        biasX = mouseX2 - getLayoutX() - 15;
                        biasY = mouseY2 - getLayoutY() + 45;
                        ltX = mouseX2 - biasX;
                        ltY = mouseY2 - biasY;
                        if((ltX >= 0)&&(ltX <= 610)&&(ltY >= 0)&&(ltY <= 280)) on_board[number] = 1;
                        if ((ltX >= 0) && (ltX < 70)) {setLayoutX(20); col = 0;}
                        else if ((ltX >= 70) && (ltX < 130)) {setLayoutX(80); col = 1;}
                        else if ((ltX >= 130) && (ltX < 190)) {setLayoutX(140); col = 2;}
                        else if ((ltX >= 190) && (ltX < 250)) {setLayoutX(200); col = 3;}
                        else if ((ltX >= 250) && (ltX < 310)) {setLayoutX(260); col = 4;}
                        else if ((ltX >= 310) && (ltX < 370)) {setLayoutX(320); col = 5;}
                        else if ((ltX >= 370) && (ltX < 430)) {setLayoutX(380); col = 6;}
                        else if ((ltX >= 430) && (ltX < 490)) {setLayoutX(440); col = 7;}
                        else if ((ltX >= 490) && (ltX < 550)) {setLayoutX(500); col = 8;}
                        else if ((ltX >= 550) && (ltX < 610)) {setLayoutX(560); col = 9;}
                        else {
                            if(on_board[number] == 1){
                                PuzzlePieces p = new PuzzlePieces(d, c, row, col);
                                GameBoard.removePiece(p, occupationArray);
                            }
                            on_board[number] = 0;
                        }

                        if ((ltY >= 0) && (ltY < 40)) {setLayoutY(60); row = 0;}
                        else if ((ltY >= 40) && (ltY < 100)) {setLayoutY(120); row = 1;}
                        else if ((ltY >= 100) && (ltY < 160)) {setLayoutY(180); row = 2;}
                        else if ((ltY >= 160) && (ltY < 220)) {setLayoutY(240); row = 3;}
                        else if ((ltY >= 220) && (ltY < 280)) {setLayoutY(300); row = 4;}
                        else {
                            if(on_board[number] == 1){
                                PuzzlePieces p = new PuzzlePieces(d, c, row, col);
                                GameBoard.removePiece(p, occupationArray);
                            }
                            on_board[number] = 0;
                        }
                    }
                    else {
                        biasX = mouseX2 - getLayoutX() + 15;
                        biasY = mouseY2 - getLayoutY() + 15;
                        ltX = mouseX2 - biasX;
                        ltY = mouseY2 - biasY;
                        if((ltX >= -40)&&(ltX <= 560)&&(ltY >= 20)&&(ltY <= 320)) on_board[number] = 1;
                        if ((ltX >= -40) && (ltX < 20)) {setLayoutX(-10); col = 0;}
                        else if ((ltX >= 20) && (ltX < 80)) {setLayoutX(50); col = 1;}
                        else if ((ltX >= 80) && (ltX < 140)) {setLayoutX(110); col = 2;}
                        else if ((ltX >= 140) && (ltX < 200)) {setLayoutX(170); col = 3;}
                        else if ((ltX >= 200) && (ltX < 260)) {setLayoutX(230); col = 4;}
                        else if ((ltX >= 260) && (ltX < 320)) {setLayoutX(290); col = 5;}
                        else if ((ltX >= 320) && (ltX < 380)) {setLayoutX(350); col = 6;}
                        else if ((ltX >= 380) && (ltX < 440)) {setLayoutX(410); col = 7;}
                        else if ((ltX >= 440) && (ltX < 500)) {setLayoutX(470); col = 8;}
                        else if ((ltX >= 500) && (ltX < 560)) {setLayoutX(530); col = 9;}
                        else {
                            if(on_board[number] == 1){
                                PuzzlePieces p = new PuzzlePieces(d, c, row, col);
                                GameBoard.removePiece(p, occupationArray);
                            }
                            on_board[number] = 0;
                        }

                        if ((ltY >= 20) && (ltY < 80)) {setLayoutY(90); row = 0;}
                        else if ((ltY >= 80) && (ltY < 140)) {setLayoutY(150); row = 1;}
                        else if ((ltY >= 140) && (ltY < 200)) {setLayoutY(210); row = 2;}
                        else if ((ltY >= 200) && (ltY < 260)) {setLayoutY(270); row = 3;}
                        else if ((ltY >= 260) && (ltY < 320)) {setLayoutY(330); row = 4;}
                        else {
                            if(on_board[number] == 1){
                                PuzzlePieces p = new PuzzlePieces(d, c, row, col);
                                GameBoard.removePiece(p, occupationArray);
                            }
                            on_board[number] = 0;
                        }
                    }
                }
                if(on_board[number] == 1) {
                    PuzzlePieces p = new PuzzlePieces(d, c, row, col);
                    if(!GameBoard.canBePut(p, occupationArray)) on_board[number] = 0;
                }
                if(on_board[number] == 0) {
                    setLayoutX(150);
                    setLayoutY(450);
                }
            }
        }
        Button[] piece = new Button[10];
        for(int i = 0; i< 10; i++) {
            piece[i] = new Button(color[i]);
            piece[i].setLayoutX(50 + 80 * i);
            piece[i].setLayoutY(630);
            root.getChildren().add(piece[i]);
        }
        Button rotate = new Button("Rotate");
        rotate.setLayoutX(600);
        rotate.setLayoutY(450);
        root.getChildren().add(rotate);
        Button flip = new Button("Flip");
        flip.setLayoutX(600);
        flip.setLayoutY(520);
        root.getChildren().add(flip);
        DraggablePiece[] pieceView = new DraggablePiece[10];
        for(int i = 0; i < 10; i++) {
            pieceView[i] = new DraggablePiece(i);
        }
        ImageView[] pview = new ImageView[10];
        Image origin = new Image(path1[current[0]]);//the screen shows the first puzzle piece at the beginning
        pieceView[current[0]].setImage(origin);
        pieceView[current[0]].setFitWidth(235);
        pieceView[current[0]].setFitHeight(110);
        pieceView[current[0]].setLayoutX(150);
        pieceView[current[0]].setLayoutY(450);
        root.getChildren().add(pieceView[current[0]]);
        for(int i = 0; i < 10; i++) {
            int num = i;    //number of the button
            piece[i].setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    current[0] = num;   //change current piece
                    for(int i = 0; i < 10; i++) {
                        if((on_board[i] == 0)&&(i != current[0])) root.getChildren().remove(pieceView[i]);
                    }
                    //the flip state will be saved
                    //when coming back from another piece, the previous state won't change
                    if(current_f[num] == 1) {
                        Image image = new Image(path1[num]);
                        pieceView[current[0]].setImage(image);
                    }
                    else {
                        Image image = new Image(path2[num]);
                        pieceView[current[0]].setImage(image);
                    }
                    //different piece will be showed in different scale
                    if((num >= 1)&(num <= 4)) {
                        pieceView[current[0]].setFitWidth(175);
                        pieceView[current[0]].setFitHeight(110);
                    }
                    else {
                        pieceView[current[0]].setFitWidth(235);
                        pieceView[current[0]].setFitHeight(110);
                    }
                    pieceView[current[0]].setRotate(current_r[num] * 90);
                    if(on_board[num] == 0) {
                        pieceView[current[0]].setLayoutX(150);
                        pieceView[current[0]].setLayoutY(450);
                        root.getChildren().add(pieceView[current[0]]);
                    }
                }
            });
        }
        flip.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                int num = current[0];//find the piece showing on screen now
                if(on_board[num] == 0) { //when this piece is on the board, this button will be useless
                    root.getChildren().remove(pieceView[current[0]]);
                    //flip the puzzle piece by using picture from another path
                    //change flip state
                    if (current_f[num] == 1) {
                        Image image = new Image(path2[num]);
                        current_f[num] = 2;
                        pieceView[current[0]].setImage(image);
                    } else {
                        Image image = new Image(path1[num]);
                        current_f[num] = 1;
                        pieceView[current[0]].setImage(image);
                    }
                    if ((num >= 1) & (num <= 4)) {
                        pieceView[current[0]].setFitWidth(175);
                        pieceView[current[0]].setFitHeight(110);
                    } else {
                        pieceView[current[0]].setFitWidth(235);
                        pieceView[current[0]].setFitHeight(110);
                    }
                    pieceView[current[0]].setLayoutX(150);
                    pieceView[current[0]].setLayoutY(450);
                    root.getChildren().add(pieceView[current[0]]);
                }
            }
        });
        rotate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                int num = current[0];//find the piece showing on screen now
                if(on_board[num] == 0) { //when this piece is on the board, this button will be useless
                    root.getChildren().remove(pieceView[current[0]]);
                    //change rotate state
                    if (current_r[num] < 3) {
                        current_r[num] += 1;
                        pieceView[current[0]].setRotate(current_r[num] * 90);
                    } else {
                        current_r[num] = 0;
                        pieceView[current[0]].setRotate(0);
                    }
                    pieceView[current[0]].setLayoutX(150);
                    pieceView[current[0]].setLayoutY(450);
                    root.getChildren().add(pieceView[current[0]]);
                }
            }
        });
        TextField ChallengeTextField = new TextField();
        ChallengeTextField.setPrefWidth(30);
        ChallengeTextField.setPromptText("0 ~ 4");
        ChallengeTextField.setPrefColumnCount(1);

        Label ChallengeTextLabel = new Label("Difficulty Level:");
        Button ChallengeButton = new Button("Challenge");

        ChallengeButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {

                makeChallenge(Games.newGames(Integer.parseInt(ChallengeTextField.getText())).objective);

                //System.out.println(Arrays.deepToString(occupationArray));
                ChallengeTextField.clear();
            }
            public void makeChallenge(String placement) {
                challengeString = placement; //store the placement string for Task 10
                int num;//the number of piece
                String f;//flip state
                Color c;
                Direction d;
                int col, row;
                //reset all previous pieces
                for(int i = 0; i < 10; i++) {

                    root.getChildren().remove(pieceView[i]);
                    root.getChildren().remove(pview[i]);
                    on_board[i] = 0;
                    for(int j = 0; j < 5; j++) {
                        occupationArray[i][j] = 0;
                    }
                }
                for(int i = 0; i < placement.length(); i += 4) {
                    if(placement.charAt(i) == 'b') {c = Color.blue; num = 0;}
                    else if(placement.charAt(i) == 'B') {c = Color.BLUE; num = 0;}
                    else if(placement.charAt(i) == 'g') {c = Color.green; num = 1;}
                    else if(placement.charAt(i) == 'G') {c = Color.GREEN; num = 1;}
                    else if(placement.charAt(i) == 'i') {c = Color.indigo; num = 2;}
                    else if(placement.charAt(i) == 'I') {c = Color.INDIGO; num = 2;}
                    else if(placement.charAt(i) == 'l') {c = Color.limegreen; num = 3;}
                    else if(placement.charAt(i) == 'L') {c = Color.LIMEGREEN; num = 3;}
                    else if(placement.charAt(i) == 'n') {c = Color.navyblue; num = 4;}
                    else if(placement.charAt(i) == 'N') {c = Color.NAVYBLUE; num = 4;}
                    else if(placement.charAt(i) == 'o') {c = Color.orange; num = 5;}
                    else if(placement.charAt(i) == 'O') {c = Color.ORANGE; num = 5;}
                    else if(placement.charAt(i) == 'p') {c = Color.pink; num = 6;}
                    else if(placement.charAt(i) == 'P') {c = Color.PINK; num = 6;}
                    else if(placement.charAt(i) == 'r') {c = Color.red; num = 7;}
                    else if(placement.charAt(i) == 'R') {c = Color.RED; num = 7;}
                    else if(placement.charAt(i) == 's') {c = Color.skyblue; num = 8;}
                    else if(placement.charAt(i) == 'S') {c = Color.SKYBLUE; num = 8;}
                    else if(placement.charAt(i) == 'y') {c = Color.yellow; num = 9;}
                    else {c = Color.YELLOW; num = 9;}
                    if(placement.charAt(i + 3) == 'N') d = Direction.NORTH;
                    else if(placement.charAt(i + 3) == 'E') d = Direction.EAST;
                    else if(placement.charAt(i + 3) == 'S') d = Direction.SOUTH;
                    else d = Direction.WEST;
                    col = placement.charAt(i + 1) - '0';

                    row = placement.charAt(i + 2) - '0';
                    PuzzlePieces p = new PuzzlePieces(d, c, row, col);
                    GameBoard.canBePut(p, occupationArray);
                    on_board[num] = 1;
                    int biasx = 0;
                    int biasy = 0;
                    char t = placement.charAt(i);
                    char o = placement.charAt(i + 3);
                    if((t == 'b')||(t == 'B')||(t == 'o')||(t == 'O')||(t == 'p')||
                            (t == 'P')||(t == 'r')||(t == 'R')||(t == 's')||(t == 'S')||(t == 'y')||(t == 'Y')) {
                        if(Character.isLowerCase(t)) f = "1";
                        else f = "2";
                        Image puzzle = new Image("file:src/comp1110/ass2/gui/assets/" + String.valueOf(t) + f + ".png");
                        pview[num] = new ImageView();
                        pview[num].setImage(puzzle);
                        pview[num].setFitWidth(235);
                        pview[num].setFitHeight(110);
                        if((o == 'E')|(o == 'W')) {
                            if(o == 'E')  pview[num].setRotate(90);
                            else pview[num].setRotate(270);
                            biasx = -60;
                            biasy = 60;
                        }
                        if(o == 'S') pview[num].setRotate(180);
                        pview[num].setLayoutX(50 + 60 * col + biasx);
                        pview[num].setLayoutY(30 + 60 * row + biasy);
                        root.getChildren().add(pview[num]);
                    }
                    else {
                        if(Character.isLowerCase(t)) f = "1";
                        else f = "2";
                        Image puzzle = new Image("file:src/comp1110/ass2/gui/assets/" + String.valueOf(t) + f + ".png");
                        pview[num] = new ImageView();
                        pview[num].setImage(puzzle);
                        pview[num].setFitWidth(175);
                        pview[num].setFitHeight(110);
                        if((o == 'E')|(o == 'W')) {
                            if(o == 'E')  pview[num].setRotate(90);
                            else pview[num].setRotate(270);
                            biasx = -30;
                            biasy = 30;
                        }
                        if(o == 'S') pview[num].setRotate(180);
                        pview[num].setLayoutX(50 + 60*col + biasx);
                        pview[num].setLayoutY(30 + 60*row + biasy);
                        root.getChildren().add(pview[num]);
                    }
                }
            }
        });
        VBox vb = new VBox();
        vb.getChildren().addAll(ChallengeTextLabel, ChallengeTextField, ChallengeButton);
        vb.setSpacing(10);
        vb.setLayoutX(690);
        vb.setLayoutY(500);
        root.getChildren().add(vb);

        Button Clear = new Button("Clear");
        Clear.setLayoutX(690);
        Clear.setLayoutY(595);
        root.getChildren().add(Clear);
        Clear.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                for(int i = 0; i < 10; i++) {
                    root.getChildren().remove(pview[i]);
                    root.getChildren().remove(pieceView[i]);
                    on_board[i] = 0;
                    for(int j = 0; j < 5; j++) {
                        occupationArray[i][j] = 0;
                    }
                }
            }
        });
    }

    // FIXME Task 11: Generate interesting challenges (each challenge may have just one solution)

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Fit Game");
        Scene scene = new Scene(root, BOARD_WIDTH, BOARD_HEIGHT);
        basic();
        setHints(scene);
        backgroundMusic();
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // FIXME Task 8: Implement challenges (you may use assets provided for you in comp1110.ass2.gui.assets)

    public void setHints(Scene scene){ // FIXME Task 10: Implement hints (should become visible when the user presses '/' -- see gitlab issue for details)
       scene.setOnKeyPressed(e -> {
           if(e.getCode() == KeyCode.SLASH){
               ArrayList l1 = new ArrayList();
               for(int i =0;i<on_board.length;i++){
                   l1.add(on_board[i]);
               }
               if(!l1.contains(1)){
                   Text t = new Text();
                   t.setText("Please Select A Challenge First");
                   t.setFont(Font.font ("Verdana", 12));
                   t.setFill(javafx.scene.paint.Color.RED);
                   t.setX(690);
                   t.setY(430);
                   Text t1 = new Text();
                   t1.setText("To Enable Hint Function");
                   t1.setFont(Font.font ("Verdana", 12));
                   t1.setFill(javafx.scene.paint.Color.RED);
                   t1.setX(690);
                   t1.setY(450);
                   hints.getChildren().add(t);
                   hints.getChildren().add(t1);
                   root.getChildren().add(hints);
               } else if(!l1.contains(0)){
                   Text t = new Text();
                   t.setText("CONGRATULATION !");
                   t.setFont(Font.font ("Verdana", 12));
                   t.setFill(javafx.scene.paint.Color.RED);
                   t.setX(690);
                   t.setY(430);
                   Text t1 = new Text();
                   t1.setText("You have completed the game !");
                   t1.setFont(Font.font ("Verdana", 12));
                   t1.setFill(javafx.scene.paint.Color.RED);
                   t1.setX(690);
                   t1.setY(450);
                   hints.getChildren().add(t);
                   hints.getChildren().add(t1);
                   root.getChildren().add(hints);
               } else {
                   ArrayList l2 = new ArrayList();
                   for(int i = 0; i<on_board.length;i++){
                       if(on_board[i]==0){
                          if(i==0){l2.add('b');}
                           if(i==1){l2.add('g');}
                           if(i==2){l2.add('i');}
                           if(i==3){l2.add('l');}
                           if(i==4){l2.add('n');}
                           if(i==5){l2.add('o');}
                           if(i==6){l2.add('p');}
                           if(i==7){l2.add('r');}
                           if(i==8){l2.add('s');}
                           if(i==9){l2.add('y');}
                       }
                   }
                   char cr = (char)l2.get(0);
                   ArrayList l3 = makeHints(challengeString);
                   for(int i = l3.size()-1; i >=0;i--){
                       char tmp = (char) Character.toLowerCase(l3.get(i).toString().charAt(0));
                       if (tmp!=cr){
                           l3.remove(i);
                       }
                   }
                   hintImage((String)l3.get(0));
                   boardCoordinate();
               }
               l1.clear();
               e.consume();
           }
       });
        scene.setOnKeyReleased(e -> {
            if(e.getCode() == KeyCode.SLASH){
                hints.getChildren().clear();
                root.getChildren().remove(hints);
            }
        });
    }

    public ArrayList<String> makeHints(String placement) {
        ArrayList<String> solutionArray = Games.getSolutionArray(placement);
        ArrayList<String> challengeArray = new ArrayList<String>();
        Color color;
        Direction direction;
        int column;
        int row;

        for(int i = 0; i < placement.length(); i += 4) {
            switch (placement.charAt(i)){
                case 'b': color = Color.blue; break;
                case 'B': color = Color.BLUE;break;
                case 'g': color = Color.green;break;
                case 'G': color = Color.GREEN;break;
                case 'i': color = Color.indigo;break;
                case 'I': color = Color.INDIGO;break;
                case 'l': color = Color.limegreen;break;
                case 'L': color = Color.LIMEGREEN;break;
                case 'n': color = Color.navyblue;break;
                case 'N': color = Color.NAVYBLUE;break;
                case 'o': color = Color.orange;break;
                case 'O': color = Color.ORANGE;break;
                case 'p': color = Color.pink;break;
                case 'P': color = Color.PINK;break;
                case 'r': color = Color.red;break;
                case 'R': color = Color.RED;break;
                case 's': color = Color.skyblue;break;
                case 'S': color = Color.SKYBLUE;break;
                case 'y': color = Color.yellow;break;
                default: color = Color.YELLOW;break;
            }
            switch (placement.charAt(i+3)){
                case 'N': direction = Direction.NORTH; break;
                case 'E': direction = Direction.EAST; break;
                case 'S': direction = Direction.SOUTH; break;
                default: direction = Direction.WEST; break;
            }
            column = placement.charAt(i + 1) - '0';
            row = placement.charAt(i + 2) - '0';
            challengeArray.add(new PuzzlePieces(direction, color, row, column).toString());
        }
        solutionArray.removeAll(challengeArray);
        return solutionArray;
    }

    public void hintImage(String imageString){
        String state;
        Character color = imageString.charAt(0);
        Character direction = imageString.charAt(3);

        if(Character.isLowerCase(color)){
            state = "1";
        } else {
            state = "2";
        }
        ImageView hintImageView = new ImageView();
        Image hintImage = new Image("file:src/comp1110/ass2/gui/assets/" + String.valueOf(color) + state + ".png");
        hintImageView.setImage(hintImage);
        if((color == 'b')||(color == 'B')||(color == 'o')||(color == 'O')||(color == 'p')||(color == 'P')||(color == 'r')||(color == 'R')||(color == 's')||(color == 'S')||(color == 'y')||(color == 'Y')) {
            hintImageView.setFitWidth(235);
            hintImageView.setFitHeight(110);

            if((direction == 'E')|(direction == 'W')) {
                if(direction == 'E')  {
                    hintImageView.setRotate(90);
                } else hintImageView.setRotate(270);
            }
            if(direction == 'S') {
                hintImageView.setRotate(180);
            }
        } else {
            hintImageView.setFitWidth(175);
            hintImageView.setFitHeight(110);

            if((direction == 'E')|(direction == 'W')) {
                if(direction == 'E')  hintImageView.setRotate(90);
                else hintImageView.setRotate(270);
            }
            if(direction == 'S') {
                hintImageView.setRotate(180);
            }
        }

        hintImageView.setLayoutX(700);
        hintImageView.setLayoutY(70);

        Image arrowImage = new Image("file:src/comp1110/ass2/gui/assets/Arrow.png");
        ImageView arrowImageView = new ImageView(arrowImage);
        arrowImageView.setFitHeight(32);
        arrowImageView.setPreserveRatio(true);
        int arrowPosition;
        switch (imageString.charAt(0)){
            case 'b': arrowPosition=0;break;
            case 'B': arrowPosition=0;break;
            case 'g': arrowPosition=1;break;
            case 'G': arrowPosition=1;break;
            case 'i': arrowPosition=2;break;
            case 'I': arrowPosition=2;break;
            case 'l': arrowPosition=3;break;
            case 'L': arrowPosition=3;break;
            case 'n': arrowPosition=4;break;
            case 'N': arrowPosition=4;break;
            case 'o':arrowPosition=5;break;
            case 'O': arrowPosition=5;break;
            case 'p': arrowPosition=6;break;
            case 'P': arrowPosition=6;break;
            case 'r': arrowPosition=7;break;
            case 'R': arrowPosition=7;break;
            case 's': arrowPosition=8;break;
            case 'S': arrowPosition=8;break;
            default: arrowPosition=9;break;
        }
        arrowImageView.setX(60 + 80 * arrowPosition);
        arrowImageView.setY(660);

        if(Character.isUpperCase(imageString.charAt(0))){
            ImageView arrowImageView2 = new ImageView(arrowImage);
            arrowImageView2.setFitHeight(30);
            arrowImageView2.setPreserveRatio(true);
            arrowImageView2.setX(608);
            arrowImageView2.setY(547);
            hints.getChildren().add(arrowImageView2);
        }

        Text t1 = new Text();
        t1.setText("Column - " + imageString.charAt(1) + ", Row - "+ imageString.charAt(2));
        t1.setFont(Font.font ("Verdana", 12));
        t1.setFill(javafx.scene.paint.Color.BLACK);
        t1.setX(750);
        t1.setY(260);
        hints.getChildren().add(arrowImageView);
        hints.getChildren().add(t1);
        hints.getChildren().add(hintImageView);
        root.getChildren().add(hints);
    }

    public void backgroundMusic(){
        MediaPlayer musicPlayer;
        String musicAddress = "src/comp1110/ass2/gui/assets/BGM.mp3";
        Media musicMedia = new Media(Paths.get(musicAddress).toUri().toString());
        musicPlayer = new MediaPlayer(musicMedia);
        musicPlayer.getOnRepeat();
        musicPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                musicPlayer.seek(Duration.ZERO);
                musicPlayer.play();
            }
        });
        musicPlayer.play();

        Image playButtonImage = new Image("file:src/comp1110/ass2/gui/assets/playButton.png");
        ImageView playButtonImageView = new ImageView(playButtonImage);
        playButtonImageView.setFitWidth(10);
        playButtonImageView.setPreserveRatio(true);

        Image pauseButtonImage = new Image("file:src/comp1110/ass2/gui/assets/pauseButton.png");
        ImageView pauseButtonImageView = new ImageView(pauseButtonImage);
        pauseButtonImageView.setFitWidth(10);
        pauseButtonImageView.setPreserveRatio(true);

        Button musicControlButton = new Button();
        musicControlButton.setGraphic(pauseButtonImageView);
        musicControlButton.setLayoutX(900);
        musicControlButton.setLayoutY(5);
        musicControlButton.setPrefSize(13,13);
        root.getChildren().add(musicControlButton);

        musicControlButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

                if(musicPlayer.getStatus().equals(MediaPlayer.Status.PLAYING)){
                    musicPlayer.pause();
                    musicControlButton.setGraphic(playButtonImageView);
                } else {
                    musicPlayer.play();
                    musicControlButton.setGraphic(pauseButtonImageView);
                }
            }
        });

        Text t1 = new Text();
        t1.setText("Hint(Press / while CAPS lock ON )");
        t1.setFont(Font.font ("Verdana", 12));
        t1.setFill(javafx.scene.paint.Color.BLACK);
        t1.setX(690);
        t1.setY(487);
        root.getChildren().add(t1);
    }

    public void boardCoordinate(){
        Text column = new Text("Column");
        column.setFill(javafx.scene.paint.Color.WHITE);
        /*column.setFont(Font.font("Verdana", FontWeight.BOLD, 11));*/
        column.setX(8);
        column.setY(340);

        Text row = new Text("Row");
        row.setFill(javafx.scene.paint.Color.WHITE);
        row.setX(648);
        row.setY(25);
        hints.getChildren().add(column);
        hints.getChildren().add(row);

        for(int i =0;i<10;i++){
            Text columnCoordinate = new Text(i + "");
            columnCoordinate.setFill(javafx.scene.paint.Color.WHITE);
            columnCoordinate.setX(75+i*60);
            columnCoordinate.setY(340);
            hints.getChildren().add(columnCoordinate);
        }

        for(int i =0;i<5;i++){
            Text rowCoordinate = new Text(i + "");
            rowCoordinate.setFill(javafx.scene.paint.Color.WHITE);
            rowCoordinate.setX(655);
            rowCoordinate.setY(60 + i*60);
            hints.getChildren().add(rowCoordinate);
        }
    }

}
