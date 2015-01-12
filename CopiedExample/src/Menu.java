import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class Menu {

	private Group myRoot;
	private BallWorld myGame;
	public Scene myScene;

	private static final int NUM_FRAMES_PER_SECOND = 60;

	public Scene init (Stage s, int width, int height) {
		myRoot = new Group();

		addIntroText();
		Button play = new Button("Play");
		play.setTranslateX(300); 
		play.setTranslateY(430);

		play.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e){
				myGame = new BallWorld();
				// attach game to the stage and display it
				Scene scene = myGame.init(s, width, height);
				s.setScene(scene);
				s.show();
				// setup the game's loop
				KeyFrame frame = myGame.start(NUM_FRAMES_PER_SECOND);
				Timeline animation = new Timeline();
				animation.setCycleCount(Animation.INDEFINITE);
				animation.getKeyFrames().add(frame);
				animation.play();
			}
		}); 
		myRoot.getChildren().add(play);
		myScene = new Scene(myRoot, width, height, Color.WHITE);
		return myScene;
	}

	public void addIntroText(){
		Text opener = new Text(100,300,"Shut the Butt");
		opener.setFont(new Font(75));
		opener.setFill(Color.BROWN);
		Text instructions = new Text(120,330,"YOU GOTTA SHUT THE BUTT \n"
				+ "SHOOT DA BUTT\n"
				+ "DODGE DA POOP.");
		instructions.setTextAlignment(TextAlignment.CENTER);
		instructions.setWrappingWidth(400);
		instructions.setFill(Color.RED);

		myRoot.getChildren().add(opener);
		myRoot.getChildren().add(instructions);
	}
}
