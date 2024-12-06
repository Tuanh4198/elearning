import { useMutation } from '@tanstack/react-query';
import { notiError, notiSuccess } from 'app/shared/notifications';
import { addQuizzes } from 'app/pages/admin/Quizz/QuizzList/apis';
import { IQuizz } from 'app/shared/model/quizz.model';

interface IMutateCallback {
  onSuccess: () => void;
  onFailed: () => void;
}

export const useMutateQuizz = (params: IMutateCallback) => {
  const addMutation = useMutation({
    mutationFn: addQuizzes,
    onSuccess() {
      notiSuccess({ message: 'Tạo câu hỏi thành công' });
      params.onSuccess();
    },
    onError(error) {
      console.error('Error posting data:', error);
      notiError({ message: 'Tạo câu hỏi thất bại!' });
      params.onFailed();
    },
  });

  const onAdd = (request: IQuizz) => {
    addMutation.mutate(request);
  };

  return {
    onAdd,
    adding: addMutation.isPending,
  };
};
