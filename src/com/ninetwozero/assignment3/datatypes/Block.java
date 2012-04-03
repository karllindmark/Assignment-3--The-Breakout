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

public class Block {

    // Constants
    public static final double DEFAULT_WIDTH = 84.5;
    public static final int DEFAULT_HEIGHT = 20;

    // Attributes
    private Rect rectangle;
    private boolean enabled, destroyed;

    // Constructs
    public Block(int l, int t, int r, int b) {

        rectangle = new Rect(l, t, r, b);
        enabled = true;
        destroyed = false;

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

    public boolean isEnabled() {

        return enabled;
    }

    public boolean isDestroyed() {

        return destroyed;

    }

    // Setters
    public void setEnabled(boolean e) {

        enabled = e;

    }

    public void setDestroyed(boolean d) {

        destroyed = d;

    }
    
    //Misc
    public int getCollisionDirection(Ball b1) {

        //We need to compare the "old" values
        Rect curr = b1.getRectangle();
        Rect old = b1.getOldRectangle();
        if( old.right < rectangle.left && curr.right >= rectangle.left) {
            
            return Ball.DIRECTION_LEFT;
                
        } else if( old.left > rectangle.right && curr.left <= rectangle.right ){
            
            return Ball.DIRECTION_RIGHT;
            
        } else if( old.top > rectangle.bottom && curr.top < rectangle.bottom ) {
            
            return Ball.DIRECTION_UP;
            
        } else if( old.bottom < rectangle.top && curr.bottom > rectangle.top ) {
            
            return Ball.DIRECTION_DOWN;
        
        } else {
            
            return Ball.DIRECTION_DOWN;
            
        }
        
    }
    
}
