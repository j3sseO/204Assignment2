//  Jesse O'Connor
//  ID: 1534760

/**
 * HttpServerRequest class that is used to parse an HTTP request.
 */
class HttpServerRequest {
    private String file = null;
    private String host = null;
    private boolean done = false;
    private int line = 0;

    public boolean isDone() { return done; }
    public String getFile() { return file; }
    public String getHost() { return host; }

    /*
     * Process the line, setting 'done' when HttpServerSession should
     * examine the contents of the request using getFile and getHost
     */
    public void process(String in) {
        if (in == null || in.compareTo("") == 0) {
            done = true;
            return;
        }

        // Process first line of GET request
        if (line == 0) {
            // Splits request into array
            String parts[] = in.split(" ");

            // Determines request is in right format and then sets fileName
            if (parts.length == 3 && parts[0].compareTo("GET") == 0) {
                file = parts[1].substring(1);

                // If the filename ends with a "/" append index.html to the filename
                if (file.endsWith("/") || file.length() == 0) {
                    file += "index.html";
                }
            }
        }

        // Gets hostName and sets to variable
        if (line == 1 && in.startsWith("Host: ")) {
            host = in.substring(6);
        }
         
        // Increments line count
        line++;
    }
}