package ru.academits.orlov.phonebookservletapi.repository;

import ru.academits.orlov.phonebookservletapi.entity.Contact;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ContactsInMemoryRepository implements ContactsRepository {
    private static final List<Contact> contacts = new ArrayList<>();
    private static final AtomicInteger newId = new AtomicInteger(1);

    @Override
    public void createOrUpdateContact(Contact contact) {
        isEmptyString(contact.getSurname());
        isEmptyString(contact.getName());
        isEmptyString(contact.getPhoneNumber());

        int contactId = contact.getId();
        String contactSurname = contact.getSurname().trim();
        String contactName = contact.getName().trim();
        String contactPhoneNumber = contact.getPhoneNumber().trim();

        synchronized (contacts) {
            if (contactId == 0) {
                if (contacts.stream()
                        .anyMatch(c -> c.getPhoneNumber().equalsIgnoreCase(contactPhoneNumber))) {
                    throw new IllegalArgumentException("Контакт с номером телефона " + contactPhoneNumber + " уже существует.");
                }

                contacts.add(new Contact(newId.getAndIncrement(), contactSurname, contactName, contactPhoneNumber));
            } else {
                Contact repositoryContact = contacts.stream()
                        .filter(c -> c.getId() == contactId)
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Контакт с id = " + contactId + " не найден."));

                if (!contactPhoneNumber.equalsIgnoreCase(repositoryContact.getPhoneNumber())
                        && contacts.stream().anyMatch(c -> c.getPhoneNumber().equalsIgnoreCase(contactPhoneNumber))) {
                    throw new IllegalArgumentException("Контакт с номером телефона " + contactPhoneNumber + " уже существует.");
                }

                repositoryContact.setSurname(contactSurname);
                repositoryContact.setName(contactName);
                repositoryContact.setPhoneNumber(contactPhoneNumber);
            }
        }
    }

    @Override
    public List<Contact> getContacts(String term) {
        synchronized (contacts) {
            if (term == null || term.isEmpty()) {
                return contacts.stream()
                        .map(Contact::new)
                        .toList();
            }

            String termLowerCase = term.toLowerCase();

            return contacts.stream()
                    .filter(contact -> contact.getSurname().toLowerCase().contains(termLowerCase)
                            || contact.getName().toLowerCase().contains(termLowerCase)
                            || contact.getPhoneNumber().toLowerCase().contains(termLowerCase))
                    .toList();
        }
    }

    @Override
    public void deleteContact(int id) {
        synchronized (contacts) {
            Contact currentContact = contacts.stream()
                    .filter(c -> c.getId() == id)
                    .findFirst()
                    .orElse(null);

            if (currentContact == null) {
                throw new IllegalArgumentException("Контакт с id = " + id + " не найден.");
            }

            contacts.remove(currentContact);
        }
    }

    private void isEmptyString(String string) {
        if (string == null || string.isBlank()) {
            throw new IllegalArgumentException("Не заполнено обязательное поле.");
        }
    }
}
