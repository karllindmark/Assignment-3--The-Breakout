
package com.ninetwozero.assignment3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.TextView;

public class MainActivity extends Activity {

    // Elements
    private TextView textTap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set it to be fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Set the contentview
        setContentView(R.layout.main);

        // Construct an animation
        AlphaAnimation animFadeOut = new AlphaAnimation(1, 0.1f);
        AlphaAnimation animFadeIn = new AlphaAnimation(0.1f, 1);
        animFadeOut.setDuration(4000);
        animFadeIn.setDuration(4000);
        animFadeOut.setRepeatCount(Animation.INFINITE);
        animFadeIn.setRepeatCount(Animation.INFINITE);

        // Merge them into one
        AnimationSet animations = new AnimationSet(false);
        animations.addAnimation(animFadeOut);
        animations.addAnimation(animFadeIn);
        animations.setRepeatCount(Animation.INFINITE);

        // Get the TextView & use the animation
        textTap = (TextView) findViewById(R.id.text_tap);
        textTap.setAnimation(animations);
        animations.start();

    }

    public void onClick(View v) {

        if (v.getId() == R.id.root) {

            startActivity(new Intent(this, GameActivity.class));

        }

    }
}