package com.krajetum.jqnap;

public class QNAPTest {

    public static void main(String argv[]){
        QNAPCore core = new QNAPCore("192.168.1.240", 8080);
        core.login("krajetum", "test");
        core.getSid();
        core.logout();
    }


}
