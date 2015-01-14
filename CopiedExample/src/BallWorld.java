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
	private int shotSpeed=10;
	private int poopSpeed=10;
	private Scene myScene;
	//private Group myRoot;
	private ImageView myPlayer;
	private Enemy myEnemy;
	private ProgressBar myHP;
	private Random myGenerator = new Random();
	private ArrayList<KeyCode> keysPressed;
	private ArrayList<Sprite> Shots;
	private ArrayList<Poop> Poops;
	private Label timerLabel;
	private int frameCounter=0;
	private IntegerProperty timeSeconds =
			new SimpleIntegerProperty(START_TIME);
	/**
	 * Create the game's scene
	 */


	public Scene init (Stage s, int width, int height) {
		// create a scene graph to organize the scene
		myRoot = new Group();
		//Create arraylists that tracks various items
		keysPressed= new ArrayList<KeyCode>(2);
		Shots= new ArrayList<Sprite>();
		Poops= new ArrayList<Poop>();
		// make the player and enemy and set their properties
		myPlayer = makeSprite(width/2, height-80, "images/hand.png");
		myEnemy = new Enemy(width/2, -50, 100, myRoot);
		//make HP bars
		makeHP(width);
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

		myEnemy.moveSprite(600);
		//Handle direction of player
		movePlayer();
		//Handle poop movement
		//See if a new loop comes out and if so, create it
		if (myGenerator.nextInt(600)<=3){
			Poops.add(makePoop());
		}
		//Handle Poop Collisions
		ArrayList<ImageView> poopsToRemove=new ArrayList<ImageView>();
		for(Poop poop: Poops){
			poop.setTranslateY(poop.getTranslateY() + poopSpeed);
			if(checkCollide(poop, myPlayer)){
				poopsToRemove.add(poop);
				myHP.setProgress(myHP.getProgress()-.05);
			}
		}
		//Handle shot collisions
		ArrayList<Sprite> shotsToRemove=new ArrayList<Sprite>();
		for(Sprite shot: Shots){
			shot.moveSprite();
		}
		myEnemy.updateAll(Shots, shotsToRemove);
		
		//Remove the poop and shots after collision occurs
		for(ImageView poop: poopsToRemove){
			Poops.remove(poop);
			myRoot.getChildren().remove(poop);
		}
		for(ImageView shot: shotsToRemove){
			Shots.remove(shot);
			myRoot.getChildren().remove(shot);
		}

	}

	/*private void testAllCollisions(ArrayList<ImageView> shots, 
			ArrayList<ImageView> shotsToRemove){
			for(ImageView shot: shots){
				shot.setTranslateY(shot.getTranslateY() - shot.getSpeed());
				collisionUpdate(shot, myEnemy, enemyHP, shotsToRemove, 100);
			}
	}*/
	
	/**
	 * What to do each time shapes collide
	 */
	
	private void collisionUpdate(ImageView shot, ImageView character, ProgressBar bar,
			ArrayList<ImageView> shotsToRemove, double HP){
		if(checkCollide(shot, character)){
			shotsToRemove.add(shot);
			bar.setProgress(bar.getProgress()-(5/HP));
		}
	}

	private boolean checkCollide (Node shot, Node character) {
		// check for collision
		if (shot.getBoundsInParent().intersects(character.getBoundsInParent())) {
			System.out.println("Collide");
			
			return true;
		}
		return false;
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
		Shots.add(makeShot());
	}

	/**
	 * 3 "make" methods follow
	 */

	private ImageView makeSprite(double X, double Y, String fileName){
		ImageView newSprite= new ImageView(new Image(getClass().getResourceAsStream(fileName)));
		newSprite.setTranslateX(X);
		newSprite.setTranslateY(Y);
		myRoot.getChildren().add(newSprite);
		return newSprite;
	}

	private Shot makeShot(){
		return new Shot(myPlayer.getTranslateX(), myPlayer.getTranslateY(), 10, myRoot);
	}
	
	private  Poop makePoop(){
		return new Poop(myEnemy.getTranslateX(), myEnemy.getTranslateY(), 10, myRoot);
	}


	/**
	 * Making hp bars 2 methods
 	 */
	private void makeHP(int width){
		//make enemy HP bar
		//make my HP bar
		myHP= fullHP("-fx-accent: green;", width-100, 150);
	}
	
	private ProgressBar fullHP(String colorString, int X, int Y){
		ProgressBar bar= new ProgressBar();
		bar.setProgress(1);
		bar.setTranslateX(X);
		bar.setTranslateY(Y);
		bar.setStyle(colorString);
		myRoot.getChildren().add(bar);
		return bar;
	}


	private void movePlayer(){
		if (keysPressed.size()>0){
			if (keysPressed.get(0).equals(KeyCode.D)){
				if (myPlayer.getTranslateX()<= myScene.getWidth()-30)
					myPlayer.setTranslateX(myPlayer.getTranslateX() + PLAYER_SPEED);
			}
			else if (keysPressed.get(0).equals(KeyCode.A)){
				if (myPlayer.getTranslateX()>=-30)
					myPlayer.setTranslateX(myPlayer.getTranslateX() - PLAYER_SPEED);
			}
		}
	}
}
