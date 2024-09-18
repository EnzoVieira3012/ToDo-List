import { fetchTasks, createTask, deleteTask } from './TaskRepository';

export const getTasks = async () => {
  const response = await fetchTasks();
  return response.data;
};

export const addTask = async (task) => {
  await createTask(task);
};

export const removeTask = async (id) => {
  await deleteTask(id);
};
