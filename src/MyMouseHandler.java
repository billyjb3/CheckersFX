import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class MyMouseHandler implements EventHandler<MouseEvent>
{
    private int i;

    MyMouseHandler(int i)
    {
        this.i = i;
    }

    public void handle(MouseEvent arg0)
    {
        if(Game.isStarted())
            if(Game.isReady())
                Game.selectSquare(i);
    }

}
