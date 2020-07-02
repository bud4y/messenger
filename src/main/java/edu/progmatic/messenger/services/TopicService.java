package edu.progmatic.messenger.services;

import edu.progmatic.messenger.modell.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class TopicService {

    @PersistenceContext
    EntityManager em;


    @Autowired
    public TopicService() {
    }


    @Transactional
    public void creatingTopic(Topic topic) {
        em.persist(topic);
    }

    @Transactional
    public List listAllTopics() {
        return  em.createQuery("SELECT c from Topic c").getResultList();

    }
}
