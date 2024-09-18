import React, { useState } from 'react';

const TaskForm = () => {
    const [title, setTitle] = useState('');
    const [description, setDescription] = useState('');
    const [status, setStatus] = useState('not_started');

    const handleSubmit = async (event) => {
        event.preventDefault();

        const task = {
            title,
            description,
            status
        };

        await addTask(task);
    };

    const addTask = async (task) => {
        try {
            const response = await fetch('/api/tasks', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(task)
            });

            if (!response.ok) {
                throw new Error('Network response was not ok');
            }

            const newTask = await response.json();
            console.log('Task added:', newTask);
        } catch (error) {
            console.error('There was a problem with the fetch operation:', error);
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <label>
                Title:
                <input
                    type="text"
                    value={title}
                    onChange={(e) => setTitle(e.target.value)}
                />
            </label>
            <br />
            <label>
                Description:
                <input
                    type="text"
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                />
            </label>
            <br />
            <label>
                Status:
                <select
                    value={status}
                    onChange={(e) => setStatus(e.target.value)}
                >
                    <option value="not_started">Not Started</option>
                    <option value="in_progress">In Progress</option>
                    <option value="completed">Completed</option>
                </select>
            </label>
            <br />
            <button type="submit">Add Task</button>
        </form>
    );
};

export default TaskForm;
