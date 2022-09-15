import java.util.ArrayList;

class ReferenceTable {

    protected ArrayList<String> get_arrlist(String string){ 
        String[] temp = string.split("");
        ArrayList<String> arrList = new ArrayList<String>();
        for (int i=0; i<temp.length; i++){
            arrList.add(temp[i]);
        }
        return arrList; 
    }
    
    protected int get_offset_index(String letter, ArrayList<String> refTableArrlist){
        for (int i=0; i<refTableArrlist.size(); i++){
            if (refTableArrlist.get(i).equals(letter)){
                return i;
            }
        }
        return 0;
    }

    protected ArrayList<String> get_offset_table(int count,ArrayList<String> tableArrList){
        ArrayList<String> newArrList = new ArrayList<String>(tableArrList);
        for (int i=0; i<count; i++){
            String getLastChar = newArrList.remove(newArrList.size()-1);
            newArrList.add(0,getLastChar);
        }
        return newArrList;
    }

}

class Encoder extends ReferenceTable {
    private String offset;
    private String refTable;

    public Encoder(String offset,String refTable){
        this.offset = offset;
        this.refTable = refTable;
    }

    private String get_offset(){
        return this.offset;
    }
    
    private void set_offset(String newOffset){
        this.offset = newOffset;
    }

    private String get_refTable(){
        return this.refTable;
    }

    public String encode(String plainText){
        ArrayList<String> refTableArrList = this.get_arrlist(this.refTable);
        int offsetIndex = this.get_offset_index(this.offset,refTableArrList);
        ArrayList<String> offsetTableArrList = this.get_offset_table(offsetIndex,refTableArrList);
        ArrayList<String> textArrList = this.get_arrlist(plainText);
        String encodedMsg = this.offset;

        for (int i=0; i<textArrList.size(); i++){
            int textIndex = this.get_offset_index((textArrList.get(i)).toUpperCase(),refTableArrList);
            if (textIndex == 0){
                encodedMsg = encodedMsg.concat(textArrList.get(i));
            } else {
                encodedMsg = encodedMsg.concat(offsetTableArrList.get(textIndex));
            }
        }
        return encodedMsg;
    }
}

class Decoder extends ReferenceTable {
    private String refTable;

    public Decoder(String refTable){
        this.refTable = refTable;
    }

    private String get_refTable(){
        return this.refTable;
    }

    public String decode(String encodedText){
        ArrayList<String> refTableArrList = this.get_arrlist(this.refTable);
        String offset = encodedText.substring(0,1);
        int offsetIndex = this.get_offset_index(offset,refTableArrList);
        ArrayList<String> offsetTableArrList = this.get_offset_table(offsetIndex,refTableArrList);
        String text = encodedText.substring(1);
        ArrayList<String> textArrList = this.get_arrlist(text);
        String decodedMsg = "";
        for (int i=0; i<textArrList.size(); i++){
            int textIndex = this.get_offset_index((textArrList.get(i)).toUpperCase(),offsetTableArrList);
            if (textIndex == 0){
                decodedMsg = decodedMsg.concat(textArrList.get(i));
            } else {
                decodedMsg = decodedMsg.concat(refTableArrList.get(textIndex));
            }   
        }
        return decodedMsg;
    }
}

class Runner {
    public static void main(String args[]){
        String table = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456879()*+,-./";
        String plainText = "HELLO WORLD";
        
        Encoder encoded = new Encoder("F", table);
        String encodedMsg = encoded.encode(plainText);
        System.out.println("<Encoding Method>\nPlain Text: "+ plainText);
        System.out.println("Encoded Message: "+encodedMsg);

        Decoder decoded = new Decoder(table);
        String decodedMsg = decoded.decode(encodedMsg);
        System.out.println("<Decoding Method>\nEncoded Message: "+ encodedMsg);
        System.out.println("Decoded Message: "+decodedMsg);
    }

}

