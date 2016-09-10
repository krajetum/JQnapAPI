package com.krajetum.jqnap;

import com.krajetum.jqnap.enums.QNAPFileIndex;
import com.krajetum.jqnap.enums.QNAPFolderIndex;
import com.krajetum.jqnap.objects.QNAPFile;
import com.krajetum.jqnap.objects.QNAPFolder;
import com.krajetum.jqnap.objects.QNAPResponse;
import com.krajetum.jqnap.objects.VideoType;
import com.krajetum.jqnap.utils.EzEncode;
import com.krajetum.jqnap.utils.Tools;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
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
                logger.info("Logged in successfully");
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
        if(!sid.equals(""))return sid;
        else{
            logger.log(Level.SEVERE, "Error: you must login first");
            return null;
        }
    }

    public List<QNAPFolder> getFolderList(String path){
        List<QNAPFolder> fileList = new ArrayList<QNAPFolder>();
        try {
            if(sid!=null){
                HttpResponse<JsonNode> jsonResponse = Unirest.post("http://"+host+"/cgi-bin/filemanager/utilRequest.cgi?func=get_tree&sid="+sid+"&is_iso=0&node="+path).asJson();
                for(int i=0; i<jsonResponse.getBody().getArray().length();i++) {
                    JSONObject object = jsonResponse.getBody().getArray().getJSONObject(i);
                    fileList.add(new QNAPFolder(object.getString(QNAPFolderIndex.TEXT),
                                              object.getString(QNAPFolderIndex.ID),
                                              object.getString(QNAPFolderIndex.MODE),
                                              object.getInt(QNAPFolderIndex.DRAGGABLE),
                                              object.getString(QNAPFolderIndex.TYPE)));
                }
                return fileList;
            }
            else{
                logger.log(Level.SEVERE, "Error: you must login first");
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<QNAPFile> getFileList(String path){
        List<QNAPFile> fileList = new ArrayList<QNAPFile>();
        try {
            if(sid!=null){
                HttpResponse<JsonNode> jsonResponse = Unirest.post("http://"+host+"/cgi-bin/filemanager/utilRequest.cgi?func=get_list&sid="+sid+"&is_iso=0&list_mode=all&path="+path+"&dir=ASC&limit=20&sort=filename&start=0")
                                                      .asJson();
                for(int i=0; i<jsonResponse.getBody().getObject().getJSONArray("datas").length();i++) {
                    JSONObject object = jsonResponse.getBody().getObject().getJSONArray("datas").getJSONObject(i);
                    fileList.add(new QNAPFile(  object.getString(QNAPFileIndex.FILENAME),
                                                object.getInt(QNAPFileIndex.isFolder) == 1,
                                                object.getDouble(QNAPFileIndex.SIZE),
                                                object.getInt(QNAPFileIndex.TYPE),
                                                new VideoType(  object.getInt(QNAPFileIndex.MP4_1080)==1,
                                                        object.getInt(QNAPFileIndex.MP4_720)==1,
                                                        object.getInt(QNAPFileIndex.MP4_480)==1,
                                                        object.getInt(QNAPFileIndex.MP4_360)==1,
                                                        object.getInt(QNAPFileIndex.MP4_240)==1)
                                                ));
                }
                return fileList;
            }
            else{
                logger.log(Level.SEVERE, "Error: you must login first");
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getFileSize(String path, String filename){
        try {
            if(sid!=null){
                HttpResponse<JsonNode> jsonResponse = Unirest.post("http://"+host+"/cgi-bin/filemanager/utilRequest.cgi?func=get_file_size&sid="+getSid()+"&path="+path+"&total="+1+"&name="+filename)
                                                        .asJson();
                JSONObject obj = jsonResponse.getBody().getObject();
                if(obj.getInt("status")==1){
                    return Tools.byteToString(obj.getLong("size"),true);
                }else{
                    return "Permission Deny";
                }
            }
            else{
                logger.log(Level.SEVERE, "Error: you must login first");
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;
    }


    public QNAPResponse createFolder(String path, String filename){
        try {
            if(sid!=null){
                HttpResponse<JsonNode> jsonResponse = Unirest.post("http://"+host+"/cgi-bin/filemanager/utilRequest.cgi?func=createdir&sid="+getSid()+"&dest_folder="+filename+"&dest_path="+path)
                        .asJson();

                JSONObject object = jsonResponse.getBody().getObject();

                return new QNAPResponse(object.getInt("status"), object.getBoolean("success"));

            }
            else{
                logger.log(Level.SEVERE, "Error: you must login first");
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;
    }

    public QNAPResponse rename(String path, String filename, String newName){
        try {
            if(sid!=null){
                HttpResponse<JsonNode> jsonResponse = Unirest.post("http://"+host+"/cgi-bin/filemanager/utilRequest.cgi?func=rename&sid="+getSid()+"path="+path+"&source_name="+filename+"&dest_name="+newName)
                        .asJson();
                JSONObject object = jsonResponse.getBody().getObject();
                return new QNAPResponse(object.getInt("status"), object.getBoolean("success"));
            }
            else{
                logger.log(Level.SEVERE, "Error: you must login first");
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     *     copy a file or a folder to another location
     *
     *     @param filename String
     *     @param sourcePath String
     *     @param destPath String
     *     @param mode int : 1=skip 0=overwrite
     *
     *     @return QNAPResponse
     *
     * */
    public QNAPResponse copy( String filename,String sourcePath,String destPath, int mode){
        try {
            if(sid!=null){
                HttpResponse<JsonNode> jsonResponse = Unirest.post("http://"+host+"/cgi-bin/filemanager/utilRequest.cgi?func=copy&sid="+getSid()+"&source_file="+filename+"source_total=1" +
                                                                    "&source_path="+sourcePath+"&dest_path="+destPath+"&mode="+mode).asJson();
                JSONObject object = jsonResponse.getBody().getObject();
                return new QNAPResponse(object.getInt("status"), object.getBoolean("success"));
            }
            else{
                logger.log(Level.SEVERE, "Error: you must login first");
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;
    }
    public QNAPResponse move( String filename,String sourcePath,String destPath, int mode){
        try {
            if(sid!=null){
                HttpResponse<JsonNode> jsonResponse = Unirest.post("http://"+host+"/cgi-bin/filemanager/utilRequest.cgi?func=move&sid="+getSid()+"&source_file="+filename+"source_total=1" +
                        "&source_path="+sourcePath+"&dest_path="+destPath+"&mode="+mode).asJson();
                JSONObject object = jsonResponse.getBody().getObject();
                return new QNAPResponse(object.getInt("status"), object.getBoolean("success"));
            }
            else{
                logger.log(Level.SEVERE, "Error: you must login first");
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static final String PREFIX = "stream2file";
    public static final String SUFFIX = ".tmp";
    public File downloadFile(String path, String filename, boolean compress){
        try {
            if(sid!=null){
                HttpResponse<InputStream> jsonResponse = Unirest.post("http://" + host + "/cgi-bin/filemanager/utilRequest.cgi?func=download&sid=" + getSid()+"&isfolder=0&compress="+
                                                                    (compress?1:0) +"&source_path="+path+"&source_file="+filename+"&source_total=1")
                                                                    .asBinary();

                // write the inputStream to a FileOutputStream
                final File tempFile = File.createTempFile(PREFIX, SUFFIX);
                tempFile.deleteOnExit();
                FileOutputStream out = new FileOutputStream(tempFile);
                IOUtils.copy(jsonResponse.getBody(), out);
                return tempFile;
            }
            else{
                logger.log(Level.SEVERE, "Error: you must login first");
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public File downloadFolder(String path, String filename, boolean compress){
        try {
            if(sid!=null){
                HttpResponse<InputStream> jsonResponse = Unirest.post("http://" + host + "/cgi-bin/filemanager/utilRequest.cgi?func=download&sid=" + getSid()+"&isfolder=1&compress="+
                                                                    (compress?1:0) +"&source_path="+path+"&source_file="+filename+"&source_total=1").asBinary();
                // write the inputStream to a FileOutputStream
                final File tempFile = File.createTempFile(PREFIX, SUFFIX);
                tempFile.deleteOnExit();
                FileOutputStream out = new FileOutputStream(tempFile);
                IOUtils.copy(jsonResponse.getBody(), out);
                return tempFile;
            }
            else{
                logger.log(Level.SEVERE, "Error: you must login first");
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }




}
