import javafx.application.Application;
import javafx.stage.Stage;


/**
 * This is the main program, it is basically boilerplate to create an animated scene.
 *
 * @author Robert C. Duvall
 */
public class Main extends Application {
    private Menu myMenu;

    /**
     * Set menu up
     */
    @Override
    public void start (Stage s) {
        s.setTitle("Game Menu");
        // create your own game here
        myMenu = new Menu();
        // attach game to the stage and display it
        myMenu.showScreen(s, 600, 600);
    }

    /**
     * Start the program.
     */
    public static void main (String[] args) {
        launch(args);
    }
}
