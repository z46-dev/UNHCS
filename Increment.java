// i also havent used java in like 5 months so take it easy

import java.util.Scanner
  
public class Increment {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter a number");
        int num = input.nextInt();
        System.out.println("incremented num = " + increment(num));
      
    }
    public static int increment(int n){
          return n -= -1;
        }
}
