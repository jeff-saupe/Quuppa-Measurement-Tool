package com.airbus.jeff;

public class Utils {

    public static double distance (Point3D p1, Point3D p2) {
        return Math.sqrt(Math.pow(p2.getX() - p1.getX(), 2.0) + Math.pow(p2.getY() - p1.getY(), 2.0));
    }

    public static int coordinateToRow(int x, int y) {
        int counter = y;

        if (x > 1) {
            for (int i = 0; i < x - 1; i++) {
                counter += Properties.MARKER_VERTICAL_AMOUNT;
            }
        }

        return counter;
    }

    public static String convertToCentimeters(String position) {
        String[] values = position.split(",");

        for (int i = 0; i < values.length; i++) {
            values[i] = String.valueOf((int) (Double.parseDouble(values[i]) * 100.0));
        }

        return values[0] + "," + values[1] + "," + values[2];
    }
}