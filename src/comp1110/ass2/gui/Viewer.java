package comp1110.ass2.gui;

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
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

/**
 * A very simple viewer for piece placements in the IQ-Fit game.
 * <p>
 * NOTE: This class is separate from your main game class.  This
 * class does not play a game, it just illustrates various piece
 * placements.
 */
public class Viewer extends Application {

    /* board layout */
    private static final int SQUARE_SIZE = 60;
    private static final int VIEWER_WIDTH = 720;
    private static final int VIEWER_HEIGHT = 480;

    private static final String URI_BASE = "assets/";
    private static final String BASEBOARD_URI = Viewer.class.getResource(URI_BASE + "board.png").toString();

    private static final String GREEN = Viewer.class.getResource(URI_BASE + "G1.png").toString();

    private final Group root = new Group();
    private final Group board = new Group();
    private final Group controls = new Group();
    private TextField textField;

    /**
     * Draw a placement in the window, removing any previously drawn one
     *
     * @param placement A valid placement string
     */
    void makePlacement(String placement) {  // FIXME Task 4: implement the simple placement viewer
        //board size: 700*370
        //length of 4 balls: 235
        //length of 3 balls: 175
        //length of 2 balls: 110
        //distance between holes: 60
        //2*3 puzzlepiece rotate 90: x - 30  y + 30
        //2*4 puzzlepiece rotate 90: x - 60  y - 60
        Image image = new Image("file:C:\\Users\\carrot\\IdeaProjects\\comp1110-ass2-tue15g\\src\\comp1110\\ass2\\gui\\assets\\board.png");
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

    /**
     * Create a basic text field for input and a refresh button.
     */
    private void makeControls() {
        Label label1 = new Label("Placement:");
        textField = new TextField();
        textField.setPrefWidth(300);
        Button button = new Button("Refresh");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                makePlacement(textField.getText());
                textField.clear();
            }
        });
        HBox hb = new HBox();
        hb.getChildren().addAll(label1, textField, button);
        hb.setSpacing(10);
        hb.setLayoutX(130);
        hb.setLayoutY(VIEWER_HEIGHT - 50);
        controls.getChildren().add(hb);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("FitGame Viewer");
        Scene scene = new Scene(root, VIEWER_WIDTH, VIEWER_HEIGHT);

        root.getChildren().add(controls);

        makeControls();

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
