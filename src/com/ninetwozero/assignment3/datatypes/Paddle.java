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

        // set the size
        width = w;
        height = h;

        // Init
        rectangle = new Rect(-1, -1, -1, -1);
        moving = false;
    }

    // Getters
    /*
     * Method to return the left position for the paddle
     * @returns int Left position for the paddle
     */
    public int getLeft() {

        return rectangle.left;

    }

    /*
     * Method to return the top position for the paddle
     * @returns int Top position for the paddle
     */

    public int getTop() {

        return rectangle.top;

    }

    /*
     * Method to return the right position for the paddle
     * @returns int Right position for the paddle
     */

    public int getRight() {

        return rectangle.right;

    }

    /*
     * Method to return the bottom position for the paddle
     * @returns int Bottom position for the paddle
     */

    public int getBottom() {

        return rectangle.bottom;

    }

    /*
     * Method to return the rectangle for the paddle
     * @returns Rect The rectangle for the paddle
     */

    public Rect getRectangle() {

        return rectangle;

    }

    /*
     * Method to return the width of the paddle
     * @returns int The width of the paddle
     */

    public int getWidth() {

        return rectangle.right - rectangle.left;
    }

    /*
     * Method to return the height of the paddle
     * @returns int The height of the paddle
     */

    public int getHeight() {

        return rectangle.bottom - rectangle.top;
    }

    /*
     * Method to verify wether or not the paddle is moving
     * @returns boolean If the ball is moving or not
     */

    public boolean isMoving() {

        return moving;

    }

    // Setters
    /*
     * Method to set the left position of the ball
     * @param int Position to be set
     */

    public void setLeft(int l) {

        rectangle.left = l;
        rectangle.right = l + width;

    }

    /*
     * Method to set the top position of the ball
     * @param int Position to be set
     */

    public void setTop(int t) {

        rectangle.top = t;
        rectangle.bottom = t + height;

    }

    /*
     * Method to set the right position of the ball
     * @param int Position to be set
     */

    public void setRight(int r) {

        rectangle.left = r - width;
        rectangle.right = r;

    }

    /*
     * Method to set the bottom position of the ball
     * @param int Position to be set
     */

    public void setBottom(int b) {

        rectangle.top = b - height;
        rectangle.bottom = b;

    }

    /*
     * Method to set the moving-flag
     * @param boolean Whether or not the paddle is moving
     */

    public void setMoving(boolean m) {

        moving = m;

    }

    /*
     * Method to set the dimensions of the paddle
     * @param int The width
     * @param int The height
     */

    public void setDimensions(int w, int h) {

        width = w;
        height = h;

    }

    // Misc

    /*
     * Method to calculate where a given rectangle intersects the paddle
     * @param Rect
     * @return int Point of intersect (relative to the paddle)
     */

    public int calculateIntersectX(Rect r) {

        if (r.left < rectangle.right && r.right > rectangle.right) {

            return (rectangle.right - rectangle.left) - ((r.right - rectangle.right) / 2);

        } else if (r.right > rectangle.left && r.left < rectangle.left) {

            return (r.right - rectangle.left) / 2;

        } else {

            return r.left - rectangle.left;
        }

    }

}
