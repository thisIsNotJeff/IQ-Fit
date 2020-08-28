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
    void makePlacement(String placement) {
        // FIXME Task 4: implement the simple placement viewer
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
        root.getChildren().add(board);

        makeBord();
        placePieces();
        makeControls();

        primaryStage.setScene(scene);
        primaryStage.show();
    }
    /**
     * Construct a basic board for pieces to be placed.
     */
    public void makeBord() {
        //board.getChildren().clear();
        ImageView baseboard = new ImageView();

        baseboard.setImage(new Image(BASEBOARD_URI));
        baseboard.setFitWidth(550);
        baseboard.setFitHeight(300);
        baseboard.setLayoutX(85);

        board.getChildren().add(baseboard);

        board.toBack();
    }


    public void placePieces() {
        ImageView pieces = new ImageView();

        pieces.setImage(new Image(GREEN));
        pieces.setFitWidth(140);
        pieces.setFitHeight(100);
        pieces.setX(125);
        pieces.setY(21);

        board.getChildren().add(pieces);


    }
}
