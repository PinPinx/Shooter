import javafx.animation.KeyFrame;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Separate the game code from some of the boilerplate code.
 *  
 * @author Robert C. Duvall
 */
class GameLoop {
	private Circle myBall;
	private ImageView imageView;

	/**
	 *  Function to do each game frame
	 */
    private EventHandler<ActionEvent> oneFrame = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent evt) {
			updateSprites();
		}
	};

	
	/**
	 * Create the game's scene
	 */
	public Scene init (Stage s, int width, int height) {
		// Create a scene graph to organize the scene
		Group root = new Group();
		// Create a place to see the shapes
		Scene scene = new Scene(root, width, height, Color.WHITE);
		// Make some shapes and set their properties
        Image image = new Image(getClass().getResourceAsStream("images/duke.gif"));
        imageView = new ImageView();
        imageView.setImage(image);
		myBall = new Circle(scene.getWidth() / 2, scene.getHeight() / 2, 60);
		myBall.setFill(Color.RED);
		// remember shapes for viewing later
		root.getChildren().add(myBall);
		root.getChildren().add(imageView);
		return scene;
	}

	/**
	 * Create the game's frame
	 */
	public KeyFrame start () {
		return new KeyFrame(Duration.millis(1000/60), oneFrame);
	}

	/**
	 * Change the sprites properties to animate them
	 * 
	 * Note, there are more sophisticated ways to animate shapes, 
	 * but these simple ways work too.
	 */
	private void updateSprites () {
		myBall.setCenterX(myBall.getCenterX() + 1);
		imageView.setRotate(imageView.getRotate() + 1);
	}
}