import javafx.scene.Group;

//Poop object
public class Poop extends Sprite{
	public Poop(double X, double Y, double YSpeed, Group myRoot){
		super(X, Y, "images/Poop.gif", myRoot);
		setVelocity(0, YSpeed);
	}
}
