package ru.javaops.masterjava.model;

import java.util.Map;

public class UsersFail {
    public String range;
    public Map<String, String> emailsAndFail;

    public UsersFail(String range, Map<String, String> emailsAndFail) {
        this.range = range;
        this.emailsAndFail = emailsAndFail;
    }
}
