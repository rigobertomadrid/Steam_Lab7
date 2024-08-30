/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package steam_lab7;

public class Game {
    private int code;
    private String title;
    private char operatingSystem;
    private int minAge;
    private double price;
    private int downloadsCount;
    private String imagePath;

    public Game(int code, String title, char operatingSystem, int minAge, double price, String imagePath) {
        this.code = code;
        this.title = title;
        this.operatingSystem = operatingSystem;
        this.minAge = minAge;
        this.price = price;
        this.downloadsCount = 0;
        this.imagePath = imagePath;
    }

    public int getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public char getOperatingSystem() {
        return operatingSystem;
    }

    public int getMinAge() {
        return minAge;
    }

    public double getPrice() {
        return price;
    }

    public int getDownloadsCount() {
        return downloadsCount;
    }

    public void incrementDownloads() {
        this.downloadsCount++;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
