package hashcash;

/**
*
* @author ryanwilliams
*/
public class ByteUtils {
   
   /**
    * Counts the leading binary zeros in a byte
    * @param valueByte the byte to count the leading binary zeros for
    * @return the number of leading zeros in the entered byte
    */
   public static int countLeadingZeros(byte valueByte) {
       //Convert the byto to a unsigned int
       int value = valueByte;
       if (value < 0) {
           value += 256;
       }
       //count the zeros
       if (value >= 0x80) {
           return 0;
       } else if (value >= 0x40) {
           return 1;
       } else if (value >= 0x20) {
           return 2;
       } else if (value >= 0x10) {
           return 3;
       } else if (value >= 0x08) {
           return 4;
       } else if (value >= 0x04) {
           return 5;
       } else if (value >= 0x02) {
           return 6;
       } else if (value >= 0x01) {
           return 7;
       } else {
           return 8;
       }
   }
}