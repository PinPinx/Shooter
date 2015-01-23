import java.util.ArrayList;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;


public class Player extends Character{
	private Group myRoot;
	private int shotCounter;
	private boolean inHard=false;
	
	
	public Player(double X, double Y, double points, Group Root){
		super(X, Y, points, "images/hand.png", Root);
		setBarLocation(500, 150);
		getHPBar().setStyle("-fx-accent: green;");
		myRoot=Root;
	}
	
	/**
	 * Creates the shot (used for when player clicks)
	 * @return the shot
	 */
	public Shot makeShot(){
		shotCounter++;
		Shot shot=new Shot(getTranslateX(), getTranslateY(), 10, myRoot);
		if(inHard){
			shot.setVelocity(.5 * getVelocity().getX(), shot.getVelocity().getY());
		}
		return shot;
	}
	
	/**
	 * 
	 * @return how many shots you have left
	 */
	public int getCounter(){
		return shotCounter;
	}
	
	/**
	 * For level 2
	 */
	public void makeHard(){
		inHard=true;
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
