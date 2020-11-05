package server;

import common.*;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 */
public class ChatServer extends AbstractServer {
    //Instance variables **********************************************

    final public static int DEFAULT_PORT = 5555;


    /**
     * The interface type variable.  It allows the implementation of
     * the display method in the client.
     */
    ChatIF serverUI;


    //Constructors ****************************************************


    /**
     * Constructs an instance of the chat server.
     *
     * @param port     The server's port number.
     * @param serverUI The interface type variable.
     */
    public ChatServer(int port, ChatIF serverUI) {
        super(port);
        this.serverUI = serverUI;

    }

    //Instance methods ************************************************

    /**
     * This method handles any messages received from the client.
     *
     * @param msg    The message received from the client.
     * @param client The connection from which the message originated.
     */

    public void handleMessageFromClient(Object msg, ConnectionToClient client) {
        System.out.println("Message received: " + msg + " from " + client);
        this.sendToAllClients(msg);
    }

    /**
     * This method overrides the one in the superclass.  Called
     * when the server starts listening for connections.
     */
    protected void serverStarted() {
        System.out.println
                ("Server listening for connections on port " + getPort());
    }

    /**
     * This method overrides the one in the superclass.  Called
     * when the server stops listening for connections.
     */
    protected void serverStopped() {
        System.out.println("Server has stopped listening for connections.");
        sendToAllClients("Server has stopped listening for connections.");

    }

    protected void serverClosed() {

        serverStopped();

        try {
            close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    protected void clientConnected(ConnectionToClient client) {

        System.out.println("Client " + client.getName() + " connected.");
    }

    synchronized protected void clientDisconnected(ConnectionToClient client) {

        sendToAllClients("Client " + client.getName() + " disconnected.");

    }

    protected void listeningException(Throwable exception) {
        System.out.println(exception);

    }

    synchronized protected void clientException(ConnectionToClient client, Throwable exception) {
        clientDisconnected(client);
    }


    /**
     * This method handles all data coming from the UI
     *
     * @param message The message from the UI.
     */
    public void handleMessageFromServerUI(String message) {
        String[] consoleInput = message.split(" ");
        try {
            if (consoleInput[0].contains("#")) {
                switch (consoleInput[0]) {

                    case "#quit":
                        try {
                            quit();
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                        break;

                    case "#stop":
                        try {
                            stopListening();
                            serverStopped();
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                        break;

                    case "#close":
                        try {
                            close();
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                        break;

                    case "#setport":
                        if (!isListening()) {
                            try {
                                setPort(Integer.parseInt(consoleInput[1]));

                                serverUI.display("Server connected via port " + consoleInput[1]);
                            } catch (Exception e) {
                                System.out.println(e);
                            }
                        } else {
                            serverUI.display("Server is still connected, cannot change port number yet.");
                        }
                        break;

                    case "#start":
                        try {
                            listen();
                            serverStarted();
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                        break;

                    case "#getport":
                        serverUI.display("The present host name is " + super.getPort());
                        break;
                }
            } else {
                serverUI.display(message);
                sendToAllClients("Message from server:  " + message);
            }
        } catch (Exception e) {
            serverUI.display
                    ("Could not send message to server.  Terminating server.");
            quit();
        }
    }

    /**
     * This method terminates the server.
     */
    public void quit() {
        try {
            close();
        } catch (IOException e) {
        }
        System.exit(0);
    }

}

//End of ChatClient class

