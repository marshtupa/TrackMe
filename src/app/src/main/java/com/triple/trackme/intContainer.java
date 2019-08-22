package com.triple.trackme;

public class intContainer {
    private int value;

    public int getValue(){
        return value;
    }
    public void setValue(int newValue){
        value = newValue;
    }
    public void increaseCount(){
        value++;
    }
    public void increaseCount(int val){
        value += val;
    }
}
