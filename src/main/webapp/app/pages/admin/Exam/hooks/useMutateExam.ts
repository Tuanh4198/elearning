import { useMutation } from '@tanstack/react-query';
import { notiError, notiSuccess } from 'app/shared/notifications';
import { addExam, deleteExam, updateExam } from 'app/pages/admin/Exam/apis';
import { IExam } from 'app/shared/model/exam.model';

interface IMutateCallback {
  onSuccess: () => void;
  onFailed: () => void;
}

export const useMutateExam = (params: IMutateCallback) => {
  const addMutation = useMutation({
    mutationFn: addExam,
    onSuccess() {
      notiSuccess({ message: 'Tạo bài thi thành công' });
      params.onSuccess();
    },
    onError(error) {
      console.error('Error posting data:', error);
      notiError({ message: 'Tạo bài thi thất bại!' });
      params.onFailed();
    },
  });

  const onAdd = (request: IExam) => {
    addMutation.mutate(request);
  };

  const updateMutation = useMutation({
    mutationFn: updateExam,
    onSuccess() {
      notiSuccess({ message: 'Cập nhật bài thi thành công' });
      params.onSuccess();
    },
    onError(error) {
      console.error('Error posting data:', error);
      notiError({ message: 'Cập nhật bài thi thất bại!' });
      params.onFailed();
    },
  });

  const onUpdate = (request: IExam) => {
    updateMutation.mutate(request);
  };

  const deleteMutation = useMutation({
    mutationFn: deleteExam,
    onSuccess() {
      notiSuccess({ message: 'Xóa bài thi thành công' });
      params.onSuccess();
    },
    onError(error) {
      console.error('Error posting data:', error);
      notiError({ message: 'Xóa bài thi thất bại!' });
      params.onFailed();
    },
  });

  const onDelete = (id?: number) => {
    deleteMutation.mutate(id);
  };

  return {
    onAdd,
    adding: addMutation.isPending,
    onUpdate,
    updating: updateMutation.isPending,
    onDelete,
    deleting: deleteMutation.isPending,
  };
};
