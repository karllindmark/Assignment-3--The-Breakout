
package com.ninetwozero.assignment3;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.ninetwozero.assignment3.datatypes.GameSurfaceView;
import com.ninetwozero.assignment3.datatypes.GameSurfaceView.GameThread;
import com.ninetwozero.assignment3.misc.Constants;

public class GameActivity extends Activity {

    // Attributes
    private GameSurfaceView gameSurfaceView;
    private GameThread gameThread;
    private View viewOverlay;

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

    @Override
    protected void onPause() {
        super.onPause();
        gameSurfaceView.getThread().pause();
    }

    public void onClick(View v) {

        if (v.getId() == R.id.window_overlay) {

            gameThread.doStart();
            v.setVisibility(View.GONE);

        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        gameThread.saveState(outState);

    }
}
