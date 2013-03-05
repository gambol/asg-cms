package hashcash;



import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author ryanwilliams
 */
public class HashCashBak {

    private final MessageDigest hashGenerator;

    /**
     * Creates a new HashCash generator/verifier that uses the SHA-256 algorithm
     *
     * @throws NoSuchAlgorithmException
     */
    public HashCashBak() throws NoSuchAlgorithmException {
        this("SHA-256");
    }

    /**
     * Creates a new HashCash generator/verifier that uses the specified
     * algorithm
     *
     * @param algorithm the algorithm to use
     * @throws NoSuchAlgorithmException
     */
    public HashCashBak(String algorithm) throws NoSuchAlgorithmException {

        //Create hash generator
        this.hashGenerator = MessageDigest.getInstance(algorithm);
    }

    /**
     * Generates a hashCash for the Specified level
     *
     * @param data the UID of the object this hashCash is for
     * @param targetLevel the target level for this hashCash
     * @return the generated hashCash
     */
    public String GenerateHashCash(String data, int targetLevel) {
        //Number of attempts
        int hashCount = 0;

        //Current Level
        int currentLevel = 0;
        //Current Hash
        String currentHashCash = "";

        //Chech if curent hashCash is at target
        while (currentLevel < targetLevel) {
            //Add 1 to hash counter
            hashCount++;

            //Create HashCash
            String hashCash = this.GenerateHashCash(data);

            //Test HashCash
            int level = this.getSecurityLevel(hashCash);
            //Check if HashCash is better than best HashCash
            if (level > currentLevel) {
                //If beter save HashCash
                currentHashCash = hashCash;
                currentLevel = level;

                //Debug
                System.out.println(hashCash + " Security Level : " + level);

            }


        }
        return currentHashCash;
    }

    
    
    /**
     * Generates a hashCash
     * @param data the UID of the object this hashCash is for
     * @return the generated hashCash
     */
    public String GenerateHashCash(String data) {
        //Generate Random Key
        String key = StringUtils.randomString(16);

        //Create HashCash
        String hashCash = this.createHashCash(data, key);
        
        return hashCash;
    }

    /**
     * Computes the security level of a hashCash
     *
     * @param hashCash the hashCash to compute the level for
     * @return the security level of the entered hashCash
     */
    public final int getSecurityLevel(String hashCash) {

        this.hashGenerator.update(hashCash.getBytes());

        byte hash[] = this.hashGenerator.digest();

        //Debug
//        for (byte item : hash) {
//            int value = item;
//            if (value < 0) {
//                value += 256;
//            }
//            System.out.print(value + " : ");
//        }
//        System.out.println();

        int level = 0;

        for (int i = 0; i < hash.length; i++) {
            int localLevel = ByteUtils.countLeadingZeros(hash[i]);
            level += localLevel;
            if (localLevel < 8) {
                i = 10000;
            }
        }

        return level;
    }

    /**
     * Creates a HashCash string
     *
     * @param data the data value for this HashCash
     * @param key the key value for this HashCash
     * @return the HashCash string
     */
    protected final String createHashCash(String data, String key) {
        String output = data + "::" + key;
        return output;
    }
}