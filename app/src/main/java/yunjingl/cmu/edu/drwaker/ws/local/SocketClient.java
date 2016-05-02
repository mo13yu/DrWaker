package yunjingl.cmu.edu.drwaker.ws.local;

import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by yapeng on 5/1/2016.
 */
public class SocketClient extends Thread{
//    InetAddress iAddress = InetAddress.getLocalHost();
//    String currentIp = iAddress.getHostAddress();
    String dstAddress;
    int dstPort=8844;
    private ObjectInputStream objInputStream = null;
    private ObjectOutputStream objOutputStream = null;
    Socket socket;
    private String math=null;

    public SocketClient(String addr, int port) {
        dstAddress = addr;
        dstPort = port;
    }

    public void run(){
        if(openConnection()){
            handleSession();
            closeConnection();
        }
    }
    public boolean openConnection() {

        try {
            socket = new Socket( dstAddress, dstPort );
            objInputStream = new ObjectInputStream( socket.getInputStream() );
//            objOutputStream = new ObjectOutputStream( socket.getOutputStream() );

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void closeConnection(){
        try {
            socket.close();
            objInputStream.close();
//            objOutputStream.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void handleSession(){
        try {
            math=(String)objInputStream.readObject();
            Log.d("test",math);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getMath(){
        //Log.d("test", math);
        return math;
    }

//    public String getIP() {
//        String strAddress = "";
//        try {
//            InetAddress local = InetAddress.getLocalHost();
//            byte[] b = local.getAddress();
//
//            for (int i = 0; i < b.length; i++) {
//                strAddress += ((int) 255 & b[i]) + ".";
//            }
//            strAddress = strAddress.substring(0, strAddress.length() - 1);
//
//        }
//        catch(UnknownHostException e){}
//        return strAddress;
//    }
}
