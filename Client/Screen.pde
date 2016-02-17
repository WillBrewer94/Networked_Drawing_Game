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
