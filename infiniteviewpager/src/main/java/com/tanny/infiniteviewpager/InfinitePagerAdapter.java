package com.tanny.infiniteviewpager;

/**
 * Created by tanny on 2018/3/13.
 * <p>
 * This adapter works with {@link InfiniteViewPager} to achieve infinite loop scrolling.
 *
 * @param <T>  the type of the adapter's data
 * @param <VH> the type of ViewHolder
 */
public abstract class InfinitePagerAdapter<T, VH extends ReuseablePagerAdapter.ViewHolder> extends ReuseablePagerAdapter<T, VH> {

    /**
     * Get the size of the viewpager's pages
     *
     * @return the size of the viewpager's pages
     */
    @Override
    public int getCount() {
        if (getData().isEmpty()) {
            return 0;
        }
        return Integer.MAX_VALUE;
    }

    /**
     * Get the size of the adapter's data set
     *
     * @return the size of the adapter's data set
     */
    public int getDataCount() {
        return super.getCount();
    }

    @Override
    protected int convertToDataIndex(int position) {
        return position % getData().size();
    }
}
