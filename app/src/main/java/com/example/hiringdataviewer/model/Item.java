package com.example.hiringdataviewer.model;

import androidx.annotation.NonNull;

import java.util.Locale;

public class Item {
    public static final String ID = "id";
    public static final String LIST_ID = "listId";
    public static final String NAME = "name";
    public static final String ITEM = "item";
    private final int id;
    private final int listId;
    private final String name;

    public Item(final int id, final int listId, final String name) {
        this.id = id;
        this.listId = listId;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "Name: %s (id: %d)",
                name,
                id
        );
    }
}
