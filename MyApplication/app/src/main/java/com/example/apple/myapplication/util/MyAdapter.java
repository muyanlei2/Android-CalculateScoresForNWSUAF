package com.example.apple.myapplication.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.apple.myapplication.R;

import java.util.List;

public class MyAdapter extends BaseAdapter {

    static class ListItem {
        private TextView year;
        private TextView season;
        private TextView name;
        private TextView score;
        private TextView weight;
    }

    private List<MyData> list;
    private LayoutInflater layoutInflater;

    public MyAdapter(Context context, List<MyData> list) {
        this.list = list;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public Object getItem(int i) {
        return list.get(i);
    }
    @Override
    public int getCount() {
        return list.size();
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ListItem li=null;
        if(view==null){
            li=new ListItem();
            view=layoutInflater.inflate(R.layout.list_item, null);
            li.year  =(TextView)view.findViewById(R.id.year  );
            li.season=(TextView)view.findViewById(R.id.season);
            li.name  =(TextView)view.findViewById(R.id.name  );
            li.score =(TextView)view.findViewById(R.id.score );
            li.weight=(TextView)view.findViewById(R.id.weight);
            view.setTag(li);
        } else {
            li=(ListItem) view.getTag();
        }

        li.year.  setText(list.get(i).year  );
        li.season.setText(list.get(i).season);
        li.name.  setText(list.get(i).name  );
        li.score. setText(list.get(i).score );
        li.weight.setText(list.get(i).weight);

        li.year.  setGravity(Gravity.CENTER);
        li.season.setGravity(Gravity.CENTER);
        li.name.  setGravity(Gravity.CENTER);
        li.score. setGravity(Gravity.CENTER);
        li.weight.setGravity(Gravity.CENTER);

        return view;
    }
}
