package hashcash;

import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class TestHashCash {

    static void p(String s) {
        try {
            HashCashBak hc = new HashCashBak();
            System.out.println(hc.getSecurityLevel(s));
        } catch (Exception e) {
            
        }
    }
    
    static void q(String ts, String cash) {
        YoukuHashCash t = new YoukuHashCash();
        String tmp = ts + cash;
        System.out.println(t.generateAnOptional(tmp));
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws NoSuchAlgorithmException {
        /*
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter the text you want to secure: ");
        String data = scan.nextLine();
        System.out.println("Enter the target securty level: ");
        int targetLevel = scan.nextInt();
        
        long startTime = System.currentTimeMillis();
        long stopTime = System.currentTimeMillis();
        double timeSec = (double) (stopTime - startTime) / 1000;
        System.out.println(hashCash + " Security Level : " + hc.getSecurityLevel(hashCash) + 
                " in " + timeSec + " seconds");
        */
        HashCashBak hc = new HashCashBak();
        
        //System.out.println(hc.getSecurityLevel("test::5ffcgihoARk7xIqu"));
        
       
        //  String hashCash = hc.GenerateHashCash(data, targetLevel);
        
       /* 
        long stopTime = System.currentTimeMillis();
        double timeSec = (double) (stopTime - startTime) / 1000;
        System.out.println(hashCash + " Security Level : " + hc.getSecurityLevel(hashCash) + 
                " in " + timeSec + " seconds");
        */
        /*
        System.out.println(hc.getSecurityLevel("M0sEzMYsxcwJ4AvASIHeQIo:mPw7rIGuLnT0IyNF"));
        System.out.println(hc.getSecurityLevel("2ULeBDIBOr4whw4yATDmcOg:ehxKvlRmiHdtbOf4"));
        System.out.println(hc.getSecurityLevel("yba9iTgHOLk1iA8yAbCKhPg:zZnHbM1BabietAF4"));
        System.out.println(hc.getSecurityLevel("DWQcTDIBIvYxxRMyAXgkVjw:A0ATSTi0x0f5T1TL"));
        
        System.out.println(hc.getSecurityLevel("Bx*25TIBJ1swYhMyAdGOLTY:eACGZFEaFTplO7iS"));
        
        System.out.println(hc.getSecurityLevel("eToWozIBJbMxgBQyAZcuCEg:RSJfiVaKAiGNIqwd"));
        
        System.out.println(hc.getSecurityLevel("UzudTIBJxMxIBYyAcmlCWIo:SQQDTCgBmtQbiSnT"));
        
        System.out.println(hc.getSecurityLevel("UAWTqjYBIVVjFjICkqoyZgo:UPCAdKaEcRlJs2hU"));
        System.out.println(hc.getSecurityLevel("Qm8YFTcGJwM3NRcyAS0vXXM:IcARw6wOwbyJ2fc5"));
        
        System.out.println(hc.getSecurityLevel("S8S5yDcGJ5k3rxcyAfCO9no:5gqQTVGKTolMkXDv"));
        System.out.println(hc.getSecurityLevel("v*yPjTcGKNs37RgyAbW43o4:GiXe4QlxIs5sFrA4"));
        
        p("ne-DzjUBLYu9GjIB9vrYqw:Whc62Eq2zdyqixsk");
        */
        
        q("ne-DzjUBLYu9GjIB9vrYqw", "Whc62Eq2zdyqixsk");
        q("S8S5yDcGJ5k3rxcyAfCO9no", "5gqQTVGKTolMkXDv");
        q("3ZMtOjQB3-oyyu4yAQkdoOw", "lKbpTuSfC5bm7qK9");
        
        // v: 2  => 4
        
    }
}
