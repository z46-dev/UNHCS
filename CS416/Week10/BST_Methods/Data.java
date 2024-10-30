/**
 * Data - the object to be stored in the data structure. It has
 *    String key       -- the key used in the Comparable interface
 *    int    value      -- a stub representing other data
 * @author mdb
 * @version 03/16/14
 *
 * Edited by rdb
 * Last edit:
 *   03/16/14 rdb Make checkstyle-compatible
 */
public class Data {
    //------------ instance variables ----------------------------
    String  key;      // key must be unique
    int     value;

    //------------------ constructor -----------------------------
    /**
     * Construct a Data object from its components.
     *
     * @param k String     key field
     * @param v int        represents data associated with key
     */
    public Data(String k, int v) {
        key   = k;
        value = v;
    }

    //-------------------- getKey() -------------------------
    /**
     * Get the key.
     *
     * @return String    the key field
     */
    public String getKey() {
        return key;
    }

    //-------------------- toString() ------------------------
    /**
     * Return a string representation for the object.
     * @return String    the representation
     */
    public String toString() {
        return  key + ":" + value;
    }

    //------------------- compareTo -----------------------------------
    /**
     * Compare this object to another Data object based on the key field.
     *
     * @param other Data     comparing object
     * @return int           <0, 0, >0 based on ordering of this to that
     */
    int compareTo(Data other) {
        return key.compareTo(other.key);
    }
}