package com.andy.free.Jsoup;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.andy.free.bean.FullMovie;
import com.andy.free.bean.Movie;
import com.andy.free.utils.ConstantUtil;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Andy Lau on 2017/10/28.
 */

public class OverAllJsoup {

    //平台类型
    private String platformType="2345";

    private String type;
    private String area;
    private String year;
    private String payState;
    private int page;

    private requestEndListener mRequestEndListener;
    private boolean suc=true;
    private List<FullMovie> movieList=new ArrayList<>();
    private List<Movie> simpleList=new ArrayList<>();

    //handler处理并回调onRequestEnd方法
    private Handler handler;

    public OverAllJsoup(String platformType, String type, String area, String year, String payState,int page) {

        this.platformType = platformType;
        this.type = type;
        this.area = area;
        this.year = year;
        this.payState = payState;
        this.page=page;

        handler=new Handler(){

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what==0){
                    suc=false;
                }
                mRequestEndListener.onRequestEnd(msg.obj,suc);
            }
        };
    }

    /*
    * 启动下载线程
    * */
    public void start(){

        new LoadThread().start();
    }

    /*
    * 线程类型
    * */
    private class LoadThread extends Thread{

        @Override
        public void run() {
            super.run();

            if (platformType.equals("iqiyi")){
                grabIqiyi();
            }else if (platformType.equals("bilibili")){
                grabBilibili();
            }else if (platformType.equals("tencent")){
               grabTencent();
            }else {
                grab2345();
            }

            Message message=new Message();
            message.what=1;
            message.obj=movieList;
            handler.sendMessage(message);
        }

        /*
        * 腾讯视频抓取电影
        * */
        private void grabTencent() {

            //https://v.qq.com/x/list/movie?area=-1&year=11&subtype=-1&pay=-1&awards=-1&offset=0
            //https://v.qq.com/x/list/movie?pay=-1&year=11&awards=-1&subtype=-1&area=-1&offset=30
            //https://v.qq.com/x/list/movie?&offset=30

            Log.d("grabTencent","grabTencent");

            String url="https://v.qq.com/x/list/movie?&offset="+(page-1)*30;
            //String url="https://v.qq.com/x/list/movie?&offset=1";

            Log.d("grabTencent",url);

            Document doc=getRequestAddHeader(url);
            if (null!=doc){
                Elements lis=doc.select("ul.figures_list").select("li");

                for (int i=0;i<lis.size();i++) {
                    FullMovie movie = new FullMovie();

                    movie.setPosterUrl("http:"+lis.get(i).select("a.figure").select("img").attr("r-lazyload"));

                    //http://puui.qpic.cn/vcover_vt_pic/0/mx0u39zyyqg9hwy1491013858/220  //puui.qpic.cn/vcover_vt_pic/0/a8shst9guwwilpf1505809504/220
                    Log.d("PosterUrl","http:"+lis.get(i).select("a.figure").select("img").attr("r-lazyload"));

                    movie.setDesc(lis.get(i).select("figure_info").text());
                    movie.setName(lis.get(i).select("strong.figure_title").select("a").attr("title"));

                    //添加url
                    ArrayList<Map<String,String>> playUrls=new ArrayList<>();
                    Map<String,String> urlMap=new HashMap<>();
                    urlMap.put("tencentUrl",lis.get(i).select("strong.figure_title").select("a").attr("href"));
                    playUrls.add(urlMap);
                    movie.setUrls(playUrls);

                    movie.setScore(lis.get(i).select("div.figure_score").text());

                    //设置演员
                    Elements actorElem=lis.get(i).select("div.figure_desc").select("a");

                    FullMovie.Actors[] actors=new FullMovie.Actors[actorElem.size()];
                    for (int j=0;j<actorElem.size();j++){

                        String name=actorElem.get(j).attr("title");
                        String actorUrl=actorElem.get(j).attr("href");
                        actors[j]=movie.new Actors(name,actorUrl);

                    }
                    movie.setActors(actors);

                    Log.d("movie",movie.toString());

                    movieList.add(movie);
                }
            }else {
                Log.d("doc","doc is null");

            }

        }

        /*
        * 2345抓取电影
        * */
        private void grab2345() {

            String url="http://dianying.2345.com/list/-------.html";
            Document doc=null;

            //拼接url
            if (page==1) {
                if (payState.equals("vip")) {

                    url = ConstantUtil.MOVIE_BASE_2345_URL + type + "-" + area + "---" + year + "--.html?3";
                } else if (payState.equals("mianfei")) {
                    url = ConstantUtil.MOVIE_BASE_2345_URL + type + "-" + area + "---" + year + "--.html?1";
                } else {
                    url = ConstantUtil.MOVIE_BASE_2345_URL + type + "-" + area + "---" + year + "--.html";
                }
            }else {
                if (payState.equals("vip")) {

                    url = ConstantUtil.MOVIE_BASE_2345_URL + type + "-" + area + "---" + year + "---"+page+".html?3";
                } else if (payState.equals("mianfei")) {
                    url = ConstantUtil.MOVIE_BASE_2345_URL + type + "-" + area + "---" + year + "--"+page+".html?1";
                } else {
                    url = ConstantUtil.MOVIE_BASE_2345_URL + type + "-" + area + "---" + year + "--"+page+".html";
                }
            }

            //发起请求

            Log.d("grab2345 url=",url);

            doc = getRequestAddHeader(url);
            if (null!=doc){

                Elements lis=doc.select("div#contentList").select("li");

                for (int i=0;i<lis.size();i++){

                    FullMovie movie=new FullMovie();
                    movie.setPosterUrl("http:"+lis.get(i).select("img").attr("data-src"));
                    movie.setScore(lis.get(i).select("span.pRightBottom").select("em").text().trim());
                    movie.setDescUrl("http:"+lis.get(i).select("a.aPlayBtn").attr("href"));
                    movie.setName(lis.get(i).select("a.aPlayBtn").attr("title"));

                    //设置演员
                    Elements actorEms=lis.get(i).select("span.sDes").select("em");

                    FullMovie.Actors[] actors=new FullMovie.Actors[actorEms.size()];
                    for (int j=0;j<actorEms.size();j++){

                        String name=actorEms.get(j).select("a").attr("title");
                        String actorUrl="http:"+actorEms.get(j).select("a").attr("href");
                        actors[j]=movie.new Actors(name,actorUrl);

                    }
                    movie.setActors(actors);

                    //添加List
                    movieList.add(movie);
                }
            }

        }

        /*
        * 哔哩哔哩抓取电影
        * */
        private List<FullMovie> grabBilibili() {

            return null;
        }

        /*
        * 爱奇艺抓取电影
        * */
        private void grabIqiyi() {
            //http://list.iqiyi.com/www/1/------27815----2---4-2-1-iqiyi--.html
            //http://list.iqiyi.com/www/1/------27815----2---4-1-1-iqiyi--.html
            String url = "http://list.iqiyi.com/www/1/------27815----2---4-" + page + "-1-iqiyi--.html";

            //发起请求
            Log.d("grabIqiyi url=",url);
            Document doc = getRequestAddHeader(url);
            if (null!=doc){
                Elements lis = doc.select("a.site-piclist_pic_link");
                Elements timeSpans = doc.select("span.icon-vInfo");
                Elements scores = doc.select("span.score");
                Elements rolesInfos = doc.select("div.role_info");


                Movie[] movies = new Movie[lis.size()];

                for (int i = 0; i < lis.size(); i++) {

                    Movie movie = new Movie();

                    String link = lis.get(i).attr("href");
                    String name = lis.get(i).attr("title");
                    String time = timeSpans.get(i).text().trim();
                    String s = scores.get(i).text();
                    String posterUrl = lis.get(i).select("img").attr("src");

                    movie.setScore(s);
                    movie.setName(name);
                    movie.setUrl(link);
                    movie.setTime(time);
                    movie.setType("付费院线");
                    movie.setPosterUrl(posterUrl);

                    if (i < rolesInfos.size()) {
                        Elements rolesEm = rolesInfos.get(i).select("a");
                        Movie.Role[] roles = new Movie.Role[rolesEm.size()];

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
                    simpleList.add(movie);
/*            Log.d("Element", "time="+time);
            Log.d("Element", "name="+name+",link="+link);
            Log.d("Element", "score="+s);*/
                }
            }

            Message message=new Message();
            message.obj=simpleList;
            message.what=1;

            handler.sendMessage(message);
        }

    }

    /*
    * jsoup添加header请求
    * */
    private Document getRequestAddHeader(String url) {

        Document doc = null;
        try {

            doc = Jsoup.connect(url)
                    .header("Accept", "*/*")
                    .header("Accept-Encoding", "gzip, deflate")
                    .header("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
                    .header("Referer", "https://www.baidu.com/")
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0")
                    .timeout(60000)
                    .maxBodySize(0)
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return doc;
    }

    /*
    * 请求完成后的监听
    * */
    public interface requestEndListener{
        void onRequestEnd(Object movieList,boolean suc);
    }

    public void setRequestListener(requestEndListener listener){
        this.mRequestEndListener=listener;
    }


}
