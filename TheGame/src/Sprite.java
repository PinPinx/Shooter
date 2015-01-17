import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

//Any sprite in game extends this class
public class Sprite extends ImageView{
	private Point2D myVelocity=new Point2D(0,0);
	
	public Sprite(double X, double Y, String fileName, Group myRoot){
		setTranslateX(X);
		setTranslateY(Y);
		setImage(new Image(getClass().getResourceAsStream(fileName)));
		myRoot.getChildren().add(this);
	}
	
//Movement according to velocity of sprite
	public void moveSprite(){
		setTranslateX(getTranslateX()+myVelocity.getX());
		setTranslateY(getTranslateY()+myVelocity.getY());
	}
	
//set velocity of sprite
	public void setVelocity(double X, double Y){
		myVelocity= new Point2D(X,Y);
	}
	
//get velocity of sprite
	public Point2D getVelocity(){
		return myVelocity;
	}
}
