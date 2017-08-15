package com.example.apple.myapplication.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.example.apple.myapplication.R;

/**
 * Created by honjane on 2015/12/25.
 */
public class SwipeView extends LinearLayout {

    private static final int RATIO_Y = 2;
    private int mLastX = 0;
    private int mLastY = 0;
    private int nScrollX;
    private Context mContext;
    private Scroller mScroller;
    private LinearLayout mLayoutContent;
    private RelativeLayout mLayoutHolder;
    private OnSlideListener mOnSlideListener;
    private int mHolderWidth;
    //判断是否横滑
    private boolean mIsHoriMove;

    public SwipeView(Context context) {
        super(context);
        initView();
    }

    public SwipeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        mContext = getContext();
        // 初始化弹性滑动对象
        mScroller = new Scroller(mContext);
        // 设置其方向为横向
        setOrientation(LinearLayout.HORIZONTAL);
        // 将slide_view_merge加载进来
        View.inflate(mContext, R.layout.layout_swipe, this);
        mLayoutContent = (LinearLayout) findViewById(R.id.layout_content);
        mLayoutHolder = (RelativeLayout) findViewById(R.id.layout_holder);
        mLayoutHolder.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        mHolderWidth = mLayoutHolder.getMeasuredWidth();
        mLayoutHolder.setLayoutParams(new LayoutParams(mHolderWidth, LayoutParams.MATCH_PARENT));
    }

    /**
     * @param contentView is item view
     */
    public void setContentItemView(View contentView) {
        mLayoutContent.addView(contentView);
    }

    public void setOnSlideListener(OnSlideListener onSlideListener) {
        mOnSlideListener = onSlideListener;
    }

    public void shrink() {
        if (getScrollX() != 0) {
            this.smoothScrollTo(0, 0);
        }
    }

    private void smoothScrollTo(int destX, int destY) {
        // x form scrollX to delta
        int scrollX = getScrollX();
        int delta = destX - scrollX;
        mScroller.startScroll(scrollX, 0, delta, 0, Math.abs(delta) * 3);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    public void onSlideTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        int scrollX = getScrollX();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //按下默认为true，当移上下则为false
                mIsHoriMove = true;
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                if (mOnSlideListener != null) {
                    mOnSlideListener.onSlide(this, OnSlideListener.SLIDE_STATUS_START_SCROLL);
                }
                break;
            case MotionEvent.ACTION_UP:
                nScrollX = 0;
                //超过HolderWidth的25%就打开holder
                if (scrollX - mHolderWidth * 0.25 > 0) {
                    nScrollX = mHolderWidth;
                }
                //smoothScrollTo作用，当超过25%时，移动1否则回退到0
                this.smoothScrollTo(nScrollX, 0);
                if (mOnSlideListener != null) {
                    mOnSlideListener.onSlide(this, nScrollX == 0 ? OnSlideListener.SLIDE_STATUS_OFF : OnSlideListener.SLIDE_STATUS_ON);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;
                //上下移动
                if (Math.abs(deltaX) < Math.abs(deltaY) * RATIO_Y) {
                    mIsHoriMove = false;
                    break;
                }
                if (!mIsHoriMove) {
                    break;
                }
                int newScrollX = scrollX - deltaX;
                if (deltaX != 0) {
                    if (newScrollX < 0) {
                        newScrollX = 0;
                    } else if (newScrollX > mHolderWidth) {
                        newScrollX = mHolderWidth;
                    }
                    this.scrollTo(newScrollX, 0);
                }
                break;
            default:
                break;
        }
        mLastX = x;
        mLastY = y;
    }

    public boolean isHoriMove() {
        return mIsHoriMove;
    }

//    public boolean isTouch() { return (nScrollX == 0 && isHoriMove()); }
}
