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

    /*
     * Constructor for the class |Block|
     * @param int Width for the block
     * @param int Height for the block
     */
    public Block(int l, int t, int r, int b) {

        rectangle = new Rect(l, t, r, b);
        enabled = true;
        destroyed = false;

    }

    // Getters
    /*
     * Method to return the left position for the block
     * @returns int Left position for the block
     */
    public int getLeft() {

        return rectangle.left;

    }

    /*
     * Method to return the top position for the block
     * @returns int Top position for the block
     */

    public int getTop() {

        return rectangle.top;

    }

    /*
     * Method to return the right position for the block
     * @returns int Right position for the block
     */

    public int getRight() {

        return rectangle.right;

    }

    /*
     * Method to return the bottom position for the block
     * @returns int Bottom position for the block
     */

    public int getBottom() {

        return rectangle.bottom;

    }

    /*
     * Method to return the rectangle for the block
     * @returns Rect The rectangle for the block
     */

    public Rect getRectangle() {

        return rectangle;

    }

    /*
     * Method to detect whether or not the block is enabled
     * @returns boolean True/false if the block is enabled or not
     */

    public boolean isEnabled() {

        return enabled;
    }

    /*
     * Method to detect whether or not the block is destroyed
     * @returns boolean True/false if the block is destroyed or not
     */

    public boolean isDestroyed() {

        return destroyed;

    }

    // Setters
    /*
     * Method to set whether or not the block is disabled
     * @param boolean True/false if the block is disabled or not
     */
    public void setEnabled(boolean e) {

        enabled = e;

    }

    /*
     * Method to set whether or not the block is destroyed
     * @param boolean True/false if the block is destroyed or not
     */
    public void setDestroyed(boolean d) {

        destroyed = d;

    }

    // Misc
    /*
     * Method to get the direction for the collision
     * @param Ball The ball to compare against
     * @return int Direction corresponding to Ball.DIRECTION*
     */
    public int getCollisionDirection(Ball b1) {

        // We need to compare the "old" values
        Rect curr = b1.getRectangle();
        Rect old = b1.getOldRectangle();
        if (old.right < rectangle.left && curr.right >= rectangle.left) {

            return Ball.DIRECTION_LEFT;

        } else if (old.left > rectangle.right && curr.left <= rectangle.right) {

            return Ball.DIRECTION_RIGHT;

        } else if (old.top > rectangle.bottom && curr.top < rectangle.bottom) {

            return Ball.DIRECTION_UP;

        } else if (old.bottom < rectangle.top && curr.bottom > rectangle.top) {

            return Ball.DIRECTION_DOWN;

        } else {

            return Ball.DIRECTION_DOWN;

        }

    }

}
