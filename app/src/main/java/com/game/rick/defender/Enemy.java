package com.game.rick.defender;

/**
 * Created by Rick on 2015/1/7.
 */

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Enemy {
    private int x = 0;
    private int y = 0;
    private int speed;
    private int width;
    private int height;
    private GameView gameView;
    private Bitmap bmp;
    private boolean isDead;

    public Enemy(GameView gameView, Bitmap bmp, int xPos, int yPos, int speed) {
        this.width = bmp.getWidth();
        this.height = bmp.getHeight();
        this.gameView = gameView;
        this.bmp = bmp;
        this.speed = speed;
        this.x = xPos;
        this.y = yPos;
        this.isDead = false;
    }

    protected void update() {
        x = x + speed;
//        int nextPos = x + speed;
//        if(nextPos < gameView.getHeight() )
//        {
//            x = nextPos;
//        }
    }

    public boolean isDead()
    {
        return this.isDead;
    }

    public void onDraw(Canvas canvas) {
        update();
//        int srcX = currentFrame * width;
//        int srcY = getAnimationRow() * height;
      //  Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
//        if(x > gameView.getHeight())
//        {
//            Rect dst = new Rect(x, y, x + width, y + height);
//            canvas.drawBitmap(bmp, dst, dst, null);
//        }

        if(x > 0)
        {
            int hitPos = x + height;
            if(gameView.hitGuard(hitPos))
            {
                if(!gameView.isGameOver())
                {
                    gameView.killGuard(y);
                }
                isDead = true;
            }
            else
            {
                canvas.drawBitmap(bmp, y, x, null);
            }
        }

//        if(gameView.hitGuard(x))
//        {
//            if(!gameView.isGameOver())
//            {
//                gameView.killGuard(y);
//            }
//        }
//        else
//        {
//            if(x > 0)
//            {
//
//            }
//        }
    }

    public boolean isCollision(float x2, float y2) {
        return x2 > y && x2 < y + height && y2 > x && y2 < x + width;
    }
}
