package remoteInterfaces;

import token.Token;

import java.rmi.Remote;
import java.rmi.RemoteException;


/* This class defines the method to be remotely accessible.
 * Must extend java.rmi.Remote.
 * This method must be defined as being capable of throwing a java.rmi.RemoteException.
 */
public interface RemoteInterface extends Remote {
    String Hello(String caller) throws RemoteException;
    void request(int id, int seq) throws RemoteException;
    void takeToken(Token token) throws RemoteException;
    void waitToken() throws RemoteException;
    void kill() throws RemoteException;
}
