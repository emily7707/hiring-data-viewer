package com.example.hiringdataviewer;

import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.hiringdataviewer.model.Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    Map<Integer, List<Item>> items;
    ExpandableListView resultsDisplay;
    TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        resultsDisplay = findViewById(R.id.display);
        items = new HashMap<>();
        fetchHiringData();
        title = findViewById(R.id.title);
        title.setText("Hiring Data");
    }

    private void fetchHiringData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://hiring.fetch.com/hiring.json";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null,
                new Response.Listener<>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        formatResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        resultsDisplay.setText("Error on data fetch: " + error.getMessage());
                    }
                });
        requestQueue.add(jsonArrayRequest);
    }

    private void formatResponse(JSONArray response){
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject jsonElement = response.getJSONObject(i);

                if (!jsonElement.isNull("name") && !jsonElement.getString("name").isEmpty()) {
                    int listId = jsonElement.getInt("listId");
                    Item item = new Item(jsonElement.getInt("id"),
                            listId,
                            jsonElement.getString("name"));

                    if (!items.containsKey(listId)) {
                        items.put(listId, new ArrayList<>());
                    }
                    items.get(listId).add(item);
                }
            }
            for (List<Item> items : items.values()) {
                items.sort(Comparator.comparing(Item::getName));
            }
//            StringBuilder builder = new StringBuilder();
//            for (Integer listId : items.keySet()) {
//                builder.append("ListID: ").append(listId).append("\n");
//                builder.append(items.get(listId).stream().map(Item::toString)
//                        .collect(Collectors.joining("\t\n")));
//                builder.append("\n");
//            }
            // todo: display by (sorted) listid
//            resultsDisplay.setText(builder.toString());
            List<Map<String, String>> groupData = new ArrayList<>();
            List<Integer> sortedListIds = new ArrayList<>(items.keySet());
            Collections.sort(sortedListIds);
            for (Integer listId : sortedListIds) {
                Map<String, String> groupMap = new HashMap<>();
                groupMap.put("listId", "List id: " + listId);
                groupData.add(groupMap);
            }
            String[] groupFrom = {"listId"};
            int[] groupTo = {R.id.heading};
            List<List<Map<String, String>>> childData = new ArrayList<>();
            for (Integer listId : items.keySet()) {
                List<Map<String, String>> children = new ArrayList<>();
                for (Item item : items.get(listId)) {
                    Map<String, String> childMap = new HashMap<>();
                    childMap.put("item", item.toString());
                    children.add(childMap);
                }
                childData.add(children);
            }

            String[] childFrom = {"item"};
            int[] childTo = {R.id.childItem};
            SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter(
                    this,
                    groupData,
                    R.layout.group_items,
                    groupFrom,
                    groupTo,
                    childData,
                    R.layout.child_items,
                    childFrom,
                    childTo
                    );

            resultsDisplay.setAdapter(adapter);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}