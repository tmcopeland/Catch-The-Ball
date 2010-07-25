package com.dylantaylor.ttb;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import com.nullwire.trace.ExceptionHandler;

/**
 *
 * @author Dylan Taylor
 */
public class TTBView extends Activity implements OnTouchListener {

    private PowerManager.WakeLock wl;
    //short integers used to determine where the user pressed on the screen
    private short x = 0; //finger x coordinate
    private short y = 0; //finger y coordinate
    //short integers containing the current location of the ball on the screen
    private short bx; //ball x coordinate (upper left corner)
    private short by; //ball y coordinate (upper left corner)
    private short bcx; //ball center x coordinate
    private short bcy; //ball center y coordinate
    //short integers containing the screen resolution. set during first draw.
    private short sHeight = 0; //screen height in pixels
    private short sWidth = 0; //screen width in pixels
    //boolean values
    private boolean firstdraw = true; //whether the screen is being drawn for the first time. used for optimization
    private boolean newBall = true; //the previous ball was successfully clicked; redraw a new one at a random location within the border
    //Bitmap resources
    final Bitmap block = BitmapFactory.decodeResource(getResources(), R.drawable.block); //block image for the border
    final Bitmap bg = BitmapFactory.decodeResource(getResources(), R.drawable.bmmini); //tiled background image
    final Bitmap ball = BitmapFactory.decodeResource(getResources(), R.drawable.ball); //ball image
    //sizes of resources; checked only once for optimization
    final private short blockHeight = (short) block.getHeight();
    final private short blockWidth = (short) block.getWidth();
    final private short bgHeight = (short) bg.getHeight();
    final private short bgWidth = (short) bg.getWidth();
    final private short ballHeight = (short) ball.getHeight();
    final private short ballWidth = (short) ball.getWidth();
    //Boundaries of the area within the border
    private short tb = (short) blockHeight; //top boundary
    private short lb = (short) blockWidth; //left boundary
    private short bb = 0; //bottom boundary; changes later
    private short rb = 0; //right boundary; changes later
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Hide the "Touch The Ball" title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Put the application in fullscreen mode.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Prevent the screen from dimming during the game.
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "DoNotDimScreen");
        ExceptionHandler.register(this);
        setContentView(new Panel(this));
    }

    @Override
    public void onPause() {
        super.onPause();
        wl.release(); //release the wake lock
    }

    @Override
    public void onResume() {
        super.onResume();
        wl.acquire();
    }

    public boolean onTouch(View v, MotionEvent e) {
        //Only record it as a press if they press down, not if they let go.
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            this.x = (short) e.getX();
            this.y = (short) e.getY();
        }
        return true;
    }

    class Panel extends View {

        public Panel(Context context) {
            super(context);
        }

        @Override
        public void onDraw(Canvas canvas) {
            canvas.drawColor(Color.BLACK);
            //if it's the first draw, set the screen height and width values.
            if (firstdraw) {
                sHeight = (short) this.getHeight();
                sWidth = (short) this.getWidth();
                firstdraw = false;
            }
            //draw background
            for (short i = 0; i < sHeight; i += bgHeight) {
                for (short j = 0; j < sWidth; j += bgWidth) {
                    canvas.drawBitmap(bg, j, i, null);
                }
            }
            //draw top border and right side
            for (short i = 0; i < sHeight; i += blockWidth) {
                canvas.drawBitmap(block, i, 0, null);
                if (i + blockHeight >= sWidth) {
                    rb = (short) ((i * blockWidth) - blockWidth); //right boundary
                    for (int j = 0; j < sHeight; j += blockHeight) {
                        canvas.drawBitmap(block, i, j, null);
                    }
                }
            }
            //draw left side and bottom border
            for (short i = 0; i < sHeight; i += blockHeight) {
                canvas.drawBitmap(block, 0, i, null);
                if (i + blockHeight >= sHeight) {
                    bb = (short) (i * blockHeight - blockHeight);
                    for (int j = 0; j < sWidth; j += blockWidth) {
                        canvas.drawBitmap(block, j, i, null);
                    }
                }
            }
            if (newBall) {
                //draw ball at the center of the screen; random location coming soon.
                canvas.drawBitmap(ball, ((sWidth / 2) - (ballWidth / 2)), ((sHeight / 2) - (ballHeight / 2)), null);
            } else {
                canvas.drawBitmap(ball, bx, by, null);
            }
        }
    }
}
