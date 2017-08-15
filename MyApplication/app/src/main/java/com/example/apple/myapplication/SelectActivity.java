package com.example.apple.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.example.apple.myapplication.util.MySelector;

import java.util.ArrayList;

public class SelectActivity extends AppCompatActivity {

    public static final String YEARLIST="yearList";
    public static final String SEASONLIST="seasonList";
    public static final String YEARLISTRTN="yearListRtn";
    public static final String SEASONLISTRTN="seasonListRtn";
    public static final int SUBMIT=1;
    public static final int CANCEL=2;

    private LinearLayout yearCheckboxGroup;
    private LinearLayout seasonCheckboxGroup;

    private int choose;
    private ArrayList<String> yearList;
    private ArrayList<String> seasonList;
    private ArrayList<String> yearListRtn;
    private ArrayList<String> seasonListRtn;

    public void back(View v) {
        choose = CANCEL;
        finish();
    }

    public void submit(View v) {
        choose = SUBMIT;
        finish();
    }

    @Override
    public void finish() {
        Intent result = new Intent();
        if(choose==CANCEL)
            setResult(SelectActivity.CANCEL, result);
        if(choose==SUBMIT) {
            result.putExtra(SelectActivity.YEARLISTRTN, MySelector.sortYearList(yearListRtn));
            result.putExtra(SelectActivity.SEASONLISTRTN, MySelector.sortSeasonList(seasonListRtn));
            setResult(SelectActivity.SUBMIT, result);
        }
        super.finish();
    }

    private int sp2px(Context context, int spValue){
        float fontScale=context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue*fontScale+0.5f);
    }

//<android.support.v4.widget.SwipeRefreshLayout
//    android:id="@+id/swipe_refresh_layout"
//    android:layout_width="match_parent"
//    android:layout_height="match_parent">
//    private boolean isRefreshing;
//    private SwipeRefreshLayout swipeRefreshLayout;
//    private void setSwipeRefreshLayout() {
//        swipeRefreshLayout.setColorSchemeColors(
//                getResources().getColor(android.R.color.holo_blue_light)
////                getResources().getColor(android.R.color.holo_red_light),
////                getResources().getColor(android.R.color.holo_orange_light),
////                getResources().getColor(android.R.color.holo_green_light)
//        );
//        isRefreshing = false;
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
////                new Thread(SelectActivity.this).start();
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (isRefreshing)
//                            return;
//                        isRefreshing = true;
//                        try {
//                            Thread.sleep(200);
//                        } catch (Exception e) {
//
//                        } finally {
//                            swipeRefreshLayout.setRefreshing(false);
//                        }
//                        isRefreshing = false;
//                    }
//                }).start();
//            }
//        });
//    }

    private CompoundButton.OnCheckedChangeListener yearChangedListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if(b) {
                yearListRtn.add(((CheckBox)compoundButton).getText().toString());
            } else {
                yearListRtn.remove(((CheckBox)compoundButton).getText().toString());
            }
        }
    };
    private CompoundButton.OnCheckedChangeListener seasonChangedListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if(b) {
                seasonListRtn.add(((CheckBox)compoundButton).getText().toString());
            } else {
                seasonListRtn.remove(((CheckBox)compoundButton).getText().toString());
            }
        }
    };

    //添加 学年 复选框
    private void addYearCheckBoxs() {
        LinearLayout space;
        CheckBox checkBox;
        for(int i=0; i<yearList.size(); i++) {
            //创建复选框
            checkBox = (CheckBox) getLayoutInflater().inflate(R.layout.checkbox, null);
            checkBox.setText(yearList.get(i).toString());
            checkBox.setHeight(sp2px(SelectActivity.this, 50));
            checkBox.setOnCheckedChangeListener(yearChangedListener);
            //创建空白间隔
            space = new LinearLayout(SelectActivity.this);
            space.setMinimumHeight(sp2px(SelectActivity.this, 20));
            //添加到LinearLayout
            yearCheckboxGroup.addView(space);
            yearCheckboxGroup.addView(checkBox);
        }
    }
    //添加 学期 复选框
    private void addSeasonCheckBoxs() {
        LinearLayout space;
        CheckBox checkBox;
        for(int i=0; i<seasonList.size(); i++) {
            //创建复选框
            checkBox = (CheckBox) getLayoutInflater().inflate(R.layout.checkbox, null);
            checkBox.setText(seasonList.get(i).toString());
            checkBox.setHeight(sp2px(SelectActivity.this, 50));
            checkBox.setOnCheckedChangeListener(seasonChangedListener);
            //创建空白间隔
            space = new LinearLayout(SelectActivity.this);
            space.setMinimumHeight(sp2px(SelectActivity.this, 20));
            //添加到LinearLayout
            seasonCheckboxGroup.addView(space);
            seasonCheckboxGroup.addView(checkBox);
        }
    }

    private void init(Bundle savedInstanceState) {
        yearCheckboxGroup   = (LinearLayout)       findViewById(R.id.year_checkbox_group);
        seasonCheckboxGroup = (LinearLayout)       findViewById(R.id.season_checkbox_group);
//        swipeRefreshLayout  = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        if(savedInstanceState==null || !savedInstanceState.containsKey(SelectActivity.SEASONLIST)) {
            Intent intent = getIntent();
            yearList   = intent.getStringArrayListExtra(SelectActivity.YEARLIST);
            seasonList = intent.getStringArrayListExtra(SelectActivity.SEASONLIST);
        } else {
            yearList      = savedInstanceState.getStringArrayList(SelectActivity.YEARLIST);
            seasonList    = savedInstanceState.getStringArrayList(SelectActivity.SEASONLIST);
        }
        yearListRtn   = new ArrayList<String>();
        seasonListRtn = new ArrayList<String>();

//        setSwipeRefreshLayout();
        yearCheckboxGroup.removeAllViewsInLayout();
        seasonCheckboxGroup.removeAllViewsInLayout();
        addYearCheckBoxs();
        addSeasonCheckBoxs();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        init(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putStringArrayList(SelectActivity.YEARLIST,      yearList);
        outState.putStringArrayList(SelectActivity.SEASONLIST,    seasonList);
        outState.putStringArrayList(SelectActivity.YEARLISTRTN,   yearListRtn);
        outState.putStringArrayList(SelectActivity.SEASONLISTRTN, seasonListRtn);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle outState) {
        super.onRestoreInstanceState(outState);
        init(outState);
    }

}
