package com.example.businesscardscanner;

public class IPAddress {

    private static String ip;

    public IPAddress(String ip) {
        this.ip = ip;
    }

    public static String getIPAddress() {
        return ip;
    }

    public void setIPAddress(String ip) {
        this.ip = ip;
    }
}
