package edu.progmatic.messenger.controllers;

import edu.progmatic.messenger.modell.Topic;
import edu.progmatic.messenger.services.TopicService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.List;


@Controller
public class TopicController {

    TopicService topicService;

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @GetMapping(path = "/listOfTopics")
    public String getTopic(Model model) {
        List<Topic> topicList = topicService.listAllTopics();
        model.addAttribute("topics", topicList);
        return "listOfTopics";
    }

    @GetMapping("/newTopic")
    public String getCreateTopic(Model model) {
        model.addAttribute("topic", new Topic());
        return "newTopic";
    }

    @PostMapping("/newTopic")
    public String createTopic(@ModelAttribute Topic topic) {
        topicService.creatingTopic(topic);
        return "redirect:/messages";
    }


}