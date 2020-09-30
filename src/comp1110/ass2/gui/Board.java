package comp1110.ass2.gui;

import comp1110.ass2.Games;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Board extends Application {

    private static final int BOARD_WIDTH = 933;
    private static final int BOARD_HEIGHT = 700;
    private final Group root = new Group();
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
        String[] color = {"blue", "green", "indigo", "limegreen", "navyblue", "orange",
                          "pink", "red", "skyblue", "yellow"};
        Image image = new Image("file:src/comp1110/ass2/gui/assets/board.png");
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitWidth(700);
        imageView.setFitHeight(370);
        root.getChildren().add(imageView);
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
        //number of piece showing on screen, show the first puzzle piece in beginning
        final int[] current = {0};
        /*the flip state of each piece
        use picture in path1 when equals to 1,use piece in path2 when equals to 2*/
        final int[] current_f = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        final int[] current_r = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        Image origion = new Image(path1[current[0]]);//the screen shows the first puzzle piece at the beginning
        ImageView pieceView = new ImageView();
        pieceView.setImage(origion);
        pieceView.setFitWidth(235);
        pieceView.setFitHeight(110);
        pieceView.setLayoutX(150);
        pieceView.setLayoutY(450);
        root.getChildren().add(pieceView);
        for(int i = 0; i < 10; i++) {
            int num = i;    //number of the button
            piece[i].setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    current[0] = num;   //change current piece
                    root.getChildren().remove(pieceView);
                    //the flip state will be saved
                    //when coming back from another piece, the previous state won't change
                    if(current_f[num] == 1) {
                        Image image = new Image(path1[num]);
                        pieceView.setImage(image);
                    }
                    else {
                        Image image = new Image(path2[num]);
                        pieceView.setImage(image);
                    }
                    //different piece will be showed in different scale
                    if((num >= 1)&(num <= 4)) {
                        pieceView.setFitWidth(175);
                        pieceView.setFitHeight(110);
                    }
                    else {
                        pieceView.setFitWidth(235);
                        pieceView.setFitHeight(110);
                    }
                    pieceView.setRotate(current_r[num] * 90);
                    pieceView.setLayoutX(150);
                    pieceView.setLayoutY(450);
                    root.getChildren().add(pieceView);
                }
            });
        }
        flip.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                int num = current[0];//find the piece showing on screen now
                root.getChildren().remove(pieceView);
                //flip the puzzle piece by using picture from another path
                //change flip state
                if(current_f[num] == 1) {
                    Image image = new Image(path2[num]);
                    current_f[num] = 2;
                    pieceView.setImage(image);
                }
                else {
                    Image image = new Image(path1[num]);
                    current_f[num] = 1;
                    pieceView.setImage(image);
                }
                if((num >= 1)&(num <= 4)) {
                    pieceView.setFitWidth(175);
                    pieceView.setFitHeight(110);
                }
                else {
                    pieceView.setFitWidth(235);
                    pieceView.setFitHeight(110);
                }
                pieceView.setLayoutX(150);
                pieceView.setLayoutY(450);
                root.getChildren().add(pieceView);
            }
        });
        rotate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                int num = current[0];//find the piece showing on screen now
                root.getChildren().remove(pieceView);
                //change rotate state
                if(current_r[num] < 3) {
                    current_r[num] += 1;
                    pieceView.setRotate(current_r[num] * 90);
                }
                else {
                    current_r[num] = 0;
                    pieceView.setRotate(0);
                }
                pieceView.setLayoutX(150);
                pieceView.setLayoutY(450);
                root.getChildren().add(pieceView);
            }
        });
    }





    // FIXME Task 10: Implement hints (should become visible when the user presses '/' -- see gitlab issue for details)

    // FIXME Task 11: Generate interesting challenges (each challenge may have just one solution)

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Fit Game");
        Scene scene = new Scene(root, BOARD_WIDTH, BOARD_HEIGHT);
        basic();
        implementChallenge();
        primaryStage.setScene(scene);
        primaryStage.show();
    }





    /*----------------------------------------------------------------------------------------------------------------*/

    // FIXME Task 8: Implement challenges (you may use assets provided for you in comp1110.ass2.gui.assets)

    private void implementChallenge() {

        TextField challengeTextField = new TextField();
        challengeTextField.setPrefWidth(30);
        challengeTextField.setPromptText("0 ~ 4");

        Label labal = new Label("Difficulty Level:");
        Button ChallengeButton = new Button("Challenge");


        ChallengeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                makePlacement(Games.newGames(Integer.parseInt(challengeTextField.getText())).objective);
                challengeTextField.clear();
            }
        });
        VBox vb = new VBox();
        vb.getChildren().addAll(labal, challengeTextField, ChallengeButton);
        vb.setSpacing(10);
        vb.setLayoutX(690);
        vb.setLayoutY(500);
        root.getChildren().add(vb);
    }

    private void makePlacement(String placement) {

        Image image = new Image("file:src/comp1110/ass2/gui/assets/board.png");
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitWidth(700);
        imageView.setFitHeight(370);
        root.getChildren().add(imageView);
        Image[] puzzle = new Image[placement.length() / 4];
        ImageView[] pview = new ImageView[placement.length() / 4];
        int n = 0;
        String num;
        for(int i = 0; i < placement.length(); i += 4) {
            int biasx = 0;
            int biasy = 0;
            char t = placement.charAt(i);
            int c = placement.charAt(i + 1) - '0';
            int r = placement.charAt(i + 2) - '0';
            char o = placement.charAt(i + 3);
            if((t == 'b')||(t == 'B')||(t == 'o')||(t == 'O')||(t == 'p')||
                    (t == 'P')||(t == 'r')||(t == 'R')||(t == 's')||(t == 'S')||(t == 'y')||(t == 'Y')) {
                if(Character.isLowerCase(t)) num = "1";
                else num = "2";
                puzzle[n] = new Image("file:src/comp1110/ass2/gui/assets/" + String.valueOf(t) + num + ".png");
                pview[n] = new ImageView();
                pview[n].setImage(puzzle[n]);
                pview[n].setFitWidth(235);
                pview[n].setFitHeight(110);
                if((o == 'E')|(o == 'W')) {
                    if(o == 'E')  pview[n].setRotate(90);
                    else pview[n].setRotate(270);
                    biasx = -60;
                    biasy = 60;
                }
                if(o == 'S') pview[n].setRotate(180);
                pview[n].setLayoutX(50 + 60*c + biasx);
                pview[n].setLayoutY(30 + 60*r + biasy);
                root.getChildren().add(pview[n]);
            }
            else {
                if(Character.isLowerCase(t)) num = "1";
                else num = "2";
                puzzle[n] = new Image("file:src/comp1110/ass2/gui/assets/" + String.valueOf(t) + num + ".png");
                pview[n] = new ImageView();
                pview[n].setImage(puzzle[n]);
                pview[n].setFitWidth(175);
                pview[n].setFitHeight(110);
                if((o == 'E')|(o == 'W')) {
                    if(o == 'E')  pview[n].setRotate(90);
                    else pview[n].setRotate(270);
                    biasx = -30;
                    biasy = 30;
                }
                if(o == 'S') pview[n].setRotate(180);
                pview[n].setLayoutX(50 + 60*c + biasx);
                pview[n].setLayoutY(30 + 60*r + biasy);
                root.getChildren().add(pview[n]);
            }
        }

    }

    /*----------------------------------------------------------------------------------------------------------------*/

}
