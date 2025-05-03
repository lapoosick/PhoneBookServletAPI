package ru.academits.orlov.phonebookservletapi.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.academits.orlov.phonebookservletapi.dto.GeneralResponse;
import ru.academits.orlov.phonebookservletapi.entity.Contact;
import ru.academits.orlov.phonebookservletapi.repository.ContactsInMemoryRepository;
import ru.academits.orlov.phonebookservletapi.repository.ContactsRepository;

import java.io.IOException;
import java.io.Serial;
import java.io.Writer;
import java.util.List;

@WebServlet("/api/contacts")
public class ContactsServlet extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 2L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        ContactsRepository contactsRepository = new ContactsInMemoryRepository();
        List<Contact> contactsList = contactsRepository.getContacts(req.getParameter("term"));

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            Writer writer = resp.getWriter();
            writer.write(objectMapper.writeValueAsString(contactsList));
        } catch (IOException e) {
            objectMapper.writeValue(resp.getWriter(), GeneralResponse.getErrorResponse("Ошибка ввода/вывода."));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        ObjectMapper objectMapper = new ObjectMapper();

        ContactsRepository contactsRepository = new ContactsInMemoryRepository();

        try {
            Contact newContact = objectMapper.readValue(req.getReader(), Contact.class);
            contactsRepository.createOrUpdateContact(newContact);

            objectMapper.writeValue(resp.getWriter(), GeneralResponse.getSuccessResponse());
        } catch (IllegalArgumentException | IOException e) {
            objectMapper.writeValue(resp.getWriter(), GeneralResponse.getErrorResponse(e.getMessage()));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        ObjectMapper objectMapper = new ObjectMapper();
        Writer writer = resp.getWriter();

        try {
            int contactId = Integer.parseInt(req.getParameter("id"));
            ContactsRepository contactsRepository = new ContactsInMemoryRepository();

            contactsRepository.deleteContact(contactId);
            objectMapper.writeValue(writer, GeneralResponse.getSuccessResponse());
        } catch (NumberFormatException e) {
            objectMapper.writeValue(writer, GeneralResponse.getErrorResponse("Переданный id (" + req.getParameter("id") + ") не является целым числом."));
        } catch (IllegalArgumentException e) {
            objectMapper.writeValue(writer, GeneralResponse.getErrorResponse(e.getMessage()));
        }
    }
}
