package br.com.comquesamba.models;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import br.com.comquesamba.utils.Utils;

public class SambaBean implements Serializable {

    private String name;
    private String description;
    private String date;
    private String imageUrl;
    private String location;
    private String street;
    private String number;
    private String district;
    private String city;
    private String ticketUrl;

    public SambaBean() { }

    public SambaBean(SambaDataDTO sambaDataDTO){
        if (sambaDataDTO != null){
            this.name = sambaDataDTO.getName();
            this.description = sambaDataDTO.getDescription();
            this.date = sambaDataDTO.getDate();
            this.imageUrl = sambaDataDTO.getImageUrl();
            this.location = sambaDataDTO.getLocation();
            this.street = sambaDataDTO.getStreet();
            this.number = sambaDataDTO.getNumber();
            this.district = sambaDataDTO.getDistrict();
            this.city = sambaDataDTO.getCity();
            this.ticketUrl = sambaDataDTO.getTicketUrl();
        }
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getAddress() {
        return getAdressComplete();
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getStreet() {
        return street;
    }
    public void setStreet(String street) {
        this.street = street;
    }
    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public String getDistrict() {
        return district;
    }
    public void setDistrict(String district) {
        this.district = district;
    }
    public String getTicketUrl() {
        return ticketUrl;
    }
    public void setTicketUrl(String ticketUrl) {
        this.ticketUrl = ticketUrl;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }


    public Date getDateObject(){
        return Utils.getDateFromString(date);
    }

    public String getSimpleDate(){
        return Utils.getSimpleDate(getDateObject());
    }

    public String getAdressComplete() {
        return street + " " + number + ", " + district + ", " + this.city;
    }
    public String getAdressPartOne() {
        return street + ", " + number;
    }
    public String getAdressPartTwo() {
        return district + ", " + this.city;
    }

    private int getDaysNumberUntilSamba(){
        Calendar today = Calendar.getInstance();
        Calendar eventDate = Calendar.getInstance();
        eventDate.setTime(getDateObject());

        return Utils.daysBetween(eventDate, today);
    }

    public String daysUntilSamba(){

        int days = getDaysNumberUntilSamba();

        if (days == 0){
            return "Hoje!";
        }

        if (days == 1){
            return "Amanhã";
        }

        return "Faltam " + String.valueOf(days) + " dias";
    }

    public String getTileForCarousel(){
        return name + "\n" + getSimpleDate() +  " (" + daysUntilSamba() + ")";
    }

    public boolean isUpToDate(){
        if (getDaysNumberUntilSamba() == 0){
            return true;
        }

        if (getDateObject().before(new Date())){
            return false;
        }

        return true;
    }

}

