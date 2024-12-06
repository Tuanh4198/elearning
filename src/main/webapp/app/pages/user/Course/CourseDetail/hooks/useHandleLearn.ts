import axios from 'axios';
import { useMutation } from '@tanstack/react-query';
import { notiError, notiSuccess } from 'app/shared/notifications';
import { formatDateZ } from 'app/shared/util/date-utils';
import { useEffect, useState } from 'react';

export interface ILearnRequest {
  learn_start_at: string;
  learn_finish_at: string;
}
export const useHandleLearn = ({
  courseId,
  onSuccessFn,
  autoPlayCountdown,
}: {
  courseId?: number;
  onSuccessFn?: () => void;
  autoPlayCountdown: boolean;
}) => {
  const [learnStartAt, setLearnStartAt] = useState<string | null>(null);

  useEffect(() => {
    if (!learnStartAt && autoPlayCountdown === true) {
      const now = formatDateZ(new Date());
      setLearnStartAt(now);
    }
  }, [autoPlayCountdown]);

  const learnCourse = async () => {
    // eslint-disable-next-line @typescript-eslint/ban-types
    let request: ILearnRequest | {} = {};
    if (learnStartAt) {
      request = {
        learn_start_at: learnStartAt,
        learn_finish_at: formatDateZ(new Date()),
      };
    }
    const response = await axios.put(`/api/course-employees/${courseId}/learn`, request);
    return response.data;
  };

  const mutation = useMutation({
    mutationFn: learnCourse,
    retry: 3,
    onSuccess() {
      notiSuccess({ message: 'Chúc mừng bạn đã hoàn thành khóa học.' });
      onSuccessFn && onSuccessFn();
    },
    onError(error) {
      console.error('Error posting data:', error);
      notiError({ message: 'Báo cáo thời gian học thất bại, vui lòng liên hệ IT để hỗ trợ' });
    },
  });

  const onLearn = () => {
    if (courseId) {
      mutation.mutate();
    }
  };

  return {
    onLearn,
    onLearning: mutation.isPending,
  };
};
