package com.tanny.infiniteviewpager;

import android.content.Context;
import android.util.AttributeSet;

import com.tanny.infiniteviewpager.indicator.Indicator;

/**
 * Created by tanny on 2018/3/13.
 * <p>
 * Infinite loop scrolling ViewPager, which needs to be used with {@link InfinitePagerAdapter}
 */
public class InfiniteViewPager extends AutoScrollViewPager {
    private InfinitePagerAdapter mAdapter;
    private Indicator mIndicator;

    public InfiniteViewPager(Context context) {
        this(context, null);
    }

    public InfiniteViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    protected void init() {
        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setIndicatorPosition(position % mAdapter.getDataCount());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * Set adapter, note that its data source cannot be empty
     *
     * @param adapter
     * @param <T>
     * @param <VH>
     */
    public <T, VH extends ReuseablePagerAdapter.ViewHolder> void setAdapter(InfinitePagerAdapter<T, VH> adapter) {
        if (adapter == null) {
            throw new NullPointerException("adapter cannot be null!");
        }
        if (adapter.getDataCount() == 0) {
            throw new IllegalArgumentException("adapter's data set cannot be empty!");
        }

        mAdapter = adapter;
        super.setAdapter(mAdapter);
        int size = mAdapter.getDataCount();
        setCurrentItem(size * (Integer.MAX_VALUE / (2 * size)));
        setIndicatorPosition(0);
    }

    public void setIndicator(Indicator indicator) {
        mIndicator = indicator;
    }

    private void setIndicatorPosition(int pos) {
        if (mIndicator != null) {
            mIndicator.setSelectedPosition(pos);
        }
    }

}
