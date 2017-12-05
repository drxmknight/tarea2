package suzukiKasami;

import remoteInterfaces.RemoteInterface;
import token.Token;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;

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
    public int[] RN;
    private int processNumber;
    public int initialDelay;
    public String state;
    //private Token token;
    public String name;
    public Token token = null;
    public boolean hasToken = false;

    public SuzukiKasami(int id, int procNumber, int initialDelay, String bearer) throws RemoteException {
        super();
        this.id = id;
        this.RN = new int[procNumber];
        this.processNumber = procNumber;
        this.name = "process" + Integer.toString(this.id);
        this.initialDelay = initialDelay;
        this.state = "green";

        for (int i = 0; i < procNumber; i++) {
            this.RN[i] = 0;
        }

        if (bearer.equals("true")) {
            this.hasToken = true;
            this.token = new Token(procNumber);
        }
    }

    // Implementation for methods specified in the remote interfaces.
    @Override
    public String Hello(String caller) {
        return "Hello " + this.name + " , im " + caller;
    }

    @Override
    public void request(int id, int seq)  {
        // Seq update.
        if (this.RN[id] < seq) {
            this.RN[id] = seq;
        }
        // If the process has the token and is Idle.
        if (this.hasToken && !this.state.equals("red")) {
            if (this.RN[id] == this.token.LN[id] + 1) {
                sendToken(id);
            }
        }

        if (this.hasToken && this.state.equals("red")) {
            this.token.queue.add(id);
        }
    }

    @Override
    public void takeToken(Token token) {
        this.token = token;
        this.hasToken = true;
        this.state = "red";
    }

    @Override
    public void waitToken() {
        this.state = "yellow";
    }

    @Override
    public void kill() {
        System.exit(0);
    }


    public void printState() {
        System.out.println("Process " + this.id + " : " + this.state);
    }

    // Send request to all processes.
    public void requestAll() {
        RemoteInterface remote;
        // Update seq number before requesting all processes.
        this.RN[this.id] += 1;
        // Send request to all processes but itself.
        for (int i = 0; i < processNumber; i++) {
            if (i != this.id) {
                String remoteProcessName = "process" + Integer.toString(i);
                try {
                    remote = (RemoteInterface) Naming.lookup(remoteProcessName);
                    remote.request(this.id, this.RN[this.id]);
                } catch (RemoteException e) {
                    System.out.println("Method for " + remoteProcessName + " not found.");
                } catch (NotBoundException | MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void sendToken(int id) {
        String receiver = "process" + Integer.toString(id);
        try {
            RemoteInterface remote = (RemoteInterface) Naming.lookup(receiver);
            remote.takeToken(this.token);
            this.hasToken = false;
            this.state = "green";
            this.token = null;
        } catch (RemoteException e) {
            System.out.println("Method for " + receiver + " not found.");
        } catch (NotBoundException | MalformedURLException e) {
            e.printStackTrace();
        }
    }

    // Update outstanding requests.
    public void updateQueue() {
        for (int i = 0; i < processNumber; i++) {
            if (!contains(this.RN, i) && this.RN[i] == this.token.LN[i] + 1) {
                this.token.queue.add(i);
            }
        }
    }

    private boolean contains(final int[] array, final int key) {
        for (int i : array) {
            if (i == key) {
                return true;
            }
        }
        return false;
    }

    public void finish() {
        System.out.println(this.name + " : " + Arrays.toString(this.token.LN));
        if (!contains(this.token.LN, 0)) {
            for (int i = 0; i < processNumber; i++) {
                if (i != this.id) {
                    String receiver = "process" + Integer.toString(i);
                    try {
                        RemoteInterface remote = (RemoteInterface) Naming.lookup(receiver);
                        remote.kill();
                    } catch (RemoteException e) {
                        System.out.println(receiver + " ended.");
                    } catch (NotBoundException | MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.println(this.name + " ended.");
            this.kill();
        }
    }
}
