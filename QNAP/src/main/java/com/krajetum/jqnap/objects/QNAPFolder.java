package com.krajetum.jqnap.objects;

public class QNAPFolder {

    private String name;
    private String id;
    private String mode;
    private int draggable;
    private String type;


    public QNAPFolder(String name, String id, String mode, int draggable, String type) {
        this.name = name;
        this.id = id;
        this.mode = mode;
        this.draggable = draggable;
        this.type = type;

    }


    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setDraggable(int draggable) {
        this.draggable = draggable;
    }

    public void setType(String type) {
        this.type = type;
    }


    @Override
    public String toString(){
        return "Name: "+name +" | ID: "+id+" | Mode: "+mode+" | Draggable: "+draggable+" | Type: "+type;
    }
}
