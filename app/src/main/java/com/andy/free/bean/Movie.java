package com.andy.free.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

/**
 * Created by Administrator on 2017/10/16.
 */

public class Movie implements Parcelable {


    private String name;
    private String url;
    private String type;
    private String score;
    private String time;
    private String posterUrl;
    private Role[] roles;

    @Override
    public String toString() {
        return "Movie{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", type='" + type + '\'' +
                ", score='" + score + '\'' +
                ", time='" + time + '\'' +
                ", posterUrl='" + posterUrl + '\'' +
                ", roles=" + Arrays.toString(roles) +
                '}';
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public Role[] getRoles() {
        return roles;
    }

    public void setRoles(Role[] roles) {
        this.roles = roles;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }


    public class Role{

        public Role() {}

        private String name;
        private String url;

        @Override
        public String toString() {
            return "Role{" +
                    "name='" + name + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }

        public Role(String name, String url) {
            this.name = name;
            this.url = url;
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
