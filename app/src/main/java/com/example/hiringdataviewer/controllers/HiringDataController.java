package com.example.hiringdataviewer.controllers;

import android.content.Context;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.hiringdataviewer.model.Item;
import com.example.hiringdataviewer.view.HiringDataViewer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HiringDataController {
    private final HiringDataViewer hiringDataViewer;
    private final RequestQueue requestQueue;
    private Map<Integer, List<Item>> items;
    private static final String URL = "https://hiring.fetch.com/hiring.json";

    public HiringDataController(final Context context, final RequestQueue requestQueue, final TextView title, final ExpandableListView resultsDisplay) {
        this.hiringDataViewer = new HiringDataViewer(context, title, resultsDisplay);
        this.requestQueue = requestQueue;
    }

    public Map<Integer, List<Item>> getItems() {
        return items; // If we're concerned about clients modifying this, we can do a deep clone
    }

    public void fetchHiringData() {
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, URL, null,
                response -> {
                    parseAndStoreResponse(response);
                    hiringDataViewer.displayData(items);
                },
                error -> hiringDataViewer.showError(error.getMessage())
        );
        requestQueue.add(jsonArrayRequest); // async call using Volley
    }

    public void parseAndStoreResponse(final JSONArray response) {
        try {
            items = new HashMap<>();
            for (int i = 0; i < response.length(); i++) {
                final JSONObject jsonElement = response.getJSONObject(i);

                // (requirement 3) filters out items where "name" is blank or null
                if (!jsonElement.isNull(Item.NAME) && !jsonElement.getString(Item.NAME).isEmpty()) {
                    final int listId = jsonElement.getInt(Item.LIST_ID);
                    final Item item = new Item(jsonElement.getInt(Item.ID),
                            listId,
                            jsonElement.getString(Item.NAME));

                    // (requirement 1) group items by "listId"
                    if (!items.containsKey(listId)) {
                        items.put(listId, new ArrayList<>());
                    }
                    items.get(listId).add(item);
                }
            }
            // (requirement 2) sort results by name
            for (List<Item> items : items.values()) {
                items.sort(Comparator.comparing(Item::getName));
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

}
