package Util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static java.lang.System.exit;
/**
 * Created by amaliujia on 14-12-20.
 */
public class SDUtil {
    public static final int masterListenerPort = 16640;
    //public static final String masterAddress = "128.237.191.211";
    public static final String masterAddress = "localhost";
    public static final int heatbeatsPort = 16641;
    public static final long heartbeatsIntervalMillionSeconds = 3500;

    public static final String REMOTESTUBIP = masterAddress;
    public static final int MASTER_RMIRegistry_PORT = 16642;
    public static final int REGISTRY_OBJID = 0x0;

    public static final int POOLSIZE = 10;

    public static final String LOGPATH = "/Users/amaliujia/Documents/github/ArcherDFS/Resources/ArcherDFS.log";
    public static final int DEFAULT_REPLICATION = 1;

    static public void fatalError (String message) {
        System.err.println (message);
        exit(1);
    }

    static public String getLocalHost()
            throws UnknownHostException {
        InetAddress inetAddress = InetAddress.getLocalHost();
        return inetAddress.getHostName();
    }
}
