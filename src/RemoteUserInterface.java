import java.rmi.Remote;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public interface RemoteUserInterface extends Remote {
    /**
     * The method to add/update key-value pair saved in the server.
     * @param key the key to add/update
     * @param value the corresponding value of the key
     * @return execution response from the server
     * @throws Exception possible exception
     */
    String put(String key, String value) throws Exception;

    /**
     * The method to get the value of certain key.
     * @param key the key
     * @return the value of the key
     */
    String get(String key) throws Exception;

    /**
     * The method to delete a key-value pair on the server.
     * @param key the key of the key-value pair to be deleted
     * @return execution response from the server
     * @throws Exception possible exception
     */
    String delete(String key) throws Exception;

    Set<String> getEntryKeyList() throws Exception;

    boolean ableToEdit(String entryKey) throws Exception;

    void putToEdit(String key) throws Exception;

    void removeFromEdit(String key) throws Exception;


    String login(String userName, String userPwd) throws Exception;

    String signup(String userName, String userPwd) throws Exception;
}
