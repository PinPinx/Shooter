import java.util.*;

import javafx.animation.KeyFrame;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;



/**
 * This represents the primary class for a game/animation.
 *
 * @author Robert C. Duvall
 */
class Level1 extends Screen{
	private static final int PLAYER_HP = 50;
	private static final int ENEMY_HP = 100;
	private static final int ENEMY_V=6;
	private static final int START_TIME = 30;
	private static final int SHOT_LIMIT = 50;
	private static final int FRAMES_PER_SEC = 60;
	private static final int POOP_FREQUENCY = 200;
	private double screenWidth;
	private Scene myScene;
	private Group myRoot;
	private Player myPlayer;
	private Enemy myEnemy;
	private Random myGenerator = new Random();
	private ArrayList<KeyCode> keysPressed;
	private ArrayList<Sprite> Shots;
	private ArrayList<Sprite> Poops;
	private int frameCounter=0;
	private IntegerProperty timeSeconds =
			new SimpleIntegerProperty(START_TIME);
	private IntegerProperty shotCount=
			new SimpleIntegerProperty(SHOT_LIMIT);
	private Stage stage;			
	private boolean cheater=false;
	private boolean gameOn=true;
	private boolean cheat2=false;

	/**
	 * Initiate a new game by setting up various class variables
	 */


	public Scene init (Stage s, int width, int height) {
		// create a scene graph to organize the scene
		stage=s;
		screenWidth=width;
		myRoot = new Group();
		//Create arraylists that tracks various items
		keysPressed= new ArrayList<KeyCode>(4);
		Shots= new ArrayList<Sprite>();
		Poops= new ArrayList<Sprite>();
		// make the player and enemy and set their properties
		myPlayer = new Player(width/2, height-80, PLAYER_HP, myRoot);
		myEnemy = new Enemy(width/2, -50, ENEMY_HP, myRoot);
		//set enemy velocity
		myEnemy.setVelocity(ENEMY_V,0); 
		//timer Implementation
		makeTimer();
		makeCounter();
		// create a place to display the shapes and react to input
		return makeScene(width, height);
	}

	/**
	 * Create the game's scene
	 */

	private Scene makeScene(int width, int height){
		myScene = new Scene(myRoot, width, height, Color.WHITE);
		myScene.setOnMouseClicked(e -> handleMouseInput(e));
		myScene.setOnKeyPressed(e -> handleKeyInput(e));
		myScene.setOnKeyReleased(e -> handleKeyRelease(e));
		return myScene;
	}

	/**
	 * Create the game's frame
	 */

	public KeyFrame start (int frameRate) {
		return new KeyFrame(Duration.millis(1000 / frameRate), e -> updateFrame());
	}

	/**
	 * Run this every frame
	 */
	private void updateFrame(){
		//check if the game isn't over so it doesn't keep running after it's over
		if(gameOn){
			goNextLevel();
			goGameOver();
			updateSprites();
			updateTimer();
		}
	}

	/**
	 * Updates timer by updating timeSeconds
	 */

	private void updateTimer(){
		frameCounter++;
		//60=frames per second
		if(frameCounter%FRAMES_PER_SEC==0){
			timeSeconds.set(timeSeconds.get()-1);
		}
	}

	/*
	 * 2 labels in the game- makeTimer and makeCounter. makeLabel helps these methods
	 */
	private Label makeLabel(Color color, IntegerProperty binder){
		Label label=new Label();
		label.textProperty().bind(binder.asString());
		label.setTextFill(color);
		label.setStyle("-fx-font-size:4em;");
		myRoot.getChildren().add(label);
		return label;
	}

	private void makeTimer(){
		makeLabel(Color.RED, timeSeconds);
	}

	private void makeCounter(){
		Label label=makeLabel(Color.BLUE, shotCount);
		label.setTranslateY(50);
	}

