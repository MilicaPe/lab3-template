package com.example.loyalty.model;


public enum Status {
    BRONZE (0),
    SILVER (1),
    GOLD(2);

    private final int value;

    Status(int value) {
        this.value = value;
    }

    public static Status valueOf(int value) {
        switch (value) {
            case 2:
                return Status.GOLD;
            case 1:
                return Status.SILVER;
            default:
                return Status.BRONZE;
        }
    }

    public int getValue() {
        return value;
    }
}
