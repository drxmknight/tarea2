package remoteInterfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;


/* This class defines the method to be remotely accessible.
 * Must extend java.rmi.Remote.
 * This method must be defined as being capable of throwing a java.rmi.RemoteException.
 */
public interface remoteInterface extends Remote {
    String Hello() throws RemoteException;
}
