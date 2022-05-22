package com.quasaroperation.service;

import com.quasaroperation.service.impl.MessageServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.env.Environment;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MessageServiceTest {

    @InjectMocks
    private MessageServiceImpl messageService;

    @Mock
    private Environment environment;

    @Test
    public void getMessageSuccessTest() {
        List<List<String>> messages = Arrays.asList(
                Arrays.asList("este", "", "", "mensaje", ""),
                Arrays.asList("", "es", "", "", "secreto"),
                Arrays.asList("este", "", "un", "", ""));
        Assert.assertEquals("este es un mensaje secreto", messageService.getMessage(messages));
    }

    @Test(expected = ResponseStatusException.class)
    public void getMessageSizeErrorTest() {
        List<List<String>> messages = Arrays.asList(
                Arrays.asList("este", "", ""),
                Arrays.asList("", "es", "", "", "secreto"),
                Arrays.asList("este", "", "un", "", ""));
        when(environment.getProperty("exception.different-size.error")).thenReturn("Error message");
        messageService.getMessage(messages);
    }

}
