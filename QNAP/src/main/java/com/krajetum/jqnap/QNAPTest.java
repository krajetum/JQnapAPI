package com.krajetum.jqnap;

import com.krajetum.jqnap.objects.QNAPFile;
import com.krajetum.jqnap.objects.QNAPResponse;

import java.io.File;
import java.util.List;

public class QNAPTest {
    public static void main(String[] argv){
        QNAPCore core = new QNAPCore("192.168.1.240", 8080);
        if(core.login("krajetum", "hacker96")){

            List<QNAPFile> fileList = core.search("int", "/Multimedia/Film", 50);
            for (QNAPFile qnapFile : fileList) {
                System.out.println(qnapFile);
            }


        }
    }
}
