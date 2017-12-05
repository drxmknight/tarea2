package suzukiKasami;

import remoteInterfaces.RemoteInterface;
import token.Token;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/*
 *  This class implements the remote interfaces. This must do:
 *  * Declare the remote interfaces being implemented.
 *  * Define the constructor for each remote object.
 *  * Provide an implementation for each remote method in the remote interfaces.
 *  Besides, this class must extend java.rmi.server.UnicastRemoteObject to make it
 *  available for clients.
 */
public class SuzukiKasami extends UnicastRemoteObject implements RemoteInterface {

    public int id;
    public int[] RM;
    private int processNumber;
    public int initialDelay;
    public String state = "green";
    //private Token token;
    public String name;
    public Token token = null;


    // Class constructor.
    public SuzukiKasami(int id, int procNumber, int initialDelay, String bearer) throws RemoteException {
        super();
        this.id = id;
        this.RM = new int[procNumber];
        this.processNumber = procNumber;
        this.name = "process" + Integer.toString(this.id);
        this.initialDelay = initialDelay;

        for (int i = 0; i < procNumber; i++) {
            this.RM[i] = 0;
        }

        if (bearer.equals("true")) {
            this.state = "red";
            System.out.println("Process " + this.id + " in Critic Section");
            this.token = new Token(procNumber);
        }
    }

    // Implementation for methods specified in the remote interfaces.
    @Override
    public String Hello(String caller) {
        return "Hello " + caller + " , im " + this.name;
    }

    @Override
    public boolean request(int id, int seq)  {
        return this.RM[id] < seq;
    }


    private void printState() {
        System.out.println("Process " + this.id + " : " + this.state);
    }

    private void requestAll() {
        for(int i = 0; i < processNumber; i++) {

        }
    }

}
