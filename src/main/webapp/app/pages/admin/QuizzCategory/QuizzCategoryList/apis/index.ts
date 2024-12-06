import axios from 'axios';

export interface ICategoryRequest {
  id?: number;
  title?: string;
}

export const addCategories = async (request: ICategoryRequest) => {
  const response = await axios.post(`/api/quizz-categories`, request);
  return response.data;
};

export const updateCategory = async (request: ICategoryRequest) => {
  const response = await axios.put(`/api/quizz-categories/${request.id}`, request);
  return response.data;
};

export const deleteCategory = async (id: ICategoryRequest['id']) => {
  const response = await axios.delete(`/api/quizz-categories/${id}`);
  return response.data;
};
