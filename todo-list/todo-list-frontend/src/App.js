import React, { useState, useEffect } from 'react';
import './App.css';

function App() {
    const [tasks, setTasks] = useState([]);
    const [title, setTitle] = useState('');
    const [description, setDescription] = useState('');
    const [status, setStatus] = useState('não iniciado');

    useEffect(() => {
        fetch('/tasks')
            .then(response => response.json())
            .then(data => setTasks(data));
    }, []);

    const handleSubmit = (event) => {
        event.preventDefault();
        fetch('/tasks', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ title, description, status })
        })
        .then(response => response.json())
        .then(newTask => {
            setTasks([...tasks, newTask]);
            setTitle('');
            setDescription('');
            setStatus('não iniciado');
        });
    };

    const handleDelete = (id) => {
        fetch(`/tasks/${id}`, { method: 'DELETE' })
            .then(() => setTasks(tasks.filter(task => task.id !== id)));
    };

    return (
        <div className="App">
            <div className="form-container">
                <h1>Lista de Tarefas</h1>
                <form onSubmit={handleSubmit}>
                    <input
                        type="text"
                        value={title}
                        onChange={(e) => setTitle(e.target.value)}
                        placeholder="Título"
                        required
                    />
                    <input
                        type="text"
                        value={description}
                        onChange={(e) => setDescription(e.target.value)}
                        placeholder="Descrição"
                        required
                    />
                    <select value={status} onChange={(e) => setStatus(e.target.value)} required>
                        <option value="não iniciado">Não Iniciado</option>
                        <option value="em andamento">Em Andamento</option>
                        <option value="concluído">Concluído</option>
                    </select>
                    <button type="submit">Criar Tarefa</button>
                </form>
            </div>
            <table className="task-table">
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
                            <td>{task.status}</td>
                            <td>
                                <button onClick={() => handleDelete(task.id)}>Excluir</button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}

export default App;
