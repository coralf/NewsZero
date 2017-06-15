package com.newt.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.Image;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.LayoutDirection;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * Created by Thinking on 2017/4/3.
 */

public class Banner extends FrameLayout {

    private static final String TAG = "Banner";


    //小圆点默认大小
    public static final int POINTER_SIZE_DEF = 20;
    //小圆点默认边框大小
    public static final int POINTER_BORDER_SIZE_DEF = 2;
    //小圆点边框颜色
    public static final String POINTER_BORDER_COLOR_DEF = "#FFFFFF";
    //小圆点选中状态下颜色
    private static final String POINTER_CURRENT_COLOR_DEF = "#C1C1C1";
    //小圆点没有选中下颜色
    private static final String POINTER_COLOR_DEF = "#FF0033";


    //轮播图
    private List<String> urls;
    private int currentPosition = Integer.MAX_VALUE / 2;
    private ViewPager viewPager;
    private LinearLayout pointer;
    private Context context;

    private int borderWidth = POINTER_BORDER_SIZE_DEF;
    private int roundRadius = POINTER_SIZE_DEF;
    private int pointerSize = POINTER_SIZE_DEF;

    private String pointerColor = POINTER_BORDER_COLOR_DEF;
    private String pointerCurrentColor = POINTER_CURRENT_COLOR_DEF;
    private OnBannerChangeListener onBannerChangeListener;
    private Thread thread;
    private int mScrollState = 1;
    //横条的透明色
    private String pointerBgApacity = "#40000000";

    //横条的高度
    private int pointerHeight = 80;

    //轮播图集合
    List<View> viewItems = new ArrayList<>();

    public Banner(@NonNull Context context) {
        super(context);
        this.context = context;
    }

   /* public Banner(@NonNull Context context, List<String> urls) {
        super(context);
        this.context = context;
        this.urls = urls;
        initView();
    }*/

    public Banner(@NonNull Context context, List<View> viewItems) {
        super(context);
        this.context = context;
        this.viewItems=viewItems;
        initView();
    }

    public Banner(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public Banner(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

    }

    public void initView() {
        initViewPager();
        initPointer();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int size = viewItems.size();
                int index = viewPager.getCurrentItem() % size;
                for (int i = 0; i < size; i++) {
                    pointer.getChildAt(i).setBackground(generatePinterDrawable("#CC0000"));
                }
                pointer.getChildAt(index).setBackground(generatePinterDrawable(pointerCurrentColor));
                if (onBannerChangeListener != null) {
                    onBannerChangeListener.bannerChanged();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Banner.this.post(new Runnable() {
                            @Override
                            public void run() {
                                if (mScrollState == 1) {
                                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
                                }
                            }
                        });
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    public interface OnBannerChangeListener {
        void bannerChanged();
    }

    private void initViewPager() {
        viewPager = new ViewPager(context);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        viewPager.setLayoutParams(layoutParams);
      /*  for (int i = 0; i < urls.size(); i++) {
            ImageView imageView = new ImageView(context);
            RequestBuilder<Drawable> drawables = Glide.with(context).load(urls.get(i));
            drawables.into(imageView);
            viewItems.add(imageView);
        }*/
        viewPager.setAdapter(new ViewPagerAdapter());
        viewPager.setCurrentItem(currentPosition);
        this.addView(viewPager);
    }

    public void setBannerChangeListener(OnBannerChangeListener onBannerChangeListener) {
        this.onBannerChangeListener = onBannerChangeListener;
    }

    //初始化横条和小圆点
    private void initPointer() {
        pointer = new LinearLayout(context);
        LinearLayout.LayoutParams llLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, pointerHeight);
        pointer.setLayoutParams(llLayoutParams);
        pointer.setBackgroundColor(Color.parseColor(pointerBgApacity));
        pointer.setGravity(Gravity.CENTER);
        pointer.setOrientation(LinearLayout.HORIZONTAL);
        int size = viewItems.size();
        GradientDrawable gradientDrawable = generatePinterDrawable(POINTER_COLOR_DEF);
        for (int i = 0; i < size; i++) {
            View view = new View(context);
            view.setBackground(gradientDrawable);
            LayoutParams layoutParams = new LayoutParams(pointerSize, pointerSize);
            layoutParams.setMarginStart(10);
            view.setLayoutParams(layoutParams);
            pointer.addView(view);
        }
        int index = currentPosition % (viewItems.size());
        View view = pointer.getChildAt(index);
        view.setBackground(generatePinterDrawable(pointerCurrentColor));
        this.addView(pointer);
    }


    //绘制小圆点
    public GradientDrawable generatePinterDrawable(String fillColorValue) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        int fillColor = Color.parseColor(fillColorValue);
        int borderColor = Color.parseColor(pointerColor);
        gradientDrawable.setColor(fillColor);
        gradientDrawable.setCornerRadius(roundRadius);
        gradientDrawable.setStroke(borderWidth, borderColor);
        return gradientDrawable;
    }


    private class ViewPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            currentPosition = position;
            int index = position % viewItems.size();
            View view = viewItems.get(index);
            container.addView(view);
            return view;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            mScrollState = 0;
        }
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            mScrollState = 1;
        }
        return super.onInterceptTouchEvent(ev);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        pointer.layout(left, this.getMeasuredHeight() - pointerHeight, right, bottom);
    }
}
