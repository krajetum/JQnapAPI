package com.krajetum.jqnap;

import com.krajetum.jqnap.objects.QNAPFile;
import com.krajetum.jqnap.objects.QNAPFolder;

import java.util.List;

public class QNAPTest {

    public static void main(String argv[]){
        QNAPCore core = new QNAPCore("192.168.1.240", 8080);
        if(core.login("krajetum", "hacker96")) {

            /*
                List<QNAPFile> fileList = core.getFileList("/Multimedia/Film");
                for (QNAPFile file : fileList) {
                    System.out.println(file);
                }
            */
            System.out.println(core.getFileSize("/Multimedia/Film", "Interstellar.mp4"));


        }
        core.logout();
    }


}
