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
class Level1 extends Screen{
	private static final int PLAYER_HP = 50;
	private static final int ENEMY_HP = 100;
	private static final int ENEMY_V=6;
	private static final int START_TIME = 60;
	private static final int SHOT_LIMIT = 50;
	private static final int FRAMES_PER_SEC = 60;
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
	 * Create the game's scene
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

	private void updateFrame(){
		//check if have to go to next level
		if(gameOn){
			goNextLevel();
			goGameOver();
			updateSprites();
			updateTimer();
		}
	}
	
/**
 * 2 methods that have to do with the timer
 */
	
	private void updateTimer(){
		frameCounter++;
		//60=frames per second
		if(frameCounter%FRAMES_PER_SEC==0){
			timeSeconds.set(timeSeconds.get()-1);
		}
	}
	
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

	private void updateSprites () {
		if(keysPressed.contains(KeyCode.W))
			myEnemy.setHP(0);
		if(keysPressed.contains(KeyCode.I))
			cheater=true;
		if(keysPressed.contains(KeyCode.M)){
			cheat2=true;
		}
		myEnemy.moveSprite(screenWidth);
		myPlayer.moveSprite(screenWidth, keysPressed);
		//See if a new poop comes out and if so, create it
		if (myGenerator.nextInt(200)<=3){
			Poops.add(myEnemy.makePoop(myPlayer));
		}
		//Move and Handle Poop Collisions
		ArrayList<Sprite> poopsToRemove=returnCollisions(myPlayer, Poops);
		//Handle shot collisions
		ArrayList<Sprite> shotsToRemove=returnCollisions(myEnemy, Shots);
		//go to different screen if HP reaches 0 for either one
		//Remove the poop and shots after collision occurs
		removeFromScreen(Poops, poopsToRemove);
		removeFromScreen(Shots, shotsToRemove);
		if(cheater){
			myPlayer.setFull();
		}
		if(cheat2){
			shotCount.set(SHOT_LIMIT);
		}
	}

	protected void goNextLevel(){
		if (myEnemy.getHP()<=0){
			gameOff();
			Level2 myGame = new Level2();
			// attach game to the stage and display it
			KeyFrame frame = myGame.start(FRAMES_PER_SEC);
			myGame.showAndAnim(stage, 600, 600, frame);
			myEnemy.makeAngry();
		}
	}
	
	private void goGameOver(){
		if (myPlayer.getHP()<=0 || timeSeconds.get()<=0 || shotCount.get()<0){
			gameOff();
			GameOver itsOver=new GameOver();
		        // attach game to the stage and display it
		    itsOver.showScreen(stage, 600, 600);
		}
	}
	
	protected void gameOff(){
			gameOn=false;
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
	/**
	 * What to do each time the mouse is clicked (create a shot)
	 */
	private void handleMouseInput (MouseEvent e) {
		Shots.add(myPlayer.makeShot());
		shotCount.set(shotCount.get()-1);
	}
	
}
