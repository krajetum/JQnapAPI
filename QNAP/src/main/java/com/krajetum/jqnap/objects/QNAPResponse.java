package com.krajetum.jqnap.objects;

/**
 * Created by Lorenzo on 10/09/2016.
 */
public class QNAPResponse {

    private int status;
    private boolean success;

    public QNAPResponse(int status, boolean success){
        this.status = status;
        this.success = success;
    }

    public String getStatus() {
        switch (status){
            case 1:return "OK";
            case 2:return "File Exists";
            case 4:return "Permission Denied";
            default:return "Error";
        }
    }

    public boolean isSuccess() {
        return success;
    }

    @Override
    public String toString() {
        return "{status:"+getStatus()+"; success:"+isSuccess()+"}";
    }
}
