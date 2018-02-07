package com.myreactorhome.deviceservice.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Account {
    private Integer id;
    private String username;
    private String email;
    private String firstName;

    private String lastName;
    private List<Integer> groupsList;
    private Integer ownerGroupId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Integer> getGroupsList() {
        return groupsList;
    }

    public void setGroupsList(List<Integer> groupsList) {
        this.groupsList = groupsList;
    }

    public Integer getOwnerGroupId() {
        return ownerGroupId;
    }

    public void setOwnerGroupId(Integer ownerGroupId) {
        this.ownerGroupId = ownerGroupId;
    }
}
