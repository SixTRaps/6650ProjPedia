import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Server implements RemoteDevInterface, RemoteUserInterface {
    private static final Logger logger = LogManager.getLogger(Server.class);
    private  ConcurrentHashMap<String, String> store;
    private int port;
    private String userAccountFileLocation = "./credential";
    private String dataFileLocation = "./data";
    private Map<String, String> userCredentials = new HashMap<>();
    private Map<String, Boolean> ableEdit = new HashMap<>();
    private static RemoteDevInterface[] servers = new Server[5];

    // Jieren creates
    private boolean loginStatus;
    private String loginUser;
    private Server myCoordinator;
    private Map<String, List<String>> pendingInfo = new HashMap<String, List<String>>();
    private Map<String, List<String>> pendingUserInfo = new HashMap<String, List<String>>();

    public Server(int port){
        this.port = port;
        loginStatus = false;
        loginUser = "";
        store = new ConcurrentHashMap<>();
        readUsersFromFolder();
        readDataFromFolder();
    }

    private void readUsersFromFolder() {
        File folder = new File(userAccountFileLocation + "/" + this.port);
        if (!folder.exists()) {
            folder.mkdirs();
            return;
        }
        File[] users = folder.listFiles();
        for (File user: users) {
            int length = user.getName().length();
            // Get the txt file name as username
            String username = user.getName().substring(0, length - 4);
            try {
                InputStreamReader reader = new InputStreamReader(new FileInputStream(user));
                BufferedReader br = new BufferedReader(reader);
                // Assume password is in one line
                String password = br.readLine();
                // Store username and password into the map
                userCredentials.put(username, password);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Read all txt files from the data folder
    private void readDataFromFolder() {
        File folder = new File(dataFileLocation + "/" + this.port);
        if (!folder.exists()) {
            folder.mkdirs();
            return;
        }
        File[] entries = folder.listFiles();
        if (entries.length == 0) return;
        for (File entry: entries) {
            int length = entry.getName().length();
            String entryKey = entry.getName().substring(0, length - 4);
            try {
                InputStreamReader reader = new InputStreamReader(new FileInputStream(entry));
                BufferedReader br = new BufferedReader(reader);
                String line = br.readLine();
                StringBuilder info = new StringBuilder(line);
                // Read all information for each entry
                while (line != null) {
                    info.append("\n");
                    line = br.readLine();
                    if (line != null) info.append(line);
                }
                // Store the entry and the information into the map
                store.put(entryKey, info.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void createUserTxt(String username, String password) {
        String folderPath = userAccountFileLocation + "/" + this.port;
        try {
            File directory = new File(folderPath);
            if (!directory.exists() || !directory.isDirectory()) {
                boolean mkdir = directory.mkdir();
                if (mkdir) {
                    System.out.println("Make directory for the server " + port + " successfully");
                } else {
                    System.out.println("Fail to make directory for the server " + port);
                }
            } else {
                //System.out.println("No need to create new directory for server " + port);
            }
            File writeName = new File(folderPath + "/" + username + ".txt");
            if (writeName.exists() && writeName.isFile()){
                writeName.delete();
            }
            writeName.createNewFile();
            BufferedWriter out = new BufferedWriter(new FileWriter(writeName));
            out.write(password);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void createDataTxt(String entry, String info) {
        String folderPath = dataFileLocation + "/" + this.port;
        try {
            File directory = new File(folderPath);
            if (!directory.exists() || !directory.isDirectory()) {
                boolean mkdir = directory.mkdir();
                if (mkdir) {
                    System.out.println("Make directory for the server " + port + " successfully");
                } else {
                    System.out.println("Fail to make directory for the server " + port);
                }
            } else {
                //System.out.println("No need to create new directory for server " + port);
            }
            File writeName = new File(folderPath + "/" + entry + ".txt");
            if (writeName.exists() && writeName.isFile()){
                writeName.delete();
            }
            writeName.createNewFile();
            BufferedWriter out = new BufferedWriter(new FileWriter(writeName));
            StringBuilder formatInfo = new StringBuilder();
            // Make the string in txt more beautiful
            for (int i = 0; i < info.length(); i++) {
                if (i > 0 && i % 120 == 0) formatInfo.append("\n");
                formatInfo.append(info.charAt(i));
            }
            out.write(formatInfo.toString());
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteUserTxt(String username) {
        String txtName = username + ".txt";
        try {
            File file = new File(userAccountFileLocation + "/" + port + "/" + txtName);
            System.out.println();
            if (file.delete()) {
                System.out.println(file.getName() + " is deleted");
            } else {
                System.out.println("Fail to delete " + file.getName() + "!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteDataTxt(String entry) {
        String txtName = entry + ".txt";
        try {
            File file = new File(dataFileLocation + "/" + port + "/" + txtName);
            System.out.println();
            if (file.delete()) {
                System.out.println(file.getName() + " is deleted");
            } else {
                System.out.println("Fail to delete " + file.getName() + "!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void election() {
        //TODO
    };

    @Override
    public String hasDataUpdate(int port, String update) throws Exception {
        return null;
    }

    @Override
    public Set<String> getEntryKeyList() {
        System.out.println(store.keySet());
        return store.keySet();
    }

    @Override
    public boolean ableToEdit(String entryKey, String username) throws Exception {
        if (ableEdit.containsKey(entryKey) == false) {
            changeEditStatus(entryKey, username);
            return true;
        } else {
            if (ableEdit.get(entryKey) == true) {
                changeEditStatus(entryKey, username);
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public void changeEditStatus(String entryKey, String username) throws Exception {
        if (ableEdit.get(entryKey) == true) {
            ableEdit.put(entryKey, false);
        } else {
            ableEdit.put(entryKey, true);
        }
    }


    @Override
    public String get(String key) throws Exception {
        logger.info("Getting " + key + " from store");
        System.out.println(store);
        System.out.println(key);
        System.out.println(store.containsKey(key));
        if (store.containsKey(key)) {
            System.out.println(store.get(key));
            return store.get(key);
        } else {
            return "None";
        }
    }

    @Override
    public String put(String key, String value) throws Exception {
        logger.info("Putting " + key + " with value " + value + " into store");
        int notifyAndCommit = myCoordinator.coordinatorReceiveChange("put", key, value);
        if (notifyAndCommit == 0) {
            logger.error("Cannot notify the coordinator");
            return "Fail";
        }
        return "Success";
    }

    @Override
    public String delete(String key) throws Exception {
        logger.info("Deleting " + key + " from store");
        int notifyAndCommit = myCoordinator.coordinatorReceiveChange("delete", key, "");
        if (notifyAndCommit == 0) {
            logger.error("Cannot notify the coordinator");
            return "Fail";
        }
        return "Success";
    }

    /**
     * Any server notify the coordinator via this function
     * @param action the action from the server
     * @param key the key from the server
     * @param value the value of the key from the server
     * @return 1 means succeed; 0 means fail
     * @throws Exception required exception for all RPC
     */
    @Override
    public int coordinatorReceiveChange(String action, String key, String value) throws Exception {
        logger.info("Send " + action + " action to the coordinator");
        int times = 0;
        while (times < 10) {
            int count = 0;
            for (RemoteDevInterface ser: servers) {
                count += ser.otherServerReceiveChange(action, key, value);
            }
            if (count == 5) {

                int commitTimes = 0;
                while (commitTimes < 10) {
                    int commitCount = 0;
                    for (RemoteDevInterface ser: servers) {

                        commitCount += ser.commitChange();
                    }
                    if (commitCount == 5) {
                        return 1;
                    }
                    commitTimes++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            times++;
        }
        logger.error("Cannot notify all the servers");
        return 0;
    }



    /**
     * Coordinator notify all the servers via this function
     * @param action the action to be done
     * @param key the key to be used
     * @param value the value to be used if any
     * @return 1 means succeed; 0 means fail
     * @throws Exception required exception for all RPC
     */
    @Override
    public int otherServerReceiveChange(String action, String key, String value) throws Exception{
        if (this.pendingInfo.containsKey(key)) {
            logger.info("ACK: Server " + this.port + " has been acknowledged");
            return 1;
        } else {
            try {
                List<String> info = Arrays.asList(action, value);
                this.pendingInfo.put(key, info);
                logger.info("ACK: Server " + this.port + " has been acknowledged -- " + key);
                return 1;
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        }
    }


    /**
     * Coordinator let all the server commit the change
     * @return 1 means succeed; 0 means fail
     * @throws Exception required exception for all RPC
     */
    @Override
    public int commitUserChange() throws Exception{
        List<String> changed = new ArrayList<>();
        for (String pendingKey: this.pendingUserInfo.keySet()) {
            List<String> info = this.pendingUserInfo.get(pendingKey);
            if (info.get(0).equals("register")) {
                userCredentials.put(pendingKey, info.get(1));
                createUserTxt(pendingKey, info.get(1));
                logger.info("Go: Server " + this.port + " successfully register " + pendingKey);
            }
            changed.add(pendingKey);
        }
        int i = 0;
        while (i < changed.size()) {
            this.pendingUserInfo.remove(changed.get(i));
            i++;
        }
        return 1;
    }

    @Override
    public String login(String userName, String userPwd) throws Exception {
        if (loginStatus) {
            return "Already login as " + loginUser + ". Please logout first";
        } else {
            if (userCredentials.containsKey(userName) && userPwd.equals(userCredentials.get(userName))) {
                loginStatus = true;
                loginUser = userName;
                return "Login successfully";
            } else {
                return "Wrong username or password";
            }
        }

    }

    @Override
    public String signup(String userName, String userPwd) throws Exception {
        if (userCredentials.containsKey(userName)) {
            return "User already exit. No need for register";
        } else {
            logger.info("Putting " + userName + " with value " + userPwd + " into store");
            int notifyAndCommit = myCoordinator.coordinatorReceiveUserChange("register", userName, userPwd);
            userCredentials.put(userName, userPwd);
            createUserTxt(userName, userPwd);
            return "Sign up successfully";
        }
        
    }

    @Override
    public String logout(String userName) throws Exception {
        if (loginStatus) {
            loginStatus = false;
            return "Logout successfully";
        } else {
            return "You are not log in. Please register or login";
        }
    }


    /**
     * Any server notify the coordinator via this function
     * @param action the action from the server
     * @param key the key from the server
     * @param value the value of the key from the server
     * @return 1 means succeed; 0 means fail
     * @throws Exception required exception for all RPC
     */
    @Override
    public int coordinatorReceiveUserChange(String action, String key, String value) throws Exception {
        logger.info("Send " + action + " action to the coordinator");
        int times = 0;
        while (times < 10) {
            int count = 0;
            for (RemoteDevInterface ser: servers) {
                count += ser.otherServerReceiveUserChange(action, key, value);
            }
            if (count == 5) {
                int commitTimes = 0;
                while (commitTimes < 10) {
                    int commitCount = 0;
                    for (RemoteDevInterface ser: servers) {
                        commitCount += ser.commitUserChange();
                    }
                    if (commitCount == 5) {
                        return 1;
                    }
                    commitTimes++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            times++;
        }
        logger.error("Cannot notify all the servers");
        return 0;
    }



    /**
     * Coordinator notify all the servers via this function
     * @param action the action to be done
     * @param key the key to be used
     * @param value the value to be used if any
     * @return 1 means succeed; 0 means fail
     * @throws Exception required exception for all RPC
     */
    @Override
    public int otherServerReceiveUserChange(String action, String key, String value) throws Exception{
        if (this.pendingUserInfo.containsKey(key)) {
            logger.info("ACK: Server " + this.port + " has been acknowledged");
            return 1;
        } else {
            try {
                List<String> info = Arrays.asList(action, value);
                this.pendingUserInfo.put(key, info);
                logger.info("ACK: Server " + this.port + " has been acknowledged and added the new key " + key);
                return 1;
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        }
    }


    /**
     * Coordinator let all the server commit the change
     * @return 1 means succeed; 0 means fail
     * @throws Exception required exception for all RPC
     */
    @Override
    public int commitChange() throws Exception{
        List<String> changed = new ArrayList<>();
        for (String pendingKey: this.pendingInfo.keySet()) {
            List<String> info = this.pendingInfo.get(pendingKey);
            if (info.get(0).equals("put")) {
                store.put(pendingKey, info.get(1));
                createDataTxt(pendingKey, info.get(1));
                logger.info("Go: Server " + this.port + " successfully put " + pendingKey +" in to the store");
                logger.info("Server " + + this.port + "'s store is" + this.store);
            } else {
                System.out.println(this.store.containsKey(pendingKey));
                if (this.store.containsKey(pendingKey)) {
                    this.store.remove(pendingKey);
                    deleteDataTxt(pendingKey);
                    logger.info("Go: Server " + this.port + " successfully delete" + pendingKey +" in to the store");
                }
                logger.info("Server " + + this.port + "'s store is" + this.store);
            }
            changed.add(pendingKey);
        }
        int i = 0;
        while (i < changed.size()) {
            this.pendingInfo.remove(changed.get(i));
            i++;
        }
        return 1;
    }


    public static void main(String[] args) {

        try {
            Server[] serverlist = new Server[5];
            for (int i = 0; i < 5; i++) {
                    int port = 32000 + i;
                    serverlist[i] = new Server(port);
                    serverlist[i].myCoordinator = serverlist[0];
                    RemoteUserInterface stub = (RemoteUserInterface) UnicastRemoteObject.exportObject(serverlist[i], port);
                    Registry registry = LocateRegistry.createRegistry(port);
                    registry.rebind(String.format("PROJPEDIA"), stub);
                    System.out.println(String.format("Server %s is running at port %s",
                            new String[]{Integer.toString(i), String.valueOf(port)}));
            }
            Registry[] registriesClient = {LocateRegistry.getRegistry("localhost", 32000), LocateRegistry.getRegistry("localhost", 32001),
                    LocateRegistry.getRegistry("localhost", 32002), LocateRegistry.getRegistry("localhost", 32003),
                    LocateRegistry.getRegistry("localhost", 32004)};
            Server.servers = new RemoteDevInterface[] {(RemoteDevInterface) registriesClient[0].lookup("PROJPEDIA"), (RemoteDevInterface) registriesClient[1].lookup("PROJPEDIA"), (RemoteDevInterface) registriesClient[2].lookup("PROJPEDIA"),
                    (RemoteDevInterface) registriesClient[3].lookup("PROJPEDIA"), (RemoteDevInterface) registriesClient[4].lookup("PROJPEDIA")};

        } catch (Exception e) {
        System.err.println("Server exception: " + e.toString());
    }

    }


}
