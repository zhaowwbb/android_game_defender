package com.game.rick.defender;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by Rick on 2015/1/9.
 */
public class Guard {
    private int x;
    private int y;
    private GameView gameView;
    private Bitmap bmp;
    private int width;
    private boolean isAlive;

    public Guard(GameView gameView, Bitmap bmp, int xPos, int yPos, int width) {
        this.width = width;
        this.gameView = gameView;
        this.bmp = bmp;
        this.x = xPos;
        this.y = yPos;
        this.isAlive = true;
    }

    public boolean isAlive()
    {
        return this.isAlive;
    }

    public boolean isDead(int pos)
    {
        if(pos >= y && pos <= (y+width) )
        {
            isAlive = false;
            return true;
        }
        return false;
    }

    public void onDraw(Canvas canvas) {
//        Rect dst = new Rect(x, y, x + width, y + width);
        Rect dst = new Rect( y, x, y + width,x + width);
        canvas.drawBitmap(bmp, null, dst, null);
//        canvas.drawBitmap(bmp, y, x, null);
    }
}
