package com.krajetum.jqnap;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QNAPCore {
    String host;
    String sid;
    Logger logger;
    public QNAPCore(String ip) {
        logger = Logger.getLogger("krajetum.jqnap");
        host = ip+":"+Integer.toString(8080);
    }
    public QNAPCore(String ip, int port){
        logger = Logger.getLogger("krajetum.jqnap");
        host = ip+":"+Integer.toString(port);
    }

    boolean login(String user, String pass){
        EzEncode encode = new EzEncode();
        try {
            logger.log(Level.INFO, "Starting connection to "+host);
            HttpResponse<JsonNode> jsonResponse = Unirest.post("http://" + host + "/cgi-bin/filemanager/wfm2Login.cgi")
                                                    .queryString("user", user)
                                                    .queryString("pwd", encode.encode(pass))
                                                    .asJson();

            int status = jsonResponse.getBody().getObject().getInt("status");
            if(status==1){
                sid = jsonResponse.getBody().getObject().getString("sid");
                return true;
            }else{
                logger.warning("Could not login. Check your username and password");
                return false;
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return false;
    }

    boolean logout(){
        try {
            HttpResponse<JsonNode> jsonResponse = Unirest.post("http://" + host + "/cgi-bin/filemanager/wfm2Login.cgi").asJson();
            return jsonResponse.getBody().getObject().getString("success").equals("true");
        } catch (UnirestException e) {
            e.printStackTrace();
        }finally {
            try {
                Unirest.shutdown();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public String getSid(){
        if(sid!=null)return sid;
        else{
            logger.log(Level.SEVERE, "Error: you must login first");
            return null;
        }
    }

    public void getList(String path){
        try {
            if(sid!=null){
                HttpResponse<JsonNode> jsonResponse = Unirest.post("http://"+host+"/cgi-bin/filemanager/utilRequest.cgi?func=get_tree&amp;sid="+sid+"&amp;is_iso=0&amp;node="+path).asJson();
                for(int i=0; i<jsonResponse.getBody().getArray().length();i++)
                    jsonResponse.getBody().getArray().getJSONObject(i).getString("text");
            }
            else{
                logger.log(Level.SEVERE, "Error: you must login first");
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

}
