package com.quasaroperation.service.impl;

import com.quasaroperation.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private Environment environment;

    @Override
    public String getMessage(List<List<String>> messages) {
        if (!messages.stream().allMatch(message -> message.size() == messages.get(0).size())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, environment.getProperty("exception.different-size.error"));
        }
        try {
            List<String> phrase = messages.get(0);
            for (List<String> message : messages) {
                for (int i = 0; i < phrase.size(); i++) {
                    if (!"".equals(message.get(i))) {
                        phrase.set(i, message.get(i));
                    }
                }
            }
            return phrase.toString().replace("[", "").replace("]", "").replace(",", "");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, environment.getProperty("exception.message.error"));
        }
    }
}
