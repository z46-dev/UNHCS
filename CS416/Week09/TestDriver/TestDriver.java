import java.util.Scanner;

/**
* Interactive Test Driver.
* @author cs416
* @version 1
*/
public class TestDriver {

    /**
    * Main method.
    * @param args Command line arguments.
    */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        LinkedList<String> l = new LinkedList<String>();

        String input = sc.nextLine();
        while (!input.equals("x")) {
            String[] tokens = input.split(" ");
            if (tokens[0].equals("a")) { // add(T value)
                String value = tokens[1];

                l.add(value);
                System.out.println(l);
            } else if (tokens[0].equals("A")) { // add(int index, T value)
                int index = Integer.parseInt(tokens[1]);
                String value = tokens[2];

                l.add(index, value);
                System.out.println(l);
            } else if (tokens[0].equals("c")) { // contains(Object o)
                String value = tokens[1];

                System.out.println(l.contains(value));
            } else if (tokens[0].equals("C")) { // clear()
                l.clear();
                System.out.println(l);
            } else if (tokens[0].equals("r")) { // remove(Object o)
                String value = tokens[1];

                l.remove(value);
                System.out.println(l);
            } else if (tokens[0].equals("R")) { // remove(int index)
                int index = Integer.parseInt(tokens[1]);

                l.remove(index);
                System.out.println(l);
            } else if (tokens[0].equals("g")) { // get(int index)
                int index = Integer.parseInt(tokens[1]);

                System.out.println(l.get(index));
            } else if (tokens[0].equals("e")) { // isEmpty()
                System.out.println(l.isEmpty());
            } else if (tokens[0].equals("s")) { // size()
                System.out.println(l.size());
            }

            System.out.println("------------------");
            input = sc.nextLine();
        }

        sc.close();
    }

}