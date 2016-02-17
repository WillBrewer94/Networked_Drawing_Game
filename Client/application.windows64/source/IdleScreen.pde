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
