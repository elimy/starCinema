package com.andy.free.Jsoup;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.andy.free.bean.TvSeries;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/9.
 */

public class TvDetailJsoup {

    private TvSeries series=new TvSeries();

    private String detailUrl;
    private Handler mHandler;
    private boolean suc;
    private requestEndListener mRequestEndListener;

    public TvDetailJsoup() {
    }

    public TvDetailJsoup(String detailUrl) {
        this.detailUrl = detailUrl;

        mHandler=new Handler(){
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

    private class LoadThread extends Thread{

        @Override
        public void run() {
            super.run();

            grabTvDetail();

            Message message=new Message();
            message.what=1;
            message.obj=series;
            mHandler.sendMessage(message);
        }

        private void grabTvDetail(){

            Log.d("grabTvDetail",detailUrl);

            Document doc=getRequestAddHeader(detailUrl);
            if (null!=doc){

                ArrayList<Map<Integer,String>> seriesMap=new ArrayList<>();
                Elements serials=doc.select("div.playNumList").select("a");

                int j=0;

                for (int i=0;i<serials.size();i++){

                    String url=serials.get(i).attr("href");
                    Map<Integer,String> map=new HashMap<>();
                    map.put(j,url);
                    seriesMap.add(map);
                    j=j+1;
                }
                series.setPlayNumAndUrls(seriesMap);


                series.setTvName(doc.select("div.tit").select("a").attr("title"));

                series.setPosterUrl("http:"+doc.select("div.posterCon").select("img").attr("src"));

                series.setScore(doc.select("em.emScore").text());

                Elements textDesLis=doc.select("ul.txtList").select("li");

                Elements actorAs=textDesLis.get(0).select("a");
                Elements actorEms=textDesLis.get(0).select("em");

                TvSeries.Actor[] actors=new TvSeries.Actor[actorAs.size()+actorEms.size()-2];
                for (int i=0;i<actorAs.size()-1;i++){
                    TvSeries.Actor actor=new TvSeries().new Actor();
                    actor.setUrl("http:"+actorAs.get(i).attr("href"));
                    actor.setName(actorAs.get(i).attr("title"));

                    actors[i]=actor;
                }

                int index=actorAs.size()-2;

                for (int i=1;i<actorEms.size();i++){

                    index=index+1;
                    TvSeries.Actor actor=new TvSeries().new Actor();
                    actor.setName(actorEms.get(i).text());

                    actors[index]=actor;
                }

                series.setActors(actors);

                series.setDirector(textDesLis.get(1).text());
                series.setTvType(textDesLis.get(2).text());

                series.setTime(textDesLis.get(3).select("a").text());

                series.setDesc(doc.select("p.pIntro").select("span").text());

            }else {
                Log.d("doc","doc is null");
            }


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
                    .maxBodySize(0)
                    .timeout(60000)
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
        void onRequestEnd(Object serial,boolean suc);
    }

    public void setRequestListener(requestEndListener listener){
        this.mRequestEndListener=listener;
    }
}
