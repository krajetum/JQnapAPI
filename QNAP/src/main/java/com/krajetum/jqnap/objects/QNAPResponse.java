package com.krajetum.jqnap.objects;

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
            case 9:return "Quota Limit Exceeded";
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
