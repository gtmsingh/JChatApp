# JChatApp
It is a chatting application which do not require internet connectivity between the persons chatting, though they need to be in the same network..! Yaa almost the same thing. It is built using JADE framework in Java which is used to implement Multi-agent technologies. PS. The documentation of the codes will be updated soon

Note:
* Agent and Node are used interchangeably throughout but points to the same entity

## How to run
### Netbeans
#### Configuration
* In the project's section (left panel) right click on `libraries` and select `Add jar/folder`
* Select `dist/lib/jade.jar` and `dist/lib/common-codec-1.3.jar` which are the libraries required to run the project
* Right click on the project and select `properties`. Go to `Run` option
* In the `Main Class` section write `jade.Boot` and in the `Arguments` section write `-gui -host 127.0.0.1`.

#### Running
* `Run` the application by clicking on the green play button or select it from `Run`>`Run Project`

#### Creating nodes
* You will see a window popup, expand the options until you see `Main-Container`. Right click on `Main-Container` and select `Start new Agent`
* Enter `Agent Name` as `master`, `Class Name` as `localChat.code` and click `OK`
* In the login box type `Username` as `gtm` and `Password` as `gtm` and click `Submit`. The `M@$tEr` is the main control panel node for the chat application (Usage described later)
* Now when you create other `Agents` in the way described above (with an exception that name should not be `master`) those `Agents` acts as the nodes which can talk among themselves.



## Usage
#### `M@$tEr` Node:
* Whenever any new agent is added this node gets a message from that node asking to invoke it.
* This node needs to write the name of the node (that asked for invokation) in the lowermost text input and click `Invoke`. You will find that msg input box of that node now becomes active and the name of that node in the `Hosts` list
* `Warning` button is a way in which we can inform any node (to cater the functionality of warning the user before `Disposing` them).
* `Dispose` terminates the node whose name is mentioned in the input box
* Closing this agent's window will terminate the whole application

#### Terminal Nodes:
* The right panel shows the list of nodes available for chat
* The left topmost panel is the message log area. The message starting with any tab is the message sent and the messages starting with a tab character represents any message received.
* Below the above panel is the message input box and below that is the input box for the name of the receiver.
* `Send` button as it says sends the message.



### TODO:
* Make it work on different machines seamlessly
* Various checks on Agent's properties of the nodes and simplify creation of node by click of a button
* Visibility improvement when different pair of people are involved in conversation
* Improvement in ease of use of the window buttons
