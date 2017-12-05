package process;

import remoteInterfaces.RemoteInterface;
import suzukiKasami.SuzukiKasami;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;


public class Process extends Thread {

    public static void main(String[] args) {

        RemoteInterface suzukiKazami;

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
        SuzukiKasami process = null;
        try {
            process = new SuzukiKasami(id, n, initialDelay, bearer);
            Naming.rebind(process.name, process);
            System.out.println(process.name + " bound to RMI.");
        } catch (RemoteException | MalformedURLException e) {
            e.printStackTrace();
        }

        if (bearer.equals("false")) {
            System.out.println("entroooo");
            try {
                suzukiKazami = (RemoteInterface) Naming.lookup("process0");
                System.out.println(suzukiKazami.Hello(process.name));
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
            RemoteInterface obj = (RemoteInterface) Naming.lookup("HelloInterface");
            System.out.println(obj.Hello());
        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            e.printStackTrace();
        }*/
    }
}
