package com.andy.free.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Andy Lau on 2017/11/13.
 */
public class Variety implements Serializable {

    public Variety() {
    }

    private String name;
    private String simpleDesc;
    private String LatestEpisode;
    private String posterUrl;
    private String descUrl;
    private String type;
    private String time;
    private String area;
    private String hostName;
    private ArrayList<simpleVariety> simpleVarieties;

    @Override
    public String toString() {
        return "Variety{" +
                "name='" + name + '\'' +
                ", simpleDesc='" + simpleDesc + '\'' +
                ", LatestEpisode='" + LatestEpisode + '\'' +
                ", posterUrl='" + posterUrl + '\'' +
                ", descUrl='" + descUrl + '\'' +
                ", type='" + type + '\'' +
                ", time='" + time + '\'' +
                ", area='" + area + '\'' +
                ", hostName='" + hostName + '\'' +
                ", simpleVarieties=" + simpleVarieties +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSimpleDesc() {
        return simpleDesc;
    }

    public void setSimpleDesc(String simpleDesc) {
        this.simpleDesc = simpleDesc;
    }

    public String getLatestEpisode() {
        return LatestEpisode;
    }

    public void setLatestEpisode(String latestEpisode) {
        LatestEpisode = latestEpisode;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getDescUrl() {
        return descUrl;
    }

    public void setDescUrl(String descUrl) {
        this.descUrl = descUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public ArrayList<simpleVariety> getSimpleVarieties() {
        return simpleVarieties;
    }

    public void setSimpleVarieties(ArrayList<simpleVariety> simpleVarieties) {
        this.simpleVarieties = simpleVarieties;
    }

    class simpleVariety implements Serializable{


        public simpleVariety() {
        }

        @Override
        public String toString() {
            return "simpleVariety{" +
                    "desc='" + desc + '\'' +
                    ", episodeName='" + episodeName + '\'' +
                    ", posterUrl='" + posterUrl + '\'' +
                    ", playUrl='" + playUrl + '\'' +
                    '}';
        }

        private String desc;
        private String episodeName;
        private String posterUrl;
        private String playUrl;

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getEpisodeName() {
            return episodeName;
        }

        public void setEpisodeName(String episodeName) {
            this.episodeName = episodeName;
        }

        public String getPosterUrl() {
            return posterUrl;
        }

        public void setPosterUrl(String posterUrl) {
            this.posterUrl = posterUrl;
        }

        public String getPlayUrl() {
            return playUrl;
        }

        public void setPlayUrl(String playUrl) {
            this.playUrl = playUrl;
        }
    }
}
