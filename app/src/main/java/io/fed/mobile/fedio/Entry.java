package io.fed.mobile.fedio;

/**
 * Created by alecu_000 on 06-Nov-15.
 */
public class Entry {

    private int itemId;
    private String createdBy, itemName, timeOfDay;

    public Entry(int itemId, String createdBy, String itemName, String timeOfDay){
        this.itemId = itemId;
        this.createdBy = createdBy;
        this.itemName = itemName;
        this.timeOfDay = timeOfDay;
    }

    public int getItemId() {
        return itemId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getItemName() {
        return itemName;
    }

    public String getTimeOfDay() {
        return timeOfDay;
    }
}
