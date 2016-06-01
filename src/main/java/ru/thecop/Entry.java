package ru.thecop;

/**
 * Created by TheCops-PC on 01.06.2016.
 */
public class Entry {
    //TODO lombok?

    private String methodName;
    private String value;

    public Entry(String methodName, String value) {
        this.methodName = methodName;
        this.value = value;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
