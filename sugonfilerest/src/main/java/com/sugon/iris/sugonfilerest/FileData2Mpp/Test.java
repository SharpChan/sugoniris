package com.sugon.iris.sugonfilerest.FileData2Mpp;

public class Test {

    public static void main(String[] args) {
        StringBuffer bf = new StringBuffer("1234567");

        bf.deleteCharAt(bf.length()-1);

        System.out.println(bf.toString());
    }
}
