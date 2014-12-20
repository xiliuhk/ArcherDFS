import Master.SDMasterNode;
import Util.SDUtil;

/**
 * Created by amaliujia on 14-12-20.
 */
public class SDArcherMain {
    public static void main(String[] args) throws Exception {
        if(args.length == 0){
            //start master
            SDMasterNode master = new SDMasterNode();
            master.startService(SDUtil.masterLinstenerPort);
        }else if(args.length == 2){
            //start salve
//            SDSlaveNode slave = new SDSlaveNode(args[0], Integer.parseInt(args[1]));
//            slave.connect();
//            slave.slaveService();
        }else{
            System.out.println("Arguments wrong");
        }
    }
}
