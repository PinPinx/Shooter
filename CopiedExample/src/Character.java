import java.util.ArrayList;
import java.util.Random;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.ProgressBar;


public class Character extends Sprite{
	private static final int DAMAGE = 5;
	private double fullHP;
	private double HP;
	protected ProgressBar HPBar;
	public Character(double X, double Y, double points, String fileName, Group myRoot){
		super(X, Y, fileName, myRoot);
		HPBar=new ProgressBar(1);
		fullHP=points;
		HP=points;
		myRoot.getChildren().add(HPBar);
	}
	
	public boolean checkCollide(Sprite projectile){
		return (projectile.getBoundsInParent().intersects(getBoundsInParent()));
			
	}
	
	public void collisionUpdate(Sprite projectile, ArrayList<Sprite> shotsToRemove){
		if(checkCollide(projectile)){
			shotsToRemove.add(projectile);
			HP=HP-DAMAGE;
			HPBar.setProgress(HPBar.getProgress()-(DAMAGE/fullHP));
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
	
	public void setHP(double points){
		HP=points;
	}
	
	public double getHP(){
		return HP;
	}
}
