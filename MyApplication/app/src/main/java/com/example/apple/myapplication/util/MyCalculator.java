package com.example.apple.myapplication.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MyCalculator {

    static class ScoreGpaPair {
        Double score;
        Double gpa;
        ScoreGpaPair(Double s, Double g) {
            this.score = s;
            this.gpa = g;
        }
    }
	private static final Map<String, Double> scoreMap;
    private static final Set<String> notCalSet;
    private static final ArrayList<ScoreGpaPair> gpaList;
	static{
		scoreMap = new HashMap<String, Double>();
        scoreMap.put("优秀",  95.0);
        scoreMap.put("良好",  85.0);
        scoreMap.put("中等",  75.0);
        scoreMap.put("及格",  65.0);
        scoreMap.put("不及格",55.0);
        notCalSet = new HashSet<String>();
        notCalSet.add("");
        notCalSet.add("合格");
        notCalSet.add("不合格");
        gpaList = new ArrayList<ScoreGpaPair>();
        gpaList.add(new ScoreGpaPair(90.0, 4.0));
        gpaList.add(new ScoreGpaPair(85.0, 3.5));
        gpaList.add(new ScoreGpaPair(80.0, 3.0));
        gpaList.add(new ScoreGpaPair(75.0, 2.5));
        gpaList.add(new ScoreGpaPair(70.0, 2.0));
        gpaList.add(new ScoreGpaPair(65.0, 1.5));
        gpaList.add(new ScoreGpaPair(60.0, 1.0));
        gpaList.add(new ScoreGpaPair( 0.0, 0.0));
    }  
	
	public static Double calculateAvg(List<MyData> list) {
		Set<String> keySet = scoreMap.keySet();
		Double sumScores=0.0, sumWeights=0.0, score, weight;
        int i = 0;
        if(list.size()>0 && list.get(0).year.equals("学年"))
            i = 1;
		for( ; i<list.size(); i++) {
            if(notCalSet.contains(list.get(i).score))
                continue;
			weight = Double.valueOf(list.get(i).weight);
			if(keySet.contains(list.get(i).score))
				score = Double.valueOf(scoreMap.get(list.get(i).score));
			else
				score = Double.valueOf(list.get(i).score);
			sumScores += score * weight;
			sumWeights += weight;
		}
		return (sumScores/sumWeights);
	}

	public static Double calculateGpa(List<MyData> list) {
        Set<String> keySet = scoreMap.keySet();
		Double sumGpas=0.0, sumWeights=0.0, weight, score, gpa;
        int i = 0;
        if(list.size()>0 && list.get(0).year.equals("学年"))
            i = 1;
		for( ; i<list.size(); i++) {
            if(notCalSet.contains(list.get(i).score))
                continue;
			weight = Double.valueOf(list.get(i).weight);
            if(keySet.contains(list.get(i).score))
                score = Double.valueOf(scoreMap.get(list.get(i).score));
            else
                score = Double.valueOf(list.get(i).score);
            gpa = 4.0;
            for(ScoreGpaPair j : gpaList) {
                if(score>j.score)
                    break;
                gpa = j.gpa;
            }
			sumGpas += gpa * weight;
			sumWeights += weight;
		}
		return (sumGpas/sumWeights);
	}

}