	/**
	 * Updates sprites. Runs in updateFrame method.
	 */
	private void updateSprites () {
		//checks if cheatcode was pressed
		checkCheats();
		//move player and enemy
		myEnemy.moveSprite(screenWidth);
		myPlayer.moveSprite(screenWidth, keysPressed);
		//See if a new poop comes out and if so, create it
		if (myGenerator.nextInt(POOP_FREQUENCY)<=3){
			Poops.add(myEnemy.makePoop(myPlayer));
		}
		//Move and Handle Poop Collisions
		ArrayList<Sprite> poopsToRemove=returnCollisions(myPlayer, Poops);
		//Move and Handle shot collisions
		ArrayList<Sprite> shotsToRemove=returnCollisions(myEnemy, Shots);
		//Remove the poop and shots after collision occurs
		removeFromScreen(Poops, poopsToRemove);
		removeFromScreen(Shots, shotsToRemove);
		//Cheatcode doers. 
		if(cheater){
			myPlayer.setFull();
		}
		if(cheat2){
			shotCount.set(SHOT_LIMIT);
		}
	}
	
	/**
	 * Check if cheatcodes have been pressed
	 */
	private void checkCheats(){
		if(keysPressed.contains(KeyCode.W))
			myEnemy.setHP(0);
		if(keysPressed.contains(KeyCode.I))
			cheater=true;
		if(keysPressed.contains(KeyCode.M))
			cheat2=true;
		if(keysPressed.contains(KeyCode.L))
			myPlayer.setHP(0);
	}
	
	/**
	 * If win, go to next level
	 */
	protected void goNextLevel(){
		if (myEnemy.getHP()<=0){
			stage.setTitle("Level 2");
			gameOff();
			Level2 myGame = new Level2();
			// attach game to the stage and display it
			KeyFrame frame = myGame.start(FRAMES_PER_SEC);
			myGame.showAndAnim(stage, 600, 600, frame);
			myEnemy.makeAngry();
		}
	}

	/**
	 * If lose, go to next level
	 */
	private void goGameOver(){
		if (myPlayer.getHP()<=0 || timeSeconds.get()<=0 || shotCount.get()<0){
			stage.setTitle("Game Over");
			gameOff();
			GameOver itsOver=new GameOver();
			// attach game to the stage and display it
			itsOver.showScreen(stage, 600, 600);
		}
	}

	/**
	 * Turn the game off
	 */
	protected void gameOff(){
		gameOn=false;
	}

	/**
	 * returns what has collided as an ArrayList<Sprite>
	 * @param character= things the projectiles are colliding with
	 * @param projectiles= all the projectiles you want to check
	 * @return
	 */
	private ArrayList<Sprite> returnCollisions(Character character, 
			ArrayList<Sprite> projectiles){
		ArrayList<Sprite> toRemove=new ArrayList<Sprite>();
		for(Sprite shot: projectiles){
			shot.moveSprite();
		}
		character.updateAll(projectiles, toRemove);
		return toRemove;
	}

	/**
	 * Removes all the sprites specified in toRemoveArray from projectiles
	 */
	private void removeFromScreen(ArrayList<Sprite> projectiles, 
			ArrayList<Sprite> toRemoveArray){
		for (Sprite toRemove: toRemoveArray){
			projectiles.remove(toRemove);
			myRoot.getChildren().remove(toRemove);
		}
	}

	/**
	 * What to do each time a key is pressed
	 */
	private void handleKeyInput (KeyEvent e) {
		KeyCode keyCode = e.getCode();
		if (!keysPressed.contains(keyCode)){
			keysPressed.add(0, keyCode);
		}
	}

	/**
	 * What to do when key is released
	 */
	private void handleKeyRelease (KeyEvent e){
		KeyCode keyCode=e.getCode();
		if (keysPressed.contains(keyCode)){
			keysPressed.remove(keyCode);
		}
	}

	/**
	 * What to do each time the mouse is clicked (create a shot)
	 */

	private void handleMouseInput (MouseEvent e) {
		Shots.add(myPlayer.makeShot());
		shotCount.set(shotCount.get()-1);
	}

	/**
	 * Needed for subclasses to see enemy
	 */

	public Enemy getEnemy(){
		return myEnemy;
	}

	/**
	 * Needed for subclasses to see player
	 */
	public Player getPlayer(){
		return myPlayer;
	}

}
