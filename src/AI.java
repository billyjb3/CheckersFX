import java.util.ArrayList;
import java.util.Random;


public class AI implements GameConstants
{
    Random r = new Random();
    private static int lastActive = -1;

    public void update(Piece[] computer, Piece[] player)
    {
        if(lastActive == -1)
        {
            if(hasJump(computer, player))
            {
                jumpMove(computer, player);
            }
            else
            {
                randomMove(computer, player);
                Game.setPlayersTurn(true);
            }
        }
        else
        {
            if(pieceHasJump(computer[lastActive], computer, player))
            {
                jumpMove(computer, player);
            }
            else
            {
                lastActive = -1;
                Game.setPlayersTurn(true);
            }
        }

    }

    public boolean pieceHasJump(Piece computer, Piece[] computerA, Piece[] player)
    {
        int cx = computer.getSqrX();
        int cy = computer.getSqrY();

        if(isJump(cx + 2, cy + 2, computer, computerA, player))
            return true;
        else if(isJump(cx - 2, cy + 2, computer, computerA, player))
            return true;
        else if(isJump(cx + 2, cy - 2, computer, computerA, player))
            return true;
        else if(isJump(cx - 2, cy - 2, computer, computerA, player))
            return true;
        return false;
    }
    public boolean hasJump(Piece[] computer, Piece[] player)
    {
        for(int i = 0; i < computer.length; i++)
        {
            int cx = computer[i].getSqrX();
            int cy = computer[i].getSqrY();

            if(isJump(cx + 2, cy + 2, computer[i], computer, player))
                return true;
            else if(isJump(cx - 2, cy + 2, computer[i], computer, player))
                return true;
            else if(isJump(cx + 2, cy - 2, computer[i], computer, player))
                return true;
            else if(isJump(cx - 2, cy - 2, computer[i], computer, player))
                return true;
        }
        return false;
    }
    public boolean isEmpty(int sx, int sy, Piece[] computer, Piece[] player)
    {
        if(!inBounds(sx, sy))
            return false;
        for(int i = 0; i < player.length; i++)
            if(isPlayer(sx, sy, player))
                return false;
        for(int i = 0; i < computer.length; i++)
            if(isComputer(sx, sy, computer))
                return false;
        return true;
    }
    public boolean isPlayer(int sx, int sy, Piece[] player)
    {
        for(int i = 0; i < player.length; i++)
            if(player[i].getSqrX() == sx & player[i].getSqrY() == sy)
                return true;
        return false;
    }
    public boolean isComputer(int sx, int sy, Piece[] computer)
    {
        for(int i = 0; i < computer.length; i++)
            if(computer[i].getSqrX() == sx & computer[i].getSqrY() == sy)
                return true;
        return false;
    }
    public boolean isJump(int sx, int sy, Piece computer, Piece[] computerA, Piece[] player)
    {
        int cx = computer.getSqrX();
        int cy = computer.getSqrY();

        if(computer.isPlaying() & isEmpty(sx, sy, computerA, player ))
        {
            if(computer.isCrowned())
            {
                if(sx == cx + 2 & sy == cy + 2)
                    return isPlayer(cx + 1, cy + 1, player);
                if(sx == cx - 2 & sy == cy + 2)
                    return isPlayer(cx - 1, cy + 1, player);
                if(sx == cx + 2 & sy == cy - 2)
                    return isPlayer(cx + 1, cy - 1, player);
                if(sx == cx - 2 & sy == cy - 2)
                    return isPlayer(cx - 1, cy - 1, player);
            }
            else
            {
                if(sx == cx + 2 & sy == cy + 2)
                    return isPlayer(cx + 1, cy + 1, player);
                if(sx == cx - 2 & sy == cy + 2)
                    return isPlayer(cx - 1, cy + 1, player);
            }
        }
        return false;
    }
    public void jump(int sx, int sy, Piece computer, Piece[] player)
    {
        moveToBox(getJumped(sx,sy,computer,player));
        computer.moveToSqr(sx, sy);
    }
    public void moveToBox(Piece p)
    {
        Random r = new Random();
        p.setPlaying(false);
        p.moveTo(PWIDTH + BSIZE + r.nextInt(PWIDTH - TSIZE) + 1, PWIDTH + r.nextInt(PWIDTH - TSIZE));
    }
    public void randomMove(Piece[] computer, Piece[] player)
    {
        Random r = new Random();
        ArrayList<int[]> moves = new ArrayList<int[]>();
        for(int i = 0; i < computer.length; i++)
        {
            if(computer[i].isPlaying())
            {
                int x = computer[i].getSqrX();
                int y = computer[i].getSqrY();
                if(computer[i].isCrowned())
                {
                    if(isEmpty(x + 1, y + 1, computer, player))
                    {
                        int[] m = new int[3];
                        m[0] = i;
                        m[1] = x + 1;
                        m[2] = y + 1;
                        moves.add(m);
                    }
                    if(isEmpty(x - 1, y + 1, computer, player))
                    {
                        int[] m = new int[3];
                        m[0] = i;
                        m[1] = x - 1;
                        m[2] = y + 1;
                        moves.add(m);
                    }
                    if(isEmpty(x + 1, y - 1, computer, player))
                    {
                        int[] m = new int[3];
                        m[0] = i;
                        m[1] = x + 1;
                        m[2] = y - 1;
                        moves.add(m);
                    }
                    if(isEmpty(x - 1, y - 1, computer, player))
                    {
                        int[] m = new int[3];
                        m[0] = i;
                        m[1] = x - 1;
                        m[2] = y - 1;
                        moves.add(m);
                    }
                }
                else
                {
                    if(isEmpty(x + 1, y + 1, computer, player))
                    {
                        int[] m = new int[3];
                        m[0] = i;
                        m[1] = x + 1;
                        m[2] = y + 1;
                        moves.add(m);
                    }
                    if(isEmpty(x - 1, y + 1, computer, player))
                    {
                        int[] m = new int[3];
                        m[0] = i;
                        m[1] = x - 1;
                        m[2] = y + 1;
                        moves.add(m);
                    }
                }
            }
        }

        if(moves.size() != 0)
        {
            int i = r.nextInt(moves.size());
            computer[moves.get(i)[0]].moveToSqr(moves.get(i)[1], moves.get(i)[2]);
        }
    }
    public boolean inBounds(int sx, int sy)
    {
        if(sx < 8 & sx >= 0 & sy < 8 & sy >= 0)
            return true;
        return false;
    }
    public Piece getJumped(int sx, int sy, Piece computer, Piece[] player)
    {
        int cx = computer.getSqrX();
        int cy = computer.getSqrY();
        int dx = sx - cx;
        int dy = sy - cy;
        int px = cx + dx/2;
        int py = cy + dy/2;
        for(int i = 0; i < player.length; i++)
            if(player[i].getSqrX() == px & player[i].getSqrY() == py)
                return player[i];
        return null;
    }
    public void jumpMove(Piece[] computer, Piece[] player)
    {
        ArrayList<int[]> moves = new ArrayList<int[]>();

        if(lastActive == -1)
        {
            for(int i = 0; i < computer.length; i++)
            {
                int cx = computer[i].getSqrX();
                int cy = computer[i].getSqrY();

                if(isJump(cx + 2, cy + 2, computer[i], computer, player))
                {
                    int[] m = new int[3];
                    m[0] = i;
                    m[1] = cx + 2;
                    m[2] = cy + 2;
                    moves.add(m);
                }
                if(isJump(cx - 2, cy + 2, computer[i], computer, player))
                {
                    int[] m = new int[3];
                    m[0] = i;
                    m[1] = cx - 2;
                    m[2] = cy + 2;
                    moves.add(m);
                }
                if(isJump(cx + 2, cy - 2, computer[i], computer, player))
                {
                    int[] m = new int[3];
                    m[0] = i;
                    m[1] = cx + 2;
                    m[2] = cy - 2;
                    moves.add(m);
                }
                if(isJump(cx - 2, cy - 2, computer[i], computer, player))
                {
                    int[] m = new int[3];
                    m[0] = i;
                    m[1] = cx - 2;
                    m[2] = cy - 2;
                    moves.add(m);
                }
            }
        }
        else
        {
            int cx = computer[lastActive].getSqrX();
            int cy = computer[lastActive].getSqrY();

            if(isJump(cx + 2, cy + 2, computer[lastActive], computer, player))
            {
                int[] m = new int[3];
                m[0] = lastActive;
                m[1] = cx + 2;
                m[2] = cy + 2;
                moves.add(m);
            }
            if(isJump(cx - 2, cy + 2, computer[lastActive], computer, player))
            {
                int[] m = new int[3];
                m[0] = lastActive;
                m[1] = cx - 2;
                m[2] = cy + 2;
                moves.add(m);
            }
            if(isJump(cx + 2, cy - 2, computer[lastActive], computer, player))
            {
                int[] m = new int[3];
                m[0] = lastActive;
                m[1] = cx + 2;
                m[2] = cy - 2;
                moves.add(m);
            }
            if(isJump(cx - 2, cy - 2, computer[lastActive], computer, player))
            {
                int[] m = new int[3];
                m[0] = lastActive;
                m[1] = cx - 2;
                m[2] = cy - 2;
                moves.add(m);
            }
        }


        if(moves.size() != 0)
        {
            int i = r.nextInt(moves.size());
            lastActive = moves.get(i)[0];
            jump(moves.get(i)[1], moves.get(i)[2], computer[moves.get(i)[0]], player);
        }
    }
}
