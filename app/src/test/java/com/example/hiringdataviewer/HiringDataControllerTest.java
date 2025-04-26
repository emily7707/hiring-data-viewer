package com.example.hiringdataviewer;

import static org.junit.Assert.assertEquals;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Test;

import com.example.hiringdataviewer.controllers.HiringDataController;
import com.example.hiringdataviewer.model.Item;

import java.util.List;
import java.util.Map;

public class HiringDataControllerTest {

    @Test
    public void fetchTypicalHiringData() throws JSONException {
        String sampleJson = "[{\"id\": 203, \"listId\": 2, \"name\": \"\"},\n" +
                "{\"id\": 684, \"listId\": 1, \"name\": \"Item 684\"},\n" +
                "{\"id\": 276, \"listId\": 1, \"name\": \"Item 276\"},\n" +
                "{\"id\": 736, \"listId\": 3, \"name\": null}]";

        JSONArray input = new JSONArray(sampleJson);

        HiringDataController controller = new HiringDataController(
                null,
                null,
                null,
                null
                );
        controller.parseAndStoreResponse(input);
        Map<Integer, List<Item>> result = controller.getItems();
        // We should only have one listId key, since 2 and 3 are empty/null
        assertEquals(1, result.keySet().size());
        assertEquals(2, result.get(1).size());
        assertEquals("Item 276", result.get(1).get(0).getName());
        assertEquals("Item 684", result.get(1).get(1).getName());
    }

    @Test
    public void fetchEmptyHiringData() throws JSONException {
        String sampleJson = "[]";

        JSONArray input = new JSONArray(sampleJson);

        HiringDataController controller = new HiringDataController(
                null,
                null,
                null,
                null
        );
        controller.parseAndStoreResponse(input);
        Map<Integer, List<Item>> result = controller.getItems();
        assertEquals(0, result.keySet().size());
    }

    @Test
    public void fetchAllNullOrEmptyHiringData() throws JSONException {
        String sampleJson = "[{\"id\": 203, \"listId\": 2, \"name\": \"\"},\n" +
                "{\"id\": 684, \"listId\": 1, \"name\": null},\n" +
                "{\"id\": 276, \"listId\": 1, \"name\": null},\n" +
                "{\"id\": 736, \"listId\": 3, \"name\": null}]";

        JSONArray input = new JSONArray(sampleJson);

        HiringDataController controller = new HiringDataController(
                null,
                null,
                null,
                null
        );
        controller.parseAndStoreResponse(input);
        Map<Integer, List<Item>> result = controller.getItems();
        assertEquals(0, result.keySet().size());
    }
}