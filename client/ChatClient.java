// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    openConnection();
  }

  
  //Instance methods ************************************************

  protected void connectionClosed() {
    if (isConnected()) {
      System.out.println("The client disconnected.");
    }
    else{
      System.out.println("The client program was terminated.");
    }
  }

  protected void connectionException(Exception exception) {
    System.out.println("The server program was terminated.");
    quit();
  }

  protected void connectionEstablished() {
    System.out.println("The connection was successfully established.");
  }


  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
    String[] consoleInput = message.split(" ");
    try
    {
      if(consoleInput[0].contains("#")) {
        switch (consoleInput[0]) {

          case "#quit":
            try {
              quit();
            }catch (Exception e){
              System.out.println(e);
            }
            break;

          case "#logoff":
            try {
              closeConnection();
            }catch (Exception e){
              System.out.println(e);
            }
            break;

          case "#sethost":
            if(!isConnected()){
              try {
                setHost(consoleInput[1]);
                clientUI.display("New client host name was set as " + consoleInput[1]);
              }catch (Exception e){
                System.out.println(e);
              }
            }
            else {
              clientUI.display("Client is still connected, cannot change host name yet.");
            }
            break;

          case "#setport":
            if(!isConnected()){
              try {
                setPort(Integer.parseInt(consoleInput[1]));
                clientUI.display("New client connected via port " + consoleInput[1]);
              }catch (Exception e){
                System.out.println(e);
              }
            }
            else {
              clientUI.display("Client is still connected, cannot change port number yet.");
            }
            break;

          case "#login":
            try {
              openConnection();
            }
            catch (Exception e){
              System.out.println(e);
            }
            break;

          case "#gethost":
            sendToServer("The present host name is " + getHost());

            break;

          case "#getport":
            sendToServer("The present client port is " + getPort());
            break;
        }
      }

      else {
        sendToServer(message);
      }

    }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
}
//End of ChatClient class
