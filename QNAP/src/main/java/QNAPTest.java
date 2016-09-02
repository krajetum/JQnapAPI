
/**
 * Created by Lorenzo on 02/09/2016.
 */
public class QNAPTest {

    public static void main(String argv[]){
        QNAPCore core = new QNAPCore("192.168.1.240", 8080);
        System.out.println(core.login("krajetum", "test"));
        core.logout();
    }


}
