package com.krajetum.jqnap.objects;

import com.krajetum.jqnap.utils.Tools;

import javax.tools.Tool;


public class QNAPFile {
    private String name;
    private boolean isFolder;
    private String size;
    private int type;
    private VideoType videoType;
    private String creationDate;
    public QNAPFile(String name, boolean isFolder, double size, int type, VideoType videoType){
        this.name = name;
        this.isFolder = isFolder;
        this.size = Tools.byteToString((long)size, true);
        this.type = type;
        if(isFolder){
            this.videoType = new VideoType(false, false, false, false, false);
        }else{
            this.videoType = videoType;
        }
    }
    public QNAPFile(String name, boolean isFolder, double size, int type, VideoType videoType, String creationDate){
        this.name = name;
        this.isFolder = isFolder;
        this.size = Tools.byteToString((long)size, true);
        this.type = type;
        if(isFolder){
            this.videoType = new VideoType(false, false, false, false, false);
        }else{
            this.videoType = videoType;
        }
    }

    public QNAPFile(String name, double size, String creationDate, int type, boolean isFolder) {
        this.creationDate = creationDate;
        this.type = type;
        this.size = Tools.byteToString((long)size, true);
        this.name = name;
        this.isFolder = isFolder;
    }

    public String getName() {
        return name;
    }

    public boolean isFolder() {
        return isFolder;
    }

    public String getSize() {
        return size;
    }

    public String getType() {
        switch (type){
            case FileType.UNDEFINED:
                return "UNDEFINED";
            case FileType.MUSIC:
                return "MUSIC";
            case FileType.VIDEO:
                return "VIDEO";
            case FileType.PHOTO:
                return "PHOTO";
            default:
                return "UNDEFINED";
        }
    }

    public VideoType getVideoType() {
        return videoType;
    }

    public String getCreationDate() {
        return creationDate;
    }

    @Override
    public String toString(){
        if(!creationDate.equals("")){
            return "{filename: "+getName()+" ; isFolder: "+isFolder()+" ; size: "+getSize()+" ; type: "+getType()+" ; video_type: "+getVideoType()+" ; creation_date: "+creationDate+"}";
        }else{
            return "{filename: "+getName()+" ; isFolder: "+isFolder()+" ; size: "+getSize()+" ; type: "+getType()+" ; video_type: "+getVideoType()+"}";
        }

    }


}


interface FileType{

    int UNDEFINED = 0;
    int MUSIC = 1;
    int VIDEO = 2;
    int PHOTO = 3;

}