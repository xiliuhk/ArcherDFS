package RMI.RMIBase;

import RMI.Client.SDRemoteObjectReference;
import Util.SDUtil;

import java.net.UnknownHostException;
import java.rmi.registry.Registry;

/**
 * Created by amaliujia on 14-12-23.
 */
public class SDLocateRegistry {
    private static Object lock = new Object();
    private static Registry registry = null;

    public static Registry getRegistry(){
        return getRegistry(null, SDUtil.RMIRegistryPort);
    }

    public static Registry getRegistry(int port){
        return getRegistry(null, port);
    }

    public static Registry getRegistry(String address){
        return getRegistry(address, SDUtil.RMIRegistryPort);
    }

    public static Registry getRegistry(String host, int port){
        synchronized (lock){
            if(registry == null){
                if(host == null){
                    try {
                        host = SDUtil.getHost();
                    } catch (UnknownHostException e){
                        host = "";
                    }
                }
                SDRemoteObjectReference ref = new SDRemoteObjectReference(
                        host, port, "", SDUtil.REGISTRY_OBJID
                );
                //TODO: getRegistry from server.
            }
        }
        return registry;
    }
}
