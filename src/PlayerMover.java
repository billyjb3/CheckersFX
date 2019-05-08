import java.util.Random;

import javafx.scene.input.MouseEvent;


public class PlayerMover implements GameConstants
{
    private static MyRectangle[] rectangles = new MyRectangle[32];
    private static int lastActive = -1;
    private static int l;

    public void update(int s, Piece[] player, Piece[] opp)
    {
        if(Game.isPlayersTurn())
            l = -1;
        else
            l = 1;

        if(lastActive == -1)
        {
            if(MyRectangle.getNumSelected() == 0 & isPlayerPiece(s, player))
            {
                rectangles[s].select();
            }
            else if(MyRectangle.getNumSelected() == 1)
            {
                if(hasJumpAll(player, opp))
                {
                    if(rectangles[s].isSelected())
                    {
                        rectangles[s].deselect();
                    }
                    else if(isPlayerPiece(s, player))
                    {
                        getSelectedRectangle().deselect();
                        rectangles[s].select();
                    }
                    else if(isJump(s, getSelectedPiece(player), opp))
                    {
                        lastActive = getSelectedPieceIndex(player);
                        jump(s, getSelectedPiece(player), opp);
                        Game.waiting(s);
                    }
                }
                else
                {
                    if(rectangles[s].isSelected())
                    {
                        rectangles[s].deselect();
                    }
                    else if(isPlayerPiece(s, player))
                    {
                        getSelectedRectangle().deselect();
                        rectangles[s].select();
                    }
                    else if(isLegal(s, getSelectedPiece(player), opp))
                    {
                        getSelectedPiece(player).moveTo((int)rectangles[s].getX(), (int)rectangles[s].getY());
                        getSelectedRectangle().deselect();
                        lastActive = -1;
                        Game.setPlayersTurn(!Game.isPlayersTurn());
                    }
                }
            }
        }
        else
        {
            if(hasJump(player[lastActive], player, opp))
            {
                if(rectangles[s].getX() == player[lastActive].getX() & rectangles[s].getY() == player[lastActive].getY())
                    rectangles[s].select();
                if(isJump(s, player[lastActive], opp))
                {
                    jump(s, player[lastActive], opp);
                    Game.waiting(s);
                }
            }
            else
            {
                lastActive = -1;
                Game.setPlayersTurn(!Game.isPlayersTurn());
            }
        }
    }
    public static boolean isLegal(int s, Piece p, Piece[] opp)
    {
        MyRectangle r = rectangles[s];
        if(!isOppPiece(s, opp))
        {
            if(p.isCrowned())
            {
                if((r.getY() + TSIZE == p.getY() | r.getY() - TSIZE == p.getY()) & (r.getX() + TSIZE == p.getX() | r.getX() - TSIZE == p.getX()))
                    return true;
            }
            else
            {
                if(r.getY() - l*TSIZE == p.getY() & (r.getX() + TSIZE == p.getX() | r.getX() - TSIZE == p.getX()))
                    return true;
            }
        }
        return false;
    }
    public static boolean isOppPiece(int s, Piece[] opp)
    {
        for(int c = 0; c < 12; c++)
        {
            Piece p = opp[c];
            if(p.getX() == rectangles[s].getX() & p.getY() == rectangles[s].getY())
                return true;
        }
        return false;
    }
    public static boolean isPlayerPiece(int s, Piece[] player)
    {
        for(int c = 0; c < 12; c++)
        {
            Piece p = player[c];
            if(p.getX() == rectangles[s].getX() & p.getY() == rectangles[s].getY())
                return true;
        }
        return false;
    }
    public static Piece getSelectedPiece(Piece[] player)
    {
        Piece p = null;
        for(int i = 0; i < rectangles.length; i++)
        {
            if(rectangles[i].isSelected())
            {
                int x = (int) rectangles[i].getX();
                int y = (int) rectangles[i].getY();
                for(int j = 0; j < player.length; j++)
                    if(player[j].getX() == x & player[j].getY() == y)
                        return player[j];
            }
        }
        return p;
    }
    public static int getSelectedPieceIndex(Piece[] player)
    {
        for(int i = 0; i < rectangles.length; i++)
        {
            if(rectangles[i].isSelected())
            {
                int x = (int) rectangles[i].getX();
                int y = (int) rectangles[i].getY();
                for(int j = 0; j < player.length; j++)
                    if(player[j].getX() == x & player[j].getY() == y)
                        return j;
            }
        }
        return -1;
    }
    public static MyRectangle getSelectedRectangle()
    {
        MyRectangle r = null;
        for(int i = 0; i < rectangles.length; i++)
        {
            if(rectangles[i].isSelected())
                r = rectangles[i];
        }
        return r;
    }
    public void initRectangles(Tile[] blackTiles)
    {
        MyMouseHandler[] handlers = new MyMouseHandler[32];

        for(int i = 0; i < 32; i++)
        {
            int x = blackTiles[i].getX();
            int y = blackTiles[i].getY();
            rectangles[i] = new MyRectangle(x,y);
            handlers[i] = new MyMouseHandler(i);
            rectangles[i].addEventHandler(MouseEvent.MOUSE_PRESSED, handlers[i]);
            Game.addRect(rectangles[i]);
        }
    }
    public boolean isJump(int s, Piece p, Piece[] opp)
    {
        if(!isOppPiece(s,opp))
        {
            if(p.isCrowned())
            {
                if(p.getX() + TSIZE*2 == rectangles[s].getX() & p.getY() - TSIZE*2 == rectangles[s].getY())
                {
                    for(int i = 0; i < opp.length; i++)
                        if(opp[i].getX() == p.getX() + TSIZE & opp[i].getY() == p.getY() - TSIZE)
                            return true;
                }
                if(p.getX() + TSIZE*2 == rectangles[s].getX() & p.getY() + TSIZE*2 == rectangles[s].getY())
                {
                    for(int i = 0; i < opp.length; i++)
                        if(opp[i].getX() == p.getX() + TSIZE & opp[i].getY() == p.getY() + TSIZE)
                            return true;
                }
                if(p.getX() - TSIZE*2 == rectangles[s].getX() & p.getY() - TSIZE*2 == rectangles[s].getY())
                {
                    for(int i = 0; i < opp.length; i++)
                        if(opp[i].getX() == p.getX() - TSIZE & opp[i].getY() == p.getY() - TSIZE)
                            return true;
                }
                if(p.getX() - TSIZE*2 == rectangles[s].getX() & p.getY() + TSIZE*2 == rectangles[s].getY())
                {
                    for(int i = 0; i < opp.length; i++)
                        if(opp[i].getX() == p.getX() - TSIZE & opp[i].getY() == p.getY() + TSIZE)
                            return true;
                }
            }
            else
            {
                if(p.getX() + TSIZE*2 == rectangles[s].getX() & p.getY() + l*TSIZE*2 == rectangles[s].getY())
                {
                    for(int i = 0; i < opp.length; i++)
                        if(opp[i].getX() == p.getX() + TSIZE & opp[i].getY() == p.getY() + l*TSIZE)
                            return true;
                }
                if(p.getX() - TSIZE*2 == rectangles[s].getX() & p.getY() + l*TSIZE*2 == rectangles[s].getY())
                {
                    for(int i = 0; i < opp.length; i++)
                        if(opp[i].getX() == p.getX() - TSIZE & opp[i].getY() == p.getY() + l*TSIZE)
                            return true;
                }
            }
        }

        return false;
    }
    public void jump(int s, Piece p, Piece[] opp)
    {
        if(p.isCrowned())
        {
            if(p.getX() + TSIZE*2 == rectangles[s].getX() & p.getY() - TSIZE*2 == rectangles[s].getY())
            {
                for(int i = 0; i < opp.length; i++)
                    if(opp[i].getX() == p.getX() + TSIZE & opp[i].getY() == p.getY() - TSIZE)
                        moveToBox(opp[i]);
            }
            if(p.getX() + TSIZE*2 == rectangles[s].getX() & p.getY() + TSIZE*2 == rectangles[s].getY())
            {
                for(int i = 0; i < opp.length; i++)
                    if(opp[i].getX() == p.getX() + TSIZE & opp[i].getY() == p.getY() + TSIZE)
                        moveToBox(opp[i]);
            }
            if(p.getX() - TSIZE*2 == rectangles[s].getX() & p.getY() - TSIZE*2 == rectangles[s].getY())
            {
                for(int i = 0; i < opp.length; i++)
                    if(opp[i].getX() == p.getX() - TSIZE & opp[i].getY() == p.getY() - TSIZE)
                        moveToBox(opp[i]);
            }
            if(p.getX() - TSIZE*2 == rectangles[s].getX() & p.getY() + TSIZE*2 == rectangles[s].getY())
            {
                for(int i = 0; i < opp.length; i++)
                    if(opp[i].getX() == p.getX() - TSIZE & opp[i].getY() == p.getY() + TSIZE)
                        moveToBox(opp[i]);
            }
        }
        else
        {
            if(p.getX() + TSIZE*2 == rectangles[s].getX() & p.getY() + l*TSIZE*2 == rectangles[s].getY())
            {
                for(int i = 0; i < opp.length; i++)
                    if(opp[i].getX() == p.getX() + TSIZE & opp[i].getY() == p.getY() + l*TSIZE)
                        moveToBox(opp[i]);
            }
            if(p.getX() - TSIZE*2 == rectangles[s].getX() & p.getY() + l*TSIZE*2 == rectangles[s].getY())
            {
                for(int i = 0; i < opp.length; i++)
                    if(opp[i].getX() == p.getX() - TSIZE & opp[i].getY() == p.getY() + l*TSIZE)
                        moveToBox(opp[i]);
            }
        }
        p.moveTo((int)rectangles[s].getX(), (int)rectangles[s].getY());
        if(getSelectedRectangle() != null)
            getSelectedRectangle().deselect();
    }
    public void moveToBox(Piece p)
    {
        Random r = new Random();
        p.setPlaying(false);
        if(Game.isPlayersTurn())
        {
            p.moveTo(r.nextInt(PWIDTH - TSIZE) - 1, r.nextInt(FHEIGHT/2) + (FHEIGHT/2) - TSIZE);
        }
        else
        {
            p.moveTo(r.nextInt(PWIDTH - TSIZE) + BSIZE + PWIDTH+ 1, r.nextInt(FHEIGHT/2) + (FHEIGHT/2) - TSIZE);
        }
    }
    public boolean hasJump(Piece p, Piece[] player, Piece[] opp)
    {
        if(p.isCrowned())
        {
            if(isOppPieceCo(p.getX() + TSIZE, p.getY() + TSIZE, opp))
            {
                if(isEmpty(p.getX() + TSIZE*2, p.getY() + TSIZE*2, player, opp))
                    return true;
            }
            else if(isOppPieceCo(p.getX() + TSIZE, p.getY() - TSIZE, opp))
            {
                if(isEmpty(p.getX() + TSIZE*2, p.getY() - TSIZE*2, player, opp))
                    return true;
            }
            else if(isOppPieceCo(p.getX() - TSIZE, p.getY() + TSIZE, opp))
            {
                if(isEmpty(p.getX() - TSIZE*2, p.getY() + TSIZE*2, player, opp))
                    return true;
            }
            else if(isOppPieceCo(p.getX() - TSIZE, p.getY() - TSIZE, opp))
            {
                if(isEmpty(p.getX() - TSIZE*2, p.getY() - TSIZE*2, player, opp))
                    return true;
            }
        }
        else
        {
            if(isOppPieceCo(p.getX() + TSIZE, p.getY() + l*TSIZE, opp))
            {
                if(isEmpty(p.getX() + TSIZE*2, p.getY() + l*TSIZE*2, player, opp))
                    return true;
            }
            else if(isOppPieceCo(p.getX() - TSIZE, p.getY() + l*TSIZE, opp))
            {
                if(isEmpty(p.getX() - TSIZE*2, p.getY() + l*TSIZE*2, player, opp))
                    return true;
            }
        }
        return false;
    }
    public boolean isOppPieceCo(int x, int y, Piece[] opp)
    {
        for(int i = 0; i < opp.length; i++)
            if(opp[i].getX() == x & opp[i].getY() == y)
                return true;
        return false;
    }
    public boolean isEmpty(int x, int y, Piece[] player, Piece[] opp)
    {
        if(x < FHALF - BHALF | x > FHALF + BHALF - TSIZE | y < 20 | y > BSIZE + 20)
            return false;
        for(int i = 0; i < opp.length; i++)
            if(opp[i].getX() == x & opp[i].getY() == y)
                return false;
        for(int i = 0; i < player.length; i++)
            if(player[i].getX() == x & player[i].getY() == y)
                return false;
        return true;
    }
    public boolean hasJumpAll(Piece[] player, Piece[] opp)
    {
        for(int i = 0; i < player.length; i++)
            if(hasJump(player[i], player, opp))
                return true;
        return false;
    }

}
