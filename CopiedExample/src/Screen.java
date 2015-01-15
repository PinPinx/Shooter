import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;


 public class Screen {
	protected Group myRoot;
	public void showScreen(Stage s, int width, int height){
		Scene scene = this.init(s, width, height);
        s.setScene(scene);
        s.show();
	}
	
	public Scene init(Stage s, int width, int height){
		return null;
	}
	
	public void startAnim(KeyFrame frame){
		Timeline animation = new Timeline();
		animation.setCycleCount(Animation.INDEFINITE);
		animation.getKeyFrames().add(frame);
		animation.play();
	}
	
	public void showAndAnim(Stage s, int width, int height, KeyFrame frame){
		showScreen(s, width, height);
		startAnim(frame);
	}
	
}
