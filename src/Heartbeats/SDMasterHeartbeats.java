package Heartbeats;

import Master.SDMasterNode;
import Master.SDSlave;
import Util.SDUtil;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.*;

/**
 * Created by amaliujia on 14-12-20.
 */
public class SDMasterHeartbeats extends TimerTask{

    //private ArrayList<SDSlave> slaveList;
    private HashMap<Integer, SDSlave> slaveList;
    private DatagramSocket listener;
    private HashMap<Integer, Boolean> responderList;
    private HashMap<String, Integer> slaveMap;

    /**
     * Constructor for SDMaterHeartbeats
     * @param map
     *          reference of slave list, used to track slaves
     */
    public SDMasterHeartbeats(HashMap<Integer, SDSlave> map){
        System.err.println("SDMasterHeartbeats Constructor");

        responderList = new HashMap<Integer, Boolean>();
        slaveMap = new HashMap<String, Integer>();
        slaveList = new HashMap<Integer, SDSlave>();

        synchronized (map){
            slaveList = map;
            Iterator iter = map.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                SDSlave value = (SDSlave)entry.getValue();
                System.out.println("created key " + value.getAddress() + value.getPortString());
                slaveMap.put(value.getAddress() + value.getPortString(), (Integer)key);
            }
        }
    }

    public void run() {
        if (!this.slaveMap.isEmpty()) {
            query();
            try {
                startListening(System.currentTimeMillis());
            } catch (IOException e) {
                System.out.println("IOException");
                e.printStackTrace();
            }
            maintain();
        }
    }


    /**
     * take turns to send hearbeat command
     */
    private void query(){
        synchronized (slaveList) {
            for (Integer ID : slaveList.keySet()) {
                SDSlave slaveNode = slaveList.get(ID);
                slaveNode.out.write("Alive?\n");
                slaveNode.out.flush();
            }
        }
    }

    /**
     * create a UDP listener and receive heart beats replies.
     * @param currentTime
     *          heart beats triggering time
     * @throws IOException
     *          throws when UDP socket receive data wrongly.
     */
    private void startListening(long currentTime) throws IOException {
        System.out.println("Start UDP listening and slave map size + " + slaveMap.size());
        try {
            listener = new DatagramSocket(SDUtil.heatbeatsPort);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        //if UDP socket creates successfully
        responderList.clear();
        if(listener != null){

            //prepare buffer
            byte[] buf = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buf, 1024);

            while(true){
                //next timeout computation
                long interval = getCurrentTimeInMillionSeconds() - currentTime;
                int timeout = (int) (SDUtil.heartbeatsIntervalMillionSeconds - interval);
                System.err.println("still have " + timeout);
                if(timeout <= 0){
                    break;
                }
                //start listening
                try {
                    listener.setSoTimeout(timeout);
                    listener.receive(packet);
                    //TODO: what should I do now?
                    String receiveBuf = new String(packet.getData(), 0, packet.getLength());
                    System.out.println("This time I get " + packet.getAddress().getHostAddress() + " " + receiveBuf);
                    String key = packet.getAddress().getHostAddress() + receiveBuf;
                    System.out.println("Get key " + key );
                    if(slaveMap.containsKey(key)){
                        responderList.put(slaveMap.get(key), true);
                    }else{
                        SDUtil.fatalError("Wrong slave key!!!!");
                    }
                } catch (SocketTimeoutException e){
                    System.err.println("UDP socket time out");
                }
            }
            listener.close();
        }
    }

    /**
     * get time in million seconds from 1-1-1970
     * @return
     *      current time in million seconds
     */
    private long getCurrentTimeInMillionSeconds(){
         Date currentData = new Date();
         return currentData.getTime();
    }

    /**
     * check whether there are some slave node breaking down
     */
    private void maintain(){
        ArrayList<String> shutDown = new ArrayList<String>();
        for (String addr : slaveMap.keySet()){
            if (!responderList.containsKey(slaveMap.get(addr))){
                shutDown.add(addr);
            }
        }
        //print
        for (String addr : shutDown){
            System.out.println(slaveMap.get(addr));
            slaveMap.remove(addr);
        }
    }
}
