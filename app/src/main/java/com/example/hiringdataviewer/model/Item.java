package com.example.hiringdataviewer.model;

import androidx.annotation.NonNull;

import java.util.Locale;

public class Item {
    private int id;
    private int listId;
    private String name;

    public Item(int id, int listId, String name) {
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
        return String.format(Locale.ENGLISH, "Item(id: %d, listId: %d, name: %s)",
                id,
                listId,
                name
        );
    }
}
