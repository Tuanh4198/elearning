import { useMutation } from '@tanstack/react-query';
import { notiError, notiSuccess } from 'app/shared/notifications';
import { deleteQuizzes } from 'app/pages/admin/Quizz/QuizzList/apis';

interface IMutateCallback {
  onSuccess: () => void;
  onFailed: () => void;
}

export const useMutateQuizzes = (params: IMutateCallback) => {
  const deleteMutation = useMutation({
    mutationFn: deleteQuizzes,
    onSuccess() {
      notiSuccess({ message: 'Xóa câu hỏi thành công' });
      params.onSuccess();
    },
    onError(error) {
      console.error('Error posting data:', error);
      notiError({ message: 'Xóa câu hỏi thất bại!' });
      params.onFailed();
    },
  });

  const onDelete = (id?: number) => {
    deleteMutation.mutate(id);
  };

  return {
    onDelete,
    deleting: deleteMutation.isPending,
  };
};
