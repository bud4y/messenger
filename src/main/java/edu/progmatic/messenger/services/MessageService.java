package edu.progmatic.messenger.services;

import edu.progmatic.messenger.modell.Message;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Scope(
        scopeName = WebApplicationContext.SCOPE_SESSION,
        proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MessageService {
    public List<Message> messages = new ArrayList<>();

    {
        messages.add(new Message(" fg sjtrh strh trh r", "Öcsesz", LocalDateTime.now().minusDays(12)));
        messages.add(new Message(" rggdf gd fg gu ktk z jdtr rh", "Gergő", LocalDateTime.now().minusDays(6)));
        messages.add(new Message("gsojgssdgls fsdgh fd hf", "Gyulus", LocalDateTime.now().minusDays(5)));
    }

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void createMessage(String text) {
        String loggedInUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        messages.add(new Message(text, loggedInUserName,LocalDateTime.now()));
    }

    public Message getMessage(long id) {
        for (Message message : messages) {
            if (message.getId() == id) {
                return message;
            }
        }
        return null;
    }


    public List<Message> messageFilter(Integer limit, String orderBy, boolean ascending) {

        List<Message> list;
        if (limit == 100) limit = messages.size();
        Comparator<Message> comparator = getMesageComparator(orderBy);

        return list = messages.stream()
                .sorted(comparator)
                .limit(limit)
                .sorted(ascending ? comparator : comparator.reversed())
                .collect(Collectors.toList());
    }

    private Comparator<Message> getMesageComparator(String by) {
        switch (by) {
            case "author":
                return Comparator.comparing(Message::getAuthor);
            case "text":
                return Comparator.comparing(Message::getText);
            case "id":
                return Comparator.comparing(Message::getId);
            default:
                return Comparator.comparing(Message::getDateTime);
        }
    }

    private List<Message> getMessageList() {
        return messages;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteMessage(int id) {
        Message messageToDelete = getMessage(id);
        messageToDelete.setDeleted(true);
    }
}