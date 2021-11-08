package com.example.neontetris;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class Form {
    Rectangle a;
    Rectangle b;
    Rectangle c;
    Rectangle d;
    Color color;

    private String name;

    // the current rotation of the piece
    public int form = 1;


    public Form (Rectangle a, Rectangle b, Rectangle c, Rectangle d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    public Form (Rectangle a, Rectangle b, Rectangle c, Rectangle d, String name) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.name = name;


        //set color of Object
        switch (name) { //consider replacing this with an enum.
            case "j":
                color = Color.BLUE;
                break;
            case "l":
                color = Color.DARKORANGE;
                break;
            case "o":
                color = Color.YELLOW;
                break;
            case "s":
                color = Color.GREEN;
                break;
            case "t":
                color = Color.PURPLE;
                break;
            case "z":
                color = Color.RED;
                break;
            case "i":
                color = Color.TURQUOISE;
                break;
        }

        this.a.setFill(color);
        this.b.setFill(color);
        this.c.setFill(color);
        this.d.setFill(color);
    }



    public String getName() {
        return name;
    }

    public void changeForm() {
        if (form !=4) {
            form++;
        } else {
            form = 1;
        }
    }
}
