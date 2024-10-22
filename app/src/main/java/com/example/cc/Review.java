package com.example.cc;

public class Review {
    private int id;
    private String tutorName;
    private String reviewText;
    private int starRating;

    public Review(int id, String tutorName, String reviewText, int starRating) {
        this.id = id;
        this.tutorName = tutorName;
        this.reviewText = reviewText;
        this.starRating = starRating;
    }

    public int getId() {
        return id;
    }

    public String getTutorName() {
        return tutorName;
    }

    public String getReviewText() {
        return reviewText;
    }

    public int getStarRating() {
        return starRating;
    }
}
