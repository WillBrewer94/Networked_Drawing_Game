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
