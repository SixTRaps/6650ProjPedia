import java.rmi.Remote;

public interface RemoteDevInterface extends Remote {
    /**
     * This method should be called by Server instance.
     * It informs the coordinator that there is a data update,
     * either put or delete operation.
     * @param port the port number of the Server
     * @param update the data upadate message
     * @return the coordinator response to the data update
     * @throws Exception possible exceptions
     */
    String hasDataUpdate(int port, String update) throws Exception;


    /**
     * Any server notify the coordinator via this function
     * @param action the action from the server
     * @param key the key from the server
     * @param value the value of the key from the server
     * @return 1 means succeed; 0 means fail
     * @throws Exception required exception for all RPC
     */
    public int coordinatorReceiveChange(String action, String key, String value) throws Exception;


    /**
     * Coordinator notify all the servers via this function
     * @param action the action to be done
     * @param key the key to be used
     * @param value the value to be used if any
     * @return 1 means succeed; 0 means fail
     * @throws Exception required exception for all RPC
     */
    public int otherServerReceiveChange(String action, String key, String value) throws Exception;

    /**
     * Coordinator let all the server commit the change
     * @return 1 means succeed; 0 means fail
     * @throws Exception required exception for all RPC
     */
    public int commitChange() throws Exception;


    public int coordinatorReceiveUserChange(String action, String key, String value) throws Exception;


    /**
     * Coordinator notify all the servers via this function
     * @param action the action to be done
     * @param key the key to be used
     * @param value the value to be used if any
     * @return 1 means succeed; 0 means fail
     * @throws Exception required exception for all RPC
     */
    public int otherServerReceiveUserChange(String action, String key, String value) throws Exception;

    /**
     * Coordinator let all the server commit the change
     * @return 1 means succeed; 0 means fail
     * @throws Exception required exception for all RPC
     */
    public int commitUserChange() throws Exception;

    public int changeCoordinator() throws Exception;
    public String election() throws Exception;

//    public int removeCoordinator() throws Exception;

    public int removeMyCoordinator() throws Exception;
}
