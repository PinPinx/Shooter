import javafx.scene.Group;


public class Shot extends Sprite{
	public Shot(double X, double Y, double YSpeed, Group myRoot){
		super(X, Y, "images/shot.png", myRoot);
		setFitHeight(30);
		setFitWidth(30);
		setVelocity(0, -YSpeed);
	}
}
