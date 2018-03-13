package com.tanny.infiniteviewpager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewParent;

import java.lang.ref.WeakReference;

/**
 * Created by tanny on 2018/3/12.
 * <p>
 * Viewpger that can be automatically scrolled
 */
public class AutoScrollViewPager extends ViewPager {
    private static final int MSG_AUTO_SCROLL = 0;

    private static final int DEFAULT_INTERVAL = 5000;

    private int mInterval = DEFAULT_INTERVAL;

    private static final int INVALID_POINTER = -1;

    private int mLastMotionX;

    private int mLastMotionY;

    private boolean mIsBeingDragged = false;

    private int mTouchSlop;

    private int mActivePointerId = INVALID_POINTER;

    private boolean mStopScrollWhenTouch = true;

    private boolean isScrolling = false;

    private Handler mHandler = new InternalHandler(this);

    private static class InternalHandler extends Handler {
        WeakReference<AutoScrollViewPager> refPager;

        InternalHandler(AutoScrollViewPager viewpager) {
            this.refPager = new WeakReference<>(viewpager);
        }

        @Override
        public void handleMessage(Message msg) {
            AutoScrollViewPager viewPager = refPager.get();
            if (viewPager == null) {
                return;
            }

            if (msg.what == MSG_AUTO_SCROLL) {
                viewPager.scrollNext();
                viewPager.sendAutoMessage();
            }
        }
    }

    public AutoScrollViewPager(Context context) {
        this(context, null);
    }

    public AutoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledPagingTouchSlop();
    }

    /**
     * Prevent slide conflicts when nesting viewpager for compatibility with lower versions.
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mIsBeingDragged = false;
                mLastMotionX = (int) ev.getX();
                mLastMotionY = (int) ev.getY();
                mActivePointerId = ev.getPointerId(0);

                requestParentDisallowInterceptTouchEvent(true);

                if (mStopScrollWhenTouch) {
                    stopAutoScroll();
                }
                break;

            case MotionEvent.ACTION_MOVE:

                final int activePointerIndex = ev.findPointerIndex(mActivePointerId);

                if (activePointerIndex == INVALID_POINTER) {
                    break;
                }

                final int x = (int) ev.getX(activePointerIndex);
                final int y = (int) ev.getY(activePointerIndex);

                int deltaX = x - mLastMotionX;
                int deltaY = y - mLastMotionY;

                if (Math.abs(deltaX) > mTouchSlop && Math.abs(deltaX) > Math.abs(deltaY)) {
                    mIsBeingDragged = true;
                }

                if (!mIsBeingDragged && Math.abs(deltaY) > mTouchSlop && Math.abs(deltaY) * 0.5f > Math.abs(deltaX)) {
                    requestParentDisallowInterceptTouchEvent(false);
                    // Avoid repeated execution
                    mIsBeingDragged = true;
                }

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mIsBeingDragged) {
                    mActivePointerId = INVALID_POINTER;
                    mIsBeingDragged = false;
                }

                if (mStopScrollWhenTouch) {
                    startAutoScroll();
                }

                break;

            default:
                break;
        }

        return super.dispatchTouchEvent(ev);

    }

    public void requestParentDisallowInterceptTouchEvent(boolean disallowIntercept) {
        final ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(disallowIntercept);
        }
    }

    private void sendAutoMessage() {
        mHandler.removeMessages(MSG_AUTO_SCROLL);
        mHandler.sendEmptyMessageDelayed(MSG_AUTO_SCROLL, mInterval);
    }

    /**
     * ViewPager will scroll to the next page, generally do not manually call this method.
     */
    public void scrollNext() {
        int currentItem = getCurrentItem();
        int nextItem = currentItem;
        if (getAdapter() != null && getAdapter().getCount() > 0) {
            nextItem = (currentItem + 1) % getAdapter().getCount();
        }

        setCurrentItem(nextItem, true);
    }

    /**
     * Set page scroll interval time in milliseconds
     *
     * @param interval the interval time in milliseconds
     */
    public void setScrollInterval(int interval) {
        this.mInterval = interval;
    }

    /**
     * ViewPager will start scrolling when this method is called.
     */
    public void startAutoScroll() {
        if (isScrolling) {
            return;
        }
        isScrolling = true;
        sendAutoMessage();
    }

    /**
     * ViewPager will stop scrolling when this method is called.
     */
    public void stopAutoScroll() {
        if (!isScrolling) {
            return;
        }
        isScrolling = false;
        mHandler.removeMessages(MSG_AUTO_SCROLL);
    }

    public boolean isAutoScrolling() {
        return isScrolling;
    }

    /**
     * <p>When the finger is on the ViewPager, set whether to stop the scroll, if you set the stop scroll,
     * it will automatically resume scrolling when you leave.<p/>
     */
    public void setStopScrollWhenTouch(boolean stopScrollWhenTouch) {
        this.mStopScrollWhenTouch = stopScrollWhenTouch;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAutoScroll();
    }
}
