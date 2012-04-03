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

package com.ninetwozero.assignment3;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.ninetwozero.assignment3.datatypes.GameSurfaceView;
import com.ninetwozero.assignment3.datatypes.GameSurfaceView.GameThread;

public class GameActivity extends Activity {

    // Attributes
    private GameSurfaceView gameSurfaceView;
    private GameThread gameThread;
    private View viewOverlay;

    /*
     * Standard onCreate() that initiates the GameActivity
     * @param Bundle The savedInstanceState
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set it to be fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Load the game_layout.xml
        setContentView(R.layout.game_layout);

        // Get the view & thread, and also pass textviews to the GameSurfaceView
        // for scores
        gameSurfaceView = (GameSurfaceView) findViewById(R.id.gamesurfaceview);
        gameThread = gameSurfaceView.getThread();
        gameSurfaceView.setTextMessage((TextView) findViewById(R.id.text_message));
        gameSurfaceView.setTextScore((TextView) findViewById(R.id.text_score));
        gameSurfaceView.setTextLives((TextView) findViewById(R.id.text_lives));
        gameSurfaceView.setViewOverlay(viewOverlay = findViewById(R.id.window_overlay));

        // Do we have an instance?
        if (savedInstanceState == null) {

            // Fresh setup
            gameThread.setState(GameThread.STATE_READY);

        } else {

            // Restore the previous state
            gameThread.restoreState(savedInstanceState);

        }

    }

    /*
     * Method that overrides the default Activity::onPause() and pauses the game
     * thread
     * @see android.app.Activity#onPause()
     */

    @Override
    protected void onPause() {
        super.onPause();
        gameSurfaceView.getThread().pause();
    }

    /*
     * Method that handles the onClick method for the overlay
     * @param View The clicked view
     */
    public void onClick(View v) {

        if (v.getId() == R.id.window_overlay) {

            gameThread.doStart();
            v.setVisibility(View.GONE);

        }

    }

    /*
     * Overridden method to save the instance state from the game thread
     * @param Bundle The (soon to be) savedInstanceState
     * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
     */

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        gameThread.saveState(outState);

    }
}
