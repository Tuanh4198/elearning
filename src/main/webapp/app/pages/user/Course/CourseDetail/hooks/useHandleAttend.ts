import axios from 'axios';
import { useMutation } from '@tanstack/react-query';
import { notiError, notiSuccess } from 'app/shared/notifications';

export const useHandleAttend = ({ courseId, onSuccessFn }: { courseId?: number; onSuccessFn?: () => void }) => {
  const attendCourse = async () => {
    const response = await axios.put(`/api/course-employees/${courseId}/attend`);
    return response.data;
  };

  const mutation = useMutation({
    mutationFn: attendCourse,
    onSuccess() {
      notiSuccess({ message: 'Điểm danh thành công' });
      onSuccessFn && onSuccessFn();
    },
    onError(error) {
      console.error('Error posting data:', error);
      notiError({ message: 'Điểm danh thất bại!' });
    },
  });

  const onAttend = () => {
    if (courseId) {
      mutation.mutate();
    }
  };

  return {
    onAttend,
    attending: mutation.isPending,
  };
};
