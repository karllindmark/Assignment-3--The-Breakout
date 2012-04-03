/*
    This file is part of Assignment 3: The Breakout

    BF3 Battlelog is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    BF3 Battlelog is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
 */

package com.ninetwozero.assignment3.datatypes;

import android.graphics.Rect;

public class Paddle {

    // Constants
    public static final int DEFAULT_WIDTH = 160;
    public static final int DEFAULT_HEIGHT = 40;
    public static final int DEFAULT_POS_X = 20;
    public static final int DEFAULT_POS_Y = 20;

    // Attributes
    private Rect rectangle;
    private int width, height;
    private boolean moving;

    // Constructs
    public Paddle(int w, int h) {

        //set the size
        width = w;
        height = h;
        
        //Init
        rectangle = new Rect(-1, -1, -1, -1);
        moving = false;
    }

    // Getters
    public int getLeft() {

        return rectangle.left;

    }

    public int getTop() {

        return rectangle.top;

    }

    public int getRight() {

        return rectangle.right;

    }

    public int getBottom() {

        return rectangle.bottom;

    }

    public Rect getRectangle() {

        return rectangle;

    }
    
    public int getWidth() {
        
        return rectangle.right - rectangle.left;
    }

    public int getHeight() {
    
        return rectangle.bottom - rectangle.top;
    }


    public boolean isMoving() {

        return moving;

    }

    // Setters
    public void setLeft(int l) {

        rectangle.left = l;
        rectangle.right = l + width;

    }

    public void setTop(int t) {

        rectangle.top = t;
        rectangle.bottom = t + height;

    }

    public void setRight(int r) {

        rectangle.left = r - width;
        rectangle.right = r;

    }

    public void setBottom(int b) {

        rectangle.top = b - height;
        rectangle.bottom = b;

    }

    public void setMoving(boolean m) {

        moving = m;

    }

    public void setDimensions(int w, int h) {

        width = w;
        height = h;

    }
    
    //Misc
    public int calculateIntersectX(Rect r) {
        
        if( r.left < rectangle.right && r.right > rectangle.right ) {
        
            return (rectangle.right-rectangle.left)-((r.right-rectangle.right)/2);
            
        } else if( r.right > rectangle.left && r.left < rectangle.left ) {
    
                return (r.right-rectangle.left)/2;
        
        } else {
         
            return r.left-rectangle.left;
        }
    
    }
    
}
