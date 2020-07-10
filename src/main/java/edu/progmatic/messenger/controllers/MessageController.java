package edu.progmatic.messenger.controllers;


import edu.progmatic.messenger.modell.Message;
import edu.progmatic.messenger.modell.Topic;
import edu.progmatic.messenger.services.MessageService;
import edu.progmatic.messenger.services.TopicService;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

            @RequestParam(value = "author", defaultValue = "") String author,
            @RequestParam(value = "text", defaultValue = "") String text,
            @RequestParam(value = "topic", defaultValue = "") String topic,
            @RequestParam(value = "startDate", defaultValue = "") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime startDate,
            @RequestParam(value = "endDate", defaultValue = "") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime endDate,
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(value = "limit", defaultValue = "100") @Valid @Min(value = 0, message = "Please select positive numbers Only") Integer limit,
            @RequestParam(value = "order", required = false, defaultValue = "true") boolean ascending,
            @RequestParam(value = "showDeleted", defaultValue = "false") boolean showDeleted,

            Model model) {

        List<Message> messageList = messageService.messageFilterDsl(limit, sortBy, ascending, author, text, startDate, endDate, showDeleted, topic);
//        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString().contains("ROLE_ADMIN")) {
//            model.addAttribute("messageList", messageList);
//        } else {
//            List<Message> hiddenDeletedMessageList = messageList.stream().filter(message -> !message.isDeleted()).collect(Collectors.toList());
//            model.addAttribute("messageList", hiddenDeletedMessageList);
//        }
        model.addAttribute("messageList", messageList);
        return "messages";
    }

//    @PostMapping("/messages/{id}")
//    public String showOneMessage(
//            @PathVariable("id") @NotNull long id,
//            Model model) {
//
//        Message message = messageService.getMessage(id);
//
//        model.addAttribute("message", message);
//        return "messages";
//    }

    @RequestMapping(path = "/create", method = RequestMethod.GET)
    public String getCreateMessage(Model model) {
        List<Topic> topicList = topicService.listAllTopics();
        model.addAttribute("topics", topicList);
        model.addAttribute("topic", new Topic());
        model.addAttribute("message", new Message(null, null, LocalDateTime.now(), false, null));
        return "create";
    }


    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createMessage(@ModelAttribute("message") Message message, BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()) {
            return "/create";
        }
//        InputStream in = getClass()
//                .getResourceAsStream("/*.jpg ,/*.png");
        messageService.createMessage(message);
//        IOUtils.toByteArray(in);
        return "redirect:/messages";
    }

    @GetMapping(value = {"/delete/{ID}"})
    public String delete(@PathVariable Long ID) {
        messageService.deleteMessage(ID);
        return "redirect:/messages";
    }

    @GetMapping(value = {"/recovery/{ID}"})
    public String recovery(@PathVariable Long ID) {
        messageService.recoveryMessage(ID);
        return "redirect:/messages";
    }

}