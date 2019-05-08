import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class MyRectangle extends Rectangle implements GameConstants
{
    private static int numSelected = 0;
    private boolean selected;

    MyRectangle(int x, int y)
    {
        this.setWidth(TSIZE);
        this.setHeight(TSIZE);
        this.setFill(Color.TRANSPARENT);
        this.setX(x);
        this.setY(y);
    }
    public void select()
    {
        this.setStroke(Color.BLUE);
        this.setStrokeWidth(3);
        selected = true;
        numSelected++;
    }
    public void deselect()
    {
        this.setStroke(Color.TRANSPARENT);
        selected = false;
        numSelected--;
    }
    public static int getNumSelected()
    {
        return numSelected;
    }
    public boolean isSelected()
    {
        return selected;
    }
}
