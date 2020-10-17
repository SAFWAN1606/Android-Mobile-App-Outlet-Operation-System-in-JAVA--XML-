package com.system.outletoperationsystemserver.Model;

public class Category {
    private  String Name ;
    private  String Image;



    private  String RestaurantId;
    public Category() {
    }

    public Category(String name, String image,String restaurantId) {
        Name = name;
        Image = image;
        RestaurantId = restaurantId;
    }
    public String getRestaurantId() {
        return RestaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        RestaurantId = restaurantId;
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
}
