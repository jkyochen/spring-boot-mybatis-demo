package com.example.todo.controller;

import com.example.todo.model.Todo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // TODO remove order
public class TodoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private Todo todo;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        todo = new Todo();
        todo.setId(1);
        todo.setTitle("Test Todo");
        todo.setDescription("Description");
        todo.setDueDate(new Date());
        todo.setStatus("PENDING");
        objectMapper = new ObjectMapper();
    }

    @Test
    @Order(1)
    public void createTodo_ShouldReturnCreatedTodo() throws Exception {
        String todoJson = objectMapper.writeValueAsString(todo);

        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(todoJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is("Test Todo")));
    }

    @Test
    @Order(2)
    public void getAllTodos_ShouldReturnTodoList() throws Exception {
        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].title", is("Test Todo")));
    }

    @Test
    @Order(3)
    public void getTodoById_ShouldReturnTodo() throws Exception {
        mockMvc.perform(get("/api/todos/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is("Test Todo")));
    }

    @Test
    @Order(4)
    public void updateTodo_ShouldReturnUpdatedTodo() throws Exception {
        String todoJson = objectMapper.writeValueAsString(todo);

        mockMvc.perform(put("/api/todos/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(todoJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is("Test Todo")));
    }

    @Test
    @Order(5)
    public void deleteTodo_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/todos/{id}", 1))
                .andExpect(status().isNoContent());
    }
}
