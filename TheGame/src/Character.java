// This entire file is part of my masterpiece.
// Danny Oh

import java.util.ArrayList;
import javafx.scene.Group;
import javafx.scene.control.ProgressBar;

//Superclass for enemy and player
public class Character extends Sprite {
	private static final int DAMAGE = 5;
	private double fullHP;
	private double HP;
	private ProgressBar HPBar;

	public Character(double X, double Y, double points, String fileName,
			Group myRoot) {
		super(X, Y, fileName, myRoot);
		HPBar = new ProgressBar(1);
		fullHP = points;
		HP = points;
		myRoot.getChildren().add(HPBar);
	}

	/**
	 * Checks collision with
	 * 
	 * @param projectile
	 * @return if collide
	 */
	public boolean checkCollide(Sprite projectile) {
		return (projectile.getBoundsInParent().intersects(getBoundsInParent()));

	}

	/**
	 * Updates player HP and HPBar, and adds the projectile to things that must
	 * be removed
	 * 
	 * @param projectile
	 * @param shotsToRemove
	 */
	public void collisionUpdate(Sprite projectile,
			ArrayList<Sprite> shotsToRemove) {
		if (checkCollide(projectile)) {
			shotsToRemove.add(projectile);
			HP = HP - DAMAGE;
			HPBar.setProgress(HP / fullHP);
		}
	}

	/**
	 * Run collisionUpdate for all sprites in shots
	 * 
	 * @param shots
	 */
	public void updateAll(ArrayList<Sprite> shots,
			ArrayList<Sprite> shotsToRemove) {
		for (Sprite s : shots) {
			collisionUpdate(s, shotsToRemove);
		}
	}

	public void setBarLocation(double X, double Y) {
		HPBar.setTranslateX(X);
		HPBar.setTranslateY(Y);
	}
	
	public void setHP(double points) {
		HP = points;
	}

	public void setFull() {
		HP = fullHP;
		HPBar.setProgress(1);
	}

	public double getHP() {
		return HP;
	}
	
	public ProgressBar getHPBar(){
		return HPBar;
	}
}
