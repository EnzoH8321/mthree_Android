package com.zybooks.diceroller.Controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GestureDetectorCompat;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zybooks.diceroller.Model.Dice;
import com.zybooks.diceroller.R;
import com.zybooks.diceroller.RollLengthDialogFragment;

public class MainActivity extends AppCompatActivity implements RollLengthDialogFragment.onRollLengthSelectedListener {

    public static final int MAX_DICE = 3;
    private Menu mMenu;
    private int mVisibleDice;
    private Dice[] mDice;
    private ImageView[] mDiceImageViews;
    private CountDownTimer mTimer;
    private TextView sumView;
    private long mTimerLength = 2000;
    private int mCurrentDie;
    private int mInitX;
    private int mInitY;
    private GestureDetectorCompat mDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sumView = findViewById(R.id.sum_textView);
        //Create an array of Dice
        mDice = new Dice[MAX_DICE];
        for (int i = 0; i < MAX_DICE; i++) {
            mDice[i] = new Dice(i + 1);
        }

        //Create an array of Imageviews
        mDiceImageViews = new ImageView[MAX_DICE];
        mDiceImageViews[0] = findViewById(R.id.dice1);
        mDiceImageViews[1] = findViewById(R.id.dice2);
        mDiceImageViews[2] = findViewById(R.id.dice3);
        //All dice are initially visible
        mVisibleDice = MAX_DICE;

        showDice();

        for (int i = 0; i < mDiceImageViews.length; i++) {

            mDiceImageViews[i].setTag(i);
        }

