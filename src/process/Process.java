package process;

import suzukiKasami.SuzukiKasami;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Random;


public class Process {

    public static void main(String[] args) {

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
        SuzukiKasami process;
        try {
            process = new SuzukiKasami(id, n, initialDelay, bearer);
            Naming.rebind(process.name, process);
            System.out.println(process.name + " bound to RMI.");
            // Initial wait before algorithm starts.
            Thread.sleep(1000);
        } catch (RemoteException | MalformedURLException | InterruptedException e) {
            e.printStackTrace();
            return;
        }

        Random r = new Random();


        while (true) {

            // Simulate a wait until the process send a request of the token.
            if (process.state.equals("green")) {
                process.printState();
                try {
                    int rand = r.nextInt(3) + 1;
                    Thread.sleep(1000 * rand);
                    // Process tries to enter to the CS.
                    if (process.hasToken)
                        process.state = "red";
                    else
                        process.state = "yellow";
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
            }

            if (process.state.equals("yellow")) {
                try {
                    process.requestAll();

                    Thread.sleep(process.initialDelay);
                    process.printState();

                    if (process.hasToken) {
                        process.state = "red";
                    }
                    else
                        process.state = "green";
                    process.printState();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Simulate a wait in the CS.
            if (process.state.equals("red")) {
                try {
                    int rand = r.nextInt(2) + 1;
                    Thread.sleep(1000 * rand);
                    // Appends outstanding requests.
                    process.update();
                    // Finish if the token went through all processes.
                    process.finish();
                    if (!process.token.queue.isEmpty()) {
                        int newId = process.token.queue.remove(0);
                        process.sendToken(newId);
                    }
                    else {
                        process.state = "green";
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
            }

        }
    }


}
