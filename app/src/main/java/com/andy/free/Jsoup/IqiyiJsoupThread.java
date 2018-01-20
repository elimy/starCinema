package com.andy.free.Jsoup;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.andy.free.bean.Movie;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy Lau on 2017/10/16.
 */

public class IqiyiJsoupThread extends Thread {

    private Handler mHanler;
    private List<Movie> movieList=new ArrayList<Movie>();
    private Movie movie;
    private Movie.Role[] roles;
    private Bundle bundle=new Bundle();
    private String mUrl;

    public IqiyiJsoupThread() {}

    /*
    * 构造函数实现handler传递消息到主线程
    * */
    public IqiyiJsoupThread(Handler handler,String mUrl) {

        this.mHanler=handler;
        this.mUrl=mUrl;
    }


    @Override
    public void run() {

        Document doc = null;
        try {
            doc = Jsoup.connect(mUrl).timeout(260000).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (null!=doc) {
            Elements lis = doc.select("a.site-piclist_pic_link");
            Elements timeSpans = doc.select("span.icon-vInfo");
            Elements scores = doc.select("span.score");
            Elements rolesInfos = doc.select("div.role_info");


            Movie[] movies = new Movie[lis.size()];

            for (int i = 0; i < lis.size(); i++) {

                movie = new Movie();

                String link = lis.get(i).attr("href");
                String name = lis.get(i).attr("title");
                String time = timeSpans.get(i).text().trim();
                String s = "";
                try {
                    s=scores.get(i).text();
                }catch (IndexOutOfBoundsException e){
                    Log.d("log",e.toString());
                }

                String posterUrl = lis.get(i).select("img").attr("src");

                movie.setScore(s);
                movie.setName(name);
                movie.setUrl(link);
                movie.setTime(time);
                movie.setType("付费院线");
                movie.setPosterUrl(posterUrl);

                if (i < rolesInfos.size()) {
                    Elements rolesEm = rolesInfos.get(i).select("a");
                    roles = new Movie.Role[rolesEm.size()];

                    for (int j = 0; j < rolesEm.size(); j++) {

                        Movie.Role role = movie.new Role();
                        String roleHref = rolesEm.get(j).attr("href").trim();
                        String roleName = rolesEm.get(j).text().trim();

                        role.setName(roleName);
                        role.setUrl(roleHref);

                        roles[j] = role;
                    }
                    movie.setRoles(roles);
                }

                movies[i] = movie;
                movieList.add(movie);
/*            Log.d("Element", "time="+time);
            Log.d("Element", "name="+name+",link="+link);
            Log.d("Element", "score="+s);*/
            }
        }
        Message message=new Message();
        message.obj=movieList;
        message.what=1;

        mHanler.sendMessage(message);
    }
}
