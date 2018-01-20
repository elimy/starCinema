package com.andy.free.Jsoup;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.andy.free.bean.TvSeries;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/6.
 */

public class TvJsoup {

    private String areaKey;
    private String tvTypeKey;
    private int pageNum;

    private List<TvSeries> seriesList=new ArrayList<>();

    private Handler mHandler;
    private boolean suc;
    private requestEndListener mRequestEndListener;

    public TvJsoup() {
    }

    public TvJsoup(String areaKey, String tvTypeKey, int pageNum) {
        this.areaKey = areaKey;
        this.tvTypeKey = tvTypeKey;
        this.pageNum = pageNum;

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

            grabTV();

            Message message=new Message();
            message.what=1;
            message.obj=seriesList;
            mHandler.sendMessage(message);
        }

        /*
        * 2345爬取电视剧
        * */
        private void grabTV(){

            //http://tv.2345.com/yanqing-neidi----2.html
            //http://tv.2345.com/-neidi--.html
            //http://tv.2345.com/---.html
            //http://tv.2345.com/yanqing-neidi--.html
            //http://tv.2345.com/-----2.html
            //http://tv.2345.com/-taiwan----2.html
            //http://tv.2345.com/yanqing-----2.html

            String url="http://tv.2345.com/"+tvTypeKey+"-"+areaKey+"--";
            if (pageNum==1){
                url=url+".html";
            }else {
                url=url+"--"+pageNum+".html";
            }

            Document doc=getRequestAddHeader(url);
            if (null!=doc){

                    Elements lis=doc.select("div#contentList").select("ul").select("li");
                    for (int i=0;i<lis.size();i++){


                        if (null!=lis.get(i).select("div.pic")||lis.get(i).select("div.pic").equals("")){
                            TvSeries tvSeries=new TvSeries();

                            tvSeries.setPosterUrl("http:"+lis.get(i).select("img").attr("data-src"));

                            //Log.d("grabTV",lis.get(i).html());

                            tvSeries.setTvNumber(lis.get(i).select("span.pRightBottom").text());
                            tvSeries.setDescUrl("http:"+lis.get(i).select("a.aPlayBtn").attr("href"));
                            tvSeries.setTvName(lis.get(i).select("span.sTit").select("a").text());

                            Elements ems=lis.get(i).select("em");

                            int h=0;

                            TvSeries.Actor[] actors=new TvSeries.Actor[ems.size()];
                            for (int j=0;j<ems.size();j++){

                                if (!("").equals(ems.get(j).select("a").text().trim())) {



                                    TvSeries.Actor actor = new TvSeries().new Actor();
                                    actor.setName(ems.get(j).select("a").text().trim());
                                    actor.setUrl("http:" + ems.get(j).select("a").attr("href"));

                                    actors[h] = actor;

                                    h=h+1;
                                }
                            }
                            tvSeries.setActors(actors);

                            if (!("").equals(lis.get(i).select("span.sTit").select("a").text())){

                                seriesList.add(tvSeries);
                            }

                        }

                    }

            }else {
                Log.d("grabTv","doc is null");
            }

        }


        /*
        * 搜狗影视大全爬取
        * */
        /*private void grabTV(){
            //http://tv.sogou.com/teleplay/list/
            //http://tv.sogou.com/teleplay/list/zone-韩国+fee-正片+page-3.html
            String url="http://tv.sogou.com/teleplay/list/";
            if (!area.equals("全部地区")){
                url=url+"zone-"+URLEncoder.encode(area);
            }

            if (!sourceType.equals("全部资源")){

                if (!area.equals("全部地区")){

                    url=url+"+fee-"+URLEncoder.encode(sourceType);
                }else {
                    url=url+"fee-"+URLEncoder.encode(sourceType);
                }
            }

            if (pageNum>1){

                if ((!area.equals("全部地区")) || (!sourceType.equals("全部资源" ))){

                    url=url+"+page-"+pageNum;
                }else {
                    url=url+"page-"+pageNum;
                }
            }

            url=url+".html";


            Log.d("grabTV",url);

            Document doc=getRequestAddHeader(url);
            if (null!=doc){
                Elements lis=doc.select("ul.sort_lst").select("li.video_item");

                for (int i=0;i<lis.size();i++){
                    TvSeries tvSeries=new TvSeries();

                    tvSeries.setPosterUrl(lis.get(i).select("img").attr("src"));
                    tvSeries.setDescUrl("http://tv.sogou.com/"+lis.get(i).select("div.sort_lst_tit").select("a").attr("href"));
                    tvSeries.setTvName(lis.get(i).select("div.sort_lst_tit").text());
                    tvSeries.setScore(lis.get(i).select("em.stress").text());
                    tvSeries.setTvNumber(lis.get(i).select("div.sort_lst_thumb_txt_lft").text());

                    TvSeries.Actor[] actors=new TvSeries.Actor[1];
                    actors[0]=new TvSeries().new Actor();
                    actors[0].setName(lis.get(i).select("p.sort_lst_txt").text());
                    tvSeries.setActors(actors);

                    seriesList.add(tvSeries);
                }
            }else {
                Log.d("doc","doc is null");
            }


        }*/

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
        void onRequestEnd(Object tvList,boolean suc);
    }

    public void setRequestListener(requestEndListener listener){
        this.mRequestEndListener=listener;
    }
}
