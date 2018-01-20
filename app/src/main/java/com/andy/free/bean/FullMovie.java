package com.andy.free.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by Administrator on 2017/10/28.
 */

public class FullMovie implements Serializable {

    //名字
    private String name;
    //类型
    private String type;
    //地区
    private String area;
    //年代
    private String year;
    //付费状态
    private String payState;
    //主演
    public Actors[] actors;
    //评分
    private String score;
    //播放链接
    private ArrayList<Map<String,String>> urls;
    //海报url
    private String posterUrl;
    //描述
    private String desc;
    //时长
    private String time;
    //2345细节链接
    private String descUrl;

    public String getDescUrl(){
        return descUrl;
    }

    public void setDescUrl(String descUrl){
        this.descUrl=descUrl;
    }

    public String getTime(){
        return time;
    }

    public void setTime(String time){
        this.time=time;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPayState() {
        return payState;
    }

    public void setPayState(String payState) {
        this.payState = payState;
    }

    public Actors[] getActors() {
        return actors;
    }

    public void setActors(Actors[] actors) {
        this.actors = actors;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public ArrayList<Map<String, String>> getUrls() {
        return urls;
    }

    public void setUrls(ArrayList<Map<String, String>> urls) {
        this.urls = urls;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public FullMovie(String name, String type, String area, String year, String payState, Actors[]
            actors, String score, ArrayList<Map<String, String>> urls, String posterUrl,String desc,String time,String descUrl) {
        this.name = name;
        this.type = type;
        this.area = area;
        this.year = year;
        this.payState = payState;
        this.actors = actors;
        this.score = score;
        this.urls = urls;
        this.posterUrl = posterUrl;
        this.desc=desc;
        this.time=time;
        this.descUrl=descUrl;
    }

    public FullMovie() {
    }

    @Override
    public String toString() {
        return "FullMovie{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", area='" + area + '\'' +
                ", year='" + year + '\'' +
                ", payState='" + payState + '\'' +
                ", actors=" + Arrays.toString(actors) +
                ", score='" + score + '\'' +
                ", urls=" + urls +
                ", posterUrl='" + posterUrl + '\'' +
                ", desc='" + desc + '\'' +
                ", time='" + time + '\'' +
                ", descUrl='" + descUrl + '\'' +
                '}';
    }

    public class Actors implements Serializable {

        private String name;
        private String personPageUrl;

        public Actors() {
        }

        public Actors(String name, String personPageUrl) {
            this.name = name;
            this.personPageUrl = personPageUrl;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPersonPageUrl() {
            return personPageUrl;
        }

        public void setPersonPageUrl(String personPageUrl) {
            this.personPageUrl = personPageUrl;
        }

        @Override
        public String toString() {
            return "Actors{" +
                    "name='" + name + '\'' +
                    ", personPageUrl='" + personPageUrl + '\'' +
                    '}';
        }
    }

}
