
package com.ninetwozero.assignment3.datatypes;

/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import com.ninetwozero.assignment3.GameOverActivity;
import com.ninetwozero.assignment3.R;
import com.ninetwozero.assignment3.misc.Constants;

public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    // Context & elements
    private Context context;
    private TextView textMessage;
    private TextView textScore;
    private TextView textLives;
    private View viewOverlay;

    // Event-related
    private final int TOUCHPOINT_X = 0;
    private final int TOUCHPOINT_Y = 1;

    // Points
    private final int POINTS_DISABLE = 1;
    private final int POINTS_DESTROY = 2;
    private final int POINTS_DEATH = -3;

    // Defaults
    private final int DEFAULT_SPACER_BLOCKS = 20;
    private final int DEFAULT_NUM_BLOCKS = 5;
    private final int DEFAULT_WIDTH = 540;
    private final int DEFAULT_HEIGHT = 960;
    private final int DEFAULT_NUM_LIVES = 3;

    // Paddle
    private double[] touchPoints;
    private double[] touchPointsPrevious;
    private Paddle paddleData;
    private int directionPaddle;

    // Ball-related
    private Ball ballData;

    // Blocks
    private Block[] blocks;
    private int numBlockCols;
    private int numBlockRows;
    private int numBlocks;
    private int numDisabledBlocks;
    private int numDestroyedBlocks;
    private int numTotalDisabledBlocks;
    private int numTotalDestroyedBlocks;

    // Scaling related
    private double scaleModifierX;
    private double scaleModifierY;
    private int scaledSpacing;
    private int scaledPositionPaddle;
    private int scaledWidthBlock;
    private int scaledHeightBlock;
    private int scaledWidthBall;
    private int scaledHeightBall;
    private int scaledWidthPaddle;
    private int scaledHeightPaddle;

    // Scoring related
    private long numPoints;
    private long numPaddleHits;
    private long numRounds;
    private int numLives;
    private int viewMaxWidth;
    private int viewMaxHeight;

    /** The thread that actually draws the animation */
    private GameThread thread;

    public GameSurfaceView(Context c, AttributeSet attrs) {
        super(c, attrs);

        // register our interest in hearing about changes to our surface
        context = c;
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);

        // Let's get the touch points
        touchPoints = new double[2];

        // create thread only; it's started in surfaceCreated()
        thread = new GameThread(holder, c, new Handler() {
            @Override
            public void handleMessage(Message m) {

                textMessage.setVisibility(m.getData().getInt("viz"));
                textMessage.setText(m.getData().getString("text"));
                textScore.setText(context.getString(R.string.info_top_score).replace("{score}",
                        m.getData().getLong("score") + ""));
                textLives.setText(context.getString(R.string.info_top_lives).replace("{num}",
                        m.getData().getInt("lives") + ""));
                viewOverlay.setVisibility(m.getData().getInt("overlay", View.INVISIBLE));

            }
        });

        setFocusable(true); // make sure we get key events

        // Init the paddle
        touchPoints = new double[2];
        touchPointsPrevious = new double[2];

        // Setup the width/height
        viewMaxWidth = 0;
        viewMaxHeight = 0;

    }

    public void initValues() {

        // Init the scaled values
        scaleModifierX = ((double) viewMaxWidth) / DEFAULT_WIDTH;
        scaleModifierY = ((double) viewMaxHeight) / DEFAULT_HEIGHT;
        scaledSpacing = (int) (DEFAULT_SPACER_BLOCKS * scaleModifierX);
        scaledPositionPaddle = (int) (Paddle.DEFAULT_POS_X * scaleModifierX);
        scaledWidthPaddle = (int) (Paddle.DEFAULT_WIDTH * scaleModifierX);
        scaledHeightPaddle = (int) (Paddle.DEFAULT_HEIGHT * scaleModifierY);
        scaledWidthBlock = (int) (Block.DEFAULT_WIDTH * scaleModifierX);
        scaledHeightBlock = (int) (Block.DEFAULT_HEIGHT * scaleModifierY);
        scaledWidthBall = (int) (Ball.DEFAULT_WIDTH * scaleModifierX);
        scaledHeightBall = (int) (Ball.DEFAULT_HEIGHT * scaleModifierY);

        // Determine the number of blocks
        numBlockRows = 3;
        numBlockCols = (viewMaxWidth - scaledSpacing) / (scaledWidthBlock + scaledSpacing);

        // INit misc
        numPoints = 0;
        numLives = DEFAULT_NUM_LIVES;

        // Init ball
        ballData = new Ball(scaledWidthBall, scaledHeightBall);

    }

    public void initPaddle() {

        paddleData = new Paddle(scaledWidthPaddle, scaledHeightPaddle);
        paddleData.setLeft(scaledPositionPaddle);
        paddleData.setBottom(viewMaxHeight - Paddle.DEFAULT_POS_Y);

    }

    public void initBlocks(boolean restart) {

        // Are there any blocks?
        if (blocks != null && !restart) {
            return;
        }

        // Init the BlockData[]
        numBlocks = numBlockRows * numBlockCols;
        blocks = new Block[numBlocks];

        // Iterate and create!
        for (int count = 0; count < numBlocks; count++) {

            // If it's the initial block
            if (count == 0) {

                blocks[count] = new Block(

                        scaledSpacing,
                        scaledSpacing,
                        scaledSpacing + scaledWidthBlock,
                        scaledSpacing + scaledHeightBlock

                        );

            } else {

                // If we're to output a new row
                if ((count % DEFAULT_NUM_BLOCKS) == 0) {

                    blocks[count] = new Block(

                            scaledSpacing,
                            blocks[count - 1].getBottom() + scaledSpacing,
                            scaledSpacing + scaledWidthBlock,
                            blocks[count - 1].getBottom() + scaledHeightBlock
                                    + scaledSpacing

                            );

                } else {

                    blocks[count] = new Block(

                            blocks[count - 1].getRight() + scaledSpacing,
                            blocks[count - 1].getTop(),
                            blocks[count - 1].getRight() + scaledSpacing
                                    + scaledWidthBlock,
                            blocks[count - 1].getBottom()

                            );

                }

            }

        }

    }

    public GameThread getThread() {
        return thread;
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        if (!hasWindowFocus) {
            thread.pause();
        }
    }

    // Setters for the different textviews
    public void setTextMessage(TextView tv) {
        textMessage = tv;
    }

    public void setTextScore(TextView tv) {
        textScore = tv;
    }

    public void setTextLives(TextView tv) {
        textLives = tv;
    }

    public void setViewOverlay(View v) {

        viewOverlay = v;

    }

    /* Callback invoked when the surface dimensions change. */
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
            int height) {
        thread.setSurfaceSize(width, height);
    }

    /*
     * Callback invoked when the Surface has been created and is ready to be
     * used.
     */
    public void surfaceCreated(SurfaceHolder holder) {

        // Let's calculate the dimensions
        Rect surfaceFrame = holder.getSurfaceFrame();
        viewMaxWidth = surfaceFrame.right - surfaceFrame.left;
        viewMaxHeight = surfaceFrame.bottom - surfaceFrame.top;

        // Let's generate teh scaled values
        initValues();

        // Generate the paddle
        initPaddle();

        // Generate the blocks
        initBlocks(false);

        // Start the thread
        if( !thread.isRunning() ) {
                 
            thread.setRunning(true);
            thread.start();
        
        }
       
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
       
        boolean retry = true;
        thread.setRunning(false);
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // Get the action
        int action = event.getAction();

        // Save the "point of impact"
        touchPointsPrevious = touchPoints.clone();
        touchPoints[TOUCHPOINT_X] = event.getX();
        touchPoints[TOUCHPOINT_Y] = event.getY();

        // Check what's up
        if (action == MotionEvent.ACTION_DOWN) {

            if ((paddleData.getLeft() <= touchPoints[TOUCHPOINT_X] && paddleData.getRight() >= touchPoints[TOUCHPOINT_X])
                    &&
                    (paddleData.getTop() <= touchPoints[TOUCHPOINT_Y] && paddleData.getBottom() >= touchPoints[TOUCHPOINT_Y])) {

                paddleData.setMoving(true);
            }

        } else if (paddleData.isMoving() && action == MotionEvent.ACTION_MOVE) {

            // Let's check this out
            if (touchPointsPrevious[TOUCHPOINT_X] > touchPoints[TOUCHPOINT_X]) {

                directionPaddle = Ball.DIRECTION_LEFT;

            } else {

                directionPaddle = Ball.DIRECTION_RIGHT;

            }

            // Calculate the position
            paddleData.setLeft((int) touchPoints[TOUCHPOINT_X] - (scaledWidthPaddle / 2));

            // Is it < 0, ie out of bounds?
            if (paddleData.getLeft() < 0) {
                paddleData.setLeft(0);
            }
            if (paddleData.getTop() < 0) {
                paddleData.setTop(0);
            }

            // Calculate the new "end"
            paddleData.setRight(paddleData.getLeft() + scaledWidthPaddle);
            paddleData.setBottom(paddleData.getTop() + scaledHeightPaddle);

            // Is it out of bounds (horizontally, right-going)
            if (paddleData.getRight() > viewMaxWidth) {

                // Set it against the floor
                paddleData.setRight(viewMaxWidth);
                paddleData.setLeft(viewMaxWidth - scaledWidthPaddle);

            }

        } else if (action == MotionEvent.ACTION_UP) {

            paddleData.setMoving(false);

        }

        return true;
    }

    public class GameThread extends Thread {

        // Game states
        public static final int STATE_READY = 0;
        public static final int STATE_RUNNING = 1;
        public static final int STATE_PAUSE = 2;
        public static final int STATE_DEATH = 3;
        public static final int STATE_LEVEL = 4;
        public static final int STATE_LOSE = 5;
        public static final int STATE_WIN = 6;
        public static final int STATE_DISABLED = 7;
        public static final int STATE_DESTROYED = 8;
        public static final int STATE_PADDLE = 9;

        private Handler mHandler;
        private int mMode;
        private boolean running;

        /** Handle to the surface manager object we interact with */
        private SurfaceHolder mSurfaceHolder;

        // Just a lil' something
        private Rect rectClear;

        // Paint
        private Paint paintReset;
        private Paint paintBackground;
        private Paint paintPaddle;
        private Paint paintBall;
        private Paint paintBlock;
        private Paint paintBlockDisabled;

        // Sound
        private MediaPlayer soundBlockDisable;
        private MediaPlayer soundBlockDestroy;
        private MediaPlayer soundWallBounce;
        private MediaPlayer soundPaddleBounce;

        public GameThread(SurfaceHolder surfaceHolder, Context c,
                Handler handler) {

            // First of all - let's grab the important stuff
            mSurfaceHolder = surfaceHolder;
            mHandler = handler;
            context = c;

            // Init the paint
            paintReset = new Paint();
            paintBackground = new Paint();
            paintPaddle = new Paint();
            paintBall = new Paint();
            paintBlock = new Paint();
            paintBlockDisabled = new Paint();

            // Set the background color
            paintReset.setARGB(255, 255, 255, 255);
            paintBackground.setARGB(16, 255, 255, 255);

            // Set the paddle color
            paintPaddle.setARGB(255, 0, 0, 0);
            paintPaddle.setAntiAlias(true);

            // Set the ball color
            paintBall.setARGB(255, 0, 0, 0);
            paintBall.setAntiAlias(true);

            // Set the block color
            paintBlock.setARGB(255, 0, 0, 0);
            paintBlock.setAntiAlias(true);
            paintBlockDisabled.setARGB(128, 0, 0, 0);
            paintBlockDisabled.setAntiAlias(true);

            // Setup the sound
            soundBlockDisable = MediaPlayer.create(context, R.raw.block);
            soundBlockDestroy = MediaPlayer.create(context, R.raw.block_destroyed);
            soundWallBounce = MediaPlayer.create(context, R.raw.wall);
            soundPaddleBounce = MediaPlayer.create(context, R.raw.paddle);

            // Misc
            rectClear = new Rect();

        }

        /**
         * Starts the game, setting parameters for the current difficulty.
         */
        public void doStart() {
            synchronized (mSurfaceHolder) {
                
                // Set the direction
                if( mMode != STATE_PAUSE ) {
                    
                    ballData.setDirectionY(Ball.DIRECTION_UP);
                    ballData.setLeft((int) (viewMaxWidth * Math.random()));
                    ballData.setTop(viewMaxHeight-100);
   
                }  

                // Let's run!
                setState(STATE_RUNNING);
            }
        }

        /**
         * Pauses the physics update & animation.
         */
        public void pause() {
            synchronized (mSurfaceHolder) {
                if (mMode == STATE_RUNNING)
                    setState(STATE_PAUSE);
            }
        }

        public synchronized void restoreState(Bundle savedState) {
            synchronized (mSurfaceHolder) {
                setState(STATE_PAUSE);

                /*
                 * TODO: RESTORE STATE? BALL POSITION, PADDLE POSITION, # of
                 * blocks left + positions
                 */
            }
        }

        @Override
        public void run() {
            while (running) {
                Canvas c = null;
                try {
                    c = mSurfaceHolder.lockCanvas(null);
                    synchronized (mSurfaceHolder) {
                        if (mMode == STATE_RUNNING) {
                            calculateGameplay();
                        }
                        doDraw(c);
                    }
                } finally {

                    // Make sure we don't leave it locked
                    if (c != null) {
                        mSurfaceHolder.unlockCanvasAndPost(c);
                    }
                }
            }
        }

        public Bundle saveState(Bundle map) {

            synchronized (mSurfaceHolder) {

                if (map != null) {

                    /* TODO: SAVE STATE IN MAP? */
                }
            }
            return map;
        }

        public void setRunning(boolean b) {
            running = b;
        }

        public void setState(int mode) {
            synchronized (mSurfaceHolder) {
                setState(mode, null);
            }
        }

        public void setState(int mode, CharSequence message) {

            synchronized (mSurfaceHolder) {

                // Init
                mMode = mode;
                Message msg = mHandler.obtainMessage();
                Bundle b = new Bundle();
                String str = "";
                int visibilityOverlay = View.GONE;

                if (mMode == STATE_RUNNING) {

                    visibilityOverlay = View.GONE;

                } else if (mMode == STATE_PADDLE) {

                    // Let's play a sound
                    soundPaddleBounce.start();

                    // Increment the number of hits
                    numPaddleHits++;

                    // Set the state
                    setState(STATE_RUNNING);

                } else if (mMode == STATE_DISABLED) {

                    // Let's play a sound
                    soundBlockDisable.start();;

                    // Update the scoring
                    numDisabledBlocks++;
                    numPoints += POINTS_DISABLE;

                    // Let's run!
                    setState(STATE_RUNNING);

                } else if (mMode == STATE_DESTROYED) {

                    // Let's play a sound
                    soundBlockDestroy.start();

                    // Update the scoring
                    numDestroyedBlocks++;
                    numPoints += POINTS_DESTROY;
                    
                    // Let's run!
                    setState(STATE_RUNNING);

                } else if (mMode == STATE_DEATH) {

                    // So... what's up?
                    numPoints += POINTS_DEATH;

                    visibilityOverlay = View.VISIBLE;
                    str = "YOU DIED. TAP TO RETRY.";

                } else if (mMode == STATE_WIN) {

                    // Save the blocks
                    numTotalDisabledBlocks = numDisabledBlocks;
                    numTotalDestroyedBlocks = numDestroyedBlocks;
                    numRounds++;

                    // Clear the counter
                    numDisabledBlocks = 0;
                    numDestroyedBlocks = 0;

                    // Re-init the blocks
                    initBlocks(true);

                    visibilityOverlay = View.VISIBLE;
                    str = "ROUND COMPLETED. TAP TO CONTINUE";

                } else if (mMode == STATE_LOSE) {

                    // Stop the thread
                    running = false;

                    // Send the user to the Game Over-activity whilst finishing
                    // this one
                    context.startActivity(new Intent(context, GameOverActivity.class)
                            .putExtra("gameRounds", numRounds).putExtra("score", numPoints)
                            .putExtra("paddleHits", numPaddleHits));
                    ((Activity) context).finish();

                } else if( mMode == STATE_PAUSE ) { 
                    
                    str = "PAUSED";
                    running = false;
                    
                } else {

                    str = "LET'S BOUNCE";
                    visibilityOverlay = View.VISIBLE;

                }
                b.putLong("score", numPoints);
                b.putInt("lives", numLives);
                b.putString("text", str);
                b.putInt("viz", !str.equals("") ? View.VISIBLE : View.GONE );
                b.putInt("overlay", visibilityOverlay);
                msg.setData(b);
                mHandler.sendMessage(msg);

            }

        }

        public void setSurfaceSize(int width, int height) {

            synchronized (mSurfaceHolder) {

                // Set the sizes
                viewMaxWidth = width;
                viewMaxHeight = height;

                // Init the rect
                rectClear.set(0, 0, viewMaxWidth, viewMaxHeight);

            }

        }

        public void unpause() {
            setState(STATE_RUNNING);
        }

        private void doDraw(Canvas canvas) {

            // If we ain't got nothing to draw on, we just exit
            if (canvas == null) {
                return;
            }

            /* BG */
            canvas.drawRect(rectClear, paintReset);
            canvas.drawRect(rectClear, paintBackground);

            /* PADDLE */
            canvas.drawRect(paddleData.getRectangle(), paintPaddle);

            /* BLOCKS */
            for (Block b : blocks) {

                // Is it to be seen as "disabled"?
                if (!b.isDestroyed()) {

                    if (b.isEnabled()) {

                        canvas.drawRect(b.getRectangle(), paintBlock);

                    } else {

                        canvas.drawRect(b.getRectangle(), paintBlockDisabled);

                    }

                }

            }

            /* BALL */
            canvas.drawRect(ballData.getRectangle(), paintBall);
        }

        /*
         * Calculates the gameplay
         */
        private void calculateGameplay() {

            if (numDestroyedBlocks == numBlocks) {

                // Set the state
                setState(STATE_WIN);
                return;
            }
            
            // Is it travelling upwards towards the roof?
            if (ballData.getTop() <= blocks[numBlocks - 1].getBottom()) {

                processForBlockCollision();
                
            } else if( ballData.getDirectionY() == Ball.DIRECTION_UP ) {
                
                if ((ballData.getTop()-ballData.getSpeedY()) < 0) {
    
                    //Set the ball at y=0
                    ballData.setTop((int)-(ballData.getTop()-ballData.getSpeedY()));
                    
                    // Set the direction
                    ballData.setDirectionY(Ball.DIRECTION_DOWN);
                    
                } 

            } else if( ballData.getDirectionY() == Ball.DIRECTION_DOWN ) {
                
                if (Rect.intersects(ballData.getRectangle(), paddleData.getRectangle())) {

                    //Handle the bounce
                    handlePaddleBounce();
                    
                    // Increment the paddle hits
                    setState(STATE_PADDLE);
                
                } else if( ballData.getBottom() > viewMaxHeight) {

                    // Set the state
                    setState((--numLives <= 0) ? STATE_LOSE : STATE_DEATH);

                }
                
            }
            
            if( ballData.getDirectionX() == Ball.DIRECTION_LEFT ) {
                
                if ( (ballData.getLeft()-ballData.getSpeedX() ) < 0 ) { 
                
                    //Reset
                    ballData.setLeft((int) -(ballData.getLeft()-ballData.getSpeedX()));
    
                    // Let's play a sound
                    soundWallBounce.start();
    
                    // Toggle the direction
                    ballData.setDirectionX(Ball.DIRECTION_RIGHT);
    
                } 
                    
            } else {
                
                if( (ballData.getRight()+ballData.getSpeedX()) > viewMaxWidth ) {
                    
                    //Reset
                    ballData.setRight((int)((viewMaxWidth*2)-(ballData.getRight()+ballData.getSpeedX())));
    
                    // Let's play a sound
                    soundWallBounce.start();
    
                    // Toggle the direction
                    ballData.setDirectionX(Ball.DIRECTION_LEFT);
                    
                }
                
            }
                
            // Let's do something about it
            ballData.move();
            
        }

        private void processForBlockCollision() {

            // Time to see if we hit a block...
            for (int count = numBlocks - 1; count >= 0; count--) {

                // Let's see
                if (!blocks[count].isDestroyed()
                        && Rect.intersects(ballData.getRectangle(), blocks[count].getRectangle())) {
                    
                    // Disable the block
                    if (blocks[count].isEnabled()) {

                        // Set them up
                        blocks[count].setEnabled(false);

                        // Let's set the state
                        setState(STATE_DISABLED);

                    } else {

                        // Set them up
                        blocks[count].setDestroyed(true);

                        // Let's set the state
                        setState(STATE_DESTROYED);
                    }

                    // Otherwise...
                    int collisionDirection = blocks[count].getCollisionDirection(ballData);
                    if( collisionDirection == Ball.DIRECTION_LEFT || collisionDirection == Ball.DIRECTION_RIGHT ) {
                        
                        Log.d(Constants.DEBUG_TAG, "Collided from " + ( (collisionDirection == Ball.DIRECTION_LEFT )? "LEFT" : "RIGHT"));
                        ballData.toggleDirectionX();
                        
                    } else {

                        Log.d(Constants.DEBUG_TAG, "Collided from " + ( (collisionDirection == Ball.DIRECTION_UP )? "UP" : "DOWN"));
                        ballData.toggleDirectionY();
                        
                    }
                }

            }

        }
        
        public void handlePaddleBounce() {
            
            int ballOnPaddlePosition = paddleData.calculateIntersectX(ballData.getRectangle());
            double multiplier = 180.0/paddleData.getWidth();
            double paddleDegrees = ballOnPaddlePosition * multiplier;
            double paddleCos = -Math.cos(Math.toRadians(paddleDegrees));
            double paddleSin = Math.sin(Math.toRadians(paddleDegrees));                    
            double speedPaddleX = Ball.DEFAULT_SPEED_X * paddleCos;
            double speedPaddleY = Ball.DEFAULT_SPEED_Y * paddleSin;
            
            //Set the speed (Y-axis)
            ballData.setSpeedY(speedPaddleY);            
            
            //Update the direction
            if( paddleCos > 0.0 ) { 

                ballData.setDirectionX(Ball.DIRECTION_RIGHT);
                ballData.setSpeedX(speedPaddleX);
                
            } else {
                
                ballData.setDirectionX(Ball.DIRECTION_LEFT);
                ballData.setSpeedX(-speedPaddleX);
                
            }

            // Set the direction
            ballData.setDirectionY(Ball.DIRECTION_UP);
            
        }

        public boolean isRunning() {

            return running;

        }
    }
}
