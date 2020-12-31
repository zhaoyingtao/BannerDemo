package com.snow.bannerdemo.xbanner;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 继承RecyclerView重写{@link #getChildDrawingOrder(int, int)}对Item的绘制顺序进行控制
 *
 * @author Chen Xiaoping (562818444@qq.com)
 * @version V1.0
 * @Datetime 2017-04-18
 */

public class RecyclerCoverFlow extends RecyclerView {
    /**
     * 按下的X轴坐标
     */
    private float mDownX;
    /**
     * 是否惯性滑动
     */
    private boolean isInertiaScroll = false;
    /**
     * 布局器构建者
     */
    private CoverFlowLayoutManger.Builder mManagerBuilder;

    private final int BANNERWHAT = 6859;
    private long interval_time = 5000;

    public RecyclerCoverFlow(Context context) {
        super(context);
        init();
    }

    public RecyclerCoverFlow(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RecyclerCoverFlow(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        createManageBuilder();
        setLayoutManager(mManagerBuilder.build());
        setChildrenDrawingOrderEnabled(true); //开启重新排序
        setOverScrollMode(OVER_SCROLL_NEVER);

        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        startBanner();
                        //滚动停止时
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        stopBanner();
                        //拖拽滚动时
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        //动画滚动时
                        break;
                }
//                Log.e("snowBanner", "=====================newState===2222==" + newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }


    /**
     * 创建布局构建器
     */
    private void createManageBuilder() {
        if (mManagerBuilder == null) {
            mManagerBuilder = new CoverFlowLayoutManger.Builder();
        }
    }

    /**
     * 设置是否为普通平面滚动
     *
     * @param isFlat true:平面滚动；false:叠加缩放滚动
     */
    public void setFlatFlow(boolean isFlat) {
        createManageBuilder();
        mManagerBuilder.setFlat(isFlat);
        setLayoutManager(mManagerBuilder.build());
    }

    /**
     * 设置Item灰度渐变
     *
     * @param greyItem true:Item灰度渐变；false:Item灰度不变
     */
    public void setGreyItem(boolean greyItem) {
        createManageBuilder();
        mManagerBuilder.setGreyItem(greyItem);
        setLayoutManager(mManagerBuilder.build());
    }

    /**
     * 设置Item灰度渐变
     *
     * @param alphaItem true:Item半透渐变；false:Item透明度不变
     */
    public void setAlphaItem(boolean alphaItem) {
        createManageBuilder();
        mManagerBuilder.setAlphaItem(alphaItem);
        setLayoutManager(mManagerBuilder.build());
    }

    /**
     * 设置无限循环滚动
     */
    public void setLoop() {
        createManageBuilder();
        mManagerBuilder.loop();
        setLayoutManager(mManagerBuilder.build());
    }

    /**
     * 设置Item 3D 倾斜
     *
     * @param d3 true：Item 3d 倾斜；false：Item 正常摆放
     */
    public void set3DItem(boolean d3) {
        createManageBuilder();
        mManagerBuilder.set3DItem(d3);
        setLayoutManager(mManagerBuilder.build());
    }

    /**
     * 设置Item的间隔比例
     *
     * @param intervalRatio Item间隔比例。
     *                      即：item的宽 x intervalRatio
     */
    public void setIntervalRatio(float intervalRatio) {
        createManageBuilder();
        mManagerBuilder.setIntervalRatio(intervalRatio);
        setLayoutManager(mManagerBuilder.build());
    }

    /**
     * 设置Item的缩放比例
     */
    public void setZoomRatio(float zoomRatio) {
        createManageBuilder();
        mManagerBuilder.setZoomRatio(zoomRatio);
        setLayoutManager(mManagerBuilder.build());
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        if (!(layout instanceof CoverFlowLayoutManger)) {
            throw new IllegalArgumentException("The layout manager must be CoverFlowLayoutManger");
        }
        super.setLayoutManager(layout);
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        int center = getCoverFlowLayout().getCenterPosition();
        // 获取 RecyclerView 中第 i 个 子 view 的实际位置
        int actualPos = getCoverFlowLayout().getChildActualPos(i);

        // 距离中间item的间隔数
        int dist = actualPos - center;
        int order;
        if (dist < 0) { // [< 0] 说明 item 位于中间 item 左边，按循序绘制即可
            order = i;
        } else { // [>= 0] 说明 item 位于中间 item 右边，需要将顺序颠倒绘制
            order = childCount - 1 - dist;
        }

        if (order < 0) order = 0;
        else if (order > childCount - 1) order = childCount - 1;

        return order;
    }

    /**
     * 获取LayoutManger，并强制转换为CoverFlowLayoutManger
     */
    public CoverFlowLayoutManger getCoverFlowLayout() {
        return ((CoverFlowLayoutManger) getLayoutManager());
    }

    /**
     * 获取被选中的Item位置
     */
    public int getSelectedPos() {
        return getCoverFlowLayout().getSelectedPos();
    }

    /**
     * 获取被选中的Item位置
     */
    public int getLoopCurrentPos() {
        return getCoverFlowLayout().getLoopCurrentPos();
    }

    /**
     * 设置选中监听
     *
     * @param l 监听接口
     */
    public void setOnItemSelectedListener(CoverFlowLayoutManger.OnSelected l) {
        getCoverFlowLayout().setOnSelectedListener(l);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getX();
                getParent().requestDisallowInterceptTouchEvent(true); //设置父类不拦截滑动事件
                break;
            case MotionEvent.ACTION_MOVE:
                if ((ev.getX() > mDownX && getCoverFlowLayout().getCenterPosition() == 0) ||
                        (ev.getX() < mDownX && getCoverFlowLayout().getCenterPosition() ==
                                getCoverFlowLayout().getItemCount() - 1)) {
                    //如果是滑动到了最前和最后，开放父类滑动事件拦截
                    getParent().requestDisallowInterceptTouchEvent(false);
                } else {
                    //滑动到中间，设置父类不拦截滑动事件
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 设置轮播间隔时间
     *
     * @param time 单位毫秒
     */
    public void setAutoCarouselInterval(long time) {
        if (time > 0) {
            this.interval_time = time;
        }
    }

    /**
     * 开启自动轮播
     */
    public void startBanner() {
        if (timeHandler != null) {
            timeHandler.removeCallbacksAndMessages(null);
            timeHandler.sendEmptyMessageDelayed(BANNERWHAT, interval_time);
        }
    }

    /**
     * 停止自动轮播
     */
    public void stopBanner() {
        if (timeHandler != null) {
            timeHandler.removeMessages(BANNERWHAT);
        }
    }


    /**
     * 是否允许水平滑动
     *
     * @param canScrollHorizontally
     */
    public void setCanScrollHorizontally(boolean canScrollHorizontally) {
        getCoverFlowLayout().setCanScrollHorizontally(canScrollHorizontally);
    }

    /**
     * 设置是否需要惯性滑动
     *
     * @param isInertiaScroll
     */
    public void setIsInertiaScroll(boolean isInertiaScroll) {
        this.isInertiaScroll = isInertiaScroll;
    }

    /**
     * 注意只有在页面销毁时再调用
     */
    public void onDestroyView() {
        if (timeHandler != null) {
            timeHandler.removeCallbacksAndMessages(null);
            timeHandler = null;
        }
    }

    public int getItemSize() {
        if (getAdapter() != null) {
            return getAdapter().getItemCount();
        }
        return 0;
    }

    private Handler timeHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what) {
                case BANNERWHAT:
                    int nextPos = 0;
                    if (mManagerBuilder.isLoop) {
                        nextPos = getLoopCurrentPos() + 1;
                    } else {
                        nextPos = getSelectedPos() + 1;
                    }
//                    if (nextPos >= getItemSize()) {
//                        nextPos = 0;
//                    }
//                    Log.e("snowBanner", "==========smoothScrollToPosition===========position=====" + nextPos + "===getSelectedPos()===" + getSelectedPos());
//                    scrollToPosition(nextPos);
//                    getCoverFlowLayout().smoothScrollToPosition( nextPos);
                    smoothScrollToPosition(nextPos);
                    timeHandler.sendEmptyMessageDelayed(BANNERWHAT, interval_time);
                    break;
            }
            return false;
        }
    });


    @Override
    public boolean fling(int velocityX, int velocityY) {
        if (!isInertiaScroll) {
            velocityX *= 0.1f;
        }
        return super.fling(velocityX, velocityY);
    }
}
