package com.game.rick.defender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.game.rick.defender.enemy.Basketball;
import com.game.rick.defender.enemy.Enemy;
import com.game.rick.defender.enemy.EnemyConst;
import com.game.rick.defender.enemy.EnemyFactory;
import com.game.rick.defender.enemy.Soccer;

public class GameView extends SurfaceView {
    private GameLoopThread gameLoopThread;
    private List<TempSprite> temps = new ArrayList<TempSprite>();
    private long lastClick;
    private Bitmap bmpBlood;
    private Bitmap bmpGens;
    private Bitmap bmpSkull;
    private Bitmap bmpGuard;
    private Bitmap bmpBird;

    private Bitmap mBmpHeart;
    private Bitmap mBmpSoccer;
    private Bitmap mBmpDeath;
    private Bitmap mBmpGhost;
    private Bitmap mBmpDiamond;
    private Bitmap mBmpPepsi;
    private Bitmap mBmpBasketBall;
    private Bitmap mBmpComputer;

    private List<Enemy> enemies = new ArrayList<Enemy>();
    private boolean isGameOver;
    private int totalGuards;
    private List<Guard> guards = new ArrayList<Guard>();
    private int guardBorder;
    private int mLevel;
    private int guardIconWidth;
    private final int HELP_INTERVAL = 3000;
    private final int LEVEL_INTERVAL = 3000;
    private long mStartTime;
    private boolean mLevelPassFlag;
    private long mLevelPassTime;
    private boolean mAllLevelPassFlag;
    private EnemyFactory mFactory;
    private Map<String, List<EnemyItem>> mLevelMap;

    //Level and  enemiese
    private final static String[][] levels = {
            {},
            /** level id, enemy type, enemy number, is random position    */
            {"1", EnemyConst.DIAMOND, "20", Boolean.toString(false)},
            {"2", EnemyConst.DIAMOND, "20", Boolean.toString(true), "30"},
            {"3", EnemyConst.HEART, "30", Boolean.toString(false)},
            {"4", EnemyConst.HEART, "40", Boolean.toString(true), "30"},

            {"5", EnemyConst.HEART, "10", Boolean.toString(true)},
            {"5", EnemyConst.PEPSI, "20", Boolean.toString(true)},

            {"6", EnemyConst.HEART, "8", Boolean.toString(true), "30"},
            {"6", EnemyConst.PEPSI, "20", Boolean.toString(false), "30"},
            {"6", EnemyConst.SOCCER, "2", Boolean.toString(true)},

            {"7", EnemyConst.PEPSI, "20", Boolean.toString(true), "30"},
            {"7", EnemyConst.SOCCER, "5", Boolean.toString(true)},
            {"7", EnemyConst.HEART, "8", Boolean.toString(true), "30"},
            {"7", EnemyConst.BASKETBALL, "2", Boolean.toString(true)},

            {"8", EnemyConst.DIAMOND, "10", Boolean.toString(true), "30"},
            {"8", EnemyConst.HEART, "10", Boolean.toString(true), "30"},
            {"8", EnemyConst.PEPSI, "10", Boolean.toString(true), "30"},
            {"8", EnemyConst.SOCCER, "10", Boolean.toString(true) },
            {"8", EnemyConst.BASKETBALL, "10", Boolean.toString(true)},

            {"9", EnemyConst.PEPSI, "20", Boolean.toString(true), "30"},
            {"9", EnemyConst.SOCCER, "20", Boolean.toString(true) },
            {"9", EnemyConst.BASKETBALL, "20", Boolean.toString(true)},

            {"10", EnemyConst.PEPSI, "50", Boolean.toString(true), "30"},
            {"10", EnemyConst.SOCCER, "30", Boolean.toString(true) },
            {"10", EnemyConst.BASKETBALL, "30", Boolean.toString(true)}
    };

    private class EnemyItem
    {
        String mType;
        int mCount;
        boolean mIsRandomPos;
        int mSpeed;
        public EnemyItem(String type, int count, boolean isRandom)
        {
            this.mType = type;
            this.mCount = count;
            this.mIsRandomPos = isRandom;
            this.mSpeed = EnemyConst.NO_SPEED;
        }

