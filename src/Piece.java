
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;

public class Piece implements GameConstants
{
    public static final int BLACK = 0;
    public static final int WHITE = 1;
    private int color = 0;
    private boolean crowned = false;
    private double xPos, yPos;
    private boolean moving = false;
    private double xDes, yDes;
    private boolean playing = true;

    private Image blackPiece = new Image("/blackPiece.png");
    private Image whitePiece = new Image("/whitePiece.png");

    private PixelReader bpr = blackPiece.getPixelReader();
    private PixelReader wpr = whitePiece.getPixelReader();

    Piece(int color, int x, int y)
    {
        this.color = color;
        this.xPos = x;
        this.yPos = y;
    }

    public boolean isMoving()
    {
        return moving;
    }
    public void moveTo(int x, int y)
    {
        if(x != xPos & y != yPos)
        {
            moving = true;
            xDes = x;
            yDes = y;
        }
    }
    public void moveToSqr(int x, int y)
    {
        moving = true;
        xDes = x*TSIZE + PWIDTH;
        yDes = y*TSIZE + 20;
    }
    public void update()
    {
        if(moving)
        {
            double x = xDes - xPos;
            double y = yDes - yPos;
            double i = Math.sqrt((x*x + y*y)/(SPEED));
            double xi = x/i;
            double yi = y/i;

            if(Math.abs(x) <= Math.abs(xi) & Math.abs(y) <= Math.abs(yi))
            {
                xPos = xDes;
                yPos = yDes;
            }
            else
            {
                xPos += xi;
                yPos += yi;
            }

            if(xDes == xPos & yDes == yPos)
                moving = false;
        }
        if(Game.isReady())
        {
            if(xPos < FHALF - BHALF | xPos > FHALF + BHALF - TSIZE | yPos < 20 | yPos > BSIZE + 20 - TSIZE)
                playing = false;
            else
                playing = true;
        }
    }
    public int getPixelColor(int x, int y)
    {
        if(color==BLACK)
            return bpr.getArgb(x, y);
        else if(color == WHITE)
            return wpr.getArgb(x,y);
        else
            return 0;
    }
    public Image getImage()
    {
        if(color==BLACK)
            return blackPiece;
        else if(color == WHITE)
            return whitePiece;
        else
            return null;
    }
    public void move(int x, int y)
    {
        xPos += x;
        yPos += y;
    }
    public void moveSqr(int sx, int sy)
    {
        xPos += sx*TSIZE;
        yPos += sy*TSIZE;
    }
    public void moveOrigin()
    {
        xPos = 0;
        yPos = 0;
    }
    public void setCrowned(boolean b)
    {
        crowned = b;
    }
    public boolean isCrowned()
    {
        return crowned;
    }
    public int getX()
    {
        return (int)xPos;
    }
    public int getY()
    {
        return (int)yPos;
    }
    public int getSqrX()
    {
        int bw = (FWIDTH - BSIZE)/2;
        return (int) ((xPos - bw)/TSIZE);
    }
    public int getSqrY()
    {
        return (int) ((yPos - 20)/TSIZE);
    }
    public boolean isPlaying()
    {
        return playing;
    }
    public void setPlaying(boolean b)
    {
        playing = b;
    }
    public void setPos(int x, int y)
    {
        xPos = x;
        yPos = y;
    }
}

