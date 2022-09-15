<?php

class ReferenceTable {

    protected function get_table_array($table){
        $refTableArray = str_split($table);
        return $refTableArray; 
    }
    
    protected function get_offset_index($char,$refTableArray){
        foreach ($refTableArray as $i => $value){
            if ($value === $char){
                return $i;
            }
        }
        return 0;
    }

    protected function get_offset_table($count,$tableArray){
        for ($i=0; $x < $count; $x++){
            $getLastChar = array_pop($tableArray);
            array_unshift($tableArray,$getLastChar);
        }
        return $tableArray;
    }
}

class Encoder extends ReferenceTable {
    private $offset;
    private $refTable;

    function __construct($offset,$refTable){
        $this->offset = $offset;
        $this->refTable = $refTable;
    }

    private function get_offset(){
        return $this->offset;
    }
    
    private function set_offset($offset){
        $this->offset = $offset;
    }

    private function get_refTable(){
        return $this->refTable;
    }

    public function encode($plainText){
        $refTableArray = $this->get_table_array($this->refTable);
        $offsetIndex = $this->get_offset_index($this->offset,$refTableArray);
        $offsetTableArray = $this->get_offset_table($offsetIndex,$refTableArray);
        $textArray = str_split($plainText);
        $encodedMsg = "$this->offset";

        foreach ($textArray as $value){
            $textIndex = $this->get_offset_index(strtoupper($value),$refTableArray);
            if ($textIndex === 0){
                $encodedMsg .= $value;
            } else {
                $encodedMsg .= $offsetTableArray[$textIndex];
            }
        }   
        return $encodedMsg;
    }
}

class Decoder extends ReferenceTable {
    private $refTable;

    function __construct($refTable){
        $this->refTable = $refTable;
    }

    private function get_refTable(){
        return $this->refTable;
    }

    public function decode($encodedText){
        $refTableArray = $this->get_table_array($this->refTable);
        $offset = substr($encodedText,0,1);
        $offsetIndex = $this->get_offset_index($offset,$refTableArray);
        $offsetTableArray = $this->get_offset_table($offsetIndex,$refTableArray);
        $text = substr($encodedText,1);
        $textArray = str_split($text);

        foreach ($textArray as $value){
            $textIndex = $this->get_offset_index(strtoupper($value),$offsetTableArray);
            if ($textIndex === 0){
                $decodedMsg .= $value;
            } else {
                $decodedMsg .= $refTableArray[$textIndex];
            }   
        }
        return $decodedMsg;
    }
}

$table = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456879()*+,-./";
$plainText = "HELLO WORLD";
$encoder = new Encoder("F",$table);
$encoded = $encoder->encode($plainText);
print_r("<Encoding Method>\nPlain Text: $plainText\nEncoded Message: $encoded\n");

$decoder = new Decoder($table);
$decoded = $decoder->decode($encoded);
print_r("<Decoding Method>\nEncoded Message: $encoded\nDecoded Message: $decoded\n");

?>