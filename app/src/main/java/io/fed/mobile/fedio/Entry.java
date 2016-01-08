package io.fed.mobile.fedio;

/**
 * Created by alecu_000 on 06-Nov-15.
 */
public class Entry {

    private int itemId;

    private String  itemName, timeOfDay;
    private double dose, caloriesPerDose;


    public Entry(int itemId, String itemName, String timeOfDay, double dose, double caloriesPerDose){
        this.itemId = itemId;
        this.itemName = itemName;
        this.timeOfDay = timeOfDay;
        this.dose = dose;
        this.caloriesPerDose = caloriesPerDose;
    }

    public Entry(){}


    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setTimeOfDay(String timeOfDay) {
        this.timeOfDay = timeOfDay;
    }

    public void setDose(double dose) {
        this.dose = dose;
    }

    public void setCaloriesPerDose(double caloriesPerDose) {
        this.caloriesPerDose = caloriesPerDose;
    }



    public int getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public String getTimeOfDay() {
        return timeOfDay;
    }

    public double getDose(){
        return dose;
    }

    public double getCaloriesPerDose() {
        return caloriesPerDose;
    }



    @Override
    public String toString() {
        return "Entry{" +
                "itemId=" + itemId +
                ", itemName='" + itemName + '\'' +
                ", timeOfDay='" + timeOfDay + '\'' +
                ", dose=" + dose +
                '}';
    }
}
