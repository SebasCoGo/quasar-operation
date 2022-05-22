package com.quasaroperation.service;

import java.util.List;

public interface MessageService {

    /**
     * Method for decrypting the sender's message
     *
     * @param messages
     * @return The decrypted message
     */
    String getMessage(List<List<String>> messages);

}
