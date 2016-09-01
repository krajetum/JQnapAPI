public class QNAPCore {
    String host;

    public QNAPCore(String ip) {
        host = ip+":"+Integer.toString(8080);
    }
    public QNAPCore(String ip, int port){
        host = ip+":"+Integer.toString(port);
    }


    public static void main(String args[]){
        EzEncode ezEncode = new EzEncode();
        System.out.println(ezEncode.ezEncode(ezEncode.utf8to16("nodilapach")));






    }

    boolean login(String user, String pass){





        return true;
    }




}
