package ru.academits.orlov.phonebookservletapi.repository;

import ru.academits.orlov.phonebookservletapi.entity.Contact;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class ContactsInMemoryRepository implements ContactsRepository {
    private static final List<Contact> contacts = new ArrayList<>();
    private static final AtomicInteger newId = new AtomicInteger(1);

    @Override
    public void saveContact(Contact contact) throws IllegalArgumentException {
        if (contact.getSurname() == null || contact.getSurname().isBlank()) {
            throw new IllegalArgumentException("Surname is empty");
        }

        if (contact.getName() == null || contact.getName().isBlank()) {
            throw new IllegalArgumentException("Name is empty");
        }

        if (contact.getPhoneNumber() == null || contact.getPhoneNumber().isBlank()) {
            throw new IllegalArgumentException("PhoneNumber is empty");
        }

        String newSurname = contact.getSurname().trim();
        String newName = contact.getName().trim();
        String newPhoneNumber = contact.getPhoneNumber().trim();

        synchronized (contacts) {
            if (contact.getId() == 0) {
                if (contacts.stream()
                        .anyMatch(c -> c.getPhoneNumber().equalsIgnoreCase(contact.getPhoneNumber()))) {
                    throw new IllegalArgumentException("Contact with phone number "
                            + newPhoneNumber + " is already in use");
                }

                contacts.add(new Contact(newId.getAndIncrement(), newSurname, newName, newPhoneNumber));
            } else {
                Contact repositoryContact = contacts.stream()
                        .filter(c -> c.getId() == contact.getId())
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Contact with id " + contact.getId() + " not found"));

                if (!newPhoneNumber.equalsIgnoreCase(repositoryContact.getPhoneNumber())
                        && contacts.stream()
                        .anyMatch(c -> c.getPhoneNumber().equalsIgnoreCase(contact.getPhoneNumber()))) {
                    throw new IllegalArgumentException("Contact with phone number "
                            + newPhoneNumber + " is already in use");
                }

                repositoryContact.setSurname(newSurname);
                repositoryContact.setName(newName);
                repositoryContact.setPhoneNumber(newPhoneNumber);
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

            return contacts.stream()
                    .filter(contact -> contact.getSurname().toLowerCase().contains(term)
                            || contact.getName().toLowerCase().contains(term)
                            || contact.getPhoneNumber().toLowerCase().contains(term))
                    .toList();
        }
    }

    @Override
    public void deleteContact(int id) {
        synchronized (contacts) {
            contacts.removeIf(contact -> contact.getId() == id);

            IntStream.range(id - 1, contacts.size())
                    .forEach(i -> contacts.get(i).setId(contacts.get(i).getId() - 1));

            newId.decrementAndGet();
        }
    }
}
