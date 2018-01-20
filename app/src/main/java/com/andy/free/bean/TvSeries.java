package com.andy.free.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/2.
 */
public class TvSeries implements Serializable {

    private String area;
    private String sourceType;
    private String tvNumber;
    private String tvName;
    private String score;
    private Actor[] actors;
    private String director;
    private String tvType;
    private String time;
    private String alias;
    private String desc;
    private String posterUrl;
    private String descUrl;
    private ArrayList<Map<Integer,String>> playNumAndUrls;

    @Override
    public String toString() {
        return "TvSeries{" +
                "area='" + area + '\'' +
                ", sourceType='" + sourceType + '\'' +
                ", tvNumber='" + tvNumber + '\'' +
                ", tvName='" + tvName + '\'' +
                ", score='" + score + '\'' +
                ", actors=" + Arrays.toString(actors) +
                ", director='" + director + '\'' +
                ", tvType='" + tvType + '\'' +
                ", time='" + time + '\'' +
                ", alias='" + alias + '\'' +
                ", desc='" + desc + '\'' +
                ", posterUrl='" + posterUrl + '\'' +
                ", descUrl='" + descUrl + '\'' +
                ", playNumAndUrls=" + playNumAndUrls +
                '}';
    }

    public ArrayList<Map<Integer, String>> getPlayNumAndUrls() {
        return playNumAndUrls;
    }

    public void setPlayNumAndUrls(ArrayList<Map<Integer, String>> playNumAndUrls) {
        this.playNumAndUrls = playNumAndUrls;
    }

    public String getDescUrl(){
        return descUrl;
    }

    public void setDescUrl(String descUrl){
        this.descUrl=descUrl;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getTvNumber() {
        return tvNumber;
    }

    public void setTvNumber(String tvNumber) {
        this.tvNumber = tvNumber;
    }

    public String getTvName() {
        return tvName;
    }

    public void setTvName(String tvName) {
        this.tvName = tvName;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public Actor[] getActors() {
        return actors;
    }

    public void setActors(Actor[] actors) {
        this.actors = actors;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getTvType() {
        return tvType;
    }

    public void setTvType(String tvType) {
        this.tvType = tvType;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public class Actor implements Serializable{

        private String name;
        private String url;

        public Actor(String name, String url) {
            this.name = name;
            this.url = url;
        }

        public Actor() {
        }

        @Override
        public String toString() {
            return "Actor{" +
                    "name='" + name + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

}
