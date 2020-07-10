package edu.progmatic.messenger.services;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.jpa.impl.JPAQueryFactory;
import edu.progmatic.messenger.modell.Message;
import edu.progmatic.messenger.modell.QMessage;
import edu.progmatic.messenger.modell.QTopic;
import edu.progmatic.messenger.modell.Topic;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Message getMessage(Long id) {
//  try {
//            Thread.sleep(10000);
//        }catch (Exception ignored){}
        return em.find(Message.class, id);

    }


    public List<Message> messageFilter(Integer limit, String sortBy, boolean ascending, String author, String text, LocalDateTime startDate, LocalDateTime endDate, boolean showDeleted, String topic) {

        List<Message> messages = getMessageList();
        if (limit == 100) limit = messages.size();
        Comparator<Message> comparator = getMesageComparator(sortBy);

        return messages.stream()
                .filter(m -> author.isEmpty() || m.getAuthor().equalsIgnoreCase(author))
                .filter(m -> m.getText().contains(text))
                .filter(m -> topic.isEmpty() || m.getMessageTopic().getTopicName().equalsIgnoreCase(topic))
                .filter(m -> startDate == null || m.getDateTime().isAfter(startDate))
                .filter(m -> endDate == null || m.getDateTime().isBefore(endDate))
                .filter(m -> showDeleted || !m.isDeleted())
                .sorted(comparator)
                .limit(limit)
                .sorted(ascending ? comparator : comparator.reversed())
                .collect(Collectors.toList());
    }

    private Comparator<Message> getMesageComparator(String sortBy) {
        switch (sortBy) {
            case "author":
                return Comparator.comparing(Message::getAuthor);
            case "text":
                return Comparator.comparing(Message::getText);
            case "dateTime":
                return Comparator.comparing(Message::getDateTime);
            default:
                return Comparator.comparing(Message::getId);
        }
    }

    public List<Message> getMessageList() {
        return em.createQuery("SELECT m FROM Message m", Message.class).getResultList();
    }

//    @PreAuthorize("hasRole('ADMIN')")

    @Transactional
    public void deleteMessage(Long id) {
        Message messageToDelete = getMessage(id);
        messageToDelete.setDeleted(true);
    }

    @Transactional
    public void recoveryMessage(Long id) {
        Message messageToDelete = getMessage(id);
        messageToDelete.setDeleted(false);
    }

    public List<Message> messageFilterDsl(Integer limit, String orderBy, boolean ascending, String author, String text, LocalDateTime startDate, LocalDateTime endDate, boolean showDeleted, String topic) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        BooleanBuilder whereCondition = new BooleanBuilder();

        QMessage messages = QMessage.message;
        QTopic qTopic = QTopic.topic;

        if (!StringUtils.isEmpty(author)) {
            whereCondition.and(messages.author.eq(author));
        }
        if (!StringUtils.isEmpty(text)) {
            whereCondition.and(messages.text.like("%" + text + "%"));
        }
        if (!StringUtils.isEmpty(startDate)) {
            whereCondition.and(messages.dateTime.after(startDate));
        }
        if (!StringUtils.isEmpty(endDate)) {
            whereCondition.and(messages.dateTime.before(endDate));
        }

        if (!StringUtils.isEmpty(topic)) {
            whereCondition.and(qTopic.topicName.eq(topic));
        }

        return queryFactory.selectFrom(messages)
                .where(whereCondition)
//                .where(messages.dateTime.between(startDate, endDate))
                .orderBy(orderSpecifier(ascending, orderSelect(orderBy)))
                .limit(limit)
                .fetch();

    }

    public List<Topic> getTopics() {
        return em.createQuery("SELECT t FROM Topic t", Topic.class).getResultList();
    }

    private ComparableExpressionBase orderSelect(String orderBy) {

        switch (orderBy) {
            case "author" :
                return QMessage.message.author;
            case "text" :
                return QMessage.message.text;
            case "dateTime":
                return QMessage.message.dateTime;
            default:
                return QMessage.message.id;
        }
    }

    private OrderSpecifier<?> orderSpecifier(boolean ascending, ComparableExpressionBase expressionBase) {
        return ascending ? expressionBase.asc() : expressionBase.desc() ;
    }
}
