package ru.academits.orlov.phonebookservletapi.repository;

import ru.academits.orlov.phonebookservletapi.entity.Contact;

import java.util.List;

public interface ContactsRepository {
    void createOrUpdateContact(Contact contact);

    List<Contact> getContacts(String term);

    void deleteContact(int id);
}
