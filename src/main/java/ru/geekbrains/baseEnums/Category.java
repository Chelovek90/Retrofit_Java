package ru.geekbrains.baseEnums;

public enum  Category {
    FOOD(1, "Food"),
    ELECTRONIC(2, "Electronic");

    public final int id;
    public final String title;

    Category(int id, String title) {
        this.id = id;
        this.title = title;
    }
}
