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

import com.ninetwozero.assignment3.misc.Constants;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class GameOverActivity extends Activity {

    // Attributes
    private long score;
    private long gameRounds;
    private long paddleHits;

    // Elements
    private TextView textScore;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set it to be fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Set the content view
        setContentView(R.layout.game_over);

        // Set the TextView
        textScore = (TextView) findViewById(R.id.text_score);

        // Get the score
        score = getIntent().getLongExtra("score", 0);

        // Populate the textfield
        textScore.setText(getString(R.string.info_top_score).replace("{score}", score + ""));

        // Update SP
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor spEditor = sharedPreferences.edit();
        spEditor.putLong(Constants.SP_LIFETIME_SCORE,
                sharedPreferences.getLong(Constants.SP_LIFETIME_SCORE, 0) + score);
        spEditor.putLong(Constants.SP_LIFETIME_ROUNDS,
                sharedPreferences.getLong(Constants.SP_LIFETIME_ROUNDS, 0) + gameRounds);
        spEditor.putLong(Constants.SP_LIFETIME_PADDLE_HITS,
                sharedPreferences.getLong(Constants.SP_LIFETIME_PADDLE_HITS, 0) + paddleHits);
        spEditor.putLong(Constants.SP_LIFETIME_LOSSES,
                sharedPreferences.getLong(Constants.SP_LIFETIME_LOSSES, 0) + 1);
        spEditor.commit();
    }

    public void onClick(View v) {

        if (v.getId() == R.id.root) {

            startActivity(new Intent(this, GameActivity.class));
            finish();
        }

    }
}
