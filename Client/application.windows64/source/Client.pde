import controlP5.*;
import g4p_controls.*;
import java.net.Socket;
import java.io.*;
import java.awt.event.KeyEvent; 

//variables
private Socket clientSocket;
private ControlP5 cp5;
private Textlabel timerLabel;
private ControlTimer timer;
private BufferedReader in;
private PrintWriter out;
private int portNum;
private String hostIP;
private Screen screen;
private boolean isTimer = false;
private PFont font;

void setup() {
  size(1200,700);
  font = createFont("Comic Sans MS", 75, true);
  cp5 = new ControlP5(this);
  screen = new LoginScreen(cp5);
}

void draw() {
  screen.display();
  if(isTimer) {
    drawTimer();
  }
}

private void switchDisplays(Screen s) {
  screen.destroyDisplay();
  screen = s;
}

void keyPressed() {
  if(keyCode == KeyEvent.VK_F1) {
    switchDisplays(new LoginScreen(cp5));
    isTimer = false;
  }
  
  if(keyCode == KeyEvent.VK_F2) {
    switchDisplays(new DrawScreen());
    createTimer();
  }
  
  if(keyCode == KeyEvent.VK_F3) {
    switchDisplays(new TextScreen(cp5));  
    createTimer();
  }
  
  if(keyCode == KeyEvent.VK_F4) {
    switchDisplays(new IdleScreen());
    isTimer = false;
  }
}

public void controlEvent(ControlEvent theEvent) {
  if(theEvent.getName().equals("submitSocket")) {
    clientSocket = screen.makeSocket();
    try {
      in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
      out = new PrintWriter(clientSocket.getOutputStream(), true);
      System.out.println("Connected To: " + clientSocket.getRemoteSocketAddress());
      waitForUI();
    } catch(IOException e) {
      System.out.println(e.getMessage());
    }
  }
  if(theEvent.getName().equals("submitSentence")) {
    
  }
}

public void waitForUI() {
  String response;
  
  try {
    System.out.println("Wait For Response");
    while(true) {
      response = in.readLine();
      
      if(response != null) {
        break;
      }
    } 
    
    System.out.println("UI Code: " + response);
    if(response.equals("1")) {
      switchDisplays(new TextScreen(cp5));
    } else if(response.equals("2")) {
      switchDisplays(new DrawScreen());
    } 
    
  } catch(IOException e) {
    
  } catch(NullPointerException e) {
    
  } 
}

public void createTimer() {
  timerLabel = new Textlabel(cp5,"--", 30, 20);
  timer = new ControlTimer();
  timer.setSpeedOfTime(1);
  timerLabel.setColor(color(0));
  isTimer = true;
  timer.reset();
}

public void drawTimer() {
  stroke(255);
  fill(255);
  rect(0, 0, 150, 110);
  PFont font = createFont("Comic Sans MS", 75,true);
  textFont(font);
  timerLabel.setFont(font);
  timerLabel.setValue(timer.toString());
  timerLabel.draw(this);
  
  if(timer.time() > 20000) {
    isTimer = false;
  }
}

public void mousePressed() {
  if(screen.getClass().equals(DrawScreen.class)) {
    screen.colorSelect();
  }
}
