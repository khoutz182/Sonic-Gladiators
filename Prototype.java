import java.security.*;
import java.io.UnsupportedEncodingException;

public class HelloWorld{

     public static void main(String []args){
        
        
        String yourString = "hello my name is philip this is the sample data file " +
        "for the recording";
        
        
        byte[] bytesOfMessage1 = null;
        byte[] bytesOfMessage2 = null;
        
        try{
        bytesOfMessage1 = yourString.substring(0,yourString.length()/2).getBytes("UTF-8");
        bytesOfMessage2 = yourString.substring(yourString.length()/2,yourString.length()-1).getBytes("UTF-8");
        } catch(UnsupportedEncodingException e){
            System.out.println("error encoding");
        }
        
        MessageDigest md = null;
        try{
        md = MessageDigest.getInstance("MD5");
        } catch(NoSuchAlgorithmException e){
            System.out.println("error algorithm");
        }
        byte[] thedigest1 = md.digest(bytesOfMessage1);
        byte[] thedigest2 = md.digest(bytesOfMessage2);
        
        byte[] finalByteArray = concat(thedigest1, thedigest2);
        
        short strengthPrev = Math.abs((short)finalByteArray[0]);
        //long strength = strengthPrev & 0xffffffffl;
        short vitalityPrev = Math.abs((short)finalByteArray[1]);
        //long vitality = vitalityPrev & 0xffffffffl;
        short dexterityPrev = Math.abs((short)finalByteArray[2]);
        //long dexterity = dexterityPrev & 0xffffffffl;
        
        System.out.println("strength: " + strengthPrev);
        System.out.println("vitality: " + vitalityPrev);
        System.out.println("dexterity: " + dexterityPrev);
        
        //This creates a 128bit/32 char thinger. We can use that to generate our stats
        //do 2 md5 hashes and combine them for 64 bits
        //256 is 8
        //first 8 bits are strength
        //second 8 bits are vitality
        //third 8 bits are dexterity
        //next 4 bits are class
        //next next 8 bits are prefix
        //next 8 bits are weapon
        //next 8 bits are suffix --12 remaining
        //last 12 bits could generate image

        
        
        
     }
     public static byte[] concat(byte[] a, byte[] b) {
       int aLen = a.length;
       int bLen = b.length;
       byte[] c= new byte[aLen+bLen];
       System.arraycopy(a, 0, c, 0, aLen);
       System.arraycopy(b, 0, c, aLen, bLen);
       return c;
     }

}
