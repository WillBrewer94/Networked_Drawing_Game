import java.io.*;
import java.net.Socket;

/**
 * Created by Will on 11/30/2015.
 * Class that represents a single client connection
 * Hold an input reader, output writer, socket connection, and score variable
 */
public class ClientConnection {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private BufferedReader bin;
    private PrintWriter bout;
    private int score = 0;

    public ClientConnection(Socket socket) {
        this.socket = socket;

        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            bin = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bout = new PrintWriter(socket.getOutputStream(), true);
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public DataOutputStream getOut() {
        return out;
    }

    public DataInputStream getIn() {
        return in;
    }

    public BufferedReader getReader() {
        return bin;
    }

    public PrintWriter getWriter() {
        return bout;
    }

    public int getScore() {
        return score;
    }

    public void incScore() {
        score++;
    }

    public String getIP() {
        return socket.getInetAddress().getHostAddress();
    }
}
