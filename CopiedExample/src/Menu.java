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
	private Stage stage;
	//private BallWorld myGame;

	private static final int NUM_FRAMES_PER_SECOND = 60;

	public Scene init (Stage s, int width, int height) {
		myRoot = new Group();
		stage=s;
		addAllText();
		addAllButtons();
		return new Scene(myRoot, width, height, Color.WHITE);
	}

	public void addButton(double X, double Y, String text){
		Button play = new Button(text);
		play.setTranslateX(X); 
		play.setTranslateY(Y);
		play.setOnAction(e -> handle(e));
		myRoot.getChildren().add(play);
	}
	
	public void addAllButtons(){
		addButton(280,430,"Play");
	}
	
	public void handle(ActionEvent e){
		Level1 game= new Level1();
		createGame(game);
	}
	
	public void createGame(Level1 game){
		KeyFrame frame = game.start(NUM_FRAMES_PER_SECOND);
		game.showAndAnim(stage, 600, 600,frame);
	}
	
	public Text addText(double X, double Y, double size, String text){
		Text toWrite = new Text(X,Y,text);
		toWrite.setFont(new Font(size));
		toWrite.setFill(Color.BROWN);
		toWrite.setTextAlignment(TextAlignment.CENTER);
		myRoot.getChildren().add(toWrite);
		return toWrite;
	}
	
	public void addAllText(){
		addText(35, 300, 75, "Pain in the Butt");
		Text instructions=addText(105, 330, 15, "YOU GOTTA SHUT THE BUTT \n"
				+ "SHOOT DA BUTT\n"
				+ "DODGE DA POOP.");
		instructions.setWrappingWidth(400);
		instructions.setFill(Color.RED);
	}
	
}
