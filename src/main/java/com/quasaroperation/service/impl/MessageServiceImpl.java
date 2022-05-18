package com.quasaroperation.service.impl;

import com.quasaroperation.service.MessageService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Override
    public String getMessage(List<List<String>> messages) {
        List<String> phrase = messages.get(0);
        for (List<String> message : messages) {
            for (int i = 0; i < phrase.size(); i++) {
                if (!"".equals(message.get(i))) {
                    phrase.set(i, message.get(i));
                }
            }
        }
        return phrase.toString().replace("[", "").replace("]", "").replace(",", "");
    }

}
