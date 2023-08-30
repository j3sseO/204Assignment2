//  Jesse O'Connor
//  ID: 1534760


import java.net.*;
import java.io.*;

/**
 * HttpServerSession that extends the Thread class.
 * Acts as a client that communicates with our HttpServer.
 */
class HttpServerSession extends Thread {

    private Socket s;
    
    /**
     * Class constructor
     * @param s The socket that was accepted
     */
    public HttpServerSession(Socket s) {
        this.s = s;
    }

    /**
     * Thread run method
     */
    public void run() {
        try {
            // Creates BufferedReader that is connect to the socket's InputStream
            BufferedReader read = new BufferedReader(new InputStreamReader(s.getInputStream()));
            BufferedOutputStream bos = new BufferedOutputStream(s.getOutputStream());
            
            // Creates new instance of HttpServerRequest
            HttpServerRequest request = new HttpServerRequest();
            
            // Loop that processes HTTP request
            String line;
            while (!request.isDone()) {
                line = read.readLine();
                request.process(line);
                //sleep(1000);
            }

            // Prints OK message allowing requested file to be returned
            println(bos, "HTTP/1.1 200 OK");
            println(bos, "");
            returnFile(request, bos);

            bos.close();
            read.close();

        } catch (Exception e) {
            System.err.println(e);
        } finally {
            try {
                // Closes socket
                s.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public void returnFile(HttpServerRequest request, BufferedOutputStream bos) {
        
        // Declares byte array of fixed size that will be used to store file in bytes
        byte[] bytes = new byte[1024];
        
        // Concatenates hostName and fileName to get the path of the file
        String path = request.getHost() + "/" + request.getFile();

        // Creates File object
        File file = new File(path);

        try {
            // Creates FileInputStream and reads from the file
            FileInputStream input = new FileInputStream(file);
            int numOfBytes = input.read(bytes);

            // While the loop is not at the end of the file, send the byte array to the client using BufferedOutputStream::write method
            while (numOfBytes != -1) {
                bos.write(bytes, 0, numOfBytes);
                numOfBytes = input.read(bytes);
                //sleep(1000);
            }

            bos.flush();
            input.close();
        } catch (Exception e) {
            
            // Returns a well-formed 404 response when the client requests a resource that the server doesn't have
            println(bos, "HTTP/1.1 404 File Not Found");
            println(bos, "");
            println(bos, request.getFile() + " not found");
        }
    }

    private boolean println(BufferedOutputStream bos, String s) {
        String news = s + "\r\n";
        byte[] array = news.getBytes();
        try {
            bos.write(array, 0, array.length);
        } catch(IOException e) {
            return false;
        }
        return true;
    }
}
