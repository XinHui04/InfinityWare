/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.time.DayOfWeek;
import java.time.LocalDate;

public class DatePicker {
    public static void showCalendarOnly() {
        LocalDate today = LocalDate.now();
        LocalDate sevenDaysLater = today.plusDays(7);
        for (int m = 0; m < 2; m++) {
            LocalDate month = today.plusMonths(m);
            LocalDate startOfMonth = month.withDayOfMonth(1);
            int lengthOfMonth = startOfMonth.lengthOfMonth();
            int firstDayOfWeek = startOfMonth.getDayOfWeek().getValue() % 7;
            System.out.println("\nCalendar for " + month.getMonth() + " " + month.getYear());
            System.out.println("Su Mo Tu We Th Fr Sa");
            for (int i = 0; i < firstDayOfWeek; i++) {
                System.out.print("   ");
            }
            for (int day = 1; day <= lengthOfMonth; day++) {
                LocalDate currentDay = LocalDate.of(month.getYear(), month.getMonth(), day);
                if (currentDay.isBefore(today) || currentDay.isEqual(today)) {
                    System.out.print("\033[31m");
                } else if (!currentDay.isBefore(today) && !currentDay.isAfter(sevenDaysLater)) {
                    System.out.print("\033[31m");
                } else if (currentDay.getDayOfWeek() == DayOfWeek.SUNDAY) {
                    System.out.print("\033[34m");
                } else {
                    System.out.print("\033[32m");
                }
                System.out.printf("%2d ", day);
                System.out.print("\033[0m");
                if ((day + firstDayOfWeek) % 7 == 0) {
                    System.out.println();
                }
            }
            System.out.println();
        }
    }

    public static boolean isValidMonth(String monthInput) {
        try {
            int month = Integer.parseInt(monthInput);
            return month >= 1 && month <= 12;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidDay(String dayInput, String monthInput) {
        try {
            int day = Integer.parseInt(dayInput);
            int month = Integer.parseInt(monthInput);
            int maxDayInMonth = LocalDate.of(LocalDate.now().getYear(), month, 1).lengthOfMonth();
            return day >= 1 && day <= maxDayInMonth;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
