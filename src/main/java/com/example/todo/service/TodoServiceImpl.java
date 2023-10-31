package com.example.todo.service;

import com.example.todo.mapper.TodoMapper;
import com.example.todo.model.Todo;
import com.example.todo.model.TodoExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoServiceImpl implements TodoService {

    private final TodoMapper todoMapper;

    @Autowired
    public TodoServiceImpl(TodoMapper todoMapper) {
        this.todoMapper = todoMapper;
    }

    @Override
    public List<Todo> getAllTodos() {
        return todoMapper.selectByExample(new TodoExample());
    }

    @Override
    public Todo getTodoById(Integer id) {
        return todoMapper.selectByPrimaryKey(id);
    }

    @Override
    public Todo createTodo(Todo todo) {
        todoMapper.insert(todo);
        return todo;
    }

    @Override
    public Todo updateTodo(Integer id, Todo todo) {
        todo.setId(id);
        todoMapper.updateByPrimaryKey(todo);
        return todo;
    }

    @Override
    public void deleteTodo(Integer id) {
        todoMapper.deleteByPrimaryKey(id);
    }
}
