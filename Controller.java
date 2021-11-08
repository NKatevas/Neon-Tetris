package com.example.neontetris;

import javafx.scene.shape.Rectangle;

public class Controller {
    // Getting the numbers and the grid from Tetris
    public static final int MOVE = Game.MOVE_SPEED;
    public static final int SIZE = Game.BLOCK_SIZE;
    public static int xMax = Game.BOARD_X;
    public static int xHalf = xMax / 2;
    public static int[][] grid = Game.grid;

    public static void MoveRight(Form form) {
        if (form.a.getX() + MOVE <= xMax - SIZE && form.b.getX() + MOVE <= xMax - SIZE
                && form.c.getX() + MOVE <= xMax - SIZE && form.d.getX() + MOVE <= xMax - SIZE) {
            int moveA = grid[((int) form.a.getX() / SIZE) + 1][((int) form.a.getY() / SIZE)];
            int moveB = grid[((int) form.b.getX() / SIZE) + 1][((int) form.b.getY() / SIZE)];
            int moveC = grid[((int) form.c.getX() / SIZE) + 1][((int) form.c.getY() / SIZE)];
            int moveD = grid[((int) form.d.getX() / SIZE) + 1][((int) form.d.getY() / SIZE)];
            if (moveA == 0 && moveA == moveB && moveB == moveC && moveC == moveD) {
                form.a.setX(form.a.getX() + MOVE);
                form.b.setX(form.b.getX() + MOVE);
                form.c.setX(form.c.getX() + MOVE);
                form.d.setX(form.d.getX() + MOVE);
            }
        }
    }

    public static void MoveLeft(Form form) {
        if (form.a.getX() - MOVE >= 0 && form.b.getX() - MOVE >= 0 && form.c.getX() - MOVE >= 0
                && form.d.getX() - MOVE >= 0) {
            int moveA = grid[((int) form.a.getX() / SIZE) - 1][((int) form.a.getY() / SIZE)];
            int moveB = grid[((int) form.b.getX() / SIZE) - 1][((int) form.b.getY() / SIZE)];
            int moveC = grid[((int) form.c.getX() / SIZE) - 1][((int) form.c.getY() / SIZE)];
            int moveD = grid[((int) form.d.getX() / SIZE) - 1][((int) form.d.getY() / SIZE)];
            if (moveA == 0 && moveA == moveB && moveB == moveC && moveC == moveD) {
                form.a.setX(form.a.getX() - MOVE);
                form.b.setX(form.b.getX() - MOVE);
                form.c.setX(form.c.getX() - MOVE);
                form.d.setX(form.d.getX() - MOVE);
            }
        }
    }

    public static Form makeRect() {
        int block = (int) (Math.random() * 100);
        String name;
        Rectangle a = new Rectangle(SIZE-1, SIZE-1), b = new Rectangle(SIZE-1, SIZE-1), c = new Rectangle(SIZE-1, SIZE-1),
                d = new Rectangle(SIZE-1, SIZE-1);
        if (block < 13) {
            a.setX(xHalf - SIZE);
            b.setX(xHalf - SIZE);
            b.setY(SIZE);
            c.setX(xHalf);
            c.setY(SIZE);
            d.setX(xHalf + SIZE);
            d.setY(SIZE);
            name = "j";
        } else if (block < 27) {
            a.setX(xHalf + SIZE);
            b.setX(xHalf - SIZE);
            b.setY(SIZE);
            c.setX(xHalf);
            c.setY(SIZE);
            d.setX(xHalf + SIZE);
            d.setY(SIZE);
            name = "l";
        } else if (block < 41) {
            a.setX(xHalf - SIZE);
            b.setX(xHalf);
            c.setX(xHalf - SIZE);
            c.setY(SIZE);
            d.setX(xHalf);
            d.setY(SIZE);
            name = "o";
        } else if (block < 55) {
            a.setX(xHalf + SIZE);
            b.setX(xHalf);
            c.setX(xHalf);
            c.setY(SIZE);
            d.setX(xHalf - SIZE);
            d.setY(SIZE);
            name = "s";
        } else if (block < 69) {
            a.setX(xHalf - SIZE);
            b.setX(xHalf);
            c.setX(xHalf);
            c.setY(SIZE);
            d.setX(xHalf + SIZE);
            name = "t";
        } else if (block < 83) {
            a.setX(xHalf + SIZE);
            b.setX(xHalf);
            c.setX(xHalf + SIZE);
            c.setY(SIZE);
            d.setX(xHalf + SIZE + SIZE);
            d.setY(SIZE);
            name = "z";
        } else {
            a.setX(xHalf - SIZE - SIZE);
            b.setX(xHalf - SIZE);
            c.setX(xHalf);
            d.setX(xHalf + SIZE);
            name = "i";
        }
        return new Form(a, b, c, d, name);
    }
}
