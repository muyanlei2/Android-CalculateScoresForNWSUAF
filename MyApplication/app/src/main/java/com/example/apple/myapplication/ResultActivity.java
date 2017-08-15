package com.example.apple.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.apple.myapplication.util.MyAdapter;
import com.example.apple.myapplication.util.MyCalculator;
import com.example.apple.myapplication.util.MyData;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    public static String LIST="list2";
    private ArrayList<MyData> list;
    private ListView listView;

    public void back(View v) {
        finish();
    }

    //解决ListView只显示一行
    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        ((ViewGroup.MarginLayoutParams) params).setMargins(0, 0, 0, 0);
        listView.setLayoutParams(params);
    }

    //初始化ListView并显示
    private void initListView() {
        listView = (ListView) findViewById(R.id.result_list_view);
        listView.setAdapter(new MyAdapter(this, list));
        setListViewHeightBasedOnChildren(listView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        if(savedInstanceState==null || !savedInstanceState.containsKey(SelectActivity.SEASONLIST))
            list = (ArrayList<MyData>) getIntent().getSerializableExtra(LIST);
        else
            list = new ArrayList<MyData>();
        initListView();
        ((TextView)findViewById(R.id.avg)).setText(String.valueOf(MyCalculator.calculateAvg(list)));
        ((TextView)findViewById(R.id.gpa)).setText(String.valueOf(MyCalculator.calculateGpa(list)));

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("list", list);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle outState) {
        super.onRestoreInstanceState(outState);
        list = (ArrayList<MyData>) outState.getSerializable("list");
        initListView();
    }
}
