package com.example.cinema.model;

import java.util.Date;
import java.util.List;

public class TicketInfo {
    private String movieTitle;
    private String movieImage; // Có thể là null
//    private String cinemaName;
    private Date showTime; // Bạn có thể sử dụng Date nếu cần chuyển đổi chuỗi thời gian thành đối tượng Date
    private List<String> seats;

    // Constructor
    public TicketInfo(String movieTitle, String movieImage, Date showTime, List<String> seats) {
        this.movieTitle = movieTitle;
        this.movieImage = movieImage;
        this.showTime = showTime;
        this.seats = seats;
    }

    // Getters and setters
    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getMovieImage() {
        return movieImage;
    }

    public void setMovieImage(String movieImage) {
        this.movieImage = movieImage;
    }

//    public String getCinemaName() {
//        return cinemaName;
//    }
//
//    public void setCinemaName(String cinemaName) {
//        this.cinemaName = cinemaName;
//    }

    public Date getShowTime() {
        return showTime;
    }

    public void setShowTime(Date showTime) {
        this.showTime = showTime;
    }

    public List<String> getSeats() {
        return seats;
    }

    public void setSeats(List<String> seats) {
        this.seats = seats;
    }
}

