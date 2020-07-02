package edu.progmatic.messenger.services;

import edu.progmatic.messenger.modell.Message;
import edu.progmatic.messenger.modell.Topic;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class MessageService {
//    public List<Message> messages = new ArrayList<>();
//
//    {
//        messages.add(new Message(" fg sjtrh strh trh r", "Öcsesz", LocalDateTime.now().minusDays(12)));
//        messages.add(new Message(" rggdf gd fg gu ktk z jdtr rh", "Gergő", LocalDateTime.now().minusDays(6)));
//        messages.add(new Message("gsojgssdgls fsdgh fd hf", "Gyulus", LocalDateTime.now().minusDays(5)));
//    }

    @PersistenceContext
    private final EntityManager em;

    public MessageService(EntityManager em) {
        this.em = em;
    }

    @Transactional
    public void createMessage(Message message) {
        String loggedInUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        message.setAuthor(loggedInUserName);
        message.setDateTime(LocalDateTime.now());
        message.setId(null);
        Topic topic = em.createQuery("SELECT t from Topic t where t.topicName =:name", Topic.class)
                .setParameter("name", message.getMessageTopic().getTopicName()).getSingleResult();
        topic.getMessages().add(message);
        message.setMessageTopic(topic);
        em.persist(message);
    }

    public Message getMessage(Long id) {

        return em.find(Message.class, id);
    }


    public List<Message> messageFilter(Integer limit, String orderBy, boolean ascending, String author, String text, LocalDateTime startDate, LocalDateTime endDate,boolean showDeleted) {
//        List<Message> messages = getMessageList();
//
//        if (limit == 100) limit = messages.size();
//        Comparator<Message> comparator = getMesageComparator(orderBy);
//
//        return messages.stream()
//                .sorted(comparator)
//                .limit(limit)
//                .sorted(ascending ? comparator : comparator.reversed())
//                .collect(Collectors.toList());
//    }

        List<Message> messages = getMessageList();
        int numOfMessagesToShow = limit < 0 ? messages.size() : limit;
        Comparator<Message> messageComparator = getMesageComparator(orderBy);

        return messages.stream()
                .filter(m -> author.isEmpty() || m.getAuthor().equalsIgnoreCase(author))
                .filter(m -> m.getText().contains(text))
                .filter(m -> startDate == null || m.getDateTime().isAfter(startDate))
                .filter(m -> endDate == null || m.getDateTime().isBefore(endDate))
                .filter(m -> showDeleted || !m.isDeleted())
                .sorted(ascending ? messageComparator : messageComparator.reversed())
                .limit(Math.min(messages.size(), numOfMessagesToShow))
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

    public List<Message> getMessageList() {
        return em.createQuery("SELECT m FROM Message m", Message.class).getResultList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteMessage(Long id) {
        Message messageToDelete = getMessage(id);
        messageToDelete.setDeleted(true);
    }
}