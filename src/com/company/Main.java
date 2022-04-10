package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

// https://metanit.com/java/tutorial/3.8.php


public class Main {

    public static void main(String[] args) {
        int x = 7;
        while (x < 100) {
            if (x % 7 == 0){
                System.out.print(x);
                System.out.print(" ");
            }
            x++;
        }
        System.out.println();
        for(int i=7; i < 100; i=i+7){
            System.out.print(i);
            System.out.print(" ");
        }


    }

    public static void runDate() {
        Date now = new Date();
        Date before = new Date(now.getTime() - 10);
        Date after = new Date(now.getTime() + 10);
        System.out.println("Current date: " + now);
        System.out.println("Current time: " + now.getTime());
        System.out.println("Date before: " + before.before(now));
        System.out.println("Date after: " + after.after(now));
        System.out.println("Date equals: " + after.equals(now));
        // https://help.gooddata.com/cloudconnect/manual/date-and-time-format.html
        SimpleDateFormat dt = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
        System.out.println("Current date: " + dt.format(now));
    }

    public static void runDate8() {
        LocalDate now = LocalDate.now();
        LocalDate before = LocalDate.of(2000, 1, 1);
        LocalDate after = LocalDate.of(2100, Month.JANUARY, 1);
        System.out.println("Current date: " + now);
        System.out.println("Date before: " + now.isBefore(after));
        System.out.println("Date after: " + now.isAfter(before));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String formatDate = now.format(formatter);
        System.out.println("Formatted date: " + formatDate);
    }

    public static void runDateTime8() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime before = LocalDateTime.of(2000, Month.JANUARY, 1, 0, 0);
        LocalDateTime after = LocalDateTime.of(2100, Month.JANUARY, 1, 0, 0);
        System.out.println("Current time: " + now);
        System.out.println("Date before: " + now.isBefore(after));
        System.out.println("Date after: " + now.isAfter(before));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        String formatDateTime = now.format(formatter);
        System.out.println("Formatted date: " + formatDateTime);
    }

    public static void runFunctionalInterface() {
        String oneLine = processFile(BufferedReader::readLine);
        System.out.println(oneLine);
        String handledLine = processFile(br -> "First line is " + br.readLine());
        System.out.println(handledLine);
    }

    public static String processFile(BufferedReaderProcessor processor) {
        try (BufferedReader reader = new BufferedReader(new FileReader("data.txt"))) {
            return processor.process(reader);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return "";
    }

    public static void runPredicate() {
        TestInterfaces<Integer> testInteger = new TestInterfaces<>();
        List<Integer> integers = List.of(1, 2, 3, 4, 5);
        List<Integer> filteredIntegers = testInteger.filter(integers, new IntPredicate());
        System.out.println(integers);
        System.out.println(filteredIntegers);
        TestInterfaces<String> testString = new TestInterfaces<>();
        List<String> strings = List.of("One", "Two", "Three");
        List<String> filteredStrings = testString.filter(strings, (s) -> s.startsWith("T"));
        System.out.println(strings);
        System.out.println(filteredStrings);
    }

    public static void runConsumer() {
        TestInterfaces<Integer> testInteger = new TestInterfaces<>();
        List<Integer> integers = List.of(1, 2, 3, 4, 5);
        testInteger.forEach(integers, (i) -> System.out.println(i));
        TestInterfaces<String> testString = new TestInterfaces<>();
        List<String> strings = List.of("One", "Two", "Three");
        testString.forEach(strings, (s) -> System.out.println("I am a string " + s));
    }

    public static void runSupplier() {
        Supplier<LocalDateTime> s = LocalDateTime::now;
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(s.get());
        }
    }

    public static void runConverter() {
        Converter<Integer, String> converterToString = new Converter<>();
        Converter<String, Integer> converterToInteger = new Converter<>();

        List<String> convertedIntegers = converterToString.map(List.of(1, 2, 3, 4, 5),
                To::toString);
        System.out.println(convertedIntegers);

        List<Integer> convertedStrings = converterToInteger.map(
                convertedIntegers, To::toInt);
        System.out.println(convertedStrings);
    }
}

class To {
    public static Integer toInt(String str) {
        return Integer.valueOf(str.replace(".", ""));
    }

    public static String toString(Integer i) {
        return i + ".";
    }
}

class TestInterfaces<T> {
    public void forEach(List<T> list, Consumer<T> consumer) {
        for (T t : list) {
            consumer.accept(t);
        }
    }

    public List<T> filter(List<T> list, Predicate<T> predicate) {
        List<T> filteredList = new ArrayList<>();
        for (T t : list) {
            if (predicate.test(t)) {
                filteredList.add(t);
            }
        }
        return filteredList;
    }
}

class Converter<T, U> {
    public List<U> map(List<T> list, Function<T, U> function) {
        List<U> convertedList = new ArrayList<>();
        for (T t : list) {
            convertedList.add(function.apply(t));
        }
        return convertedList;
    }
}

@FunctionalInterface
interface BufferedReaderProcessor {
    String process(BufferedReader bufferedReader) throws IOException;
}

class IntPredicate implements Predicate<Integer> {
    @Override
    public boolean test(Integer integer) {
        return integer < 3;
    }
}
