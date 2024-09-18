import React, { useState, useEffect } from 'react';
import axios from 'axios';

const TaskTable = () => {
  const [tasks, setTasks] = useState([]);

  useEffect(() => {
    const fetchTasks = async () => {
      try {
        const response = await axios.get('http://localhost:8080/tasks');
        setTasks(response.data);
      } catch (error) {
        console.error('Erro ao buscar tarefas:', error);
      }
    };

    fetchTasks();
  }, []);

  const updateStatus = async (taskId, newStatus) => {
    try {
      await axios.patch(`http://localhost:8080/tasks/${taskId}`, { status: newStatus });
      setTasks(tasks.map(task => 
        task.id === taskId ? { ...task, status: newStatus } : task
      ));
    } catch (error) {
      console.error('Erro ao atualizar status:', error);
    }
  };

  return (
    <div>
      <h2>Lista de Tarefas</h2>
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Título</th>
            <th>Descrição</th>
            <th>Status</th>
            <th>Ações</th>
          </tr>
        </thead>
        <tbody>
          {tasks.map(task => (
            <tr key={task.id}>
              <td>{task.id}</td>
              <td>{task.title}</td>
              <td>{task.description}</td>
              <td>
                <select
                  value={task.status}
                  onChange={(e) => updateStatus(task.id, e.target.value)}
                >
                  <option value="não iniciado">Não iniciado</option>
                  <option value="em andamento">Em andamento</option>
                  <option value="concluído">Concluído</option>
                </select>
              </td>
              <td>
                <button onClick={() => updateStatus(task.id, 'concluído')}>
                  Marcar como Concluído
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default TaskTable;
