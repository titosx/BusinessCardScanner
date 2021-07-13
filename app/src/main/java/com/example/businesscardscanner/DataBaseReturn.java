package com.example.businesscardscanner;

import java.io.Serializable;
import java.util.ArrayList;

public class DataBaseReturn implements Serializable {
    ArrayList<String> names;
    ArrayList<String> phones;
    ArrayList<String> addresses;
    ArrayList<String> cities;
    ArrayList<String> emails;
}
