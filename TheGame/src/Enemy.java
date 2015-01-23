import java.util.Random;

import javafx.scene.Group;


public class Enemy extends Character{
	private Group myRoot;
	private Random myGenerator = new Random();
	private boolean Angry=false;
	public Enemy(double X, double Y, double points, Group Root){
		super(X, Y, points, "images/But.gif", Root);
		setVelocity(6,0);
		setBarLocation(500, 130);
		getHPBar().setStyle("-fx-accent: red;");
		myRoot=Root;
	}
	
	/**
	 * For level 2
	 */
	public void makeAngry(){
		Angry=true;
	}
	
	/**
	 * Makes poop- directs at player if level 2
	 * @param player 
	 * @return the poop
	 */
	public Poop makePoop(Character player){
		Poop poop=new Poop(getTranslateX(), getTranslateY(), 10, myRoot);
		if(Angry){
			poop.setVelocity(.01*(player.getTranslateX()-getTranslateX()), .01*(player.getTranslateY()-getTranslateY()));
		}
		return poop;
	}
	

	public void moveSprite(double width){
		if (getTranslateX() >= width-50 || getTranslateX() <= -50) {
			setVelocity(getVelocity().getX() * -1, getVelocity().getY());
		}
		else if (myGenerator.nextInt(300)<=7)
			setVelocity(getVelocity().getX() * -1, getVelocity().getY());
		if (Angry){
			setVelocity(getVelocity().getX() * 1.0002, getVelocity().getY() * 1.0002);
		}
		moveSprite();
	}
	
}
