package com.example.todo.service;

import com.example.todo.model.Todo;

import java.util.List;

public interface TodoService {
    List<Todo> getAllTodos();
    Todo getTodoById(Integer id);
    Todo createTodo(Todo todo);
    Todo updateTodo(Integer id, Todo todo);
    void deleteTodo(Integer id);
}
