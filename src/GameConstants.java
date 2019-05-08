
public interface GameConstants
{
    int FWIDTH = 1250;
    int FHEIGHT = FWIDTH/16*9;
    int FHALF = FWIDTH/2;
    int TSIZE = 80;
    int BSIZE = TSIZE*8;
    int BHALF = BSIZE/2;
    int PWIDTH = (FWIDTH - BSIZE)/2;
    int PHEIGHT = FHEIGHT/2;

    int BLACK = 0;
    int WHITE = 1;
    double SPEED = 500;
    int LOSE = 0;
    int WIN = 1;
}
