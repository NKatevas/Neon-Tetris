package com.example.neontetris;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;


public class Game extends Application {

    // Constants
    public static final int MOVE_SPEED = 30;
    public static final int BLOCK_SIZE = 30;
    public static final int BOARD_X = BLOCK_SIZE * 12; // X SIZE of the board
    public static final int BOARD_Y = BLOCK_SIZE * 24; // Y SIZE of the board

    // Screen Variables
    private static Pane group = new Pane();
    private static Scene scene = new Scene(group, BOARD_X, BOARD_Y + 60);

    // Logic Variables
    public static int[][] grid = new int[BOARD_X/BLOCK_SIZE][BOARD_Y/BLOCK_SIZE];
    private static Form form;
    public static int score = 0;
    private static Form nextObj = Controller.makeRect();
    private static int lineScore = 0;
    private static int top = 0;
    private static boolean game = true;

    // Four Line score (Tetris) event variables
    private static int tTimer = 0;
    private static boolean tDisplay = false;
    private static Text tText;

    // variabes for animation
    private static ArrayList<Rectangle> animatedLines = new ArrayList<>();
    private static int aTimer = 0;


    @Override
    public void start(Stage stage) throws Exception {
        for (int[] a : grid) {
            Arrays.fill(a, 0);
        }

        // Create text "SCORE"
        Text scoretext = new Text("SCORE: ");
        scoretext.setStyle("-fx-font: 20 bahnschrift;");
        scoretext.setY(BOARD_Y + 35);
        scoretext.setX(235);
        scoretext.setFill(Color.SILVER);

        // Create text "LINES"
        Text level = new Text("LINES: ");
        level.setStyle("-fx-font: 20 bahnschrift;");
        level.setY(BOARD_Y + 35);
        level.setX(35);
        level.setFill(Color.GOLD);

        // Line of the Border of the Gamefield
        Line line = new Line(0, BOARD_Y, BOARD_X, BOARD_Y);
        line.setStyle("-fx-stroke: white;");


        // First Object Creation
        Form startObj = nextObj;
        group.getChildren().addAll(scoretext, line, level);
        group.getChildren().addAll(startObj.a, startObj.b, startObj.c, startObj.d);
        moveOnKeyPress(startObj);
        form = startObj;
        nextObj = Controller.makeRect();

        // Create scene and show
        scene.setFill(Color.BLACK);
        stage.setScene(scene);
        stage.setTitle("NEON TETRIS");
        stage.show();

        // Game Cycle
        Timer fall = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                Platform.runLater(new Runnable() {
                    public void run() {
                        if (form.a.getY() == 0 || form.b.getY() == 0 || form.c.getY() == 0
                                || form.d.getY() == 0)
                            top++;
                        else
                            top = 0;


                        // If a Tetris is achieved
                        if (tTimer > 0 && tTimer < 10) {
                            if (!tDisplay) {
                                tText = new Text("TETRIS!");
                                tText.setFill(Color.TURQUOISE);
                                tText.setStyle("-fx-font: 75 bahnschrift;");
                                tText.setY(150);
                                tText.setX(50);
                                group.getChildren().add(tText);
                                tDisplay = true;
                            }
                            tTimer++;
                        }
                        else if (tTimer >= 10)
                        {
                           tTimer = 0;
                           group.getChildren().remove(tText);
                           tDisplay = false;
                        }


                        // animation of lines
                        if (aTimer > 0) {
                            aTimer++;
                            if (aTimer > 2) {
                                for (Rectangle rect: animatedLines) {
                                    group.getChildren().remove(rect);
                                }
                                aTimer = 0;
                            }
                        }

                        if (top == 2) {
                            // GAME OVER
                            Text over = new Text("GAME OVER");
                            over.setFill(Color.RED);
                            over.setStyle("-fx-font: 65 bahnschrift;");
                            over.setY(150);
                            over.setX(0);
                            group.getChildren().add(over);
                            game = false;
                        }
                        // Exit
                         else if (top == 15) {
                            System.exit(0);
                        }

                        if (game) {
                            MoveDown(form);
                            scoretext.setText("SCORE: " + Integer.toString(score));
                            level.setText("LINES: " + Integer.toString(lineScore));
                        }
                    }
                });
            }
        };
        fall.schedule(task, 0, 300);
    }

    private void moveOnKeyPress(Form form) {
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case RIGHT:
                        Controller.MoveRight(form);
                        break;
                    case DOWN:
                        MoveDown(form);
                        score++;
                        break;
                    case LEFT:
                        Controller.MoveLeft(form);
                        break;
                    case UP:
                        MoveTurn(form);
                        break;
                }
            }
        });
    }



    private void RemoveRows(Pane pane) {
        ArrayList<Node> rects = new ArrayList<Node>();
        ArrayList<Integer> lines = new ArrayList<Integer>();
        ArrayList<Node> newrects = new ArrayList<Node>();

        // List for Animation
        ArrayList<Integer> aLines = new ArrayList<>();

        int full = 0;
        for (int i = 0; i < grid[0].length; i++) {
            for (int j = 0; j < grid.length; j++) {
                if (grid[j][i] == 1)
                    full++;
            }
            if (full == grid.length) {
                lines.add(i);

                aLines.add(i);
            }

            full = 0;
        }



        //remove Lines
        if (lines.size() > 0)
            do {
                for (Node node : pane.getChildren()) {
                    if (node instanceof Rectangle)
                        rects.add(node);
                }

                // advanced point scoring:
                score += 100;
                lineScore++;
                if(lines.size() == 2) { // Two lines at once: 250p
                    score += 50;
                } else if(lines.size() == 3) { // Three lines at once: 500p
                    score += 150;
                } else if(lines.size() == 4) { // Four lines at once (Tetris): 1000p
                    score += 400;
                    tTimer = 1;
                }

                for (Node node : rects) {
                    Rectangle a = (Rectangle) node;
                    if (a.getY() == lines.get(0) * BLOCK_SIZE) {
                        grid[(int) a.getX() / BLOCK_SIZE][(int) a.getY() / BLOCK_SIZE] = 0;
                        pane.getChildren().remove(node);
                    } else
                        newrects.add(node);
                }

                for (Node node : newrects) {
                    Rectangle a = (Rectangle) node;
                    if (a.getY() < lines.get(0) * BLOCK_SIZE) {
                        grid[(int) a.getX() / BLOCK_SIZE][(int) a.getY() / BLOCK_SIZE] = 0;
                        a.setY(a.getY() + BLOCK_SIZE);
                    }
                }
                lines.remove(0);
                rects.clear();
                newrects.clear();
                for (Node node : pane.getChildren()) {
                    if (node instanceof Rectangle)
                        rects.add(node);
                }
                for (Node node : rects) {
                    Rectangle a = (Rectangle) node;
                    try {
                        grid[(int) a.getX() / BLOCK_SIZE][(int) a.getY() / BLOCK_SIZE] = 1;
                    } catch (ArrayIndexOutOfBoundsException e) {
                    }
                }
                rects.clear();
            } while (lines.size() > 0);

        // animate Lines
        animateLine(aLines);
    }



    // Movement of Singular cubes/object parts
    private void MoveDown(Rectangle rect) {
        if (rect.getY() + MOVE_SPEED < BOARD_Y)
            rect.setY(rect.getY() + MOVE_SPEED);

    }

    private void MoveRight(Rectangle rect) {
        if (rect.getX() + MOVE_SPEED <= BOARD_X - BLOCK_SIZE)
            rect.setX(rect.getX() + MOVE_SPEED);
    }

    private void MoveLeft(Rectangle rect) {
        if (rect.getX() - MOVE_SPEED >= 0)
            rect.setX(rect.getX() - MOVE_SPEED);
    }

    private void MoveUp(Rectangle rect) {
        if (rect.getY() - MOVE_SPEED > 0)
            rect.setY(rect.getY() - MOVE_SPEED);
    }


    // Movement of objects
    private void MoveDown(Form form) {
        // if any of the form pieces are at the bottom or the spaces of each of the pieces directly below are occupied:
        if (form.a.getY() == BOARD_Y - BLOCK_SIZE || form.b.getY() == BOARD_Y - BLOCK_SIZE || form.c.getY() == BOARD_Y - BLOCK_SIZE
                || form.d.getY() == BOARD_Y - BLOCK_SIZE || moveA(form) || moveB(form) || moveC(form) || moveD(form)) {

            // Write down the resting position of the form to the grid.
            grid[(int) form.a.getX() / BLOCK_SIZE][(int) form.a.getY() / BLOCK_SIZE] = 1;
            grid[(int) form.b.getX() / BLOCK_SIZE][(int) form.b.getY() / BLOCK_SIZE] = 1;
            grid[(int) form.c.getX() / BLOCK_SIZE][(int) form.c.getY() / BLOCK_SIZE] = 1;
            grid[(int) form.d.getX() / BLOCK_SIZE][(int) form.d.getY() / BLOCK_SIZE] = 1;

            // Check if a row is filled and needs to be removed
            RemoveRows(group);

            // create the next form
            Form a = nextObj;
            nextObj = Controller.makeRect();
            this.form = a;
            group.getChildren().addAll(a.a, a.b, a.c, a.d);
            moveOnKeyPress(a);
        }

        if (form.a.getY() + MOVE_SPEED < BOARD_Y && form.b.getY() + MOVE_SPEED < BOARD_Y && form.c.getY() + MOVE_SPEED < BOARD_Y
                && form.d.getY() + MOVE_SPEED < BOARD_Y) {
            int movea = grid[(int) form.a.getX() / BLOCK_SIZE][((int) form.a.getY() / BLOCK_SIZE) + 1];
            int moveb = grid[(int) form.b.getX() / BLOCK_SIZE][((int) form.b.getY() / BLOCK_SIZE) + 1];
            int movec = grid[(int) form.c.getX() / BLOCK_SIZE][((int) form.c.getY() / BLOCK_SIZE) + 1];
            int moved = grid[(int) form.d.getX() / BLOCK_SIZE][((int) form.d.getY() / BLOCK_SIZE) + 1];
            if (movea == 0 && moveb == 0 && movec == 0 && moved == 0) {
                form.a.setY(form.a.getY() + MOVE_SPEED);
                form.b.setY(form.b.getY() + MOVE_SPEED);
                form.c.setY(form.c.getY() + MOVE_SPEED);
                form.d.setY(form.d.getY() + MOVE_SPEED);
            }
        }
    }


    private boolean moveA(Form form) {
        return (grid[(int) form.a.getX() / BLOCK_SIZE][((int) form.a.getY() / BLOCK_SIZE) + 1] == 1);
    }

    private boolean moveB(Form form) {
        return (grid[(int) form.b.getX() / BLOCK_SIZE][((int) form.b.getY() / BLOCK_SIZE) + 1] == 1);
    }

    private boolean moveC(Form form) {
        return (grid[(int) form.c.getX() / BLOCK_SIZE][((int) form.c.getY() / BLOCK_SIZE) + 1] == 1);
    }

    private boolean moveD(Form form) {
        return (grid[(int) form.d.getX() / BLOCK_SIZE][((int) form.d.getY() / BLOCK_SIZE) + 1] == 1);
    }



    // Turn Method. Every Rotation of every piece is hardcoded with singular movement of each block.
    private void MoveTurn(Form form) {
        int f = form.form;
        Rectangle a = form.a;
        Rectangle b = form.b;
        Rectangle c = form.c;
        Rectangle d = form.d;
        switch (form.getName()) {
            case "j":
                if (f == 1 && cB(a, 1, -1) && cB(c, -1, -1) && cB(d, -2, -2)) {
                    MoveRight(form.a);
                    MoveDown(form.a);
                    MoveDown(form.c);
                    MoveLeft(form.c);
                    MoveDown(form.d);
                    MoveDown(form.d);
                    MoveLeft(form.d);
                    MoveLeft(form.d);
                    form.changeForm();
                    break;
                }
                if (f == 2 && cB(a, -1, -1) && cB(c, -1, 1) && cB(d, -2, 2)) {
                    MoveDown(form.a);
                    MoveLeft(form.a);
                    MoveLeft(form.c);
                    MoveUp(form.c);
                    MoveLeft(form.d);
                    MoveLeft(form.d);
                    MoveUp(form.d);
                    MoveUp(form.d);
                    form.changeForm();
                    break;
                }
                if (f == 3 && cB(a, -1, 1) && cB(c, 1, 1) && cB(d, 2, 2)) {
                    MoveLeft(form.a);
                    MoveUp(form.a);
                    MoveUp(form.c);
                    MoveRight(form.c);
                    MoveUp(form.d);
                    MoveUp(form.d);
                    MoveRight(form.d);
                    MoveRight(form.d);
                    form.changeForm();
                    break;
                }
                if (f == 4 && cB(a, 1, 1) && cB(c, 1, -1) && cB(d, 2, -2)) {
                    MoveUp(form.a);
                    MoveRight(form.a);
                    MoveRight(form.c);
                    MoveDown(form.c);
                    MoveRight(form.d);
                    MoveRight(form.d);
                    MoveDown(form.d);
                    MoveDown(form.d);
                    form.changeForm();
                    break;
                }
                break;
            case "l":
                if (f == 1 && cB(a, 1, -1) && cB(c, 1, 1) && cB(b, 2, 2)) {
                    MoveRight(form.a);
                    MoveDown(form.a);
                    MoveUp(form.c);
                    MoveRight(form.c);
                    MoveUp(form.b);
                    MoveUp(form.b);
                    MoveRight(form.b);
                    MoveRight(form.b);
                    form.changeForm();
                    break;
                }
                if (f == 2 && cB(a, -1, -1) && cB(b, 2, -2) && cB(c, 1, -1)) {
                    MoveDown(form.a);
                    MoveLeft(form.a);
                    MoveRight(form.b);
                    MoveRight(form.b);
                    MoveDown(form.b);
                    MoveDown(form.b);
                    MoveRight(form.c);
                    MoveDown(form.c);
                    form.changeForm();
                    break;
                }
                if (f == 3 && cB(a, -1, 1) && cB(c, -1, -1) && cB(b, -2, -2)) {
                    MoveLeft(form.a);
                    MoveUp(form.a);
                    MoveDown(form.c);
                    MoveLeft(form.c);
                    MoveDown(form.b);
                    MoveDown(form.b);
                    MoveLeft(form.b);
                    MoveLeft(form.b);
                    form.changeForm();
                    break;
                }
                if (f == 4 && cB(a, 1, 1) && cB(b, -2, 2) && cB(c, -1, 1)) {
                    MoveUp(form.a);
                    MoveRight(form.a);
                    MoveLeft(form.b);
                    MoveLeft(form.b);
                    MoveUp(form.b);
                    MoveUp(form.b);
                    MoveLeft(form.c);
                    MoveUp(form.c);
                    form.changeForm();
                    break;
                }
                break;
            case "o":
                break;
            case "s":
                if (f == 1 && cB(a, -1, -1) && cB(c, -1, 1) && cB(d, 0, 2)) {
                    MoveDown(form.a);
                    MoveLeft(form.a);
                    MoveLeft(form.c);
                    MoveUp(form.c);
                    MoveUp(form.d);
                    MoveUp(form.d);
                    form.changeForm();
                    break;
                }
                if (f == 2 && cB(a, 1, 1) && cB(c, 1, -1) && cB(d, 0, -2)) {
                    MoveUp(form.a);
                    MoveRight(form.a);
                    MoveRight(form.c);
                    MoveDown(form.c);
                    MoveDown(form.d);
                    MoveDown(form.d);
                    form.changeForm();
                    break;
                }
                if (f == 3 && cB(a, -1, -1) && cB(c, -1, 1) && cB(d, 0, 2)) {
                    MoveDown(form.a);
                    MoveLeft(form.a);
                    MoveLeft(form.c);
                    MoveUp(form.c);
                    MoveUp(form.d);
                    MoveUp(form.d);
                    form.changeForm();
                    break;
                }
                if (f == 4 && cB(a, 1, 1) && cB(c, 1, -1) && cB(d, 0, -2)) {
                    MoveUp(form.a);
                    MoveRight(form.a);
                    MoveRight(form.c);
                    MoveDown(form.c);
                    MoveDown(form.d);
                    MoveDown(form.d);
                    form.changeForm();
                    break;
                }
                break;
            case "t":
                if (f == 1 && cB(a, 1, 1) && cB(d, -1, -1) && cB(c, -1, 1)) {
                    MoveUp(form.a);
                    MoveRight(form.a);
                    MoveDown(form.d);
                    MoveLeft(form.d);
                    MoveLeft(form.c);
                    MoveUp(form.c);
                    form.changeForm();
                    break;
                }
                if (f == 2 && cB(a, 1, -1) && cB(d, -1, 1) && cB(c, 1, 1)) {
                    MoveRight(form.a);
                    MoveDown(form.a);
                    MoveLeft(form.d);
                    MoveUp(form.d);
                    MoveUp(form.c);
                    MoveRight(form.c);
                    form.changeForm();
                    break;
                }
                if (f == 3 && cB(a, -1, -1) && cB(d, 1, 1) && cB(c, 1, -1)) {
                    MoveDown(form.a);
                    MoveLeft(form.a);
                    MoveUp(form.d);
                    MoveRight(form.d);
                    MoveRight(form.c);
                    MoveDown(form.c);
                    form.changeForm();
                    break;
                }
                if (f == 4 && cB(a, -1, 1) && cB(d, 1, -1) && cB(c, -1, -1)) {
                    MoveLeft(form.a);
                    MoveUp(form.a);
                    MoveRight(form.d);
                    MoveDown(form.d);
                    MoveDown(form.c);
                    MoveLeft(form.c);
                    form.changeForm();
                    break;
                }
                break;
            case "z":
                if (f == 1 && cB(b, 1, 1) && cB(c, -1, 1) && cB(d, -2, 0)) {
                    MoveUp(form.b);
                    MoveRight(form.b);
                    MoveLeft(form.c);
                    MoveUp(form.c);
                    MoveLeft(form.d);
                    MoveLeft(form.d);
                    form.changeForm();
                    break;
                }
                if (f == 2 && cB(b, -1, -1) && cB(c, 1, -1) && cB(d, 2, 0)) {
                    MoveDown(form.b);
                    MoveLeft(form.b);
                    MoveRight(form.c);
                    MoveDown(form.c);
                    MoveRight(form.d);
                    MoveRight(form.d);
                    form.changeForm();
                    break;
                }
                if (f == 3 && cB(b, 1, 1) && cB(c, -1, 1) && cB(d, -2, 0)) {
                    MoveUp(form.b);
                    MoveRight(form.b);
                    MoveLeft(form.c);
                    MoveUp(form.c);
                    MoveLeft(form.d);
                    MoveLeft(form.d);
                    form.changeForm();
                    break;
                }
                if (f == 4 && cB(b, -1, -1) && cB(c, 1, -1) && cB(d, 2, 0)) {
                    MoveDown(form.b);
                    MoveLeft(form.b);
                    MoveRight(form.c);
                    MoveDown(form.c);
                    MoveRight(form.d);
                    MoveRight(form.d);
                    form.changeForm();
                    break;
                }
                break;
            case "i":
                if (f == 1 && cB(a, 2, 2) && cB(b, 1, 1) && cB(d, -1, -1)) {
                    MoveUp(form.a);
                    MoveUp(form.a);
                    MoveRight(form.a);
                    MoveRight(form.a);
                    MoveUp(form.b);
                    MoveRight(form.b);
                    MoveDown(form.d);
                    MoveLeft(form.d);
                    form.changeForm();
                    break;
                }
                if (f == 2 && cB(a, -2, -2) && cB(b, -1, -1) && cB(d, 1, 1)) {
                    MoveDown(form.a);
                    MoveDown(form.a);
                    MoveLeft(form.a);
                    MoveLeft(form.a);
                    MoveDown(form.b);
                    MoveLeft(form.b);
                    MoveUp(form.d);
                    MoveRight(form.d);
                    form.changeForm();
                    break;
                }
                if (f == 3 && cB(a, 2, 2) && cB(b, 1, 1) && cB(d, -1, -1)) {
                    MoveUp(form.a);
                    MoveUp(form.a);
                    MoveRight(form.a);
                    MoveRight(form.a);
                    MoveUp(form.b);
                    MoveRight(form.b);
                    MoveDown(form.d);
                    MoveLeft(form.d);
                    form.changeForm();
                    break;
                }
                if (f == 4 && cB(a, -2, -2) && cB(b, -1, -1) && cB(d, 1, 1)) {
                    MoveDown(form.a);
                    MoveDown(form.a);
                    MoveLeft(form.a);
                    MoveLeft(form.a);
                    MoveDown(form.b);
                    MoveLeft(form.b);
                    MoveUp(form.d);
                    MoveRight(form.d);
                    form.changeForm();
                    break;
                }
                break;
        }
    }



    private boolean cB(Rectangle rect, int x, int y) {
        boolean xb = false;
        boolean yb = false;
        if (x >= 0)
            xb = rect.getX() + x * MOVE_SPEED <= BOARD_X - BLOCK_SIZE;
        if (x < 0)
            xb = rect.getX() + x * MOVE_SPEED >= 0;
        if (y >= 0)
            yb = rect.getY() - y * MOVE_SPEED > 0;
        if (y < 0)
            yb = rect.getY() + y * MOVE_SPEED < BOARD_Y;
        return xb && yb && grid[((int) rect.getX() / BLOCK_SIZE) + x][((int) rect.getY() / BLOCK_SIZE) - y] == 0;
    }



    private void animateLine(ArrayList<Integer> aLines) {

        if (aLines.isEmpty()) {
            return;
        }
        for (int line: aLines) {
            Rectangle rect = new Rectangle();
            rect.setX(0);
            rect.setY(line * BLOCK_SIZE);
            rect.setWidth(BOARD_X);
            rect.setHeight(BLOCK_SIZE);
            rect.setFill(Color.WHITE);
            animatedLines.add(rect);
            group.getChildren().add(rect);
        }

        aTimer = 1;

    }
}




