package edu.osu.restchooser;

/**
 * Created by paranjay on 11/13/15.
 */
public class Restaurant {
    String cuisine;
    String reviews;
    String address;
    String businessId;
    String reviewRating;
    String name;

    public Restaurant(String businessId, String address, String reviewRating, String cuisine,
                      String reviews, String name)
    {
        this.cuisine = cuisine;
        this.reviews = reviews;
        this.address = address;
        this.businessId = businessId;
        this.reviewRating = reviewRating;
        this.name = name;

    }
}
