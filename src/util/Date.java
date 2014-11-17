package util;

import exceptions.WrongDateException;

import java.util.Random;

/**
 * Created by Wilson on 04/11/2014.
 */
public class Date {

    private int day;
    private int month;
    private int year;

    public Date(int day, int month, int year) throws WrongDateException {
        if (month < 1 || month > 12) {
            throw new WrongDateException("Illegal month value: " + month);
        }
        if ((month == 4 || month == 6 || month == 9 || month == 11) && (day < 1 || day > 30)) {
            throw new WrongDateException("Illegal day value: " + day);
        }
        boolean isLeapYear = ((year % 4 == 0) && (year % 100 != 0) || (year % 400 == 0));
        if (month == 2 && isLeapYear && (day < 1 || day > 29)) {
            throw new WrongDateException("Illegal day value: " + day);
        }
        if (month == 2 && !isLeapYear && (day < 1 || day > 28)) {
            throw new WrongDateException("Illegal day value: " + day);
        }
        if (day < 1 || day > 31) {
            throw new WrongDateException("Illegal day value: " + day);
        }

        this.day = day;
        this.month = month;
        this.year = year;
    }

    public boolean isEarlier(Date d1) {
        if (this.year > d1.getYear()) {
            return false;
        }

        if (this.month > d1.getMonth()) {
            return false;
        }

        if (this.day > d1.getDay()) {
            return false;
        }

        return true;
    }

    public static Date[] getNewDates() {
        Date[] arr = new Date[2];
        Random r = new Random();
        Date date1, date2;
        do {
            try {
                date1 = new Date(r.nextInt(31) + 1, r.nextInt(12) + 1, r.nextInt(10) + 2014);
            } catch (WrongDateException e) {
                continue;
            }
            break;
        } while (true);

        do {
            try {
                date2 = new Date(r.nextInt(31) + 1, r.nextInt(12) + 1, r.nextInt(10) + 2014);
            } catch (WrongDateException e) {
                continue;
            }
            if (date1.isEarlier(date2)) {
                break;
            }
        } while (true);

        arr[0] = date1;
        arr[1] = date2;

        return arr;
    }

    public static Date getNewDate() {
        Random r = new Random();
        Date date1;
        do {
            try {
                date1 = new Date(r.nextInt(31) + 1, r.nextInt(12) + 1, r.nextInt(10) + 2014);
            } catch (WrongDateException e) {
                continue;
            }
            break;
        } while (true);

        return date1;
    }

    public static Date getBiggerDate(Date date1) {
        Random r = new Random();
        Date date2;

        do {
            try {
                date2 = new Date(r.nextInt(31) + 1, r.nextInt(12) + 1, r.nextInt(10) + 2014);
            } catch (WrongDateException e) {
                continue;
            }
            if (date1.isEarlier(date2)) {
                break;
            }
        } while (true);

        return date2;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