        public EnemyItem(String type, int count, boolean isRandom, int speed)
        {
            this.mType = type;
            this.mCount = count;
            this.mIsRandomPos = isRandom;
            this.mSpeed = speed;
        }

        public String toString()
        {
            return "[mType=" + mType + ",mCount=" + mCount + ",mIsRandomPos=" +  mIsRandomPos + ",mSpeed= " + mSpeed + "]";
        }
    }

    public GameView(Context context) {
        super(context);
        gameLoopThread = new GameLoopThread(this);
        getHolder().addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                boolean retry = true;
                gameLoopThread.setRunning(false);
                while (retry) {
                    try {
                        gameLoopThread.join();
                        retry = false;
                    } catch (InterruptedException e) {}
                }
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                init();
                gameLoopThread.setRunning(true);
                gameLoopThread.start();
                mStartTime = System.currentTimeMillis();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {
            }
        });

        loadImage();
    }

    private void loadImage()
    {
        bmpBlood = BitmapFactory.decodeResource(getResources(), R.drawable.blood1);
        bmpGens = BitmapFactory.decodeResource(getResources(), R.drawable.gens);
        bmpSkull = BitmapFactory.decodeResource(getResources(), R.drawable.skull);
        bmpBird = BitmapFactory.decodeResource(getResources(), R.drawable.bird);

        mBmpHeart = BitmapFactory.decodeResource(getResources(), R.drawable.heart);
        mBmpSoccer = BitmapFactory.decodeResource(getResources(), R.drawable.soccer);
        mBmpDeath = BitmapFactory.decodeResource(getResources(), R.drawable.death);
        mBmpGhost = BitmapFactory.decodeResource(getResources(), R.drawable.ghost);
        mBmpDiamond = BitmapFactory.decodeResource(getResources(), R.drawable.diamond);
        mBmpPepsi = BitmapFactory.decodeResource(getResources(), R.drawable.pepsi);
        mBmpBasketBall = BitmapFactory.decodeResource(getResources(), R.drawable.basketball);
        mBmpComputer = BitmapFactory.decodeResource(getResources(), R.drawable.computer);
    }

    public Bitmap getBitMap(String name)
    {
        Bitmap bmp = mBmpHeart;
        if(EnemyConst.HEART.equals(name))
        {
            bmp = mBmpHeart;
        }
        else if(EnemyConst.DIAMOND.equals(name))
        {
            bmp = mBmpDiamond;
        }
        else if(EnemyConst.SOCCER.equals(name))
        {
            bmp = mBmpSoccer;
        }
        else if(EnemyConst.BASKETBALL.equals(name))
        {
            bmp = mBmpBasketBall;
        }
        else if(EnemyConst.GHOST.equals(name))
        {
            bmp = mBmpGhost;
        }
        else if(EnemyConst.DEATH.equals(name))
        {
            bmp = mBmpDeath;
        }
        else if(EnemyConst.PEPSI.equals(name))
        {
            bmp = mBmpPepsi;
        }
        else if(EnemyConst.COMPUTER.equals(name))
        {
            bmp = mBmpComputer;
        }
        return bmp;
    }

    private void initLevels()
    {
        mLevelMap = new HashMap<String, List<EnemyItem>>();
        for(int i = 1; i < levels.length; i++)
        {
            String[] strs = levels[i];
            String levelStr = strs[0];
            String type = strs[1];
            int count = Integer.parseInt(strs[2]);
            boolean isRandom = Boolean.parseBoolean(strs[3]);
            EnemyItem item = null;
            if(strs.length > 4)
            {
                int speed = Integer.parseInt(strs[4]);
                item = new EnemyItem(type, count, isRandom, speed);
            }
            else
            {
                item = new EnemyItem(type, count, isRandom);
            }

            List<EnemyItem> list = mLevelMap.get(levelStr);
            if(null == list)
            {
                list = new ArrayList<EnemyItem>();
                mLevelMap.put(levelStr, list);
            }
            list.add(item);
        }
    }

    private void createEnemy(int level)
    {
        String levelStr = String.valueOf(level);
        temps.clear();
        mFactory.resetEnemies();
        List<EnemyItem> list = mLevelMap.get(levelStr);
        if(list != null)
        {
            for(int i = 0; i < list.size(); i++)
            {
                EnemyItem item = list.get(i);
                for(int j = 0; j < item.mCount; j++)
                {
                    Enemy e = null;
                    if(item.mSpeed == EnemyConst.NO_SPEED)
                    {
                        e = mFactory.createEnemy(item.mType, item.mIsRandomPos);
                    }
                    else
                    {
                        e = mFactory.createEnemy(item.mType, item.mIsRandomPos, item.mSpeed);
                    }

                    enemies.add(e);
                }
            }
        }
        else
        {
            mAllLevelPassFlag = true;
        }
    }

    private void init()
    {
        isGameOver = false;
        totalGuards = 0;
        mLevel = 1;
        mLevelPassFlag = false;
        mAllLevelPassFlag = false;
        mFactory = EnemyFactory.getInstance(this);
        initLevels();
        createEnemy(mLevel);
        createGuards();
    }

    public void setGameOver(boolean isOver)
    {
        this.isGameOver = isOver;
    }

    public boolean isGameOver()
    {
        return this.isGameOver;
    }

    public boolean hitGuard(int x)
    {
        return x >= guardBorder;
    }

    public boolean killGuard(Enemy e)
    {
        boolean ret = false;
        for(int i = guards.size() - 1; i >= 0; i--)
        {
            Guard g = guards.get(i);
            int attackLeft = e.getY();
            int attackRight = e.getY() + e.getWidth();

            int guardLeft = g.getY();
            int guardRight = g.getY() + g.getWidth() ;

            if(attackRight < guardLeft)continue;
            if(attackLeft > guardRight)continue;
            guards.remove(g);
        }

        if(guards.size() == 0)
        {
            isGameOver = true;
        }
        return ret;
    }

    private void createGuards()
    {
        bmpGuard = BitmapFactory.decodeResource(getResources(), R.drawable.halloween);
        totalGuards = this.getWidth()/bmpGuard.getWidth();

        guardIconWidth = this.getWidth()/totalGuards;

        int x = this.getHeight() - guardIconWidth;
        for(int i = 0; i < totalGuards; i++)
        {
            int y = i*guardIconWidth;
            Guard g = new Guard(this, bmpGuard, x, y, guardIconWidth);
            guards.add(g);
        }
        this.guardBorder = this.getHeight() - guardIconWidth;
    }

    private boolean printHelp(Canvas canvas)
    {
        if(System.currentTimeMillis() - mStartTime < HELP_INTERVAL)
        {
            Paint hPaint = new Paint();
            hPaint.setTextSize(40);
            hPaint.setColor(Color.CYAN);
            hPaint.setTextAlign(Paint.Align.LEFT);

            int picX = (this.getHeight() - bmpGens.getHeight())/2;
            int picY = (this.getWidth() - bmpGens.getWidth())/2;
            canvas.drawBitmap(bmpGens,picY, picX, hPaint);

            String str1 = "Protect bottom items";
            String str2 = "Tap enemy to destroy";

            canvas.drawText(str1, (this.getWidth()/2) - 200, picX - 130 > 0 ? picX - 130 : 0,hPaint);
            canvas.drawText(str2, (this.getWidth()/2) - 200, picX - 50,hPaint);

//            Rect dst = new Rect(10, 50, 210, 350);
//            canvas.drawBitmap(bmpGens, null, dst, null);
            return true;
        }
        return false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        if(printHelp(canvas))
        {
            return;
        }
        if(isGameOver)
        {
            int x = (this.getWidth()/2) - 200;
            int y = (this.getHeight()/2) - 200;
            Rect dst = new Rect(x, y, x + 400, y + 400);
            canvas.drawBitmap(bmpSkull, null, dst, null);
        }
        else
        {
            checkEnemies();
            //show level passed message
            if(!mAllLevelPassFlag && levelPassed(mLevel, canvas))
            {
                return;
            }
            for (int i = temps.size() - 1; i >= 0; i--) {
                temps.get(i).onDraw(canvas);
            }

            for(Enemy e : enemies)
            {
                e.onDraw(canvas);
            }
            for(Guard g : guards)
            {
                if(g.isAlive())
                {
                    g.onDraw(canvas);
                }
            }
            showStat(canvas);
            if(mAllLevelPassFlag)
            {
                allLevelPassed(canvas);
            }
//            int gameHeight = this.getHeight();
//            int genHeight = bmpGens.getHeight();
//            int genX = this.getHeight() - bmpGens.getHeight();
//            int genY = this.getWidth() - bmpGens.getWidth();
//            Rect gendst = new Rect(genX, genY, genX + 128, genY + 128);
//            canvas.drawBitmap(bmpSkull, null, gendst, null);
//            canvas.drawBitmap(bmpGens, 0, genX, null);
        }

//        Paint p = new Paint(Color.BLUE);
//        String s = "AAAA:" + enemies.size();
//        canvas.drawText(s, 30, 30, p);
    }

    private void showStat(Canvas canvas) {
        Paint green = new Paint();
//        green.setARGB(500,23,45,100);
        green.setColor(Color.CYAN);
        green.setStyle(Paint.Style.FILL);
        Rect rec = new Rect();
        rec.set(0,60,this.getWidth(),80);
        canvas.drawRect(rec,green);
        Paint who = new Paint();
        who.setTextSize(25);
        who.setColor(Color.RED);
        who.setTextAlign(Paint.Align.LEFT);
        String str = "Remained Enemies:" + enemies.size();
        canvas.drawText(str, 10,40,who);

        Paint levelPaint = new Paint();
        levelPaint.setTextSize(25);
        levelPaint.setColor(Color.BLUE);
        levelPaint.setTextAlign(Paint.Align.RIGHT);
        String levelStr = "Level: " + mLevel;
        canvas.drawText(levelStr, 530,40,levelPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (System.currentTimeMillis() - lastClick > 300) {
            lastClick = System.currentTimeMillis();
            float x = event.getX();
            float y = event.getY();
            synchronized (getHolder()) {
                for (int i = enemies.size() - 1; i >= 0; i--) {
                    Enemy e = enemies.get(i);
                    if (e.isCollision(x, y)) {
                        enemies.remove(e);
                        temps.add(new TempSprite(temps, this, x, y, bmpBlood));
                        break;
                    }
                }
            }
        }
        return true;
    }

    private void checkEnemies()
    {
        synchronized (getHolder()) {
            for (int i = enemies.size() - 1; i >= 0; i--) {
                Enemy e = enemies.get(i);
                if(e.isDead())
                {
                    enemies.remove(e);
                }
            }
            if(!mLevelPassFlag)
            {
                if(enemies.size() == 0 && !isGameOver)
                {
                    mLevelPassFlag = true;
                    // level++;
                    mLevelPassTime = System.currentTimeMillis();
                }
            }
        }
    }

    private boolean levelPassed(int level, Canvas canvas)
    {
        if(mLevelPassFlag && System.currentTimeMillis() - mLevelPassTime > LEVEL_INTERVAL)
        {
            mLevelPassFlag = false;
            this.mLevel = level + 1;
            createEnemy(this.mLevel);
        }

        if(mLevelPassFlag)
        {
            Paint hPaint = new Paint();
            hPaint.setTextSize(50);
            hPaint.setColor(Color.CYAN);
            hPaint.setTextAlign(Paint.Align.LEFT);

            int picX = (this.getHeight() - bmpGens.getHeight())/2;
            int picY = (this.getWidth() - bmpGens.getWidth())/2;
            canvas.drawBitmap(bmpGens,picY, picX, hPaint);

            String str = "Level " + level + " passed!";
            canvas.drawText(str, (this.getWidth() / 2) - 200, picX - 130 > 0 ? picX - 130 : 0,hPaint);
            return true;
        }
        return false;
    }

    private void allLevelPassed(Canvas canvas)
    {
        Paint hPaint = new Paint();
        hPaint.setTextSize(60);
        hPaint.setColor(Color.CYAN);
        hPaint.setTextAlign(Paint.Align.LEFT);

        int picX = (this.getHeight() - bmpGens.getHeight())/2;
        int picY = (this.getWidth() - bmpGens.getWidth())/2;
        canvas.drawBitmap(bmpBird,picY, picX, hPaint);

        String str = "All Levels passed!";
        canvas.drawText(str, (this.getWidth() / 2) - 200, picX - 130 > 0 ? picX - 130 : 0,hPaint);
    }
}