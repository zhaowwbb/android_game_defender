package com.game.rick.defender.enemy;

import android.graphics.Bitmap;

import com.game.rick.defender.GameView;

/**
 * Created by rick on 17/01/2015.
 */
public class Soccer extends Enemy {
    private final static int DEFAULT_SPEED = 40;
    private boolean mMoveLeft;
    private int mOrigY;
    public Soccer(GameView gameView, Bitmap bmp)
    {
        super(gameView, bmp);
        this.mSpeed = DEFAULT_SPEED;
    }

    private int getVarPos()
    {
        return mSpeed / 4;
    }

    public void setY(int yPos)
    {
        super.setY(yPos);
        int mid = (mGameView.getWidth() - mWidth)/2;
        if(yPos > mid)
        {
            mMoveLeft = true;
        }
        else
        {
            mMoveLeft = false;
        }
        mOrigY = yPos;
    }

    protected void update() {

        mX = mX + mSpeed;
        int pos = (mOrigY * mSpeed) /mGameView.getHeight();
        if(mMoveLeft)
        {
            int yPos = mY - pos;
            if(yPos > 0)
            {
                mY = yPos;
            }
            else
            {
                mY = 0;
            }
        }
        else
        {
            int yPos = mY + pos;
            int right = mGameView.getWidth() - mWidth;
            if(yPos > right)
            {
                mY = right;
            }
            else
            {
                mY = yPos;
            }
        }

    }

    public boolean isCollision(float x2, float y2) {
        return x2 > mY && x2 < mY + mHeight && y2 > mX && y2 < mX + mWidth;
    }
}
