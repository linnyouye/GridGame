package com.lyy.gridgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.LinkedList;
import java.util.List;

public class GameView extends View {

    private static final String TAG = "lyy-GameView";
    private static final boolean D = true;


    private Context mContext;
    private OnGameFinishedListener mListener;

    private int[][] mStatus;
    private int mStartX;
    private int mStartY;
    private int mCurrentX;
    private int mCurrentY;
    private int mMaxX;
    private int mMaxY;
    private int mGridWidth;
    private int mPathWidth;

    private List<Point> mPointList;
    private Path mPointPath;

    private Paint redPaint, whitePaint, bluePaint;

    private Bitmap mPlayerBitmap;
    private Bitmap mWallBitmap;
    private Bitmap mBarrierBitmap;

    public GameView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init(context);

    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init(context);
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // TODO Auto-generated constructor stub
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        initPaint();

        mPointList = new LinkedList<>();
        mPointPath = new Path();

        setOnTouchListener(new OnTouchListener() {

            private float startX;
            private float startY;
            private long startTime;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub

                if (D)
                    Log.i(TAG, "onTouch:" + event.getAction());

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (D)
                            Log.i(TAG, "action_down:" + event.toString());
                        startX = event.getX();
                        startY = event.getY();
                        startTime = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        if (D)
                            Log.i(TAG, "action_end:" + event.toString());
                        if (D)
                            detectGesture(event.getX() - startX, event.getY()
                                    - startY, System.currentTimeMillis()
                                    - startTime);
                        break;
                }

                return true;
            }
        });
    }

    private void detectGesture(float dx, float dy, long time) {
        // TODO Auto-generated method stub
        if (D)
            Log.i(TAG, "detectGesture");

        if (D)
            Log.i(TAG, "duration=" + time);

        if (D)
            Log.i(TAG, "dx=" + dx + ",dy=" + dy);

        if (Math.abs(dx) < 20 && Math.abs(dy) < 20) {
            return;
        }
        if (Math.abs(dx / dy) < 2 && Math.abs(dx / dy) > 0.5) {
            return;
        }

        if (Math.abs(dx) > Math.abs(dy)) {
            if (dx > 0) {
                goRight();
            } else {
                goLeft();
            }
        } else {
            if (dy > 0) {
                goDown();
            } else {
                goUp();
            }
        }

        isWon();

    }

    private void initPaint() {
        redPaint = new Paint();
        redPaint.setColor(Color.RED);
        whitePaint = new Paint();
        whitePaint.setColor(Color.WHITE);
        bluePaint = new Paint();
        bluePaint.setStrokeWidth(mPathWidth);
        bluePaint.setAntiAlias(true);
        bluePaint.setStyle(Paint.Style.STROKE);
        bluePaint.setColor(0xff3366ff);
        bluePaint.setPathEffect(new CornerPathEffect(mPathWidth / 2));
    }

    public void initStatus(int[][] status, int startX, int startY) {
        mStatus = status;
        mMaxX = status[0].length - 1;
        mMaxY = status.length - 1;

        mStatus[startY][startX] = 0;
        mCurrentX = startX;
        mCurrentY = startY;

        mPointList.add(new Point(startX, startY));

        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (isStatusValid()) {
            int dx = getMeasuredWidth() / mStatus[0].length;
            int dy = getMeasuredHeight() / mStatus.length;
            mGridWidth = Math.min(dx, dy);
            mPathWidth = mGridWidth - 4;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        if (!isStatusValid()) {
            return;
        }


        int numX = mStatus[0].length;
        int numY = mStatus.length;

        float dx = mGridWidth;
        float dy = mGridWidth;
        //draw grid
        for (int i = 0; i < numY; i++) {
            for (int j = 0; j < numX; j++) {
                if (mStatus[i][j] < 0) {
                    canvas.drawRect(j * dx, i * dy, (j + 1) * dx, (i + 1) * dy,
                            redPaint);
                } else {
                    canvas.drawRect(j * dx, i * dy, (j + 1) * dx, (i + 1) * dy,
                            whitePaint);
                }
            }
        }


        //draw path
//        for (int i = 0; i < mPointList.size() - 1; i++) {
//            Point startPoint = mPointList.get(i);
//            Point endPoint = mPointList.get(i + 1);
//            canvas.drawLine((startPoint.x + 0.5f) * dx, (startPoint.y + 0.5f) * dy, (endPoint.x + 0.5f) * dx, (endPoint.y + 0.5f) * dy, bluePaint);
//        }

        mPointPath.reset();
        for (int i = 0; i < mPointList.size(); i++) {
            Point point = mPointList.get(i);
            if (i == 0) {
                mPointPath.moveTo((point.x + 0.5f) * dx, (point.y + 0.5f) * dy);
            } else {
                mPointPath.lineTo((point.x + 0.5f) * dx, (point.y + 0.5f) * dy);
            }


        }

        canvas.drawPath(mPointPath, bluePaint);


    }

    private boolean isStatusValid() {
        if (mStatus == null) {
            return false;
        }
        if (mStatus.length == 0) {
            return false;
        }
        if (mStatus[0].length == 0) {
            return false;
        }
        return true;
    }

    private boolean goLeft() {
        if (D)
            Log.i(TAG, "goLeft");
        if (mCurrentX > 0 && mStatus[mCurrentY][mCurrentX - 1] <= 0) {
            return false;
        } else {
            while (mCurrentX > 0 && mStatus[mCurrentY][mCurrentX - 1] > 0) {
                mStatus[mCurrentY][mCurrentX - 1]--;
                mCurrentX--;
            }
            mPointList.add(new Point(mCurrentX, mCurrentY));
            invalidate();
            return true;
        }
    }

    private boolean goDown() {
        if (D)
            Log.i(TAG, "goDown");
        if (mCurrentY < mMaxY && mStatus[mCurrentY + 1][mCurrentX] <= 0) {
            return false;
        } else {
            while (mCurrentY < mMaxY && mStatus[mCurrentY + 1][mCurrentX] > 0) {
                mStatus[mCurrentY + 1][mCurrentX]--;
                mCurrentY++;
            }
            mPointList.add(new Point(mCurrentX, mCurrentY));
            invalidate();
            return true;
        }
    }

    private boolean goRight() {
        if (D)
            Log.i(TAG, "goRight");
        if (mCurrentX < mMaxX && mStatus[mCurrentY][mCurrentX + 1] <= 0) {
            return false;
        } else {
            while (mCurrentX < mMaxX && mStatus[mCurrentY][mCurrentX + 1] > 0) {
                mStatus[mCurrentY][mCurrentX + 1]--;
                mCurrentX++;
            }
            mPointList.add(new Point(mCurrentX, mCurrentY));
            invalidate();
            return true;
        }
    }

    private boolean goUp() {
        if (D)
            Log.i(TAG, "goUp");
        if (mCurrentY > 0 && mStatus[mCurrentY - 1][mCurrentX] <= 0) {
            return false;
        } else {
            while (mCurrentY > 0 && mStatus[mCurrentY - 1][mCurrentX] > 0) {
                mStatus[mCurrentY - 1][mCurrentX]--;
                mCurrentY--;
            }
            mPointList.add(new Point(mCurrentX, mCurrentY));
            invalidate();
            return true;
        }
    }

    private boolean isWon() {
        for (int i = 0; i < mStatus.length; i++) {
            for (int j = 0; j < mStatus[0].length; j++) {
                if (mStatus[i][j] != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    interface OnGameFinishedListener {
        public void onGameFinished(boolean isWon);
    }

}
