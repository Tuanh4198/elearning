import { IQuizz } from 'app/shared/model/quizz.model';
import axios from 'axios';

export const addQuizzes = async (request: IQuizz) => {
  const response = await axios.post(`/api/quizzes`, request);
  return response.data;
};

export const updateQuizzes = async (request: IQuizz) => {
  const response = await axios.put(`/api/quizzes/${request.id}`, request);
  return response.data;
};

export const deleteQuizzes = async (id: IQuizz['id']) => {
  const response = await axios.delete(`/api/quizzes/${id}`);
  return response.data;
};
