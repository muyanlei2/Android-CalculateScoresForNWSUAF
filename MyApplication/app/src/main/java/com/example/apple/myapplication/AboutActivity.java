package com.example.apple.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    public void back(View v) {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ((TextView)findViewById(R.id.about_text_1)).setText(
                "第三章 考核与成绩记载 \n" +
                "　　第八条 课程考核分为考试、考查两种。考试课成绩一般采用百分制评定；" +
                "考查课、实践教学环节可采用五级分制。" +
                "五级即优秀(90-100分)、良好(80-89分)、中等(70-79分)、及格(60-69分)、不及格(60分以下)；" +
                "通识类选修课、创新与技能可采用合格、不合格评定，并且不参与学分成绩和学分绩点的计算，不再折算相应的百分制成绩。\n" +
                "　　在学分成绩和学分绩点计算中，采用五级分制评定的课程成绩，优秀以95分计算；" +
                "良好以85分计算；中等对以75分计算；及格以65分计算；不及格以55分计算。");
        ((TextView)findViewById(R.id.about_text_2)).setText(
                "第三章 考核与成绩记载 \n" +
                "　　第十四条 学校采取学分成绩衡量学生的学习质量。为了便于学生对外交流，同时使用学分绩点,计算公式为:\n" +
                "　　学分成绩=∑(课程总评成绩×课程学分)/∑课程学分\n" +
                "　　学分绩点=∑(课程绩点×课程学分)/∑课程学分\n" +
                "　　百分制成绩与课程绩点的对应关系规定如下：");

    }
}
