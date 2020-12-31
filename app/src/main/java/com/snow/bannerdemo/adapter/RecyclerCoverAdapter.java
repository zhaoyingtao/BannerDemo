package com.snow.bannerdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.snow.bannerdemo.R;


public class RecyclerCoverAdapter extends RecyclerView.Adapter<RecyclerCoverAdapter.ViewHolder> {

    private Context mContext;
    private int[] mColors = {R.mipmap.item1, R.mipmap.item2, R.mipmap.item3, R.mipmap.item4,
            R.mipmap.item5, R.mipmap.item6};

    private onItemClick clickCb;
    private boolean is3D;

    public RecyclerCoverAdapter(Context c, boolean is3D) {
        mContext = c;
        this.is3D = is3D;
    }

    public RecyclerCoverAdapter(Context c, onItemClick cb, boolean is3D) {
        mContext = c;
        clickCb = cb;
        this.is3D = is3D;
    }

    public void setOnClickLstn(onItemClick cb) {
        this.clickCb = cb;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = R.layout.layout_item;
        if (is3D) layout = R.layout.layout_item_mirror;
        View v = LayoutInflater.from(mContext).inflate(layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Glide.with(mContext).load(mColors[position]).into(holder.img);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext, "点击了："+position, Toast.LENGTH_SHORT).show();
                if (clickCb != null) {
                    clickCb.clickItem(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mColors.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;

        public ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
        }
    }

    public interface onItemClick {
        void clickItem(int pos);
    }
}
