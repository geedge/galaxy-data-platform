package com.mesalab.hos.entity.lifecycle;

import javax.xml.bind.annotation.XmlElement;

public class ExpirationObj {
    private int days;

    public ExpirationObj() {
    }

    @XmlElement(name = "Days")
    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    @Override
    public String toString() {
        return "ExpirationObj{" +
                "days=" + days +
                '}';
    }
}
