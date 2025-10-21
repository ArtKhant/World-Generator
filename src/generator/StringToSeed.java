package generator;

public class StringToSeed {
    public StringToSeed(){

    }

    public short convert(String seed){

        short seedToVal = 0;

        for(byte i = 0; i < seed.length(); i++){

            seedToVal += seed.charAt(i);

        }


       return seedToVal;

    }
}
