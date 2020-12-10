package com.airbus.jeff;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import com.airbus.jeff.Logger.Prefix;

public class Extractor {

    private final URL url = new URL(String.format(Properties.QPE_URL, Properties.TAG));

    public Extractor() throws IOException {
        double[] distances = new double[Utils.coordinateToRow(Properties.MARKER_HORIZONTAL_AMOUNT, Properties.MARKER_VERTICAL_AMOUNT)];

        Scanner scanner = new Scanner(System.in);

        int previousX = Properties.MARKER_HORIZONTAL;
        int previousY = Properties.MARKER_VERTICAL;

        for (int x = Properties.MARKER_HORIZONTAL; x <= Properties.MARKER_HORIZONTAL_AMOUNT; x++) {
            for (int y = Properties.MARKER_VERTICAL; y <= Properties.MARKER_VERTICAL_AMOUNT; y++) {
                // Calculate and print measured coordinates of the tag
                int tagX = x * Properties.BOX_SIZE;
                int tagY = y * Properties.BOX_SIZE;
                int tagZ = Properties.TAG_VERTICAL_OFFSET;

                Point3D relativeTagPosition = new Point3D(tagX, tagY, tagZ);

                tagX += Properties.TABLE_X;
                tagY += Properties.TABLE_Y;
                tagZ += Properties.TABLE_Z;

                Point3D realTagPosition = new Point3D(tagX, tagY, tagZ);

                Logger.println(Prefix.RELATIVE, "\t\t" + relativeTagPosition.toString() + "\n");
                Logger.println(Prefix.TABLE, "\t\t" + realTagPosition.toString());

                // Dialog loop
                while (true) {
                    // Read input
                    String input = scanner.nextLine();

                    if (input.equals("") || input.equalsIgnoreCase("ok")) {
                        // Check whether the file is currently opened or not
                        if (!Properties.EXCEL_FILE.renameTo(Properties.EXCEL_FILE)) {
                            Logger.println(Prefix.ERROR, "\t\t\t\t" + "Please close the file");
                            continue;
                        }

                        // Get the calculated position of the tag from the QPE
                        Point3D qpeTagPosition = getPositionFromQPE();
                        if (qpeTagPosition == null) {
                            Logger.println(Prefix.ERROR, "\t\t\t\t" + "Please try again");
                            continue;
                        }

                        distances[Utils.coordinateToRow(x, y) - 1] = Utils.distance(qpeTagPosition, realTagPosition);

                        Logger.println(Prefix.QPE, "\t" + qpeTagPosition.toString() + " (Difference: " + distances[Utils.coordinateToRow(x, y) - 1] + ")");
                        Logger.println(Prefix.NONE,  "\n" + Properties.SEPARATOR + "\n");

                        // Calculate the row and write the positions to Excel
                        int rowPosition = Properties.ROW_START - 2 + Utils.coordinateToRow(x, y);
                        writeToExcel(rowPosition, realTagPosition, qpeTagPosition);

                        break;
                    } else if (input.contains("show")) {
                        Logger.println(Prefix.RELATIVE, "\t\t" + relativeTagPosition.toString() + "\n");
                        Logger.println(Prefix.TABLE, "\t\t" + realTagPosition.toString());
                    } else if (input.contains("goto")) {
                        String point = input.trim().split("goto")[1].trim();

                        x = Integer.parseInt(point.split(" ")[0]);
                        y = Integer.parseInt(point.split(" ")[1]) - 1;
                        break;
                    } else if (input.contains("back")) {
                        x = previousX;
                        y = previousY - 1;
                        break;
                    } else {
                        Logger.println(Prefix.NONE, "\n" + Properties.SEPARATOR + "\n");
                        break;
                    }
                }

                // Save current x and y
                previousX = x;
                previousY = y;
            }
        }
    }

    private Point3D getPositionFromQPE() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8))){
            String json = reader.readLine();
            String raw = json.split("smoothedPosition\":\\[")[1].split("]")[0];
            String[] position = Utils.convertToCentimeters(raw).split(",");

            int tagX = Integer.parseInt(position[0]);
            int tagY = Integer.parseInt(position[1]);
            int tagZ = Integer.parseInt(position[2]);

            return new Point3D(tagX, tagY, tagZ);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void writeToExcel (int rowIndex, Point3D realPosition, Point3D qpePosition) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(Properties.EXCEL_FILE);
        XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
        fileInputStream.close();
        XSSFSheet sheet = workbook.getSheet(Properties.EXCEL_SHEET);

        // Retrieve row
        Row row = sheet.getRow(rowIndex);
        if (row == null) {
            row = sheet.createRow(rowIndex);
        }

        // Set cell values
        setCellValue(row, Properties.COLUMN_REAL, realPosition.getX());
        setCellValue(row, Properties.COLUMN_REAL + 1, realPosition.getY());

        setCellValue(row, Properties.COLUMN_QPE, qpePosition.getX());
        setCellValue(row, Properties.COLUMN_QPE + 1, qpePosition.getY());

        // Open output stream
        FileOutputStream fileOut = new FileOutputStream(Properties.EXCEL_FILE);

        // Close streams
        workbook.write(fileOut);
        workbook.close();
        fileOut.close();
    }

    private void setCellValue(Row row, int columnIndex, int value) {
        Cell cell = row.getCell(columnIndex);
        if (cell == null) {
            cell = row.createCell(columnIndex);
        }
        cell.setCellValue(value);
    }

    public static void main(String[] args) {
        try {
            new Extractor();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
