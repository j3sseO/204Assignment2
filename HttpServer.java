//  Jesse O'Connor
//  ID: 1534760

import java.net.*;

/**
 * HttpServer class that acts as a server, constantly
 * accepting incoming connections.
 */
class HttpServer
{
    /**
     * Main method for HttpServer class
     */
    public static void main(String args[])
    {
        System.out.println("web server starting");
        try {
            
            // Creates server socket on port 51234
            ServerSocket server = new ServerSocket(51234);
            
            // Infinite loop that accepts incoming connections
            while (true) {
                Socket s = server.accept();
                HttpServerSession sesh = new HttpServerSession(s);
                
                // Gets info about connection and prints ip address
                InetAddress ia = s.getInetAddress();
                String ip = ia.getHostAddress();
                System.out.println("Connection recieved from " + ip);
                
                // Starts the thread 
                sesh.start();
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}