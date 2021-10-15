package com.juanpablo.eduky.Videos;

/**
 * Created by PERSONAL on 22/04/2018.
 */

public class Video {

    String nombreVideo, urlVideo;

    public Video() {
    }

    public Video(String nombreVideo, String urlVideo) {
        this.nombreVideo = nombreVideo;
        this.urlVideo = urlVideo;
    }

    public String getNombreVideo() {
        return nombreVideo;
    }

    public void setNombreVideo(String nombreVideo) {
        this.nombreVideo = nombreVideo;
    }

    public String getUrlVideo() {
        return urlVideo;
    }

    public void setUrlVideo(String urlVideo) {
        this.urlVideo = urlVideo;
    }

    @Override
    public String toString() {
        return "Video{" +
                "nombreVideo='" + nombreVideo + '\'' +
                ", urlVideo='" + urlVideo + '\'' +
                '}';
    }
}
