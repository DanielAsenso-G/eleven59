package com.eleven59.eleven59.model;

import java.time.LocalTime;

public class TimeSlot implements Comparable<TimeSlot> {
    private int day;
    private LocalTime start;
    private LocalTime end;

    public TimeSlot(int day, LocalTime start, LocalTime end) {
        this.day = day;
        this.start = start;
        this.end = end;
    }

    public int getDay() {
        return day;
    }

    public LocalTime getStart() {
        return start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setStart(LocalTime start) {
        this.start = start;
    }

    public void setEnd(LocalTime end) {
        this.end = end;
    }

    @Override
    public int compareTo(TimeSlot other) {
        // Becuase tree are already sorted that is why we need a comparator for it to sort it in the treemap
        if (this.day != other.day) {
            return Integer.compare(this.day, other.day);
        }

        return this.start.compareTo(other.start);
    }

    @Override
    public String toString() {
        return "TimeSlot{" +
                "day=" + day +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
