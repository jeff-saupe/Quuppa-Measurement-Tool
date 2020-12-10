package com.airbus.jeff;

import java.io.File;

public class Properties {
    // Excel
    public static final File EXCEL_FILE         = new File("Messungen.xlsx");
    public static final String EXCEL_SHEET      = "Versuch1";
    public static final int COLUMN_REAL         = 1;
    public static final int COLUMN_QPE          = 3;
    public static final int ROW_START           = 8;

    // Quuppa
    public static final String QPE_URL  = "http://192.168.200.2:8080/qpe/getTagPosition?version=2&tag=%s";
    public static final String TAG      = "2";

    // Table
    public static final int TABLE_X       = 105;
    public static final int TABLE_Y       = 269;
    public static final int TABLE_Z       = 73;
    public static final int TABLE_WIDTH   = 160;
    public static final int TABLE_HEIGHT  = 80;

    public static final int TAG_VERTICAL_OFFSET = 0;

    // Table scale information
    public static final int BOX_SIZE                    = 10;

    public static final int MARKER_HORIZONTAL_AMOUNT    = 15;
    public static final int MARKER_HORIZONTAL           = 1;

    public static final int MARKER_VERTICAL_AMOUNT      = 7;
    public static final int MARKER_VERTICAL             = 1;

    // Miscellaneous
    public static final String SEPARATOR = "-------------------------------------------------------";
}
