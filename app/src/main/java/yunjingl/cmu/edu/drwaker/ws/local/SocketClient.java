package yunjingl.cmu.edu.drwaker.ws.local;

import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * connect to remote server database through socket.
 */
public class SocketClient extends Thread{
    String dstAddress;
    int dstPort=8844;
    private ObjectInputStream objInputStream = null;
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
        return math;
    }

}
