package com.example.apple.myapplication.util;

import android.content.Context;
import android.widget.Toast;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class MySpider implements Serializable {

	private static final long serialVersionUID = -7060210544600464483L;

	private String nocache;
	private String JSESSIONID;
	private String username;
	
	public MySpider(Context context) throws IOException {
		this.nocache = this.getNocache();
		this.JSESSIONID = this.downloadImageAndGetJsessionid(context);
	}

	public void redownloadVerifyCode(Context context) throws IOException {
		this.downloadImageAndGetJsessionid(context);
	}
	
	public void login(String username, String password, String verifyCode) throws IOException, MyException {
		this.username = username;
		Connection con2 = Jsoup.connect("http://219.245.195.49/StuInfoMgmtSys/UserServlet")
				.data("username", username)
				.data("password", password) 
				.data("verifyCode", verifyCode) 
				.data("method", "login")  
				.cookie("JSESSIONID", this.JSESSIONID)
				.userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
		con2.timeout(10000).post();
		if(con2.response().body().contains("错误提示： 您输入的验证码不正确！!"))
			throw new MyException("错误提示： 您输入的验证码不正确！!");
		else if(con2.response().body().contains("错误提示： 密码不匹配!"))
			throw new MyException("错误提示： 密码不匹配!");
		else if(con2.response().body().contains("错误提示： 用户名"))
			throw new MyException("错误提示： 学号\""+this.username+"\"不存在!");
	}
	
	public ArrayList<MyData> getDataList() throws IOException {
		Document doc = this.getDoc();
		ArrayList<MyData> list = this.getList(doc);
		return list;
	}
	
	private String getNocache() {
		String nocache = String.valueOf(new Date().getTime());
		return nocache;
	}
	
	private String downloadImageAndGetJsessionid(Context context) throws IOException {
		Connection con1 = Jsoup.connect("http://219.245.195.49/StuInfoMgmtSys/UserServlet")
				.data("method","getVerifyImg")
				.data("nocache", this.nocache)
				.userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
		con1.timeout(3000).get();

		byte[] img = con1.response().bodyAsBytes();
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		try {
			fos = context.openFileOutput("verify_code.jpeg", Context.MODE_PRIVATE);
			bos = new BufferedOutputStream(fos);
			bos.write(img);
		} catch (Exception e){
			Toast.makeText(context.getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
		} finally {
			if(bos!=null)
				bos.close();
			if(fos!=null)
				fos.close();
		}
        
        this.JSESSIONID = con1.response().cookies().get("JSESSIONID");
		return JSESSIONID;
	}
	
	private Document getDoc() throws IOException {
		Connection con3 = Jsoup.connect("http://219.245.195.49/StuInfoMgmtSys/UserServlet?method=getAchievement")
				.data("method", "getAchievement")
				.cookie("JSESSIONID", this.JSESSIONID)
				.userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
		Document doc = con3.timeout(3000).get();
		return doc;
	}
	
	private ArrayList<MyData> getList(Document doc) {
		ArrayList<MyData> list = new ArrayList<MyData>();
		Elements trs = doc.getElementsByTag("tr");
		for(int i=0; i<trs.size(); i++) {
			Elements ths = trs.get(i).children();
			MyData data = new MyData();
			data.year = ths.get(0).text();
			data.season = ths.get(1).text();
			data.name = ths.get(3).text();
			data.score = ths.get(10).text();
			data.weight = ths.get(11).text();
			list.add(data);
		}
		return list;
	}

}
