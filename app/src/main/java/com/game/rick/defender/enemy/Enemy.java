package com.game.rick.defender.enemy;

/**
 * Created by Rick on 2015/1/7.
 */

import android.graphics.Bitmap;
import android.graphics.Canvas;
//import android.graphics.Rect;

import com.game.rick.defender.GameView;

public class Enemy {
    protected int mX = 0;
    protected int mY = 0;
    protected int mSpeed;
    protected int mWidth;
    protected int mHeight;
    protected GameView mGameView;
    protected Bitmap mBmp;
    protected boolean mIsDead;
    public final static int DEFAULT_SPEED = 20;

    public Enemy(GameView gameView, Bitmap bmp)
    {
        this.mGameView = gameView;
        this.mBmp = bmp;
        this.mWidth = bmp.getWidth();
        this.mHeight = bmp.getHeight();
        this.mIsDead = false;
        this.mSpeed = DEFAULT_SPEED;
    }

    public void setX(int xPos)
    {
        this.mX = xPos;
    }

    public void setY(int yPos)
    {
        this.mY = yPos;
    }

    public int getY()
    {
        return this.mY;
    }

    public int getWidth()
    {
        return this.mWidth;
    }

    public int getHeight()
    {
        return this.mHeight;
    }

    public void setSpeed(int speed)
    {
        this.mSpeed = speed;
    }

    protected void update() {
        mX = mX + mSpeed;
    }

    public boolean isDead()
    {
        return this.mIsDead;
    }

    protected void checkKill(Canvas canvas)
    {
        if(mX > 0)
        {
            int hitPos = mX + mHeight;
            if(mGameView.hitGuard(hitPos))
            {
                if(!mGameView.isGameOver())
                {
                    mGameView.killGuard(this);
                }
                mIsDead = true;
            }
            else
            {
                canvas.drawBitmap(mBmp, mY, mX, null);
            }
        }
    }

    public void onDraw(Canvas canvas) {
        update();
        checkKill(canvas);
    }

    public boolean isCollision(float x2, float y2) {
        return x2 > mY && x2 < mY + mHeight && y2 > mX && y2 < mX + mWidth;
    }

    public String toString()
    {
        return "[x=" + mX + ",y=" + mY + ",img=" +  mBmp + ",speed=" + mSpeed + "]";
    }
}
