import React, { useState, useEffect } from 'react';

const TaskList = () => {
  const [tasks, setTasks] = useState([]);

  const fetchTasks = async () => {
    try {
      const response = await fetch('/api/tasks');
      if (response.ok) {
        const tasks = await response.json();
        setTasks(tasks);
      } else {
        console.error('Erro ao carregar tarefas:', response.statusText);
      }
    } catch (error) {
      console.error('Erro de rede:', error);
    }
  };

  useEffect(() => {
    fetchTasks();
  }, []);

  return (
    <ul>
      {tasks.map(task => (
        <li key={task.id}>
          <h3>{task.title}</h3>
          <p>{task.description}</p>
          <span>Status: {task.status}</span>
        </li>
      ))}
    </ul>
  );
};

export default TaskList;
