package com.example.apple.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.apple.myapplication.util.MyData;
import com.example.apple.myapplication.util.MySelector;
import com.example.apple.myapplication.util.MySpider;
import com.example.apple.myapplication.util.SwipeAdapter;
import com.example.apple.myapplication.util.SwipeListView;

import java.util.ArrayList;

public class InfoActivity extends AppCompatActivity {

    public static final int SELECT=1;

    private Handler handler;
    private ArrayList<MyData> list;
    public static ArrayList<MyData> list2;
    private ArrayList<String> yearListRtn;
    private ArrayList<String> seasonListRtn;

    //页面跳转：回退到上个页面
    public void back(View v) {
        finish();
    }

    //页面跳转：选择页面
    public void select(View v) {
        Intent intent = new Intent(InfoActivity.this, SelectActivity.class);
        ArrayList<String> yearList = MySelector.getYearList(list);
        ArrayList<String> seasonList = MySelector.getSeasonList(list);
        intent.putStringArrayListExtra(SelectActivity.YEARLIST, yearList);
        intent.putStringArrayListExtra(SelectActivity.SEASONLIST, seasonList);
        startActivityForResult(intent, SELECT);
    }

    //页面跳转：计算页面
    public void calculate(View v) {
        Intent intent = new Intent(InfoActivity.this, ResultActivity.class);
        intent.putExtra(ResultActivity.LIST, list2);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==InfoActivity.SELECT) {
            switch (resultCode) {
                case SelectActivity.SUBMIT:
                    yearListRtn = data.getStringArrayListExtra(SelectActivity.YEARLISTRTN);
                    seasonListRtn = data.getStringArrayListExtra(SelectActivity.SEASONLISTRTN);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            MyData title = MySelector.getTitle(list);
                            ArrayList<MyData> list1 = new ArrayList<MyData>();
                            for (String s : seasonListRtn) {
                                list1.addAll(MySelector.selectBySeason(list, s));
                            }
                            list1.add(0, title);
                            list2 = new ArrayList<MyData>();
                            for (String s : yearListRtn) {
                                list2.addAll(MySelector.selectByYear(list1, s));
                            }
                            list2.add(0, title);
                            initListView();
                        }
                    });
                    break;
                case SelectActivity.CANCEL:
                    break;
                default:
                    break;
            }
        }
    }

    private void initListView() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                SwipeListView mListView = (SwipeListView) findViewById(R.id.swipeListView);
                SwipeAdapter swipeAdapter = new SwipeAdapter(InfoActivity.this);
                if (list2 != null && !list2.isEmpty()) {
                    swipeAdapter.setListData(list2);
                }
                mListView.setAdapter(swipeAdapter);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    list = ((MySpider)InfoActivity.this.getIntent().getSerializableExtra("s")).getDataList();
                    list2 = list;
                    initListView();
                } catch (Exception e) {
                    finish();
                }
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("list", list);
        outState.putSerializable("list2", list2);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle outState) {
        super.onRestoreInstanceState(outState);
        list = (ArrayList<MyData>) outState.getSerializable("list");
        list2 = (ArrayList<MyData>) outState.getSerializable("list2");
        initListView();
    }

}