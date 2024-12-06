import { useMutation } from '@tanstack/react-query';
import { notiError, notiSuccess } from 'app/shared/notifications';
import { updateQuizzes } from 'app/pages/admin/Quizz/QuizzList/apis';
import { IQuizz } from 'app/shared/model/quizz.model';

interface IMutateCallback {
  onSuccess: () => void;
  onFailed: () => void;
}

export const useMutateQuizz = (params?: IMutateCallback) => {
  const updateMutation = useMutation({
    mutationFn: updateQuizzes,
    onSuccess() {
      notiSuccess({ message: 'Cập nhật câu hỏi thành công' });
      params && params.onSuccess();
    },
    onError(error) {
      console.error('Error posting data:', error);
      notiError({ message: 'Cập nhật câu hỏi thất bại!' });
      params && params.onFailed();
    },
  });

  const onUpdate = (request: IQuizz) => {
    updateMutation.mutate(request);
  };

  return {
    onUpdate,
    updating: updateMutation.isPending,
  };
};
