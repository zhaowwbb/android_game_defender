package com.game.rick.defender.enemy;

import com.game.rick.defender.GameView;

import java.util.Random;

/**
 * Created by rick on 17/01/2015.
 */
public class EnemyFactory {
    private static EnemyFactory mInstance;
    private static Random mRandom;
    private static GameView mView;
    private int mLastEnemyXPos;
    private int mLastEnemyYPos;


    private EnemyFactory()
    {
        resetEnemies();
        mRandom = new Random();
    }

    public void resetEnemies()
    {
        mLastEnemyXPos = 0;
        mLastEnemyYPos = 0;
    }

    public synchronized static EnemyFactory getInstance(GameView view)
    {
        if(null == mInstance)
        {
            mInstance = new EnemyFactory();
            mView = view;
        }
        return mInstance;
    }

    private void setSequencePosition(Enemy e)
    {
        int xPos = 0;
        int yPos = 0;
        int xWidth = mView.getWidth() - e.getWidth();

        xPos = mLastEnemyXPos - e.getHeight();
        mLastEnemyXPos = xPos;
        yPos = mLastEnemyYPos + e.getWidth();
        if (yPos > xWidth) {
            yPos = xWidth;
            mLastEnemyYPos = 0 - e.getWidth();
        }
        else
        {
            mLastEnemyYPos = yPos;
        }

        e.setX(xPos);
        e.setY(yPos);
    }

    private void setRandomPosition(Enemy e)
    {
        int xPos = 0;
        int yPos = 0;
        int xWidth = mView.getWidth() - e.getWidth();
        int columns = mView.getWidth()/e.getWidth();
        int next = mRandom.nextInt(columns);
        xPos = mLastEnemyXPos - e.getHeight();
        mLastEnemyXPos = xPos;
        yPos = e.getWidth() * next;
        if (yPos > xWidth) {
            yPos = xWidth;
        }
        e.setX(xPos);
        e.setY(yPos);
    }

    public Enemy createEnemy(String name, boolean isRandom)
    {
        Enemy e = null;
        if(EnemyConst.HEART == name)
        {
            e = new Enemy(mView, mView.getBitMap(EnemyConst.HEART));
        }
        else if(EnemyConst.DIAMOND == name)
        {
            e = new Enemy(mView, mView.getBitMap(EnemyConst.DIAMOND));
        }
        else if(EnemyConst.SOCCER == name)
        {
            e = new Soccer(mView, mView.getBitMap(EnemyConst.SOCCER));
        }
        else if(EnemyConst.BASKETBALL == name)
        {
            e = new Basketball(mView, mView.getBitMap(EnemyConst.BASKETBALL));
        }
        else if(EnemyConst.GHOST == name)
        {
            e = new Enemy(mView, mView.getBitMap(EnemyConst.GHOST));
        }
        else if(EnemyConst.DEATH == name)
        {
            e = new Enemy(mView, mView.getBitMap(EnemyConst.DEATH));
        }
        else if(EnemyConst.PEPSI == name)
        {
            e = new Enemy(mView, mView.getBitMap(EnemyConst.PEPSI));
        }
        else if(EnemyConst.COMPUTER == name)
        {
            e = new Enemy(mView, mView.getBitMap(EnemyConst.COMPUTER));
        }
        if(isRandom)
        {
            setRandomPosition(e);
        }
        else
        {
            setSequencePosition(e);
        }
        return e;
    }

    public Enemy createEnemy(String name, boolean isRandom, int speed)
    {
        Enemy e = createEnemy(name, isRandom);
        e.setSpeed(speed);
        return e;
    }
}
