# 6650projpedia

## 1. Introduction

The project constructs a program that serves as a Wikipedia-like encyclopedia. It supports multiple users to do CRUD operations for entries in the database.
For the front-end, we use Java Swing to design GUI. As for the database, we use .txt files to store all users' information and all entries with their 
definitions in different folders. 

The project mainly utilizes the Java RMI library to build the server program. Multiple servers and databases are constructed to ensure data availability. 
Besides, a two-phase commit algorithm is implemented to improve the fault tolerance. In this system, sign-up and login function are achieved. User names 
and passwords will be stored as .txt files, and users need to login first to look up the definition of entries. Only one user can edit a specific entry at 
the same time. 

## 2. Algorithm

### a. Distributed mutual exclusion

We implement the central server algorithm for mutual exclusion. The central server algorithm simulates a single processor system. One process in the 
distributed system is elected as the coordinator. When a process wants to enter a resource, it sends a request message (a) identifying the resource. 
If nobody is currently in the section, the coordinator sends back a grant message (b) and marks that process as using the resource. When a process is done 
with its resource, it sends a release message (c) to the coordinator.

We implement this mutual exclusion when many users want to edit the definition at the same time. If someone is editting a entry, anyone else trying to 
edit the same entry will receive an alert message.

### b. Fault tolerance

For fault tolerance, we consider about coordinator crash and other servers crash. If minortity of servers other than coordinator crash, it will send a 
server fail message to client. Since the coordinator is the most important part of servers it will be omitted due to the two-phases commit protocol. But
if coordinator crash, servers will make a election to find a new coordinator. We implement a election algorithm to make sure the fault tolerance in this 
case.

### c. distributed consensus

Consensus is an act where in a system of processes agrees upon a value or decision. To satisfy it, we use a two-phase commit protocol. In our 
system, after the server has received the changing request from the client, the coordinator contacts all the participants to suggest the value proposed 
by the client and solicit their response. After receiving all the responses, the coordinator decides to commit if all participants agreed upon the value 
or abort if someone disagrees. In the second phase, the coordinator contacts all participants ¸again and communicates the commit or abort decision.

### d. Data redundancy

Under the primary-backup system, the client sends requests only to one designated primary server and only the primary server executes the request. If the 
primary fails, one of the backup servers becomes the primary. We plan to go even further by allowing users to request to any server, but that server will 
transfer the message to the primary server. In our project, we set 5 servers to replicate the data.

## 3. Use Case

### (1) Start interface

When user runs the `server.java` and `client.java`, there will be a welcome interface. Our project title and a short introduction is shown in a frame. 
Besides, there are two buttons, one is for users to sign up, and the other is for existed user to login.

### (2) Sign up interface

When user clicks the `sign up` botton at the start interface, we will see a sign-up interface. After inputting the username, password, and a port number, 
user will sign up successfully. And the password can be found under the `./credential/($portNum)/($username).txt`. Then user can login to our Projpedia
system automatically when signed up.

### (3) Log in interface

When signed up successfully before, user can click the `log in` button. Then username, password, and port number is needed to log in. If there is something
wrong with any of these information, the system will show a dialog to remind user of specific problem.

### (4) Main page

When logging into the projpedia system, user will see some options:

#### 1. View all entries

After clicking `View all entries` button, all existed entries will be demonstrated on the page. User can click back to return.

#### 2. Create new entry

User can create a new entry and add it into the database by clicking `Create new entry`. In this page, there are two text fields. One is for the name of
the entry, and the other is a bigger field for deifinition or explanation for the entry. If complete, user can click `submit` button to push all 
information into the database. The data can be found under `./data/($portNum)/($entry).txt`. 

Click `back` to return to main page.

#### 3. Edit entry

User can access a page for editting via clicking `Edit entry` button. A dialog will be popped-up, and user needs to input the entry name to be editted.
If the entry exists, user will see the definition and explanation of that entry; Otherwise, an alert dialog will be popped-up. Besides, user can edit in 
the text field, and the .txt file will be updated in the database too. 

Click `submit` to confirm the edit. Click `back` to return.

#### 4. Delete entry

After clicking `Delete entry`, user can delete the existed entries. A dialog will ask to input the name of the entry. If exists, user can choose `OK` to 
access next step. Then an alert will be sent to user to make sure the operation is being considered. If all steps are confirmed, the entry will be deleted
from the system.

#### 5. Log out

Log out from the system after clicking `Log out` button.


## 4. How to run
