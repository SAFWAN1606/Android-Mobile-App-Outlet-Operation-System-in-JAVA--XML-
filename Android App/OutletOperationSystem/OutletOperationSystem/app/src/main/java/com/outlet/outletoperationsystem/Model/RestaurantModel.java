package com.outlet.outletoperationsystem.Model;

public class RestaurantModel {
    private String Name ;
    private String Image;
    private String Location;

    public RestaurantModel() {
    }

    public RestaurantModel(String name, String image, String location) {
        Name = name;
        Image = image;
        Location = location;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }
}
