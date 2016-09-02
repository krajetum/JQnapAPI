import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.IOException;

public class QNAPCore {
    String host;
    String sid;
    public QNAPCore(String ip) {
        host = ip+":"+Integer.toString(8080);
    }
    public QNAPCore(String ip, int port){
        host = ip+":"+Integer.toString(port);
    }

    boolean login(String user, String pass){
        EzEncode encode = new EzEncode();
        try {
            HttpResponse<JsonNode> jsonResponse = Unirest.post("http://" + host + "/cgi-bin/filemanager/wfm2Login.cgi")
                    .queryString("user", user)
                    .queryString("pwd", encode.encode(pass))
                    .asJson();
            System.out.println(jsonResponse.getBody().toString());

            int status = jsonResponse.getBody().getObject().getInt("status");
            if(status==1){
                sid = jsonResponse.getBody().getObject().getString("sid");
                return true;
            }else{
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





}
