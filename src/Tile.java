import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;


public class Tile
{
    private static Image blackTile = new Image("/blackTile.png");
    private static Image whiteTile = new Image("/whiteTile.png");
    private static PixelReader br = blackTile.getPixelReader();
    private static PixelReader wr = whiteTile.getPixelReader();

    private static final int BLACK = 0;
    private static final int WHITE = 1;
    private int color;
    private boolean selected = false;
    private int xPos, yPos;

    Tile(int color, int x, int y)
    {
        this.color = color;
        this.xPos = x;
        this.yPos = y;
    }

    public Image getImage()
    {
        if(color == BLACK)
            return blackTile;
        else
            return whiteTile;
    }
    public Color getPixelColor(int x, int y)
    {
        if(color == BLACK)
            return br.getColor(x, y);
        else
            return wr.getColor(x, y);
    }
    public int getX()
    {
        return xPos;
    }
    public int getY()
    {
        return yPos;
    }
    public void setSelected(boolean b)
    {
        selected = b;
    }
}
