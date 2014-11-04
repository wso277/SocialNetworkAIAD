package util;

import exceptions.WrongDateException;

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
