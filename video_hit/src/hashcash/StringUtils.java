package hashcash;

import java.util.Random;

/**
 *
 * @author ryanwilliams
 */
public class StringUtils {

    /**
     * The charset to pull random symbols from
     */
    private static final char[] randomSymbols = new char[62];

    /**
     * Generates the randomSymbols charset
     */
    static {
        //Add numbers
        for (int idx = 0; idx < 10; ++idx) {
            randomSymbols[idx] = (char) ('0' + idx);
        }
        //Add lowercase leters
        for (int idx = 10; idx < 36; ++idx) {
            randomSymbols[idx] = (char) ('a' + idx - 10);
        }
        //Add upercase leters
        for (int idx = 36; idx < 62; ++idx) {
            randomSymbols[idx] = (char) ('A' + idx - 36);
        }
    }
    
    /**
     * The random number generator used for methods in the StringUtils class
     */
    private static Random rng = new Random();

    /**
     * Converts a byte array to a hex string
     * @param input the byte array to convert
     * @return the hex string
     */
    public static String toHex(byte input[]) {
        StringBuilder strBuf = new StringBuilder(input.length * 2);

        for (byte item : input) {
            String itemHex = Integer.toHexString(0xff & item);
            if (itemHex.length() == 1) {
                //append 0 to output if hex string is smaller than 2 chars.
                strBuf.append('0');
            }
            strBuf.append(itemHex);
        }

        return strBuf.toString();
    }

    /**
     * Creates a random string of the specified length
     * @param length the length of the string to generate
     * @return the generated string
     */
    public static String randomString(int length) {
        char strBuf[] = new char[length];
        
        for (int i = 0;i < strBuf.length; i++) {
            strBuf[i] = randomSymbols[rng.nextInt(randomSymbols.length)];
        }
        
        return new String(strBuf);
    }
}