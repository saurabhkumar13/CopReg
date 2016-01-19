package com.thefallen.copreg;

/**
 * Created by root on 1/19/16.
 * This is the classfor storing the data of each member
 */
public class MemberData {
    private String name = "";
    private String entry_no = "";

    public MemberData(String name, String entry_no) {
        this.name = name;
        this.entry_no = entry_no;
    }

    public String getName() {
        return name;
    }

    public String getEntry_no() {
        return entry_no;
    }
}
