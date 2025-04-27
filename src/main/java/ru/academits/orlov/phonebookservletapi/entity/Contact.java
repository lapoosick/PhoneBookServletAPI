package ru.academits.orlov.phonebookservletapi.entity;

public class Contact {
    private int id;
    private int ordinalNumber;
    private String surname;
    private String name;
    private String phoneNumber;

    public Contact(Contact contact) {
        id = contact.getId();
        ordinalNumber = contact.getOrdinalNumber();
        surname = contact.getSurname();
        name = contact.getName();
        phoneNumber = contact.getPhoneNumber();
    }

    public Contact(int id, int ordinalNumber, String surname, String name, String phoneNumber) {
        this.id = id;
        this.ordinalNumber = ordinalNumber;
        this.surname = surname;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public Contact() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrdinalNumber() {
        return ordinalNumber;
    }

    public void setOrdinalNumber(int ordinalNumber) {
        this.ordinalNumber = ordinalNumber;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "id = " + id + ", surname = " + surname + ", name = " + name + ", phoneNumber = " + phoneNumber;
    }
}
