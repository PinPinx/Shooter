import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * This is the main program, it is basically boilerplate to create an animated scene.
 *
 * @author Robert C. Duvall
 */
public class Main extends Application {
    private static final int NUM_FRAMES_PER_SECOND = 60;
    private Menu myMenu;

    /**
     * Set things up at the beginning.
     */
    @Override
    public void start (Stage s) {
        s.setTitle("Game Menu");
        // create your own game here
        myMenu = new Menu();
        // attach game to the stage and display it
        myMenu.showScreen(s, 600, 600);

        // setup the game's loop
       /* KeyFrame frame = myMenu.start(NUM_FRAMES_PER_SECOND);
        Timeline animation = new Timeline();
        animation.setCycleCount(Animation.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.playFromStart();*/
        
    }

    /**
     * Start the program.
     */
    public static void main (String[] args) {
        launch(args);
    }
}
