import java.io.*;
import java.util.Scanner;

import server.ChatServer;
import common.*;

    /**
     * This class constructs the UI for a chat client.  It implements the
     * chat interface in order to activate the display() method.
     * Warning: Some of the code here is cloned in ServerConsole
     *
     * @author Fran&ccedil;ois B&eacute;langer
     * @author Dr Timothy C. Lethbridge
     * @author Dr Robert Lagani&egrave;re
     * @version September 2020
     */
    public class ServerConsole implements ChatIF
    {
        //Class variables *************************************************

        /**
         * The default port to connect on.
         */
        final public static int DEFAULT_PORT = 5555;

        //Instance variables **********************************************

        /**
         * The instance of the client that created this ConsoleChat.
         */
        ChatServer server;

        /**
         * Scanner to read from the console
         */
        Scanner fromConsole;


        //Constructors ****************************************************

        /**
         * Constructs an instance of the ClientConsole UI.
         *
         * @param port The port to connect on.
         */
        public ServerConsole(int port)
        {
            try
            {
                server = new ChatServer(port, this);
                server.listen();

            }
            catch(Exception exception)
            {
                System.out.println("Error: Can't setup connection!"
                        + " Terminating client.");
                System.exit(1);
            }
            // Create scanner object to read from console
            fromConsole = new Scanner(System.in);
        }


        //Instance methods ************************************************

        /**
         * This method waits for input from the console.  Once it is
         * received, it sends it to the client's message handler.
         */
        public void accept()
        {
            try
            {

                String message;
                boolean serverConnected = true;

                while (serverConnected)
                {
                    message = fromConsole.nextLine();
                    server.handleMessageFromServerUI(message);
                }
            }
            catch (Exception ex)
            {
                System.out.println
                        ("Unexpected error while reading from console!");
            }
        }

        /**
         * This method overrides the method in the ChatIF interface.  It
         * displays a message onto the screen.
         *
         * @param message The string to be displayed.
         */
        public void display(String message)
        {
            System.out.println("SERVER MSG> " + message);
        }


        //Other methods ************************************************


        //Class methods ***************************************************

        /**
         * This method is responsible for the creation of
         * the server instance (there is no UI in this phase).
         *
         * @param //args[0] The port number to listen on.  Defaults to 5555
         *          if no argument is entered.
         */

        public static void main(String[] args)
        {
            int port = 0; //Port to listen on

            try
            {
                port = Integer.parseInt(args[0]); //Get port from command line
            }
            catch(Throwable t)
            {
                port = DEFAULT_PORT; //Set port to 5555
            }

            ServerConsole chat= new ServerConsole(port);
            chat.accept();  //Wait for console data

        }
    }
//End of ServerConsole class


