package com.sarafan.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.sarafan.domain.Message;
import com.sarafan.domain.Views;
import com.sarafan.exception.NoMessageException;
import com.sarafan.repo.MessageRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("message")
public class MessageController {

    private final MessageRepo messageRepo;

    public MessageController(MessageRepo messageRepo) {
        this.messageRepo = messageRepo;
    }

    @GetMapping
    @JsonView(Views.IdName.class)
    public List<Message> allMessage(){
        return messageRepo.findAll();
    }

    // По переменной id, которая приходит на url, Spring сам находит сообщение
    @GetMapping("{id}")
    @JsonView(Views.FullMessage.class)
    public Message getMessage(@PathVariable("id") Message message){
        return message;
    }

    @PostMapping
    public Message addMessage(@RequestBody Message message){
        message.setCreationDate(LocalDateTime.now());
        return messageRepo.save(message);
    }

    @PutMapping("{id}")
    public Message updateMessage(
            @PathVariable("id") Message messageFromDB,
            @RequestBody Message message) {

        //Spring копирует все поля пришедшего JSONа в сообщение, полученное из БД, кроме id
        BeanUtils.copyProperties(message, messageFromDB, "id");

        return messageRepo.save(messageFromDB);
    }

    @DeleteMapping("{id}")
    public void deleteMessage(@PathVariable("id") Message message){
        messageRepo.delete(message);
    }


}
