package main.util;

import java.util.ArrayList;
import java.util.List;

public class Printer {

    public static void printTable(List<?> objects) {
        if (objects.isEmpty()) {
            System.out.println("No data to print.");
            return;
        }

        int numColumns = objects.get(0).getClass().getDeclaredFields().length;
        List<Integer> columnWidths = getColumnWidths(objects, numColumns);

        printHorizontalLine(columnWidths);
        printHeader(objects.get(0).getClass(), columnWidths);
        printHorizontalLine(columnWidths);

        for (Object obj : objects) {
            printRow(obj, columnWidths);
        }

        printHorizontalLine(columnWidths);
    }

    private static List<Integer> getColumnWidths(List<?> objects, int numColumns) {
        List<Integer> columnWidths = new ArrayList<>();
        for (int i = 0; i < numColumns; i++) {
            columnWidths.add(0);
        }

        for (Object obj : objects) {
            for (int i = 0; i < numColumns; i++) {
                int fieldWidth = getFieldWidth(obj, i);
                columnWidths.set(i, Math.max(columnWidths.get(i), fieldWidth));
            }
        }

        return columnWidths;
    }

    private static int getFieldWidth(Object obj, int fieldIndex) {
        String fieldValue = "";
        try {
            fieldValue = obj.getClass().getDeclaredFields()[fieldIndex].get(obj).toString();
        } catch (IllegalAccessException | IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        return fieldValue.length();
    }

    private static void printHorizontalLine(List<Integer> columnWidths) {
        for (int width : columnWidths) {
            System.out.print("+");
            for (int i = 0; i < width + 2; i++) {
                System.out.print("-");
            }
        }
        System.out.println("+");
    }

    private static void printHeader(Class<?> cls, List<Integer> columnWidths) {
        String[] fieldNames = getFieldNames(cls);
        printRow(fieldNames, columnWidths);
    }

    private static String[] getFieldNames(Class<?> cls) {
        String[] fieldNames = new String[cls.getDeclaredFields().length];
        for (int i = 0; i < fieldNames.length; i++) {
            fieldNames[i] = cls.getDeclaredFields()[i].getName();
        }
        return fieldNames;
    }

    private static void printRow(Object obj, List<Integer> columnWidths) {
        String[] fieldValues = getFieldValues(obj);
        printRow(fieldValues, columnWidths);
    }

    private static String[] getFieldValues(Object obj) {
        String[] fieldValues = new String[obj.getClass().getDeclaredFields().length];
        for (int i = 0; i < fieldValues.length; i++) {
            try {
                fieldValues[i] = obj.getClass().getDeclaredFields()[i].get(obj).toString();
            } catch (IllegalAccessException | IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
        return fieldValues;
    }

    private static void printRow(String[] values, List<Integer> columnWidths) {
        for (int i = 0; i < values.length; i++) {
            String value = values[i];
            int width = columnWidths.get(i);
            System.out.print("| " + value);
            for (int j = 0; j < width - value.length() + 1; j++) {
                System.out.print(" ");
            }
        }
        System.out.println("|");
    }
}
