/* Catch The Ball by Dylan Taylor
 * ------------------------------
 * An attempt at making a basic game for the Android platform.
 * The idea is to have a basic circle bouncing around the screen, and have a
 * timer (based on the difficulty) that the user must tap the ball within.
 * As the user progresses through levels, the ball will bounce faster.
 * If the user taps and misses, one seconds will be subtracted from the time left.
 * Difficulties and Timer Length:
 *  Easy        12 Seconds
 *  Medium      9 Seconds
 *  Hard        6 Seconds
 *  Insane      3 Seconds
 *
 *  This file is part of Catch The Ball.
 *
 *  Catch The Ball is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Catch The Ball is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Catch The Ball.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.dylantaylor.ctb;

import android.view.Window;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import com.nullwire.trace.ExceptionHandler;

/**
 *
 * @author Dylan Taylor
 */
public class CTB extends Activity implements OnClickListener {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        ExceptionHandler.register(this);
        setContentView(R.layout.main);
        View easyButton = findViewById(R.id.easy_button);
        easyButton.setOnClickListener(this);
        View mediumButton = findViewById(R.id.medium_button);
        mediumButton.setOnClickListener(this);
        View hardButton = findViewById(R.id.hard_button);
        hardButton.setOnClickListener(this);
        View insaneButton = findViewById(R.id.insane_button);
        insaneButton.setOnClickListener(this);
        View exitButton = findViewById(R.id.exit_button);
        exitButton.setOnClickListener(this);
        View aboutButton = findViewById(R.id.about_button);
        aboutButton.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.easy_button:
                startGame(12);
                break;
            case R.id.medium_button:
                startGame(9);
                break;
            case R.id.hard_button:
                startGame(6);
                break;
            case R.id.insane_button:
                startGame(3);
                break;
            case R.id.about_button:
                Intent a = new Intent(this, AboutDialog.class);
                startActivity(a);
                break;
            case R.id.exit_button:
                this.finish();
                break;
        }
    }

    private void startGame(int time) { //starts the game, with a 'time' second limit
        Intent intent = new Intent(this, CTBView.class); //creates a new intent
        intent.putExtra("timer", time); //adds timer data to the intent
        sendBroadcast(intent); //should send the timer data to the activity
        startActivity(intent); //starts the activity
    }
}