        mDetector = new GestureDetectorCompat(this, new DiceGestureListener());

//        //Moving finger left or right changes dice number
//        mDiceImageViews[0].setOnTouchListener((v,event) -> {
//            int action = event.getAction();
//
//            switch (action) {
//                case MotionEvent.ACTION_DOWN:
//                    mInitX = (int) event.getX();
//                case MotionEvent.ACTION_MOVE:
//                    int x = (int) event.getX();
//
//                    //See if movement is at least 20 pixels
//                    if (Math.abs(x - mInitX) >= 20) {
//                        if (x > mInitX) {
//                            mDice[0].addOne();
//                        }
//                        else {
//                            mDice[0].subtractOne();
//                        }
//                        showDice();
//                        mInitX = x;
//                    }
//                    return true;
//            }
//            return true;
//        });
//        //Move finger right or left
//        mDiceImageViews[0].setOnTouchListener((v,event) -> {
//            int action = event.getAction();
//
//            switch (action) {
//                case MotionEvent.ACTION_DOWN:
//                    mInitY = (int) event.getY();
//                case MotionEvent.ACTION_MOVE:
//                    int y = (int) event.getY();
//
//                    //See if movement is at least 20 pixels
//                    if (Math.abs(y - mInitY) >= 20) {
//                        if (y > mInitY) {
//                            mDice[0].addOne();
//                        }
//                        else {
//                            mDice[0].subtractOne();
//                        }
//                        showDice();
//                        mInitY = y;
//                    }
//                    return true;
//            }
//            return true;
//        });
    }

    private class DiceGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            if (velocityY > 0) {
                rollDice();
            }


            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            rollDice();
            return true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        mCurrentDie = (int) v.getTag(); //which die is selected
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_one) {
            mDice[mCurrentDie].addOne();
            showDice();
            return true;
        }
        else if (item.getItemId() == R.id.subtract_one) {
            mDice[mCurrentDie].subtractOne();
            showDice();
            return true;
        }
        else if (item.getItemId() == R.id.roll) {
            rollDice();
            return true;
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public void onRollLengthClick(int which) {
        //convert to milliseconds
        mTimerLength = 1000L * (which + 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar_menu, menu);
        mMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_one) {
            changeDiceVisibility(1);
            showDice();
            return true;
        } else if (item.getItemId() == R.id.action_two) {
            changeDiceVisibility(2);
            showDice();
            return true;
        } else if (item.getItemId() == R.id.action_three) {
            changeDiceVisibility(3);
            showDice();
            return true;
        } else if (item.getItemId() == R.id.action_stop) {
            mTimer.cancel();
            item.setVisible(false);
            return true;
        } else if (item.getItemId() == R.id.action_roll) {
            rollDice();
            return true;
        } else if (item.getItemId() == R.id.action_roll_length) {
            RollLengthDialogFragment dialog = new RollLengthDialogFragment();
            dialog.show(getSupportFragmentManager(), "rollLengthDialog");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void rollDice() {
        mMenu.findItem(R.id.action_stop).setVisible(true);

        if (mTimer != null) {
            mTimer.cancel();
        }

        mTimer = new CountDownTimer(mTimerLength, 100) {
            @Override
            public void onTick(long l) {
                for (int i = 0; i < mVisibleDice; i++) {
                    mDice[i].roll();
                }
                showDice();
            }

            @Override
            public void onFinish() {
                mMenu.findItem(R.id.action_stop).setVisible(false);


                mDiceImageViews[0].setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.red_900));
                mDiceImageViews[1].setColorFilter(Color.parseColor(getResources().getString(R.color.red_900)));
                mDiceImageViews[2].setColorFilter(Color.parseColor(getResources().getString(R.color.red_900)));

                int firstDice = mDice[0].getValueNumber();
                int secondDice = mDice[1].getValueNumber();
                int thirdDice = mDice[2].getValueNumber();
                int sum = firstDice + secondDice + thirdDice;

                //Set toast
                Context context = getApplicationContext();
                CharSequence textWinner = "You are a winner!";
                CharSequence textLoser = "You are a loser";
                int duration = Toast.LENGTH_SHORT;


                if (mVisibleDice == 2) {
                    if (sum % 7 == 0 || sum % 11 == 0 ) {
                        Toast toast = Toast.makeText(context, textWinner, duration);
                        toast.show();
                        mDiceImageViews[0].setColorFilter(Color.argb(1.0F,56,39,212));
                        mDiceImageViews[1].setColorFilter(Color.argb(1.0F,56,39,212));
                        mDiceImageViews[2].setColorFilter(Color.argb(1.0F,56,39,212));
                    } else if (sum % 1 == 0 || sum % 12 == 0) {
                        Toast toast = Toast.makeText(context, textLoser, duration);
                        toast.show();
                        mDiceImageViews[0].setColorFilter(Color.argb(1.0F,0,0,0));
                        mDiceImageViews[1].setColorFilter(Color.argb(1.0F,0,0,0));
                        mDiceImageViews[2].setColorFilter(Color.argb(1.0F,0,0,0));
                    }
                } else if (mVisibleDice == 3) {
                    if (sum % 7 == 0 || sum % 11 == 0) {
                        Toast toast = Toast.makeText(context, textWinner, duration);
                        toast.show();
                        mDiceImageViews[0].setColorFilter(Color.argb(1.0F,56,39,212));
                        mDiceImageViews[1].setColorFilter(Color.argb(1.0F,56,39,212));
                        mDiceImageViews[2].setColorFilter(Color.argb(1.0F,56,39,212));
                    } else if (sum == 18 || sum == 3) {
                        Toast toast = Toast.makeText(context, textLoser, duration);
                        toast.show();
                        mDiceImageViews[0].setColorFilter(Color.argb(1.0F,0,0,0));
                        mDiceImageViews[1].setColorFilter(Color.argb(1.0F,0,0,0));
                        mDiceImageViews[2].setColorFilter(Color.argb(1.0F,0,0,0));
                    }
                }

                sumView.setText(String.valueOf("The score is: " + sum));
            }
        }.start();
    }

    private void changeDiceVisibility(int numVisible) {
        mVisibleDice = numVisible;

        //Make dice visible
        for (int i = 0; i < numVisible; i++) {
            mDiceImageViews[i].setVisibility(View.VISIBLE);
        }

        //hide remaining dice
        for (int i = numVisible; i < MAX_DICE; i++) {
            mDiceImageViews[i].setVisibility(View.GONE);
        }
    }

    private void showDice() {
        // Display only the number of dice visible
        for (int i = 0; i < mVisibleDice; i++) {
            Drawable diceDrawable = ContextCompat.getDrawable(this, mDice[i].getImageId());
            mDiceImageViews[i].setImageDrawable(diceDrawable);
            //for sight impaired users
            mDiceImageViews[i].setContentDescription(Integer.toString(mDice[i].getNumber()));
        }
    }


}