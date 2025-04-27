package ru.academits.orlov.phonebookservletapi.repository;

import ru.academits.orlov.phonebookservletapi.entity.Contact;

import java.util.List;

public interface ContactsRepository {
    void createContact(Contact contact) throws IllegalArgumentException;

    List<Contact> getContacts(String term);

    void deleteContact(int id);
}
