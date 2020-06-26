package edu.progmatic.messenger.user;

import edu.progmatic.messenger.modell.Message;
import edu.progmatic.messenger.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Scope(
        scopeName = WebApplicationContext.SCOPE_SESSION,
        proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserInfo {
    private String lastAuthor;

    private List<Message> messagesInSession = new ArrayList<>();

    private MessageService messageService;

    @Autowired
    public UserInfo(MessageService messageService) {
        this.messageService = messageService;
    }


    public int getMessageCountInSesson() {
        return messagesInSession.size();
    }


    public String getLastAuthor() {
        return lastAuthor;
    }

    public void setLastAuthor(String lastAuthor) {
        this.lastAuthor = lastAuthor;
    }
}
