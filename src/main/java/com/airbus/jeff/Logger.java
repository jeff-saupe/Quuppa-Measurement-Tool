package com.airbus.jeff;

public class Logger {
    public enum Prefix {
        RELATIVE("Relative position "),
        TABLE("Absolute position "),
        QPE("Calculated position "),
        ERROR("Error "),
        NONE("");

        private final String prefix;

        Prefix(String prefix) {
            this.prefix = prefix;
        }

        @Override
        public String toString() {
            return prefix;
        }
    }

    public static void print (Prefix prefix, String message) {
        System.out.print(prefix.toString() + message);
    }

    public static void println (Prefix prefix, String message) {
        System.out.println(prefix.toString() + message);
    }
}
