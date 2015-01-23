// This entire file is part of my masterpiece.
// Danny Oh

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.stage.Stage;

//Superclass for anything that represents a new "screen"

 public class Screen {	
	//Shows the screen on stage
	public void showScreen(Stage s, int width, int height){
		Scene scene = this.init(s, width, height);
        s.setScene(scene);
        s.show();
	}
	//to be coded in subclasses
	public Scene init(Stage s, int width, int height){
		return null;
	}
	//starts the frame updates
	public void startAnim(KeyFrame frame){
		Timeline animation = new Timeline();
		animation.setCycleCount(Animation.INDEFINITE);
		animation.getKeyFrames().add(frame);
		animation.play();
	}
	//Does both showing and animating
	public void showAndAnim(Stage s, int width, int height, KeyFrame frame){
		showScreen(s, width, height);
		startAnim(frame);
	}
	
}
