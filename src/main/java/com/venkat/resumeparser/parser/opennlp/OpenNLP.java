package com.venkat.resumeparser.parser.opennlp;

import static java.lang.System.out;

public class OpenNLP {

    public static void main(String[] args) {
        patternMatch();
    }

    private static void patternMatch(){
        out.println("Phone number: 1234567890 Valid: " + isPhoneNumber("1234567890"));
        out.println("Phone number: 123 456 7890 Valid: " + isPhoneNumber("123 456 7890"));
        out.println("Phone number: 123-456-7890 Valid: " + isPhoneNumber("123-456-7890"));
        out.println("Phone number: 123-456-7890 x1234 Valid: " + isPhoneNumber("123-456-7890 x1234"));
        out.println("Phone number: (123)456-7890 Valid: " + isPhoneNumber("(123) 456-7890"));
        out.println();
    }

    private static boolean isPhoneNumber(String phoneNumber) {
        if (phoneNumber.matches("[0-9]{10}")) {
            return true;
        } else if (phoneNumber.matches("\\d{3}\\s\\d{3}\\s\\d{4}")) {
            return true;
        } else if (phoneNumber.matches("\\d{3}[-]\\d{3}[-]\\d{4}")) {
            return true;
        } else if (phoneNumber.matches("\\(\\d{3}\\)\\s\\d{3}-\\d{4}")) {
            return true;
        } else {
            return false;
        }
    }
}
