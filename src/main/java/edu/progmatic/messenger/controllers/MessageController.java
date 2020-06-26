package edu.progmatic.messenger.controllers;


import edu.progmatic.messenger.modell.Message;
import edu.progmatic.messenger.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class MessageController {
    private final MessageService messageService;



    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @RequestMapping(value = "/messages", method = RequestMethod.GET)
    public String messages(

            @RequestParam(value = "sortBy", defaultValue = "dateTime") String sortBy,
            @RequestParam(value = "limit", defaultValue = "100") int limit,
            @RequestParam(value = "order", required = false) boolean ascending,
            Model model) {
        List<Message> messageList = messageService.messageFilter(limit, sortBy, ascending);
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
        model.addAttribute("message", new Message(null, null, LocalDateTime.now()));
        return "create";
    }


    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createMessage( @ModelAttribute("message") Message message, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "/create";
        }

        messageService.createMessage(message.getText());

        return "redirect:/messages";
    }

    @RequestMapping(value = "/messages/delete/{messageId}", method = RequestMethod.POST)
    public String deleteMessage(@PathVariable("messageId") int messageId) {
        messageService.deleteMessage(messageId);
        return "redirect:/messages";
    }


}