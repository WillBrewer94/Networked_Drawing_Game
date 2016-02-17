import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by Will on 11/10/2015.
 * This class contains the logic to run the game, send and receive responses, and accept user connections
 */
public class GameServer {
    private int portNum;
    private String hostIP;
    private ServerSocket serverSocket;
    private ClientConnection[] clients;

    public GameServer() {
        try {
            //initializes instance variables and socket connection
            clients = new ClientConnection[4];
            serverSocket = new ServerSocket(0);
            portNum = serverSocket.getLocalPort();
            hostIP = InetAddress.getLocalHost().getHostAddress();

        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void waitForConnections() {
        //Sets up client sockets and waits for connections to server,
        //then saves each connection in the order they came in
        try {
            for(int i = 0; i < clients.length; i++){
                System.out.println("Waiting for Player " + (i + 1) + " to connect...");
                clients[i] = new ClientConnection(serverSocket.accept());
                System.out.println("Player " + (i + 1) + " connected: " + clients[i].getIP() + "\n");
            }
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateClientUI() {
        //Sends a UI code to the clients so they can update their UI for their specific role
        System.out.println("Updating player 1 to write screen");

        clients[0].getWriter().println("1");
        clients[1].getWriter().print("2");
        clients[2].getWriter().print("2");
        clients[3].getWriter().print("3");
    }

    public String waitForSentence() {
        //sets up the server to wait for a sentence to be sent
        String response;

        try {
            while(true) {
                response = clients[0].getReader().readLine();
                if(response != null) {
                    return response;
                }
            }
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public void sendSentence(String s) {
        //redirects the sentence to the two drawers, player 2 and 3
        clients[1].getWriter().println(s);
        clients[2].getWriter().println(s);
    }

    public BufferedImage[] waitForDrawings() {
        //sets up the server to wait for both drawers to send their drawings
        //uses multithreading to allow for multiple simultaneous messages, otherwise two
        //messages wouldn't have been able to be received
        //I'm pretty proud of this one :D
        BufferedImage[] images = new BufferedImage[2];
        CyclicBarrier gate = new CyclicBarrier(3);
        ClientThread player2 = new ClientThread(clients[1], gate);
        ClientThread player3 = new ClientThread(clients[2], gate);

        player2.start();
        player3.start();

        try {
            gate.await();
        } catch(BrokenBarrierException e) {
            System.out.println(e.getMessage());
        } catch(InterruptedException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Threads Started!");

        images[0] = player2.getDrawing();
        images[1] = player3.getDrawing();

        return images;
    }

    public void sendDrawings(BufferedImage[] images) {
        //sends both drawings to the sentence writer, so they can vote for their favorite
        try {
            ImageIO.write(images[0], "PNG", clients[0].getSocket().getOutputStream());
            ImageIO.write(images[1], "PNG", clients[0].getSocket().getOutputStream());
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public int waitForWinner() {
        //sets up the server to wait for a response from the grader
        int response = 0;

        try {
            while((response = clients[0].getReader().read()) != 0) {
                return response;
            }
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }

        return 0;
    }

    public void shiftDown() {
        //shifts every client down to simulate a turn order
        ClientConnection[] temp = new ClientConnection[clients.length];
        for(int i = 0; i < clients.length; i++) {
            try {
                temp[i] = clients[i + 1];
            } catch(IndexOutOfBoundsException e) {
                temp[0] = clients[clients.length - 1];
            }
        }
        clients = temp;
    }

    public void score() {
        //Sets the server up to receive a score from the two drawers, letting them know
        //who won
        int response = 0;

        try {
            while((response = clients[0].getReader().read()) != 0) {
                if(response == 1) {
                    clients[1].incScore();
                } else if(response == 2) {
                    clients[2].incScore();
                }
            }
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void run() {
        //contains main game logic and game loop
        int loop = 0;
        Scanner s = new Scanner(System.in);

        System.out.println("Host IP: " + hostIP);
        System.out.println("Server Port: " + portNum + "\n");

        waitForConnections();

        //main game loop
        while (loop < 4) {
            //send signal to all clients to update client UI
            try {
                Thread.sleep(5000);
            } catch(InterruptedException e) {
            }

            updateClientUI();

            //wait to receive sentence from player 1
            //send sentence to player 2 and 3
            System.out.println("Waiting for Sentence");
            sendSentence(waitForSentence());

            //wait for player 2 and 3 to send drawings
            //send drawings to player 1
            sendDrawings(waitForDrawings());

            //wait for player 1 to send winning player
            //notify all players who won so clients can update score
            score();

            //shift all players down in preparation for next turn
            shiftDown();

            //iterates loop to advance to next turn
            loop++;
        }

        //once game is over, send pictures to all clients for timeline
    }

    public static void main(String[] args) {
        GameServer s = new GameServer();
        s.run();
    }
}
