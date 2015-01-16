import javafx.scene.paint.Color;
import javafx.scene.text.Text;


public class GameWin extends Menu{
	
	public void addAllButtons(){
		addButton(240,420,"Kill the Butt again?");
	}
	
	public void addAllText(){
		addText(90, 130, 150, "YOU\nWON!\n");
		Text instructions=addText(105, 370, 15, "Wow! You shut the butt\n"
				+ "by shooting the butt\n"
				+ "and dodging the poop.");
		instructions.setWrappingWidth(400);
		instructions.setFill(Color.RED);
	}

}
