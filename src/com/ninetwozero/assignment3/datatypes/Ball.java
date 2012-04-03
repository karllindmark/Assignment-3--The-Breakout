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
    private int width, height;
    private int directionX, directionY;
    private double speedX, speedY;

    /*
     * Constructor for the class |Ball|
     * @param int Width for the ball
     * @param int Height for the ball
     */
    public Ball(int w, int h) {

        // Set the size
        width = w;
        height = h;

        // Default init
        rectangle = new Rect(-1, -1, -1, -1);
        oldRectangle = new Rect(-1, -1, -1, -1);

        // The direction
        directionX = (Math.random() < 0.5) ? Ball.DIRECTION_LEFT : Ball.DIRECTION_RIGHT;
        directionY = DIRECTION_UP;

        // The speed
        speedX = DEFAULT_SPEED_X;
        speedY = DEFAULT_SPEED_Y;

    }

    // Getters
    /*
     * Method to return the left position for the ball
     * @returns int Left position for the ball
     */
    public int getLeft() {

        return rectangle.left;

    }

    /*
     * Method to return the top position for the ball
     * @returns int Top position for the ball
     */

    public int getTop() {

        return rectangle.top;

    }

    /*
     * Method to return the right position for the ball
     * @returns int Right position for the ball
     */

    public int getRight() {

        return rectangle.right;

    }

    /*
     * Method to return the bottom position for the ball
     * @returns int Bottom position for the ball
     */

    public int getBottom() {

        return rectangle.bottom;

    }

    /*
     * Method to return the rectangle for the ball
     * @returns Rect The rectangle for the ball
     */

    public Rect getRectangle() {

        return rectangle;

    }

    /*
     * Method to return the previous rectangle for the ball
     * @returns Rect The previous rectangle for the ball
     */

    public Rect getOldRectangle() {

        return oldRectangle;

    }

    /*
     * Method to return the direction on the X-axis
     * @returns int Direction corresponding to DIRECTION_*
     */

    public int getDirectionX() {

        return directionX;

    }

    /*
     * Method to return the direction on the Y-axis
     * @returns int Direction corresponding to DIRECTION_*
     */

    public int getDirectionY() {

        return directionY;

    }

    /*
     * Method to return the speed on the X-axis
     * @returns double Speed on the x-axis
     */

    public double getSpeedX() {

        return speedX;
    }

    /*
     * Method to return the balls speed on the Y-axis
     * @returns double Speed on the Y-axis
     */

    public double getSpeedY() {

        return speedY;
    }

    /*
     * Method to return the balls width
     * @returns int Width of the ball
     */

    public int getWidth() {

        return width;

    }

    /*
     * Method to return the balls height
     * @returns int Height of the ball
     */

    public int getHeight() {

        return height;

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
     * Method to set the direction on the X-axis
     * @param int Direction to be set
     */

    public void setDirectionX(int d) {

        directionX = d;

    }

    /*
     * Method to set the direction on the Y-axis
     * @param int Direction to be set
     */

    public void setDirectionY(int d) {

        directionY = d;

    }

    /*
     * Method to set the speed on the X-axis
     * @param double Speed to be set
     */

    public void setSpeedX(double s) {

        speedX = s;

    }

    /*
     * Method to set the speed on the Y-axis
     * @param double Speed to be set
     */

    public void setSpeedY(double s) {

        speedY = s;

    }

    /*
     * Method to set the dimensions of the ball
     * @param int The width
     * @param int The height
     */

    public void setDimensions(int w, int h) {

        width = w;
        height = h;

    }

    // Misc methods
    /*
     * Method to toggle the direction on the X-axis
     */

    public void toggleDirectionX() {

        directionX = (directionX == DIRECTION_LEFT) ? DIRECTION_RIGHT : DIRECTION_LEFT;

    }

    /*
     * Method to toggle the direction on the Y-axis
     */
    public void toggleDirectionY() {

        directionY = (directionY == DIRECTION_UP) ? DIRECTION_DOWN : DIRECTION_UP;

    }

    /*
     * Method to actually move the ball
     */
    public void move() {

        // Save the "old" rectangle
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

    /*
     * Overridden toString() method for easier output
     */
    @Override
    public String toString() {

        return rectangle.left + ", " + rectangle.top + ", " + rectangle.right + ", "
                + rectangle.bottom;

    }
}
