package com.andy.free.Jsoup;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.andy.free.bean.Variety;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/13.
 */

public class VarietyJsoup {

    private String varietyAreaKey;
    private int pageNum;

    private List<Variety> varietyList=new ArrayList<>();
    private Handler mHandler;
    private boolean suc;
    private requestEndListener mRequestEndListener;

    public VarietyJsoup() {
    }

    public VarietyJsoup(String varietyAreaKey, int pageNum) {
        this.varietyAreaKey = varietyAreaKey;
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

            grabVariety();

            Message message=new Message();
            message.what=1;
            message.obj=varietyList;
            mHandler.sendMessage(message);
        }
    }

    /*
    * 2345抓取综艺
    * */
    private void grabVariety() {

        //http://kan.2345.com/zongyi/l/
        //http://kan.2345.com/zongyi/ldqdalu/
        //http://kan.2345.com/zongyi/ldqgangtai/
        //http://kan.2345.com/zongyi/ldqrihan/
        //http://kan.2345.com/zongyi/ldqoumei/
        //http://kan.2345.com/zongyi/lpxdefault/2/
        //http://kan.2345.com/zongyi/ldqgangtai/pxdefault/2/
        String url="";
        if (pageNum==1){
            url="http://kan.2345.com/zongyi/l"+varietyAreaKey;
        }else {
            url="http://kan.2345.com/zongyi/l"+varietyAreaKey+"/pxdefault/"+pageNum;
        }

        Document doc=getRequestAddHeader(url);

        if (null!=doc) {

            Elements lis=doc.select("ul.v_picTxt").select("li");
            for (int i=0;i<lis.size();i++){

                if (lis.get(i).select("div.pic").size()!=0){
                    Variety variety=new Variety();
                    variety.setName(lis.get(i).select("a.aPlayBtn").attr("title"));
                    variety.setPosterUrl("http:"+lis.get(i).select("img").attr("data-src"));
                    variety.setLatestEpisode(lis.get(i).select("em").text());
                    //variety.setSimpleDesc(lis.get(i).select("p.star").text());
                    variety.setDescUrl("http:"+lis.get(i).select("a.aPlayBtn").attr("href"));

                    varietyList.add(variety);
                }
            }


        }else {

            Log.d("log","doc is null");
        }


    }


    /*
    * 360影视抓取综艺
    * */
    private void grabVarietyBy360() {

        //http://www.360kan.com/zongyi/list?cat=all&act=all&area=all
        //http://www.360kan.com/zongyi/list?cat=all&act=all&area=10
        //http://www.360kan.com/zongyi/list?cat=all&act=all&area=10&pageno=3
        String url="";
        if (pageNum==1){
            url="http://www.360kan.com/zongyi/list?cat=all&act=all&area="+varietyAreaKey;
        }else {

            url="http://www.360kan.com/zongyi/list?cat=all&act=all&area="+varietyAreaKey+"&pageno="+pageNum;
        }

        Log.d("grabVariety",url);

        Document doc=getRequestAddHeader(url);

        if (null!=doc){

            Elements lis=doc.select("li.item");
            for (int i=0;i<lis.size();i++){
                Variety variety=new Variety();
                variety.setName(lis.get(i).select("span.s1").text());
                variety.setPosterUrl(lis.get(i).select("img").attr("src"));
                variety.setLatestEpisode(lis.get(i).select("span.hint").text());
                variety.setSimpleDesc(lis.get(i).select("p.star").text());
                variety.setDescUrl("http://www.360kan.com"+lis.get(i).select("a.js-tongjic").attr("href"));

                varietyList.add(variety);
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
                    .timeout(60000)
                    .header("Accept", "*/*")
                    .header("Accept-Encoding", "gzip, deflate")
                    .header("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
                    .header("Referer", "https://www.baidu.com/")
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0")
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
        void onRequestEnd(Object varietyList,boolean suc);
    }

    public void setRequestListener(requestEndListener listener){
        this.mRequestEndListener=listener;
    }
}
