package com.example.hiringdataviewer.view;

import android.content.Context;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import com.example.hiringdataviewer.R;
import com.example.hiringdataviewer.model.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HiringDataViewer {
    private final TextView title;
    private final ExpandableListView resultsDisplay;
    private final Context context;
    private static final String[] GROUP_FROM = {Item.LIST_ID};
    private static final int[] GROUP_TO = {R.id.heading};
    private static final String[] CHILD_FROM = {Item.ITEM};
    private static final int[] CHILD_TO = {R.id.childItem};;

    public HiringDataViewer(final Context context, final TextView title, final ExpandableListView resultsDisplay) {
        this.title = title;
        this.resultsDisplay = resultsDisplay;
        this.context = context;
    }

    public void showError(final String error) {
        title.setText("Error on data fetch: " + error);
    }

    public void displayData(final Map<Integer, List<Item>> items) {
        title.setText("Hiring Data");
        populateUI(items);
    }

    private void populateUI(final Map<Integer, List<Item>> items){
        // (requirement 2) sort results by "listId", then create
        // data groupings for an easy-to-read display in ExpandableListView
        final List<Map<String, String>> groupData = new ArrayList<>();
        final List<Integer> sortedListIds = new ArrayList<>(items.keySet());
        Collections.sort(sortedListIds);

        for (Integer listId : sortedListIds) {
            final Map<String, String> groupMap = new HashMap<>();
            groupMap.put(Item.LIST_ID, "List ID: " + listId);
            groupData.add(groupMap);
        }

        final List<List<Map<String, String>>> childData = new ArrayList<>();
        for (Integer listId : items.keySet()) {
            final List<Map<String, String>> children = new ArrayList<>();
            for (Item item : items.get(listId)) {
                final Map<String, String> childMap = new HashMap<>();
                childMap.put(Item.ITEM, item.toString());
                children.add(childMap);
            }
            childData.add(children);
        }

        final SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter(
                this.context,
                groupData,
                R.layout.group_items,
                GROUP_FROM,
                GROUP_TO,
                childData,
                R.layout.child_items,
                CHILD_FROM,
                CHILD_TO
        );

        resultsDisplay.setAdapter(adapter);
    }
}
