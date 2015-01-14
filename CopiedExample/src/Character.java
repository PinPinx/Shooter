import java.util.ArrayList;
import java.util.Random;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.ProgressBar;


public class Character extends Sprite{
	protected ProgressBar HPBar;
	protected double HP;
	protected Random myGenerator = new Random();
	
	public Character(double X, double Y, double points, String fileName, Group myRoot){
		super(X, Y, fileName, myRoot);
		HPBar=new ProgressBar(1);
		HP=points;
		myRoot.getChildren().add(HPBar);
	}
	
	public boolean checkCollide(Sprite projectile){
		return (projectile.getBoundsInParent().intersects(getBoundsInParent()));
			
	}
	
	public void collisionUpdate(Sprite projectile, ArrayList<Sprite> shotsToRemove){
		if(checkCollide(projectile)){
			shotsToRemove.add(projectile);
			HPBar.setProgress(HPBar.getProgress()-(5/HP));
		}
	}
	
	public void updateAll(ArrayList<Sprite> shots, ArrayList<Sprite> shotsToRemove){
		for(Sprite s: shots){
			collisionUpdate(s, shotsToRemove);
		}
	}
	
	public void setBarLocation(double X, double Y){
		HPBar.setTranslateX(X);
		HPBar.setTranslateY(Y);
	}
	
	protected void setHP(double points){
		HP=points;
	}
	
}
