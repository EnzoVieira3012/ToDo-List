import axios from 'axios';

const apiUrl = '/api/tasks';

export const fetchTasks = () => axios.get(apiUrl);
export const createTask = (task) => axios.post(apiUrl, task);
export const deleteTask = (id) => axios.delete(`${apiUrl}/${id}`);
