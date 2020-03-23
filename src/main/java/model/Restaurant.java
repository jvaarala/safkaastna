package model;

import javax.persistence.*;

/**
 * Data Transfer Object (DTO) for Restaurants
 */
@Entity
@Table(name = "restaurant")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private int id;
    @Column
    private String name;
    @Column
    private String address;
    @Column
    private String postal_code;
    @Column
    private String city;
    @Column
    private String www;
    @Column
    private String admin;
    @Column
    private String admin_www;
    @Column
    private double lat;
    @Column
    private double lng;

    public Restaurant() { }

    public Restaurant (int id, double lat, double lng) {
        this.lng = lng;
        this.lat = lat;
        this.id = id;
    }

    public Restaurant(int id, String name, String address, String postal_code, String city, String www, String admin, String admin_www, double lat, double lng) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.postal_code = postal_code;
        this.city = city;
        this.www = www;
        this.admin = admin;
        this.admin_www = admin_www;
        this.lat = lat;
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getWww() {
        return www;
    }

    public void setWww(String www) {
        this.www = www;
    }

    public String getAdmin() { return admin; }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getAdmin_www() {
        return admin_www;
    }

    public void setAdmin_www(String admin_www) { this.admin_www = admin_www; }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public double getLat() { return lat; }

    public void setLat(double lat) { this.lat = lat; }

    public double getLng() { return lng; }

    public void setLng(double lng) { this.lng = lng; }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", postal_code=" + postal_code +
                ", city='" + city + '\'' +
                ", www='" + www + '\'' +
                ", admin='" + admin + '\'' +
                ", admin_www='" + admin_www + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }
}
