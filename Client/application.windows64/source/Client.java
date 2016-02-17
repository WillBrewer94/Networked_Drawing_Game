import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import controlP5.*; 
import g4p_controls.*; 
import java.net.Socket; 
import java.io.*; 
import java.awt.event.KeyEvent; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Client extends PApplet {





 

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

public void setup() {
  size(1200,700);
  font = createFont("Comic Sans MS", 75, true);
  cp5 = new ControlP5(this);
  screen = new LoginScreen(cp5);
}

public void draw() {
  screen.display();
  if(isTimer) {
    drawTimer();
  }
}

private void switchDisplays(Screen s) {
  screen.destroyDisplay();
  screen = s;
}

public void keyPressed() {
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
//ControlTimer class (paste into new tab in processing)

public class ControlTimer {

 long millisOffset;

 int ms, s, m, h, d;

 float _mySpeed = 1;

 int current, previous;

 /**
  * create a new control timer, a timer that counts up in time.
  */
 public ControlTimer() {
  reset();
 }

 /**
  * return a string representation of the current status of the timer.
  *
  * @return String
  */
 public String toString() {
  update();
  return (((s < 10) ? "0" + s : ""
    + s) // + " : " +
  // ((ms<100) ? "0" + ms: "" +ms)
  );
 }

 /**
  * called to update the timer.
  */
 public void update() {
  current = (int) time();
  if (current > previous + 10) {
   ms = (int) (current * _mySpeed);
   s = (int) (((current * _mySpeed) / 1000));
   m = (int) (s / 60);
   h = (int) (m / 60);
   d = (int) (h / 24);
   ms %= 1000;
   s %= 60;
   m %= 60;
   h %= 24;
   previous = current;
  }

 }

 /**
  * get the time in milliseconds since the timer was started.
  *
  * @return long
  */
 public long time() {
  return (System.currentTimeMillis() - millisOffset);
 }

 /**
  * reset the timer.
  */
 public void reset() {
  millisOffset = System.currentTimeMillis();
  current = previous = 0;
  s = 0; // Values from 0 - 59
  m = 0; // Values from 0 - 59
  h = 0; // Values from 0 - 23
  update();
 }

 /**
  * set the speed of time, for slow motion or high speed.
  *
  * @param theSpeed int
  */
 public void setSpeedOfTime(float theSpeed) {
  _mySpeed = theSpeed;
  update();
 }

 /**
  * Get the milliseconds of the timer.
  */
 public int millis() {
  return ms;
 }

 /**
  * Seconds position of the timer.
  */
 public int second() {
  return s;
 }

 /**
  * Minutes position of the timer.
  */
 public int minute() {
  return m;
 }

 /**
  * Hour position of the timer in international format (0-23).
  */
 public int hour() {
  return h;
 }

 /**
  * day position of the timer.
  */
 public int day() {
  return d;
 }
}
public class DrawScreen extends Screen {
  int black;
  int red;
  int green;
  int blue;
  int white;
  int lightGrey;
  int currentColor;
  boolean isPressed = false;
  int frame = 0;
  
  ControlP5 cp5;
  
  PImage mouseCursor;
  PImage background;
  PImage redBlob;
  PImage blackBlob;
  PImage blueBlob;
  PImage greenBlob;
  PImage title;
  PImage avatar; 
  
  public DrawScreen() {
    super();
    createDisplay();
  }
  
  private void createDisplay() {
    smooth();
    mouseCursor = loadImage("data/MouseCursor.png");
    background(255);
    
    //background
    background = loadImage("data/bg.png");
    image(background, 0, 0);
    
    //avatar
    avatar = loadImage("data/drawavatar.png");
    avatar.resize(306, 348);
    image(avatar, 850, 330);
    
    //paint blobs
    blackBlob = loadImage("data/blackBlob.png");
    image(blackBlob,30,140);
    redBlob = loadImage("data/redBlob.png");
    image(redBlob,30,280);
    redBlob = loadImage("data/greenBlob.png");
    image(redBlob,30,420);
    redBlob = loadImage("data/blueBlob.png");
    image(redBlob,30,560);
    title = loadImage("data/title.png");
    image(title,200,15);
  
    //defining colors
    black = color(102);
    red = color(204,51,51);
    green = color(102,204,102);
    blue = color(51,102,204);
    white = color(255);
    lightGrey = color(230);
    currentColor = color(102);
  }
  
  public void display() {
    if (mousePressed) {
      isPressed = true;
      if (mouseX>170 && mouseY>130) {
        stroke(currentColor);
        strokeWeight(16);
        line(mouseX, mouseY, pmouseX, pmouseY);
      }
    }
  }
  
  public void waitForSentence() {
    
  }
  
  public void sendDrawing() {
    
  }
  
  public void colorSelect() {
  //choosing color
  System.out.println("triggered");
    if ((mouseX>30) && (mouseY>140) && (mouseX<150) && (mouseY<264)) {
      currentColor = color(black);
      strokeWeight(16);
    } else if ((mouseX>30) && (mouseY>0) && (mouseX<150) && (mouseY<404)) {
      currentColor = color(red);
      strokeWeight(16);
    } else if ((mouseX>30) && (mouseY>420) && (mouseX<150) && (mouseY<544)) {
      currentColor = color(green);
      strokeWeight(16);
    } else if ((mouseX>30) && (mouseY>560) && (mouseX<150) && (mouseY<684)) {
      currentColor = color(blue);
      strokeWeight(48);
    }
  }
}
public class IdleScreen extends Screen {
  PImage avatar;
  
  public IdleScreen() {
    super();
    background(255, 255, 255);
    avatar = loadImage("idleavatar.png");
    image(avatar, 0, 0);
  }
  
  public void display() {
    
  }
}
public class LoginScreen extends Screen {
  ControlP5 cp5;
  PImage avatar;
  int aWidth = 1915;
  int aHeight = 2180;
  int purple = color(147, 125, 186);
  int grey = color(158, 152, 141); 
  int orange = color(230, 157, 49);
  String textValue = "";
  Textfield ip;
  Textfield port;
  
  public LoginScreen(ControlP5 cp5) {
    super();
    this.cp5 = cp5;
    createDisplay();
  }
  
  private void createDisplay() {
    background(255, 255, 255);
    avatar = loadImage("start.png");
 
    PFont font = createFont("arial rounded", 20);
    
    ip = cp5.addTextfield("ip")
       .setPosition(735, 399)
       .setLabelVisible(false)
       .setSize(400,40)
       .setFocus(true)
       .setFont(font)
       .setColorActive(orange)
       .setColorBackground(grey)
       .setColorForeground(grey)
       .setColor(color(0, 0, 0))
       ;
       
    port = cp5.addTextfield("port")
       .setPosition(735, 463)
       .setLabelVisible(false)
       .setFont(font)
       .setColorBackground(grey)
       .setColorActive(orange)
       .setColorForeground(grey)
       .setSize(400,40)
       .setColor(color(0, 0, 0))
       ;
       
    cp5.addButton("submitSocket")
      .setLabel("submit")
      .setPosition(1085, 527)
      .setSize(50, 40)
      .setColorBackground(orange)
      .setColorActive(purple)
      .setColorForeground(orange)
      ;
      
    text("port", 735, 399);
    text("ip", 735, 463);
    image(avatar, 50, 50);
      
    cp5.hide();
  }
  
  public void destroyDisplay() {
    cp5.remove("ip");
    cp5.remove("port");
    cp5.remove("submitSocket");
  }
  
  public void display() {
    cp5.show();
  }
  
  public Socket makeSocket() {
    Socket s = null;
    try {
      s = new Socket(ip.getText(), parseInt(port.getText()));
      System.out.println(ip.getText());
      System.out.println(port.getText());
    } catch(IOException e) {
      System.out.println(e.getMessage());
    }
    return s;
  }
}
public abstract class Screen {
  private Socket s;
  
  public Screen() {
  
  } 
  
  public void display() {
    //empty abstract method
  }
  
  public void destroyDisplay() {
    
  }
  
  public Socket makeSocket() {
    Socket s = null;
    return s;
  }
  
  public void colorSelect() {};
}
public class TextScreen extends Screen {
  PImage background;
  PImage title;
  PImage avatar;
  
  int purple = color(147, 125, 186);
  int grey = color(158, 152, 141); 
  int orange = color(230, 157, 49);
  
  ControlP5 cp5;
  String textValue = "";
  boolean drawTimer = false;
  
  public TextScreen(ControlP5 cp5) {
    super();
    this.cp5 = cp5;
    createDisplay();
  }
  
  public void createDisplay() {
    background(255);
    PFont font = createFont("Arial Rounded",32,true);
    textFont(font);
    
    //background
    background = loadImage("data/bg.png");
    image(background, 0, 0);
    
    //avatar
    avatar = loadImage("data/writeavatar.png");
    avatar.resize(252, 374); 
    image(avatar, 400, 300);
    
    title = loadImage("data/title.png");
    image(title,200,15);
   
    //PFont p = createFont("Comic Sans MS",14,true); 
    //cp5.setControlFont(new ControlFont(createFont("Comic Sans MS",14,true), 20));
    
    cp5.addTextfield("sentence")
      .setPosition(220, 180)
      .setSize(840, 60)
      .setFont(font)
      .setLabelVisible(false)
      .setColorBackground(color(255,255,255))
      .setColorActive(orange)
      .setFocus(true)
      .setColorForeground(purple)
      .setColorLabel(grey)
      .setColor(orange);
    
    // is this necessary? can we just take the value when the time is up?
    cp5.addBang("submitSentence")
      .setPosition(220, 280)
      .setLabel("submit")
      .setColorBackground(orange)
      .setColorActive(purple)
      .setColorForeground(orange)
      .setSize(80, 40)
      .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);    
   
    textFont(font);
    cp5.hide();
  }
  
  public void display() {
    cp5.show();
    drawTimer();
  }
  
  public void turnOver() {
    
  }
  
  public void destroyDisplay() {
    cp5.remove("sentence");
    cp5.remove("submitSentence");
  }
  
  public void sendSentence() {
    
  }
  
  public void waitForDrawing() {
    
  }
  
  public void sendWinner() {
    
  }
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Client" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
