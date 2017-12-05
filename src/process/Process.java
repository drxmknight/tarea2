package process;

import remoteInterfaces.remoteInterface;
import remoteObjects.remoteObject;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Random;


public class Process extends Thread {

    private int id;
    private int[] RM;
    private int initialDelay;
    private String state = "green";
    //private Token token;
    private String name;

    private Process(int id, int procNumber, int initialDelay, String bearer) {
        this.id = id;
        this.RM = new int[procNumber];
        this.name = "process" + Integer.toString(this.id);
        this.initialDelay = initialDelay;

        for (int i = 0; i < procNumber; i++) {
            this.RM[i] = 0;
        }

        if (bearer.equals("true")) {
            this.state = "red";
            System.out.println("Process " + this.id + " in Critic Section");
            //this.token = new Token(Integer.parseInt(args[1]));
        }


    }

    private void printState() {
        System.out.println("Process " + this.id + " : " + this.state);
    }

    private void requestAll() {

    }

    public static void main(String[] args) {

        remoteInterface suzukiKazami;

        if (args.length < 4) {
            System.out.println("Wrong arguments.");
            return;
        }

        // Get arguments from stdin.
        int id = Integer.parseInt(args[0]);
        int n = Integer.parseInt(args[1]);
        int initialDelay = Integer.parseInt(args[2]);
        String bearer = args[3];

        // Create process.
        Process process = new Process(id, n, initialDelay, bearer);

        // Bind process methods to rmi registry.
        try {
            suzukiKazami = new remoteObject();
            Naming.rebind(process.name, suzukiKazami);
            System.out.println(process.name + " bound to RMI.");
        } catch (RemoteException | MalformedURLException e) {
            e.printStackTrace();
            return;
        }



        if (bearer.equals("false")) {
            System.out.println("entroooo");
            try {
                suzukiKazami = (remoteInterface) Naming.lookup("process1");
                System.out.println(suzukiKazami.Hello());
            } catch (NotBoundException | MalformedURLException | RemoteException e) {
                e.printStackTrace();
            }

        }

        /*while (true) {
            // Simulate a wait until the process send a request of the token.
            if (process.state.equals("green")) {
                try {
                    Random r = new Random();
                    int rand = r.nextInt(9) + 1;
                    Thread.sleep(1000 * rand);
                    process.state = "yellow";
                    process.printState();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
            }

            // Simulate a wait in the critic section.
            if (process.state.equals("red")) {
                try {
                    Random r = new Random();
                    int rand = r.nextInt(2) + 1;
                    Thread.sleep(1000 * rand);
                    process.state = "green";
                    process.printState();
                    *//*if (process.token.queue.isEmpty()) {
                        System.out.println("No hay mas procesos");
                    }*//*
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }*/

        /*try {
            remoteInterface obj = (remoteInterface) Naming.lookup("HelloInterface");
            System.out.println(obj.Hello());
        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            e.printStackTrace();
        }*/
    }
}
