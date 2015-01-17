import javafx.scene.paint.Color;
import javafx.scene.text.Text;

//Game over menu
public class GameOver extends Menu{
	
	public void addAllButtons(){
		addButton(230,420,"Get pooped on again?");
	}
	
	
	public void addAllText(){
		addText(90, 110, 100, "GET\nPOOPED\nON");
		Text instructions=addText(105, 370, 15, "DON'T FAIL AGAIN \n"
				+ "HAND DO NOT LIKE\n"
				+ "GETTING POOPED ON.");
		instructions.setWrappingWidth(400);
		instructions.setFill(Color.RED);
	}
}
