package com.example.todo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import com.example.todo.mapper.TodoMapper;
import com.example.todo.model.Todo;
import com.example.todo.model.TodoExample;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class TodoServiceUnitTest {

    @Mock
    private TodoMapper todoMapper;

    @InjectMocks
    private TodoServiceImpl todoService;

    private Todo todo;
    private List<Todo> todoList;

    @BeforeEach
    public void setUp() {
        todo = new Todo();
        todo.setId(1);
        todo.setTitle("Test Todo");
        todo.setDescription("Description");
        todo.setDueDate(new Date());
        todoList = Arrays.asList(todo);
    }

    @Test
    public void getAllTodos_ShouldReturnTodoList() {
        when(todoMapper.selectByExample(any(TodoExample.class))).thenReturn(todoList);

        List<Todo> result = todoService.getAllTodos();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(todoList.size(), result.size());
        assertEquals(todoList.get(0).getTitle(), result.get(0).getTitle());
    }

    @Test
    public void getTodoById_ShouldReturnTodo() {
        given(todoMapper.selectByPrimaryKey(todo.getId())).willReturn(todo);

        Todo result = todoService.getTodoById(todo.getId());

        assertNotNull(result);
        assertEquals(todo.getId(), result.getId());
        assertEquals(todo.getTitle(), result.getTitle());
    }

    @Test
    public void createTodo_ShouldReturnNewTodo() {
        doAnswer(invocation -> {
            Todo param = invocation.getArgument(0);
            param.setId(1);
            return null;
        }).when(todoMapper).insert(any(Todo.class));

        Todo result = todoService.createTodo(todo);

        assertNotNull(result);
        assertEquals(todo.getId(), result.getId());
    }

    @Test
    public void updateTodo_ShouldReturnUpdatedTodo() {
        when(todoMapper.updateByPrimaryKey(todo)).thenReturn(1);

        todo.setTitle("Test Todo1");
        todo.setDescription("Description1");
        todo.setDueDate(new Date());
        Todo result = todoService.updateTodo(todo.getId(), todo);

        assertNotNull(result);
        assertEquals(todo.getTitle(), result.getTitle());
    }

    @Test
    public void deleteTodo_ShouldDeleteTodo() {
        when(todoMapper.deleteByPrimaryKey(todo.getId())).thenReturn(1);

        assertDoesNotThrow(() -> todoService.deleteTodo(todo.getId()));
        verify(todoMapper).deleteByPrimaryKey(todo.getId());
    }
}
