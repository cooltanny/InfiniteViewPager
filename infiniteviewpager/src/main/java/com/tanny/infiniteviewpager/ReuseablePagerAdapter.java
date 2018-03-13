package com.tanny.infiniteviewpager;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tanny on 2018/3/12.
 * <p>
 * Adapter used by {@link android.support.v4.view.ViewPager} to reuse pages to avoid duplicate creation of pages.
 */
public abstract class ReuseablePagerAdapter<T, VH extends ReuseablePagerAdapter.ViewHolder> extends PagerAdapter {

    private List<VH> mCacheViews = new ArrayList<>();

    private Map<View, VH> mShowingViews = new HashMap<>();

    private ArrayList<T> mDatas = new ArrayList<>();

    public void setData(List<T> data) {
        mDatas.clear();
        mDatas.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    public List<T> getData() {
        return mDatas;
    }

    public T getItemData(int position) {
        if (position < 0 || position >= mDatas.size()) {
            throw new IllegalArgumentException("postion cannot be out of the data source range!");
        }
        return mDatas.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final VH holder;
        if (mCacheViews.isEmpty()) {
            holder = onCreateViewHolder(container);
        } else {
            holder = mCacheViews.remove(0);
        }
        mShowingViews.put(holder.itemView, holder);
        onBindViewHolder(holder, convertToDataIndex(position));
        container.addView(holder.itemView);
        return holder.itemView;
    }

    protected int convertToDataIndex(int position) {
        return position;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        VH holder = mShowingViews.get(object);
        mShowingViews.remove(object);
        mCacheViews.add(holder);
    }

    /**
     * Called when ViewPager needs a new {@link ViewHolder}
     *
     * @param parent The ViewPager into which the new View will be added
     * @return A new ViewHolder that holds a View
     */
    public abstract VH onCreateViewHolder(ViewGroup parent);

    /**
     * Called by ViewPager to display the data at the specified position.
     *
     * @param holder   The ViewHolder which should be updated the contents at the specified position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    public abstract void onBindViewHolder(VH holder, int position);

    /**
     * A ViewHolder describes an item view within the ViewPager
     */
    public static class ViewHolder {
        public final View itemView;

        public ViewHolder(View itemView) {
            if (itemView == null) {
                throw new IllegalArgumentException("itemView must not be null");
            }
            this.itemView = itemView;
        }
    }
}


