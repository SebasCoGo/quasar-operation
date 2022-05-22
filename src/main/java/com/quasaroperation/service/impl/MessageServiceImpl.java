package com.quasaroperation.service.impl;

import com.quasaroperation.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private Environment environment;

    /**
     * @see MessageService#getMessage(List)
     */
    @Override
    public String getMessage(List<List<String>> messages) {
        String messageError = null;
        try {
            if (!messages.stream().allMatch(m -> m.size() == messages.stream().findFirst().get().size())) {
                messageError = environment.getProperty("exception.different-size.error");
                throw new Exception();
            }
            List<String> phrase = messages.stream().findFirst().get();
            messages.forEach(message -> {
                for (int i = 0; i < phrase.size(); i++) {
                    if (!"".equals(message.get(i))) {
                        phrase.set(i, message.get(i));
                    }
                }
            });
            return phrase.toString().replace("[", "").replace("]", "").replace(",", "");

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, messageError.equals(null) ? environment.getProperty("exception.message.error") : messageError);
        }
    }
}
