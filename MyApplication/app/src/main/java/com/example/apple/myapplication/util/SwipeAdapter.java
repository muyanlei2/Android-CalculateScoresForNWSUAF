package com.example.apple.myapplication.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.apple.myapplication.R;

import java.util.List;

/**
 * Created by honjane on 2015/12/25.
 */
public class SwipeAdapter extends BaseAdapter {
    private final LayoutInflater mInflater;
    private List<MyData> mMyDatas;
    private Context mContext;
    private SwipeView mOldSwipeView;

    public SwipeAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public void setListData(List<MyData> lists) {
        mMyDatas = lists;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mMyDatas.size();
    }

    @Override
    public MyData getItem(int position) {
        return mMyDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        SwipeView swipeView = (SwipeView) convertView;
        if (swipeView == null) {
            View view = mInflater.inflate(R.layout.layout_item, null);
            swipeView = new SwipeView(mContext);
            swipeView.setContentItemView(view);
            holder = new ViewHolder(swipeView);
            swipeView.setOnSlideListener(new OnSlideListener() {

                @Override
                public void onSlide(View view, int status) {

                    if (mOldSwipeView != null && mOldSwipeView != view) {
                        mOldSwipeView.shrink();
                    }

                    if (status == SLIDE_STATUS_ON) {
                        mOldSwipeView = (SwipeView) view;
                    }
                }
            });
            swipeView.setTag(holder);
        } else {
            holder = (ViewHolder) swipeView.getTag();
        }
        if (SwipeListView.mSwipeView != null) {
            SwipeListView.mSwipeView.shrink();
        }
        MyData MyData = getItem(position);
        if (MyData == null) {
            return swipeView;
        }

        holder.title.setText(MyData.name);
        holder.desc.setText(MyData.score);
        holder.leftView.setText("删除");
        holder.leftView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMyDatas == null) {
                    return;
                }
                mMyDatas.remove(position);
                notifyDataSetChanged();
            }
        });

        return swipeView;
    }

    static class ViewHolder {
        private TextView title;
        private TextView desc;
        private TextView leftView;

        ViewHolder(View view) {
            title = (TextView) view.findViewById(R.id.tv_title);
            desc = (TextView) view.findViewById(R.id.tv_desc);
            leftView = (TextView) view.findViewById(R.id.tv_left);
        }
    }
}
