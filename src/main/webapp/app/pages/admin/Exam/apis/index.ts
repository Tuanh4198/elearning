import axios from 'axios';
import { IExam } from 'app/shared/model/exam.model';

export const addExam = async (request: IExam) => {
  const response = await axios.post(`/api/exams`, request);
  return response.data;
};

export const updateExam = async (request: IExam) => {
  const response = await axios.put(`/api/exams/${request.id}`, request);
  return response.data;
};

export const deleteExam = async (id: IExam['id']) => {
  const response = await axios.delete(`/api/exams/${id}`);
  return response.data;
};
