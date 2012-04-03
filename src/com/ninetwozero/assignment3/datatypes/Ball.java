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

import com.ninetwozero.assignment3.misc.Constants;

import android.graphics.Rect;
import android.util.Log;

public class Ball {

    // Constants
    public static final int DEFAULT_WIDTH = 20;
    public static final int DEFAULT_HEIGHT = 20;
    public static final double DEFAULT_SPEED_X = 7.0;
    public static final double DEFAULT_SPEED_Y = 5.0;
    public static final int DIRECTION_START = -1;
    public static final int DIRECTION_UP = 0;
    public static final int DIRECTION_RIGHT = 1;
    public static final int DIRECTION_DOWN = 2;
    public static final int DIRECTION_LEFT = 3;
    public static final int DIRECTION_NONE = 4;

    // Attributes
    private Rect rectangle, oldRectangle;
    private int width, height, oWidth, oHeight;
    private int directionX, directionY;
    private double speedX, speedY;
    private boolean moving;

    // Constructs
    public Ball(int w, int h) {

        //Set the size
        width = w;
        height = h;
        oWidth = w;
        oHeight = h;
        
        //Default init
        rectangle = new Rect(-1, -1, -1, -1);
        oldRectangle = new Rect(-1, -1, -1, -1);
        
        //The direction
        directionX = (Math.random() < 0.5) ? Ball.DIRECTION_LEFT : Ball.DIRECTION_RIGHT;
        directionY = DIRECTION_UP;
        
        //The speed
        speedX = DEFAULT_SPEED_X;
        speedY = DEFAULT_SPEED_Y;
        
        //No, we're not moving
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
    
    public Rect getOldRectangle() {
        
        return oldRectangle;
        
    }

    public int getDirectionX() {

        return directionX;

    }

    public int getDirectionY() {

        return directionY;

    }

    public double getSpeedX() {

        return speedX;
    }

    public double getSpeedY() {

        return speedY;
    }
    
    public int getWidth() {
        
        return width;
    
    }
    
    public int getHeight() {
        
        return height;
        
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

    public void setDirectionX(int d) {

        directionX = d;

    }

    public void setDirectionY(int d) {

        directionY = d;

    }

    public void setMoving(boolean m) {

        moving = m;

    }

    public void setSpeedX(double s) {

        speedX = s;

    }

    public void setSpeedY(double s) {

        speedY = s;

    }

    public void setDimensions(int w, int h) {

        width = w;
        height = h;
        oWidth = w;
        oHeight = h;

    }

    // Misc methods
    public void toggleDirectionX() {

        directionX = (directionX == DIRECTION_LEFT) ? DIRECTION_RIGHT : DIRECTION_LEFT;

    }

    public void toggleDirectionY() {

        directionY = (directionY == DIRECTION_UP) ? DIRECTION_DOWN : DIRECTION_UP;

    }
    
    public void move() {

        //Save the "old" rectangle
        oldRectangle.set(rectangle.left, rectangle.top, rectangle.right, rectangle.bottom);
        
        // Let's see what's up on the vertical axel
        if (directionX == DIRECTION_LEFT) {

            rectangle.left -= speedX;
            rectangle.right -= speedX;

        } else {

            rectangle.left += speedX;
            rectangle.right += speedX;

        }

        // Let's see what's up on the horizontal axel
        if (directionY == DIRECTION_UP) {

            rectangle.top -= speedY;
            rectangle.bottom -= speedY;

        } else {

            rectangle.top += speedY;
            rectangle.bottom += speedY;

        }
        
    }
    
    @Override
    public String toString() {
        
        return rectangle.left + ", " + rectangle.top + ", " + ", " + rectangle.right + rectangle.bottom;
        
    }
}
