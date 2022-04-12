package br.ce.wcaquino.consumer.tasks.pact;

import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit.PactProviderRule;
import au.com.dius.pact.consumer.junit.PactVerification;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import br.ce.wcaquino.tasks.model.Task;
import br.ce.wcaquino.tasks.service.TasksConsumer;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import javax.swing.*;
import java.io.Console;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TasksConsumerContractTest {

    @Rule
    public PactProviderRule mockProvider = new PactProviderRule("Tasks", this);
//    public PactProviderRule mockProvider = new PactProviderRule("Tasks", "localhost", 8080, this);

    @Pact(consumer = "BasicConsumer")
    public RequestResponsePact createPact(PactDslWithProvider builder){
        return builder
                .given("There is a task with id = 1")
                .uponReceiving("Retrive Task #1")
                    .path("/todo/1")
                    .method("GET")
                .willRespondWith()
                    .status(200)
                    .body("{\"id\": 1,\"task\":\"Task from pact\",\"dueDate\":\"2020-01-01\"}")
                .toPact();
    }

    @Test
    @PactVerification
    public void test() throws IOException {
        //Arrange
        TasksConsumer consumer = new TasksConsumer(mockProvider.getUrl());
        System.out.println("teste leo  " + mockProvider.getUrl());

        //Act
        Task task = consumer.getTask(1L);

        //Assert
        assertThat(task.getId(), is(1L));
        assertThat(task.getTask(), is("Task from pact"));
    }

}

