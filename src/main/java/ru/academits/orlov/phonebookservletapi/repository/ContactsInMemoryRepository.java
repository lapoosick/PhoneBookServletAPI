package ru.academits.orlov.phonebookservletapi.repository;

import ru.academits.orlov.phonebookservletapi.entity.Contact;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class ContactsInMemoryRepository implements ContactsRepository {
    private static final List<Contact> contacts = new ArrayList<>();
    private static final AtomicInteger newId = new AtomicInteger(1);
    private static final AtomicInteger newOrdinalNumber = new AtomicInteger(1);

    @Override
    public void createContact(Contact contact) throws IllegalArgumentException {
        if (contact.getSurname() == null || contact.getSurname().isBlank()) {
            throw new IllegalArgumentException("Не указана фамилия.");
        }

        if (contact.getName() == null || contact.getName().isBlank()) {
            throw new IllegalArgumentException("Не указано имя.");
        }

        if (contact.getPhoneNumber() == null || contact.getPhoneNumber().isBlank()) {
            throw new IllegalArgumentException("Не указан телефон.");
        }

        int contactOrdinalNumber = contact.getOrdinalNumber();
        String contactSurname = contact.getSurname().trim();
        String contactName = contact.getName().trim();
        String contactPhoneNumber = contact.getPhoneNumber().trim();

        synchronized (contacts) {
            if (contact.getId() == 0) {
                if (contacts.stream()
                        .anyMatch(c -> c.getPhoneNumber().equalsIgnoreCase(contactPhoneNumber))) {
                    throw new IllegalArgumentException("Контакт с таким номером телефона уже существует.");
                }

                if (contacts.stream()
                        .anyMatch(c -> c.getOrdinalNumber() == contactOrdinalNumber)) {
                    throw new IllegalArgumentException("Контакт с таким порядковым номером уже существует.");
                }

                contacts.add(new Contact(newId.getAndIncrement(), newOrdinalNumber.getAndIncrement(),
                        contactSurname, contactName, contactPhoneNumber));
            } else {
                Contact repositoryContact = contacts.stream()
                        .filter(c -> c.getId() == contact.getId())
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Контакт с таким id не существует."));

                if (!contactPhoneNumber.equalsIgnoreCase(repositoryContact.getPhoneNumber())
                        && contacts.stream()
                        .anyMatch(c -> c.getPhoneNumber().equalsIgnoreCase(contact.getPhoneNumber()))) {
                    throw new IllegalArgumentException("Контакт с таким номером телефона уже существует.");
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
                    .filter(c -> c.getId() == id).findFirst().orElse(null);

            if (currentContact == null) {
                throw new IllegalArgumentException("Не удалось удалить контакт.");
            }

            int currentContactOrdinalNumber = currentContact.getOrdinalNumber();

            contacts.remove(currentContact);

            IntStream.range(currentContactOrdinalNumber - 1, contacts.size())
                    .forEach(i -> contacts.get(i).setOrdinalNumber(contacts.get(i).getOrdinalNumber() - 1));

            newOrdinalNumber.decrementAndGet();
        }
    }
}
