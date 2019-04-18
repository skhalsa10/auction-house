package Auction.Agent;

import Auction.Messages.Message;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

public class BankConnection implements Runnable {
    private String hostName;
    private int portNumber;
    private Socket socket;
    private boolean connected = true;
    private LinkedBlockingQueue<Message> messages = new LinkedBlockingQueue<>();
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public BankConnection(String hostName, int portNumber){
        this.hostName = hostName;
        this.portNumber = portNumber;
        connect();
    }

    public void sendMessage(Message m) {
        try {
            //messages.put(m);
            out.writeObject(m);
        }
        catch(Exception e) {
            System.err.println(e);
        }

    }

    public void connect() {
        try {

            this.socket = new Socket(hostName, portNumber);
            in = new ObjectInputStream(this.socket.getInputStream());
            out = new ObjectOutputStream(this.socket.getOutputStream());
        }
        catch(Exception e) {
            System.err.println(e);
        }
        connected = true;
        new Thread(this).start();

    }
    @Override
    public void run () {
        //connect();

        while(connected) {
            //System.out.println("in run bank ");
            try {
                in.readObject();


            }
            catch (Exception e) {
                System.err.println(e);
            }
        }
    }
}
