package com.snow.bannerdemo.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.snow.bannerdemo.R;
import com.snow.bannerdemo.adapter.RecyclerCoverAdapter;
import com.snow.bannerdemo.xbanner.CoverFlowLayoutManger;
import com.snow.bannerdemo.xbanner.RecyclerCoverFlow;

public class GalleryActivity extends AppCompatActivity implements RecyclerCoverAdapter.onItemClick {
    RecyclerCoverFlow recyclerCoverFlow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        recyclerCoverFlow = findViewById(R.id.recyclerCoverFlow);
        findViewById(R.id.btn_01).setOnClickListener(view -> {
            recyclerCoverFlow.stopBanner();
        });
        findViewById(R.id.btn_02).setOnClickListener(view -> {
            recyclerCoverFlow.startBanner();
        });

        recyclerCoverFlow.setLoop();//设置无限循环滚动
        recyclerCoverFlow.setIntervalRatio(0.17f);//设置Item的间隔比例
        recyclerCoverFlow.setZoomRatio(1.2f);//设置Item的缩放比例
        recyclerCoverFlow.setAutoCarouselInterval(3000);//设置轮播间隔时间
//        recyclerCoverFlow.setIsInertiaScroll(true);//设置是否需要惯性滑动,默认false

        recyclerCoverFlow.setAdapter(new RecyclerCoverAdapter(this, this, false));
//        PagerSnapHelper snapHelper = new PagerSnapHelper();
//        snapHelper.attachToRecyclerView(recyclerCoverFlow);
        recyclerCoverFlow.setOnItemSelectedListener(new CoverFlowLayoutManger.OnSelected() {
            @Override
            public void onItemSelected(int position) {
                Log.e("snowBanner", "=====================position=====" + position);
//                ((TextView)findViewById(R.id.index)).setText((position+1)+"/"+mList.getLayoutManager().getItemCount());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        recyclerCoverFlow.startBanner();
    }

    @Override
    protected void onPause() {
        super.onPause();
        recyclerCoverFlow.stopBanner();
    }

    @Override
    public void clickItem(int pos) {
        int position = recyclerCoverFlow.getSelectedPos();
        if (position == pos) {
            showToast(position);
        } else {
            Log.e("ssss", "=======dddd=當前不是最顶上的item====");
        }
    }

    private Toast toast;

    private void showToast(int pos) {
        if (toast == null) {
            toast = Toast.makeText(this, "点击了" + pos + "个item", Toast.LENGTH_SHORT);
        } else {
            toast.setText("点击了" + pos + "个item");
        }
        toast.show();
    }
}