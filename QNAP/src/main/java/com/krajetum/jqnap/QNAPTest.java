package com.krajetum.jqnap;


import com.krajetum.jqnap.objects.QNAPResponse;


import java.io.File;


public class QNAPTest {

    public static void main(String argv[]){
        QNAPCore core = new QNAPCore("192.168.1.240", 8080);
        if(core.login("krajetum", "hacker96")) {
            QNAPResponse response = core.uploadFile("/Public", new File("D:/Film/thumb.jpg"), false);

            System.out.println(response);

        }
        core.logout();
    }


}

