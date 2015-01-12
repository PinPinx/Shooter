
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * This is the main program, it is basically boilerplate to create
 * an animated scene.
 * 
 * @author Robert C. Duvall
 */
public class BallWorld extends Application
{
	private GameLoop myGame;

	/**
	 * Set things up at the beginning.
	 */
	@Override
	public void start (Stage s)
	{
		s.setTitle("BallWorld!");
		// create your own game here
		myGame = new GameLoop();
		// attach game to the stage and display it
		Scene scene = myGame.init(s, 400, 400);
		s.setScene(scene);
		s.show();
		
		// sets the game's loop 
		KeyFrame frame = myGame.start();
		Timeline animation = new Timeline();
		animation.setCycleCount(Timeline.INDEFINITE);
		animation.getKeyFrames().add(frame);
		animation.play();
	}

	/**
	 * Start the program.
	 */
	public static void main(String[] args)
	{
		launch(args);
	}
}


