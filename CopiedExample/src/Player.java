import java.util.ArrayList;

import javafx.scene.Group;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.KeyCode;


public class Player extends Character{
	private Group myRoot;
	
	public Player(double X, double Y, double points, Group Root){
		super(X, Y, points, "images/hand.png", Root);
		setBarLocation(500, 150);
		HPBar.setStyle("-fx-accent: green;");
		myRoot=Root;
	}
	
	public Shot makeShot(){
		return new Shot(getTranslateX(), getTranslateY(), 10, myRoot);
	}
	
	public void moveSprite(double width, ArrayList<KeyCode> keys){
		if (keys.size()>0){
			if (keys.get(0).equals(KeyCode.D) && getTranslateX()<=width-30){
					setVelocity(10,0);
			}
			else if (keys.get(0).equals(KeyCode.A) && getTranslateX()>=-30){
				setVelocity(-10,0);
			}
			else
				setVelocity(0,0);
	
		}
		else
			setVelocity(0,0);
		moveSprite();
}
}
