package com.game.rick.defender;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by Rick on 2015/1/9.
 */
public class Guard {
    private int mX;
    private int mY;
    private GameView gameView;
    private Bitmap mBmp;
    private int mWidth;
    private boolean isAlive;

    public Guard(GameView gameView, Bitmap bmp, int xPos, int yPos, int width) {
        this.mWidth = width;
        this.gameView = gameView;
        this.mBmp = bmp;
        this.mX = xPos;
        this.mY = yPos;
        this.isAlive = true;
    }

    public int getWidth()
    {
        return this.mWidth;
    }

    public int getY()
    {
        return this.mY;
    }

    public boolean isAlive()
    {
        return this.isAlive;
    }

    public boolean isDead(int pos)
    {
        if(pos >= mY && pos <= (mY + mWidth) )
        {
            isAlive = false;
            return true;
        }
        return false;
    }

    public void onDraw(Canvas canvas) {
//        Rect dst = new Rect(x, y, x + width, y + width);
        Rect dst = new Rect( mY, mX, mY + mWidth,mX + mWidth);
        canvas.drawBitmap(mBmp, null, dst, null);
//        canvas.drawBitmap(bmp, y, x, null);
    }
}
