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

public class Menu extends Screen{

	private Group myRoot;
	//private BallWorld myGame;

	private static final int NUM_FRAMES_PER_SECOND = 60;

	public Scene init (Stage s, int width, int height) {
		myRoot = new Group();

		addIntroText();
		Button play = new Button("Play");
		play.setTranslateX(300); 
		play.setTranslateY(430);

		play.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e){
				BallWorld myGame = new BallWorld();
				// attach game to the stage and display it
				myGame.showScreen(s, 600, 600);
				// setup the game's loop
				KeyFrame frame = myGame.start(NUM_FRAMES_PER_SECOND);
				startAnim(frame);
			}
		}); 
		myRoot.getChildren().add(play);
		return new Scene(myRoot, width, height, Color.WHITE);
	}

	public void addIntroText(){
		Text opener = new Text(35,300,"Pain in the Butt");
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
