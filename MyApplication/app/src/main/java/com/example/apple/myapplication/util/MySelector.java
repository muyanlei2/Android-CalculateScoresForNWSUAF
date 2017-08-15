package com.example.apple.myapplication.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class MySelector {

	public static MyData getTitle(ArrayList<MyData> list) {
        return list.get(0);
    }

    public static ArrayList<String> sortYearList(ArrayList<String> yearList) {
        Collections.sort(yearList,new Comparator<String>() {
            @Override
            public int compare(String str1, String str2) {
                return (str1).compareTo(str2);
            }
        });
        return yearList;
    }
    public static ArrayList<String> sortSeasonList(ArrayList<String> seasonList) {
        Collections.sort(seasonList,new Comparator<String>() {
            @Override
            public int compare(String str1, String str2) {
                byte b1, b2;
                if(str1.equals("春"))
                    b1 = 1;
                else if(str1.equals("夏"))
                    b1 = 2;
                else if(str1.equals("秋"))
                    b1 = 3;
                else if(str1.equals("冬"))
                    b1 = 4;
                else
                    b1 = 5;
                if(str2.equals("春"))
                    b2 = 1;
                else if(str2.equals("夏"))
                    b2 = 2;
                else if(str2.equals("秋"))
                    b2 = 3;
                else if(str2.equals("冬"))
                    b2 = 4;
                else
                    b2 = 5;
                return (b1+"").compareTo(b2+"");
            }
        });
        return seasonList;
    }

    public static ArrayList<String> getYearList(ArrayList<MyData> list) {
		Set<String> yearSet = new HashSet<String>();
		for(int i=1; i<list.size(); i++) {
			yearSet.add(((MyData)list.get(i)).year);
		}
		ArrayList<String> yearList = new ArrayList<String>();
		yearList.addAll(yearSet);
		return sortYearList(yearList);
	}
	public static ArrayList<String> getSeasonList(ArrayList<MyData> list) {
		Set<String> seasonSet = new HashSet<String>();
		for(int i=1; i<list.size(); i++) {
			seasonSet.add(((MyData)list.get(i)).season);
		}
		final ArrayList<String> seasonList = new ArrayList<String>();
		seasonList.addAll(seasonSet);

		return sortSeasonList(seasonList);
	}
	
	public static ArrayList<MyData> selectByYear(ArrayList<MyData> list, String year) {
		ArrayList<MyData> selectedList = new ArrayList<MyData>();
		for(int i=1; i<list.size(); i++) {
			if(list.get(i).year.equals(year)) 
				selectedList.add(list.get(i));
		}
		return selectedList;
	}
	public static ArrayList<MyData> selectBySeason(ArrayList<MyData> list, String season) {
		ArrayList<MyData> selectedList = new ArrayList<MyData>();
		for(int i=1; i<list.size(); i++) {
			if(list.get(i).season.equals(season)) 
				selectedList.add(list.get(i));
		}
		return selectedList;
	}

}
