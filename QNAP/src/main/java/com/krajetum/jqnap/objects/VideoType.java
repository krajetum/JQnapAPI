package com.krajetum.jqnap.objects;

public class VideoType{
    private boolean mp4_1080;
    private boolean mp4_720;
    private boolean mp4_480;
    private boolean mp4_360;
    private boolean mp4_240;

    public VideoType(boolean mp4_1080, boolean mp4_720, boolean mp4_480, boolean mp4_360, boolean mp4_240){
        this.mp4_1080 = mp4_1080;
        this.mp4_720 = mp4_720;
        this.mp4_480 = mp4_480;
        this.mp4_360 = mp4_360;
        this.mp4_240 = mp4_240;
    }

    public boolean isMp4_1080() {
        return mp4_1080;
    }

    public boolean isMp4_720() {
        return mp4_720;
    }

    public boolean isMp4_480() {
        return mp4_480;
    }

    public boolean isMp4_360(){
        return mp4_360;
    }

    public boolean isMp4_240(){
        return mp4_240;
    }

    @Override
    public String toString() {
        return "{MP4_1080: "+isMp4_1080()+"; MP4_720: "+isMp4_720()+"; MP4_480: "+isMp4_480()+"; MP4_360: "+isMp4_360()+"; MP4_240:"+isMp4_240()+"}";
    }
}
