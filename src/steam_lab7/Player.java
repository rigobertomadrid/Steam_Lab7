/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package steam_lab7;

import java.util.Calendar;

public class Player {
    private int code;
    private String username;
    private String password;
    private String name;
    private long birthdate;
    private int downloadsCount;
    private String imagePath;
    private UserType userType;

    public Player(int code, String username, String password, String name, Calendar birthdate, String imagePath, UserType userType) {
        this.code = code;
        this.username = username;
        this.password = password;
        this.name = name;
        this.birthdate = birthdate.getTimeInMillis();
        this.downloadsCount = 0;
        this.imagePath = imagePath;
        this.userType = userType;
    }

    public int getCode() {
        return code;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public long getBirthdate() {
        return birthdate;
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

    public UserType getUserType() {
        return userType;
    }

    public int getAge() {
        Calendar birthCalendar = Calendar.getInstance();
        birthCalendar.setTimeInMillis(birthdate);
        return Calendar.getInstance().get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR);
    }
}
    