/**
* Interactive Test Driver for Map-3 Assignment.
* @author cs416
* @version 1
*/
public class TestDriver {

    /**
     * Main method.
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        LinkedMap<Integer, String> lm = new LinkedMap<Integer, String>();

        lm.put(1, "words");
        lm.put(2, "and");
        lm.put(3, "more");
        lm.put(4, "stuff");

        lm.remove(2);
        lm.remove(1);
        lm.remove(4);

        System.out.println(lm.containsKey(3));
    }

    // public static void main(String[] args) {
    //     Scanner sc = new Scanner(System.in);

    //     LinkedMap<String, Integer> l = new LinkedMap<String, Integer>();

    //     String input = sc.nextLine();
    //     while (!input.equals("x")) {
    //         String[] tokens = input.split(" ");
    //         if (tokens[0].equals("p")) { // put(K key, V value)
    //             String key = tokens[1];
    //             Integer value = Integer.parseInt(tokens[2]);
    //             l.put(key, value);
    //             System.out.println(l);
    //         } else if (tokens[0].equals("P")) { // putifAbsent(K key, V value)
    //             String key = tokens[1];
    //             Integer value = Integer.parseInt(tokens[2]);
    //             l.putIfAbsent(key, value);
    //             System.out.println(l);
    //         } else if (tokens[0].equals("c")) { // contains(Object o)
    //             String key = tokens[1];

    //             System.out.println(l.containsKey(key));
    //         } else if (tokens[0].equals("C")) { // clear()
    //             l.clear();
    //             System.out.println(l);
    //         } else if (tokens[0].equals("r")) { // remove(Object o)
    //             String key = tokens[1];

    //             l.remove(key);
    //             System.out.println(l);
    //         } else if (tokens[0].equals("e")) { // isEmpty()
    //             System.out.println(l.isEmpty());
    //         } else if (tokens[0].equals("s")) { // size()
    //             System.out.println(l.size());
    //         } else if (tokens[0].equals("g")) { // get(int index)
    //             String key = tokens[1];

    //             System.out.println(l.get(key));
    //         }

    //         System.out.println("------------------");
    //         input = sc.nextLine();
    //     }

    //     sc.close();
    // }
}