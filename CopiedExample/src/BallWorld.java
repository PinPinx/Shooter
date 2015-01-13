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
	private ImageView myEnemy;
	private ProgressBar enemyHP;
	private ProgressBar myHP;
	private Point2D myEnemyVelocity;
	private Random myGenerator = new Random();
	private ArrayList<KeyCode> keysPressed;
	private ArrayList<ImageView> Shots;
	private ArrayList<ImageView> Poops;
	private Label timerLabel;
	private int frameCounter;
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
		Shots= new ArrayList<ImageView>();
		Poops= new ArrayList<ImageView>();
		// make the player and enemy and set their properties
		myPlayer = makeChar(width/2, height-80, "images/hand.png");
		myEnemy = makeChar(width/2, -50, "images/But.gif");
		//make HP bars
		makeHP(width);
		//set enemy velocity
		myEnemyVelocity = new Point2D(6, 0);
		// remember shapes for viewing later
		myRoot.getChildren().add(myEnemy);
		myRoot.getChildren().add(myPlayer);
		frameCounter=0;
		//timer Implementation
		makeTimer();
		// create a place to display the shapes and react to input
		return makeScene(width, height);
	}


	/**
	 * Create the game's frame
	 */
	public KeyFrame start (int frameRate) {
		return new KeyFrame(Duration.millis(1000 / frameRate), e -> updateFrame());
	}

	/**
	 * What to do each game frame
	 *
	 * Change the sprite properties each frame by a tiny amount to animate them
	 *
	 * Note, there are more sophisticated ways to animate shapes, but these simple ways work too.
	 */

	private void updateFrame(){
		updateSprites();
		updateTimer();
	}
	
	private void updateTimer(){
		frameCounter++;
		if(frameCounter%60==0){
			timeSeconds.set(timeSeconds.get()-1);
		}
	}

	private void updateSprites () {
		
		moveEnemy();
		//Handle direction of player
		movePlayer();
		//Handle poop movement
		//See if a new poop comes out and if so, create it
				makePoop(600, myEnemy.getTranslateX(), myEnemy.getTranslateY(), 
						Poops);
		ArrayList<ImageView> poopsToRemove=new ArrayList<ImageView>();
		for(ImageView poop: Poops){
			poop.setTranslateY(poop.getTranslateY() + poopSpeed);
			if(checkCollide(poop, myPlayer)){
				poopsToRemove.add(poop);
				myHP.setProgress(myHP.getProgress()-.05);
			}
		}
		//Handle shot movement
		ArrayList<ImageView> shotsToRemove=new ArrayList<ImageView>();
		for(ImageView shot: Shots){
			shot.setTranslateY(shot.getTranslateY() - shotSpeed);
			if(checkCollide(shot, myEnemy)){
				shotsToRemove.add(shot);
				enemyHP.setProgress(enemyHP.getProgress()-.05);
			}
		}
		//Remove the poop and shots after collision occurs
		for(ImageView poop: poopsToRemove){
			Poops.remove(poop);
			myRoot.getChildren().remove(poop);
		}
		for(ImageView shot: shotsToRemove){
			Shots.remove(shot);
			myRoot.getChildren().remove(shot);
		}

		/*if (myEnemy.getTranslateY() >= myScene.getHeight() || myEnemy.getTranslateY() <= 0) {
            myEnemyVelocity = new Point2D(myEnemyVelocity.getX(), myEnemyVelocity.getY() * -1);
        }*/
	}

	/**
	 * What to do each time a key is pressed
	 */
	private void handleKeyInput (KeyEvent e) {
		KeyCode keyCode = e.getCode();
		if (!keysPressed.contains(keyCode)){
			keysPressed.add(0, keyCode);
		}
		/* if (keyCode == KeyCode.D && myPlayer.getTranslateX()<=myScene.getWidth()-30) {
            myPlayer.setTranslateX(myPlayer.getTranslateX() + PLAYER_SPEED);
        }
        else if (keyCode == KeyCode.A && myPlayer.getTranslateX()>=-30) {
            myPlayer.setTranslateX(myPlayer.getTranslateX() - PLAYER_SPEED);
        }*/
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
		makeShot();
	}
	
	/**
	 * 3 "Make" methods follow
	 */
	
	private ImageView MakeSprite(double X, double Y, String fileName){
		ImageView newSprite= new ImageView(new Image(getClass().getResourceAsStream(fileName)));
		newSprite.setTranslateX(X);
		newSprite.setTranslateY(Y);
		myRoot.getChildren().add(newSprite);
		return newSprite;
	}
	
	private void makeShot(){
		ImageView newShot = MakeSprite(myPlayer.getTranslateX(), myPlayer.getTranslateY(),
				"images/shot.png");
		newShot.setFitHeight(30);
		newShot.setFitWidth(30);
		myRoot.getChildren().add(newShot);
		Shots.add(newShot);
	}
	
	
	private  ImageView makePoop(int chance, double X, double Y, ArrayList<ImageView> adder){
		if(myGenerator.nextInt(chance)<=3){
			ImageView newSprite=MakeSprite(X, Y, "images/Poop.gif");
			adder.add(newSprite);
			return newSprite;
		}
		return null;
	}
	
	/**
	 * What to do each time shapes collide
	 */
	private boolean checkCollide (Node shot, Node character) {
		// check for collision
		if (shot.getBoundsInParent().intersects(character.getBoundsInParent())) {
			System.out.println("Collide");
			return true;
		}
		return false;
	}
	
	private ImageView makeChar(double X, double Y, String fileName){
		ImageView image= new ImageView(new Image(getClass().getResourceAsStream(fileName)));
		image.setTranslateX(X);
		image.setTranslateY(Y);
		return image;
	}
	
	private void makeHP(int width){
		//make enemy HP bar
		enemyHP= fullHP("-fx-accent: red;", width-100, 130);
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

	private void makeTimer(){
		timerLabel = new Label();
		timerLabel.textProperty().bind(timeSeconds.asString());
		timerLabel.setTextFill(Color.RED);
		timerLabel.setStyle("-fx-font-size: 4em;");
		myRoot.getChildren().add(timerLabel);
	}
	
	private Scene makeScene(int width, int height){
		myScene = new Scene(myRoot, width, height, Color.WHITE);
		myScene.setOnMouseClicked(e -> handleMouseInput(e));
		myScene.setOnKeyPressed(e -> handleKeyInput(e));
		myScene.setOnKeyReleased(e -> handleKeyRelease(e));
		return myScene;
	}

	private void moveEnemy(){
		myEnemy.setTranslateX(myEnemy.getTranslateX() + myEnemyVelocity.getX());
		myEnemy.setTranslateY(myEnemy.getTranslateY() + myEnemyVelocity.getY());
		if (myEnemy.getTranslateX() >= myScene.getWidth()-50 || myEnemy.getTranslateX() <= -50) {
			myEnemyVelocity = new Point2D(myEnemyVelocity.getX() * -1, myEnemyVelocity.getY());
		}
		if (myGenerator.nextInt(300)<=2)
			myEnemyVelocity = new Point2D(myEnemyVelocity.getX() * -1, myEnemyVelocity.getY());
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
