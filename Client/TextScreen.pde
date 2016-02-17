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
