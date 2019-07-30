package br.com.comquesamba.models;

public class SambaDataDTO {

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

    public SambaDataDTO(){
    }

    public SambaDataDTO(SambaBean sambaBean){
        if (sambaBean != null){
            this.name = sambaBean.getName();
            this.description = sambaBean.getDescription();
            this.date = sambaBean.getDate();
            this.imageUrl = sambaBean.getImageUrl();
            this.location = sambaBean.getLocation();
            this.street = sambaBean.getStreet();
            this.number = sambaBean.getNumber();
            this.district = sambaBean.getDistrict();
            this.city = sambaBean.getCity();
            this.ticketUrl = sambaBean.getTicketUrl();
        }
    }

    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public String getDate() {
        return date;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public String getLocation() {
        return location;
    }
    public String getStreet() {
        return street;
    }
    public String getNumber() {
        return number;
    }
    public String getDistrict() {
        return district;
    }
    public String getCity() {
        return city;
    }
    public String getTicketUrl() {
        return ticketUrl;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public void setStreet(String street) {
        this.street = street;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public void setDistrict(String district) {
        this.district = district;
    }
    public void setTicketUrl(String ticketUrl) {
        this.ticketUrl = ticketUrl;
    }
    public void setCity(String city) {
        this.city = city;
    }
}
