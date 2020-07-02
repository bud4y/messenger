package edu.progmatic.messenger.controllers;

import edu.progmatic.messenger.modell.Message;
import edu.progmatic.messenger.services.MessageService;
import edu.progmatic.messenger.user.UserInfo;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
//
//@ExtendWith(SpringExtension.class)
//@WebMvcTest(controllers = MessageController.class)
//class MessageControllerTest {
//
//    @Autowired
//    MockMvc mockMvc;
//    @MockBean
//    MessageService messageService;
//
//
//    @Test
//    void messages() {
//    }
//
//    @Test
//    @WithUserDetails("user")
//    void showOneMessage() throws Exception {
//        LocalDateTime dateTime = LocalDateTime.now();
//        Mockito.when(messageService.getMessage(0)).thenReturn(new Message("Üzenet", "User",dateTime));
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/message/0"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.view().name("oneMessage"))
//                .andExpect(MockMvcResultMatchers.model().attributeExists("message"))
//                .andExpect(MockMvcResultMatchers.model().attribute("message", Matchers.allOf(
//                        Matchers.hasProperty("author", Matchers.is("User")),
//                        Matchers.hasProperty("text", Matchers.is("Üzenet")),
//                        Matchers.hasProperty("dateTime", Matchers.is(dateTime))
//                )));
//
//        Mockito.verify(messageService, Mockito.times(1)).getMessage(0);
//        Mockito.verifyNoMoreInteractions(messageService);
//    }
//
//
//    @Test
//    void getCreateMessage() {
//    }
//
//    @Test
//    void createMessage() {
//    }
//
//    @Test
//    @WithUserDetails("admin")
//    void deleteMessage()  throws Exception {
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/messages/delete/0"))
//                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
//                .andExpect(MockMvcResultMatchers.redirectedUrl("/messages"));
//
//        Mockito.verify(messageService, Mockito.times(1)).deleteMessage(0);
//        Mockito.verifyNoMoreInteractions(messageService);
//    }
//}