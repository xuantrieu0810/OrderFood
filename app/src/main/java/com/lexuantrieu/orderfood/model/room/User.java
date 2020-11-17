package com.lexuantrieu.orderfood.model.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user")
public class User {
    @PrimaryKey
    private int id;

    @ColumnInfo(name = "username")
    private String username;

    @ColumnInfo(name = "fullname")
    private String fullname;

    @ColumnInfo(name = "role")
    private int role;

    @ColumnInfo(name = "token")
    private String token;

    public User(String username, String fullname, int role, String token) {
        this.username = username;
        this.fullname = fullname;
        this.role = role;
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
