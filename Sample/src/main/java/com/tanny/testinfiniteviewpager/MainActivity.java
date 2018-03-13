package com.tanny.testinfiniteviewpager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tanny.infiniteviewpager.indicator.Indicator;
import com.tanny.infiniteviewpager.InfinitePagerAdapter;
import com.tanny.infiniteviewpager.InfiniteViewPager;
import com.tanny.infiniteviewpager.ReuseablePagerAdapter;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private InfiniteViewPager mViewPager;
    private MyPagerAdapter mPagerAdapter;
    private Indicator mIndicator;
    private Data[] mDatas = {new Data(R.mipmap.image1, "AAA"),
            new Data(R.mipmap.image2, "BBB"), new Data(R.mipmap.image3, "CCC"), new Data(R.mipmap.image4, "DDD"),};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        mViewPager = findViewById(R.id.viewpager);
        mIndicator = findViewById(R.id.indicator);
        mPagerAdapter = new MyPagerAdapter();
        mPagerAdapter.setData(Arrays.asList(mDatas));
        mIndicator.setCount(mDatas.length);
        mViewPager.setIndicator(mIndicator);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.startAutoScroll();
    }

    private class MyPagerAdapter extends InfinitePagerAdapter<Data, MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent) {
            View view = View.inflate(MainActivity.this, R.layout.viewpager_item, null);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            Data data = getItemData(position);
            holder.imageView.setImageResource(data.imageResId);
            holder.textView.setText(data.title);
        }
    }

    static class MyViewHolder extends ReuseablePagerAdapter.ViewHolder {

        private ImageView imageView;
        private TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_image);
            textView = itemView.findViewById(R.id.tv_title);
        }
    }

    static class Data {
        int imageResId;
        String title;

        public Data(int imageResId, String title) {
            this.imageResId = imageResId;
            this.title = title;
        }
    }
}
