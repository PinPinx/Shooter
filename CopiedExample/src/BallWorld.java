import java.util.*;

import javafx.animation.KeyFrame;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;


/**
 * This represents the primary class for a game/animation.
 *
 * @author Robert C. Duvall
 */
class BallWorld {
    private static final int PLAYER_SPEED = 10;
    
    private int shotSpeed=10;
    private int poopSpeed=20;
    private Scene myScene;
    private Group myRoot;
    private ImageView myPlayer;
    private ImageView myEnemy;
    private Point2D myEnemyVelocity;
    private Random myGenerator = new Random();
    private ArrayList<KeyCode> keysPressed;
    private ArrayList<ImageView> Shots;
    private ArrayList<ImageView> Poops;
    
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
        // make some shapes and set their properties
        myPlayer = new ImageView(new Image(getClass().getResourceAsStream("images/hand.png")));
        myPlayer.setTranslateX(width/2);
        myPlayer.setTranslateY(height-80);
        myEnemy = new ImageView(new Image(getClass().getResourceAsStream("images/But.gif")));
        myEnemy.setTranslateX(width/2);
        myEnemy.setTranslateY(-50);
        //myEnemy.setOnMouseClicked(e -> handleMouseInput(e));
        myEnemyVelocity = new Point2D(6, 0);
        // remember shapes for viewing later
        myRoot.getChildren().add(myEnemy);
        myRoot.getChildren().add(myPlayer);

        // create a place to display the shapes and react to input
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
        return new KeyFrame(Duration.millis(1000 / frameRate), e -> updateSprites());
    }

    /**
     * What to do each game frame
     *
     * Change the sprite properties each frame by a tiny amount to animate them
     *
     * Note, there are more sophisticated ways to animate shapes, but these simple ways work too.
     */
    private void updateSprites () {
        //myPlayer.setRotate(myPlayer.getRotate() + 1);
        myEnemy.setTranslateX(myEnemy.getTranslateX() + myEnemyVelocity.getX());
        myEnemy.setTranslateY(myEnemy.getTranslateY() + myEnemyVelocity.getY());
        if (myEnemy.getTranslateX() >= myScene.getWidth()-50 || myEnemy.getTranslateX() <= -50) {
            myEnemyVelocity = new Point2D(myEnemyVelocity.getX() * -1, myEnemyVelocity.getY());
        }
        if (myGenerator.nextInt(150)<=2)
        	myEnemyVelocity = new Point2D(myEnemyVelocity.getX() * -1, myEnemyVelocity.getY());
        //See if a new poop comes out
        if (myGenerator.nextInt(150)<=2){
        	ImageView newPoop = new ImageView(new Image(getClass().getResourceAsStream("images/Poop.gif")));
        	newPoop.setTranslateX(myEnemy.getTranslateX());
        	newPoop.setTranslateY(myEnemy.getTranslateY());
        	Poops.add(newPoop);
        }
        
        //Handle direction of player
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
        //Handle poop movement
        for(ImageView poop: Poops){
        	poop.setTranslateY(poop.getTranslateY() + poopSpeed);
        	
        }
        //Handle shot movement
        for(ImageView shot: Shots){
        	shot.setTranslateY(shot.getTranslateY() - shotSpeed);
        	if (shot.getTranslateY()<-30){
        		Shots.remove(shot);
        	}
        }
        /*if (myEnemy.getTranslateY() >= myScene.getHeight() || myEnemy.getTranslateY() <= 0) {
            myEnemyVelocity = new Point2D(myEnemyVelocity.getX(), myEnemyVelocity.getY() * -1);
        }*/
        checkCollide(myPlayer, myEnemy);
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
     * What to do each time the mouse is clicked
     */
    private void handleMouseInput (MouseEvent e) {
    	ImageView newShot = new ImageView(new Image(getClass().getResourceAsStream("images/shot.png")));
    	newShot.setFitHeight(30);
    	newShot.setFitWidth(30);
    	myRoot.getChildren().add(newShot);
    	newShot.setTranslateX(myPlayer.getTranslateX()+50);
        newShot.setTranslateY(myPlayer.getTranslateY());
        Shots.add(newShot);
        /*myEnemy.setScaleX(myEnemy.getScaleX() * ENEMY_GROWTH_FACTOR);
        myEnemy.setScaleY(myEnemy.getScaleY() * ENEMY_GROWTH_FACTOR);*/
    }

    /**
     * What to do each time shapes collide
     */
    private void checkCollide (Node player, Node enemy) {
        // check for collision
        if (player.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
            System.out.println("Collide!");
        }
    }
}
