package remoteObjects;

import remoteInterfaces.remoteInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
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
public class remoteObject extends UnicastRemoteObject implements remoteInterface {

    // Class constructor. Its invoked even if omitted. Added for clarity.
    public remoteObject() throws RemoteException {
        super();
    }

    // Implementation for methods specified in the remote interfaces.
    @Override
    public String Hello() {
        return "Hello World";
    }



    public static void main(String[] args) {
        try {
            remoteInterface suzukiKazami = new remoteObject();
            Naming.rebind("suzukiKazami", suzukiKazami);
            System.out.println("suzukiKazami Object Bound to the RMI.");
        } catch (RemoteException | MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
