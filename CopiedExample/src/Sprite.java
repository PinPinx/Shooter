import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class Sprite extends ImageView{
	private Point2D myVelocity=new Point2D(0,0);
	
	public Sprite(double X, double Y, String fileName, Group myRoot){
		setTranslateX(X);
		setTranslateY(Y);
		setImage(new Image(getClass().getResourceAsStream(fileName)));
		myRoot.getChildren().add(this);
	}
	

	public void moveSprite(){
		setTranslateX(getTranslateX()+myVelocity.getX());
		setTranslateY(getTranslateY()+myVelocity.getY());
	}
	
	public void setVelocity(double X, double Y){
		myVelocity= new Point2D(X,Y);
	}
	
	public Point2D getVelocity(){
		return myVelocity;
	}
}
