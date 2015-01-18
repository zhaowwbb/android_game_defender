package com.game.rick.defender.enemy;

import android.graphics.Bitmap;

import com.game.rick.defender.GameView;

/**
 * Created by rick on 17/01/2015.
 */
public class Basketball extends Enemy {
    private boolean isGoLeft;
    private final static int DEFAULT_SPEED = 20;

    public Basketball(GameView gameView, Bitmap bmp)
    {
        super(gameView, bmp);
        isGoLeft = true;
        mSpeed = DEFAULT_SPEED;
    }

    private int getVarPos()
    {
        return mSpeed * 3;
    }

    protected void update() {
        mX = mX + mSpeed;
        if(isGoLeft)
        {
            if(mY <= 0)
            {
                isGoLeft = false;
            }
            else
            {
                int pos = mY - getVarPos();
                if(pos > 0)
                {
                    mY = pos;
                }
                else
                {
                    mY = 0;
                }
            }
        }
        else
        {
            int rBorder = mGameView.getWidth() - mBmp.getWidth();
            if(mY >=  rBorder)
            {
                isGoLeft = true;
            }
            else
            {
                int pos = mY + getVarPos();
                if(pos < rBorder)
                {
                    mY = pos;
                }
                else
                {
                    mY = rBorder;
                }
            }
        }
    }
}
