public class DrawScreen extends Screen {
  int black;
  int red;
  int green;
  int blue;
  int white;
  int lightGrey;
  color currentColor;
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
