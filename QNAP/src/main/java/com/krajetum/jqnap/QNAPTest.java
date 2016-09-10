package com.krajetum.jqnap;

import com.krajetum.jqnap.objects.QNAPFile;
import com.krajetum.jqnap.objects.QNAPFolder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.List;

public class QNAPTest {

    public static void main(String argv[]){
        QNAPCore core = new QNAPCore("192.168.1.240", 8080);
        if(core.login("krajetum", "hacker96")) {
            File downloadFile = core.downloadFile("/Public", "test.txt", true);
            try {
                FileUtils.copyFile(downloadFile, new File("D:\\Film\\file.zip"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        core.logout();
    }


}
