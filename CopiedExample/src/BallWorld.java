import java.util.*;

import javafx.animation.KeyFrame;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;



/**
 * This represents the primary class for a game/animation.
 *
 * @author Robert C. Duvall
 */
class BallWorld extends Screen{
	private static final int PLAYER_SPEED = 10;
	private static final int START_TIME = 60;
	private double screenWidth;
	private Scene myScene;
	private Group myRoot;
	private Player myPlayer;
	private Enemy myEnemy;
	private Random myGenerator = new Random();
	private ArrayList<KeyCode> keysPressed;
	private ArrayList<Sprite> Shots;
	private ArrayList<Sprite> Poops;
	private Label timerLabel;
	private int frameCounter=0;
	private IntegerProperty timeSeconds =
			new SimpleIntegerProperty(START_TIME);
	/**
	 * Create the game's scene
	 */


	public Scene init (Stage s, int width, int height) {
		// create a scene graph to organize the scene
		screenWidth=width;
		myRoot = new Group();
		//Create arraylists that tracks various items
		keysPressed= new ArrayList<KeyCode>(2);
		Shots= new ArrayList<Sprite>();
		Poops= new ArrayList<Sprite>();
		// make the player and enemy and set their properties
		myPlayer = new Player(width/2, height-80, 50, myRoot);
		myEnemy = new Enemy(width/2, -50, 100, myRoot);
		//set enemy velocity
		myEnemy.setVelocity(6,0); 
		//timer Implementation
		makeTimer();
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

	private void updateFrame(){
		updateSprites();
		updateTimer();
	}
	
/**
 * 2 methods that have to do with the timer
 */
	
	private void updateTimer(){
		frameCounter++;
		if(frameCounter%60==0){
			timeSeconds.set(timeSeconds.get()-1);
		}
	}
	
	private void makeTimer(){
		timerLabel = new Label();
		timerLabel.textProperty().bind(timeSeconds.asString());
		timerLabel.setTextFill(Color.RED);
		timerLabel.setStyle("-fx-font-size: 4em;");
		myRoot.getChildren().add(timerLabel);
	}

	private void updateSprites () {

		myEnemy.moveSprite(screenWidth);
		myPlayer.moveSprite(screenWidth, keysPressed);
		//See if a new poop comes out and if so, create it
		if (myGenerator.nextInt(600)<=3){
			Poops.add(myEnemy.makePoop());
		}
		//***********************
		//CLEAN THIS@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
		//Move and Handle Poop Collisions
		ArrayList<Sprite> poopsToRemove=returnCollisions(myPlayer, Poops);
		//Handle shot collisions
		ArrayList<Sprite> shotsToRemove=returnCollisions(myEnemy, Shots);
		
		//Remove the poop and shots after collision occurs
		removeFromScreen(Poops, poopsToRemove);
		removeFromScreen(Shots, shotsToRemove);

	}

	private ArrayList<Sprite> returnCollisions(Character character, 
			ArrayList<Sprite> projectiles){
		ArrayList<Sprite> toRemove=new ArrayList<Sprite>();
		for(Sprite shot: projectiles){
			shot.moveSprite();
		}
		character.updateAll(projectiles, toRemove);
		return toRemove;
	}
	
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
	}
	
}
