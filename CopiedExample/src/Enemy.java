import javafx.scene.Group;


public class Enemy extends Character{
	private Group myRoot;
	public Enemy(double X, double Y, double points, Group Root){
		super(X, Y, points, "images/But.gif", Root);
		setVelocity(6,0);
		setBarLocation(500, 130);
		HPBar.setStyle("-fx-accent: red;");
		myRoot=Root;
	}
	
	public Poop makePoop(){
		return new Poop(getTranslateX(), getTranslateY(), 10, myRoot);
	}
	

	public void moveSprite(double width){
		if (getTranslateX() >= width-50 || getTranslateX() <= -50) {
			setVelocity(getVelocity().getX() * -1, getVelocity().getY());
		}
		else if (myGenerator.nextInt(300)<=4)
			setVelocity(getVelocity().getX() * -1, getVelocity().getY());
		moveSprite();
	}
	
}
