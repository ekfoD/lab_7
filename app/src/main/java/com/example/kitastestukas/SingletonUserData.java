package com.example.kitastestukas;

import com.example.kitastestukas.Models.UserInput;

public class SingletonUserData {
    public static SingletonUserData instance;

    private UserInput userInput;

    public UserInput getUserInput() {
        return userInput;
    }

    public void setUserInput(UserInput userInput) {
        this.userInput = userInput;
    }

    private SingletonUserData() {}
    public static SingletonUserData getInstance() {
        if (instance == null) {
            instance = new SingletonUserData();
            return instance;
        }
        else
            return instance;
    }
}
