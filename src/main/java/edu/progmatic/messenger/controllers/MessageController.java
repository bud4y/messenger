package edu.progmatic.messenger.controllers;


import edu.progmatic.messenger.modell.Message;
import edu.progmatic.messenger.modell.Topic;
import edu.progmatic.messenger.services.MessageService;
import edu.progmatic.messenger.services.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class MessageController {
    private final MessageService messageService;
    private final TopicService topicService;


    @Autowired
    public MessageController(MessageService messageService, TopicService topicService) {
        this.messageService = messageService;
        this.topicService = topicService;
    }



    @RequestMapping(value = "/messages", method = RequestMethod.GET)
    public String messages(
//
//            @RequestParam(value = "sortBy", defaultValue = "dateTime") String sortBy,
//            @RequestParam(value = "limit", defaultValue = "100") int limit,
//            @RequestParam(value = "order", required = false) boolean ascending,
//            Model model) {

            @RequestParam(value = "author", defaultValue = "") String author,
            @RequestParam(value = "text", defaultValue = "") String text,
            @RequestParam(value = "startDate", defaultValue = "") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime startDate,
            @RequestParam(value = "endDate", defaultValue = "") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime endDate,
            @RequestParam(value = "sortBy", defaultValue = "date") String sortBy,
            @RequestParam(value = "messageCount", defaultValue = "-1") int limit,
            @RequestParam(value = "ascending", defaultValue = "true") boolean ascending,
            @RequestParam(value = "showDeleted", defaultValue = "false") boolean showDeleted,
            Model model) {

        List<Message> messageList = messageService.messageFilter(limit,sortBy, ascending, author,text,startDate,endDate, showDeleted);

        model.addAttribute("messageList", messageList);
        return "messages";
    }

    @GetMapping("/message/{id}")
    public String showOneMessage(
            @PathVariable("id") @NotNull long id,
            Model model) {

        Message message = messageService.getMessage(id);

        model.addAttribute("message", message);
        return "oneMessage";
    }

    @RequestMapping(path = "/create", method = RequestMethod.GET)
    public String getCreateMessage(Model model) {
        List<Topic> topicList = topicService.listAllTopics();
        model.addAttribute("topics", topicList);
        model.addAttribute("message", new Message(null, null, LocalDateTime.now(),false,null));
        return "create";
    }


    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createMessage(@ModelAttribute("message") Message message, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/create";
        }
        messageService.createMessage(message);

        return "redirect:/messages";
    }

    @RequestMapping(value = "/messages/delete/{messageId}", method = RequestMethod.POST)
    public String deleteMessage(@PathVariable("messageId") Long messageId) {
        messageService.deleteMessage(messageId);
        return "redirect:/messages";
    }
}