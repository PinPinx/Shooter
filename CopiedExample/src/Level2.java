import javafx.animation.KeyFrame;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Level2 extends Level1{
	
	private Stage stage;
	
	public Scene init (Stage s, int width, int height) {
		Scene scene=super.init(s, width, height);
		stage=s;
		super.getEnemy().makeAngry();
		super.getPlayer().makeHard();
		return scene;
	}
	
	protected void goNextLevel(){
		if (super.getEnemy().getHP()<=0){
			clearElements();
			GameWin youWin=new GameWin();
	        // attach game to the stage and display it
			youWin.showScreen(stage, 600, 600);
		}
	}
}
