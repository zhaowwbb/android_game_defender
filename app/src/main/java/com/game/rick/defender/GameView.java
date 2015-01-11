package com.game.rick.defender;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
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

public class GameView extends SurfaceView {
    private GameLoopThread gameLoopThread;
    private List<TempSprite> temps = new ArrayList<TempSprite>();
    private long lastClick;
    private Bitmap bmpBlood;
    private Bitmap bmpGens;
    private Bitmap bmpSkull;
    private Bitmap bmpGuard;
    private Bitmap bmpBird;

    private Bitmap bmpHeart;
    private Bitmap bmpSoccer;
    private Bitmap bmpDeath;
    private Bitmap bmpGhost;
    private Bitmap bmpDiamond;
    private Bitmap bmpPepsi;
    private Bitmap bmpBasketBall;
    private Bitmap bmpComputer;

    private List<Enemy> enemies = new ArrayList<Enemy>();
    private boolean isGameOver;
    private int totalGuards;
    private List<Guard> guards = new ArrayList<Guard>();
    private int guardBorder;
    private int mLevel;
    private int guardIconWidth;
    private final int HELP_INTERVAL = 5000;
    private final int LEVEL_INTERVAL = 3000;
    private long mStartTime;
    private boolean mLevelPassFlag;
    private long mLevelPassTime;
    private boolean mAllLevelPassFlag;

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

        bmpHeart = BitmapFactory.decodeResource(getResources(), R.drawable.heart);
        bmpSoccer = BitmapFactory.decodeResource(getResources(), R.drawable.soccer);
        bmpDeath = BitmapFactory.decodeResource(getResources(), R.drawable.death);
        bmpGhost = BitmapFactory.decodeResource(getResources(), R.drawable.ghost);
        bmpDiamond = BitmapFactory.decodeResource(getResources(), R.drawable.diamond);
        bmpPepsi = BitmapFactory.decodeResource(getResources(), R.drawable.pepsi);
        bmpBasketBall = BitmapFactory.decodeResource(getResources(), R.drawable.basketball);
        bmpComputer = BitmapFactory.decodeResource(getResources(), R.drawable.computer);
    }

    private void addEnemies(Bitmap bmp, int count, int speed)
    {
        Random rnd = new Random();
        int xPos = 0;
        int yPos = 0;
        int xWidth = this.getWidth() - bmp.getWidth();
        int columns = this.getWidth()/bmp.getWidth();

        for(int i = 0; i < count; i++) {
            xPos = 0 - i * bmp.getHeight();
            //int xPos = rnd.nextInt(xWidth);
            int next = rnd.nextInt(columns);
            yPos = bmp.getWidth() * next;
            if (yPos > xWidth) {
                yPos = xWidth;
            }
            enemies.add(new Enemy(this, bmp, xPos, yPos, speed));
        }
    }

    private void createEnemy(int level)
    {
        if(level == 1)
        {
            addEnemies(bmpHeart, 10, 5);
        }
        else if(level == 2)
        {
            addEnemies(bmpSoccer, 20, 10);
        }
        else if(level == 3)
        {
            addEnemies(bmpDiamond, 30, 15);
        }
        else if(level == 4)
        {
            addEnemies(bmpBasketBall, 40, 20);
        }
        else if(level == 5)
        {
            addEnemies(bmpComputer, 50, 25);
        }
        else if(level == 6)
        {
            addEnemies(bmpPepsi, 60, 30);
        }
        else if(level == 7)
        {
            addEnemies(bmpGhost, 70, 35);
        }
        else if(level == 8)
        {
            addEnemies(bmpDeath, 80, 40);
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

    public boolean killGuard(int y)
    {
        boolean ret = false;
        for(int i = guards.size() - 1; i >= 0; i--)
        {
            Guard g = guards.get(i);
            if(g.isDead(y))
            {
                guards.remove(g);
            }
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

//    private void createEnemy() {
//        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ghost);
//        int count = 100;
//        Random rnd = new Random();
//        int xPos = 0;
//        int yPos = 0;
//        int xWidth = this.getWidth() - bmp.getWidth();
////        int rPos = this.getWidth() - bmp.getWidth();
//        for(int i = 0; i < count; i++)
//        {
//            xPos = 0-i*bmp.getHeight();
//            //int xPos = rnd.nextInt(xWidth);
//            yPos = (i*bmp.getHeight())%(this.getWidth());
//            if(yPos > xWidth)
//            {
//                yPos = xWidth;
//            }
//            enemies.add(new Enemy(this, bmp, xPos, yPos, 10));
//        }
////        xPos = this.getHeight() - bmp.getHeight();
////        yPos = this.getWidth() - bmp.getWidth();
////        enemies.add(new Enemy(this, bmp, xPos, yPos, 5));
//    }

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