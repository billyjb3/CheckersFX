import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.application.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Game extends Application implements GameConstants
{
    private static Group root = new Group();
    private static AI ai = new AI();
    private static PlayerMover mover = new PlayerMover();

    private static Image back = new Image("/background.jpg");
    private static Image crown = new Image("/crown.png");
    private static Tile[] tiles = new Tile[64];
    private static Tile[] blackTiles = new Tile[32];
    private static Tile[] whiteTiles = new Tile[32];
    private static Piece[] blackPieces = new Piece[12];
    private static Piece[] whitePieces = new Piece[12];

    private static Button startB = new Button("Start");
    private static Button blackB = new Button("Black");
    private static Button whiteB = new Button("White");
    private static Button computerB = new Button("Computer");
    private static Button player2B = new Button("Player 2");
    private static Button giveupB = new Button("Give up");
    private static Button giveup2B = new Button("Give up");
    private static Button newGameB = new Button("New Game");
    private static Button rematchB = new Button("Rematch");
    private static Text checkersT = new Text("Checkers");
    private static Text chooseColorT = new Text("Choose Color");
    private static Text choseOpponentT = new Text("Choose Opponent");
    private static Text player1T = new Text("Player 1");
    private static Text player2T = new Text();
    private static Text victorT = new Text();

    private static Canvas canvas = new Canvas(FWIDTH, FHEIGHT);
    private static GraphicsContext gc = canvas.getGraphicsContext2D();
    private static PixelWriter cwriter = gc.getPixelWriter();
    private static PixelReader crownReader = crown.getPixelReader();

    private static boolean playing = false;
    private static boolean playersTurn = true;
    private static boolean waiting = false;
    private static boolean twoPlayers;

    private static int playerColor = BLACK;
    private static int waitSelected;

    public void start(Stage stage) throws Exception
    {
        stage.setTitle("Checkerssss");
        stage.setResizable(false);
        stage.setHeight(FHEIGHT);
        stage.setWidth(FWIDTH);
        Scene s = new Scene(root);
        stage.setScene(s);
        drawStart();

        new AnimationTimer()
        {
            public void handle(long arg0)
            {
                initNodes();
                if(playing)
                {
                    update();
                    render();
                }
            }
        }.start();

        stage.show();
    }
    public static void main(String[] args)
    {
        initNodes();
        initPieces();
        initTiles();
        launch(args);
    }

    public static void initPieces()
    {
        Random r = new Random();

        if(blackPieces[0] == null)
        {
            for(int i = 0; i < 12; i++)
                blackPieces[i] = new Piece(BLACK,r.nextInt(PWIDTH - TSIZE), PHEIGHT + r.nextInt(PHEIGHT - TSIZE));
            for(int i = 0; i < 12; i++)
                whitePieces[i] = new Piece(WHITE, PWIDTH + BSIZE + r.nextInt(PWIDTH - TSIZE), PHEIGHT + r.nextInt(PHEIGHT - TSIZE));
        }
        else
        {
            for(int i = 0; i < 12; i++)
                blackPieces[i].setPos(r.nextInt(PWIDTH - TSIZE), PHEIGHT + r.nextInt(PHEIGHT - TSIZE));
            for(int i = 0; i < 12; i++)
                whitePieces[i].setPos(PWIDTH + BSIZE + r.nextInt(PWIDTH - TSIZE), PHEIGHT + r.nextInt(PHEIGHT - TSIZE));
        }
    }
    public static void initTiles()
    {
        int sx = FWIDTH/2 - BSIZE/2;
        int color = WHITE;

        for(int r = 0; r < 8; r++)
        {
            for(int x = 0; x < 8; x++)
            {
                tiles[r*8 + x] = new Tile(color, sx + x*TSIZE, r*TSIZE + 20);
                if(color == WHITE)
                    color = BLACK;
                else
                    color = WHITE;
            }
            if(color == WHITE)
                color = BLACK;
            else
                color = WHITE;
        }

        boolean b = true;
        int black = 0;
        int white = 0;
        for(int i = 0; i < 64; i++)
        {
            if(b)
            {
                whiteTiles[white++] = tiles[i];
                if((i+1)%8 != 0)
                    b = !b;
            }
            else
            {
                blackTiles[black++] = tiles[i];
                if((i+1)%8 != 0)
                    b = !b;
            }
        }
    }
    public static void initNodes()
    {
        //start
        startB.setLayoutX(FHALF - startB.getWidth()/2);
        startB.setLayoutY(FHEIGHT/2 + TSIZE);
        checkersT.setFont(Font.font(200));
        checkersT.setX(FHALF - checkersT.getLayoutBounds().getWidth()/2);
        checkersT.setY(250);

        //choose color
        blackB.setLayoutX((FWIDTH-BSIZE)/4 - blackB.getWidth());
        blackB.setLayoutY(FHEIGHT/2 - TSIZE);
        whiteB.setLayoutX(FWIDTH - blackB.getLayoutX() - whiteB.getWidth());
        whiteB.setLayoutY(blackB.getLayoutY());
        chooseColorT.setFont(Font.font(100));
        chooseColorT.setLayoutX(FWIDTH/2 - chooseColorT.getLayoutBounds().getWidth()/2);
        chooseColorT.setLayoutY(250);

        //choose opponent
        computerB.setLayoutX(FHALF - computerB.getWidth() - 25);
        computerB.setLayoutY(FHEIGHT/2);
        player2B.setLayoutX(FHALF + 25);
        player2B.setLayoutY(FHEIGHT/2);
        choseOpponentT.setFont(Font.font(100));
        choseOpponentT.setX(FHALF - choseOpponentT.getLayoutBounds().getWidth()/2);
        choseOpponentT.setY(250);

        //in-game
        if(twoPlayers)
            player2T.setText("Player 2");
        else
            player2T.setText("Player C");
        player1T.setFont(Font.font(50));
        player2T.setFont(Font.font(50));
        player1T.setX((FWIDTH-BSIZE)/4 - player1T.getLayoutBounds().getWidth()/2);
        player1T.setY(50);
        player2T.setX(FWIDTH - player1T.getX() - player1T.getLayoutBounds().getWidth());
        player2T.setY(50);
        giveupB.setLayoutX(PWIDTH/2 - giveupB.getWidth()/2);
        giveupB.setLayoutY(75);
        giveup2B.setLayoutX(FWIDTH - PWIDTH/2 - giveup2B.getWidth()/2);
        giveup2B.setLayoutY(75);

        //end-game
        victorT.setFont(Font.font(150));
        victorT.setFill(Color.BLUE);
        victorT.setX(FHALF - victorT.getLayoutBounds().getWidth()/2);
        victorT.setY(FHEIGHT/2 - victorT.getLayoutBounds().getHeight()/2);
        newGameB.setLayoutX(FHALF - newGameB.getWidth() - 25);
        newGameB.setLayoutY(FHEIGHT/2);
        rematchB.setLayoutX(FHALF + 25);
        rematchB.setLayoutY(FHEIGHT/2);
    }

    public static void drawBoard()
    {
        for(int i = 0; i < 64; i++)
            gc.drawImage(tiles[i].getImage(), tiles[i].getX(), tiles[i].getY());
    }
    public static void drawPieces()
    {
        crownCheck();
        for(int i = 0; i < 12; i++)
            for(int x = 0; x < TSIZE; x++)
                for(int y = 0; y < TSIZE; y++)
                {
                    if(blackPieces[i].getPixelColor(0,0) != blackPieces[i].getPixelColor(x,y))
                        cwriter.setArgb(blackPieces[i].getX() + x, blackPieces[i].getY() + y, blackPieces[i].getPixelColor(x,y));
                    if(blackPieces[i].isCrowned())
                        if(crownReader.getArgb(0, 0) != crownReader.getArgb(x,y))
                            cwriter.setArgb(blackPieces[i].getX() + x, blackPieces[i].getY() + y, crownReader.getArgb(x, y));
                }
        for(int i = 0; i < 12; i++)
            for(int x = 0; x < TSIZE; x++)
                for(int y = 0; y < TSIZE; y++)
                {
                    if(whitePieces[i].getPixelColor(0,0) != whitePieces[i].getPixelColor(x,y))
                        cwriter.setArgb(whitePieces[i].getX() + x, whitePieces[i].getY() + y, whitePieces[i].getPixelColor(x,y));
                    if(whitePieces[i].isCrowned())
                        if(crownReader.getArgb(0, 0) != crownReader.getArgb(x,y))
                            cwriter.setArgb(whitePieces[i].getX() + x, whitePieces[i].getY() + y, crownReader.getArgb(x, y));
                }
    }
    public static void drawStart()
    {
        root.getChildren().removeAll(root.getChildren());
        gc.clearRect(0, 0, FWIDTH, FHEIGHT);
        gc.drawImage(back, 0, 0);
        root.getChildren().addAll(canvas, checkersT, startB);
        drawPieces();

        startB.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                root.getChildren().removeAll(checkersT, startB);
                gc.clearRect(0,0,FWIDTH,FHEIGHT);
                gc.drawImage(back, 0, 0);
                drawPieces();
                root.getChildren().addAll(chooseColorT, blackB, whiteB);
            }
        });
        blackB.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent e)
            {
                root.getChildren().removeAll(blackB, whiteB, chooseColorT);
                root.getChildren().addAll(choseOpponentT, player2B, computerB);
                playerColor = BLACK;
            }
        });
        whiteB.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent e)
            {
                root.getChildren().removeAll(blackB, whiteB, chooseColorT);
                root.getChildren().addAll(choseOpponentT, player2B, computerB);
                playerColor = WHITE;
            }
        });
        computerB.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent e)
            {
                root.getChildren().removeAll(choseOpponentT, player2B, computerB);
                twoPlayers = false;
                playing = true;
                mover.initRectangles(blackTiles);
                root.getChildren().addAll(player1T, player2T, giveupB);
                setPieces();
            }
        });
        player2B.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent e)
            {
                root.getChildren().removeAll(choseOpponentT, player2B, computerB);
                twoPlayers = true;
                playing = true;
                mover.initRectangles(blackTiles);
                root.getChildren().addAll(player1T, player2T, giveupB, giveup2B);
                setPieces();
            }
        });
    }
    public static void drawEndScreen(int status)
    {
        root.getChildren().removeAll(giveupB, giveup2B);

        if(twoPlayers)
        {
            if(status == WIN)
                victorT.setText("Player 1 Wins!!");
            else
                victorT.setText("Player 2 Wins!!");
        }
        else
        {
            if(status == WIN)
                victorT.setText("YOU WON!!");
            else
                victorT.setText("YOU LOST!!");
        }

        root.getChildren().addAll(newGameB, rematchB, victorT);

        newGameB.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent e)
            {
                initPieces();
                drawStart();
            }
        });
        rematchB.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent e)
            {
                root.getChildren().removeAll(newGameB, rematchB, victorT);
                if(twoPlayers)
                    root.getChildren().addAll(giveupB, giveup2B);
                else
                    root.getChildren().add(giveupB);
                setPieces();
                playing = true;
            }
        });
    }

    public static void render()
    {
        gc.clearRect(0,0,FWIDTH,FHEIGHT);
        gc.drawImage(back, 0, 0);
        drawBoard();
        drawPieces();
    }
    public static void update()
    {
        giveupB.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent e)
            {
                playing = false;
                drawEndScreen(LOSE);
            }
        });
        giveup2B.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent e)
            {
                playing = false;
                drawEndScreen(WIN);
            }
        });
        for(int i = 0; i < 24; i++)
        {
            if(i < 12)
            {
                if(blackPieces[i].isMoving())
                    blackPieces[i].update();
            }
            else
            if(whitePieces[i - 12].isMoving())
                whitePieces[i - 12].update();
        }

        if(isReady() & !playersTurn & !twoPlayers)
        {
            if(playerColor == BLACK)
            {
                ai.update(whitePieces, blackPieces);
            }
            else
            {
                ai.update(blackPieces, whitePieces);
            }

        }
        if(waiting & isReady())
        {
            waiting = false;
            if(isPlayersTurn())
            {
                if(playerColor == BLACK)
                    mover.update(waitSelected, blackPieces, whitePieces);
                else
                    mover.update(waitSelected, whitePieces, blackPieces);
            }
            else if(!isPlayersTurn())
            {
                if(playerColor == BLACK)
                    mover.update(waitSelected, whitePieces, blackPieces);
                else
                    mover.update(waitSelected, blackPieces, whitePieces);
            }

        }
        if(isReady())
            winnerCheck();
    }

    public static void selectSquare(int i)
    {
        if(playersTurn & isReady())
        {
            if(playerColor == BLACK)
                mover.update(i, blackPieces, whitePieces);
            else
                mover.update(i, whitePieces, blackPieces);
        }
        else if(!playersTurn & isReady())
        {
            if(playerColor == BLACK)
                mover.update(i, whitePieces, blackPieces);
            else
                mover.update(i, blackPieces, whitePieces);
        }
    }
    public static void setPieces()
    {
        for(int i = 0; i < 24; i++)
        {
            if(playerColor == BLACK)
            {
                if(i < 12)
                {
                    blackPieces[i].setCrowned(false);
                    blackPieces[i].setPlaying(true);
                    blackPieces[i].moveTo(blackTiles[31 - i].getX(), blackTiles[31 - i].getY());
                }
                else
                {
                    whitePieces[i - 12].setCrowned(false);
                    whitePieces[i - 12].setPlaying(true);
                    whitePieces[i - 12].moveTo(blackTiles[i - 12].getX(), blackTiles[i - 12].getY());
                }
            }
            else
            {
                if(i < 12)
                {
                    whitePieces[i].setPlaying(true);
                    whitePieces[i].setCrowned(false);
                    whitePieces[i].moveTo(blackTiles[31 - i].getX(), blackTiles[31 - i].getY());
                }
                else
                {
                    blackPieces[i - 12].setPlaying(true);
                    blackPieces[i - 12].setCrowned(false);
                    blackPieces[i - 12].moveTo(blackTiles[i - 12].getX(), blackTiles[i - 12].getY());
                }
            }
        }

    }
    public static void setPlayersTurn(boolean b)
    {
        playersTurn = b;
    }
    public static void addRect(MyRectangle r)
    {
        root.getChildren().add(r);
    }
    public static void crownCheck()
    {
        if(playerColor == BLACK)
        {
            for(int i = 0; i < 12; i++)
            {
                if(blackPieces[i].getY() == 20)
                {
                    int x = blackPieces[i].getX();
                    if(x == FHALF - TSIZE*3 | x == FHALF - TSIZE | x == FHALF + TSIZE | x == FHALF + TSIZE*3)
                        blackPieces[i].setCrowned(true);
                }
            }
            for(int i = 0; i < 12; i++)
            {
                if(whitePieces[i].getY() == TSIZE*7 + 20)
                {
                    int x = whitePieces[i].getX();
                    if(x == FHALF - TSIZE*4 | x == FHALF - TSIZE*2 | x == FHALF | x == FHALF + TSIZE*2)
                        whitePieces[i].setCrowned(true);
                }
            }
        }
        else
        {
            for(int i = 0; i < 12; i++)
            {
                if(whitePieces[i].getY() == 20)
                {
                    int x = whitePieces[i].getX();
                    if(x == FHALF - TSIZE*3 | x == FHALF - TSIZE | x == FHALF + TSIZE | x == FHALF + TSIZE*3)
                        whitePieces[i].setCrowned(true);
                }
            }
            for(int i = 0; i < 12; i++)
            {
                if(blackPieces[i].getY() == TSIZE*7 + 20)
                {
                    int x = blackPieces[i].getX();
                    if(x == FHALF - TSIZE*4 | x == FHALF - TSIZE*2 | x == FHALF | x == FHALF + TSIZE*2)
                        blackPieces[i].setCrowned(true);
                }
            }
        }
    }
    public static void waiting(int s)
    {
        waiting = true;
        waitSelected = s;
    }
    public static void winnerCheck()
    {
        int b = 0;
        int w = 0;
        for(int i = 0; i < 12; i++)
            if(!blackPieces[i].isPlaying())
                b += 1;
        for(int i = 0; i < 12; i++)
            if(!whitePieces[i].isPlaying())
                w += 1;

        if(b == 12)
        {
            playing = false;
            if(playerColor == BLACK)
            {
                drawEndScreen(LOSE);
            }
            else
            {
                drawEndScreen(WIN);
            }
        }
        else if(w == 12)
        {
            playing = false;
            if(playerColor == BLACK)
            {
                drawEndScreen(WIN);
            }
            else
            {
                drawEndScreen(LOSE);
            }
        }
    }

    public static boolean isReady()
    {
        for(int i = 0; i < 12; i++)
        {
            if(blackPieces[i].isMoving())
                return false;
            if(whitePieces[i].isMoving())
                return false;
        }
        return true;
    }
    public static boolean isStarted()
    {
        return playing;
    }
    public static boolean isPlayersTurn()
    {
        return playersTurn;
    }
    public static int getPlayerColor()
    {
        return playerColor;
    }
}

	