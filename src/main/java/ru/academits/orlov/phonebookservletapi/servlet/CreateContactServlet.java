package ru.academits.orlov.phonebookservletapi.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.academits.orlov.phonebookservletapi.entity.Contact;
import ru.academits.orlov.phonebookservletapi.repository.ContactsInMemoryRepository;
import ru.academits.orlov.phonebookservletapi.repository.ContactsRepository;

import java.io.IOException;
import java.io.Serial;
import java.io.Writer;
import java.util.List;

@WebServlet("/api/contacts")
public class CreateContactServlet extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 2L;

    private final ContactsRepository contactsRepository = new ContactsInMemoryRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        List<Contact> contactsList = contactsRepository.getContacts(req.getParameter("term"));

        Writer writer = resp.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();

        writer.write(objectMapper.writeValueAsString(contactsList));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        ObjectMapper mapper = new ObjectMapper();
        Contact newContact = mapper.readValue(req.getReader(), Contact.class);

        try {
            contactsRepository.saveContact(newContact);
        } catch (IllegalArgumentException e) {
            mapper.writeValue(resp.getWriter(), "contactSaveError: " + e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        int contactId = Integer.parseInt(req.getParameter("id"));

        contactsRepository.deleteContact(contactId);
    }
}
