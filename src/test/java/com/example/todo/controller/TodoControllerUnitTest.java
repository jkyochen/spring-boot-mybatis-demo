package com.example.todo.controller;

import com.example.todo.model.Todo;
import com.example.todo.service.TodoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TodoControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoService todoService;

    private Todo todo;

    @BeforeEach
    public void setUp() {
        todo = new Todo();
        todo.setId(1);
        todo.setTitle("Test Todo");
        todo.setDescription("Description");
        todo.setDueDate(new Date());
    }

    @Test
    public void getAllTodos_ShouldReturnTodoList() throws Exception {
        given(todoService.getAllTodos()).willReturn(Collections.singletonList(todo));

        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is(todo.getTitle())));
    }

    @Test
    public void getTodoById_ShouldReturnTodo() throws Exception {
        given(todoService.getTodoById(todo.getId())).willReturn(todo);

        mockMvc.perform(get("/api/todos/{id}", todo.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is(todo.getTitle())));
    }

    @Test
    public void createTodo_ShouldReturnCreatedTodo() throws Exception {
        given(todoService.createTodo(any())).willReturn(todo);

        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(todo)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is(todo.getTitle())));
    }

    @Test
    public void updateTodo_ShouldReturnUpdatedTodo() throws Exception {
        given(todoService.updateTodo(eq(todo.getId()), any())).willReturn(todo);

        mockMvc.perform(put("/api/todos/{id}", todo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(todo)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is(todo.getTitle())));
    }

    @Test
    public void deleteTodo_ShouldReturnNoContent() throws Exception {
        willDoNothing().given(todoService).deleteTodo(todo.getId());

        mockMvc.perform(delete("/api/todos/{id}", todo.getId()))
                .andExpect(status().isNoContent());
    }
}
