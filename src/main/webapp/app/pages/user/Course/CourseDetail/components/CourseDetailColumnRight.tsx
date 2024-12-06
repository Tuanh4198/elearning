import React, { Fragment, useMemo } from 'react';
import { Alert, Box, Button, Flex, Image, Loader, LoadingOverlay, Skeleton, Text, ThemeIcon, Tooltip } from '@mantine/core';
import { CourseMetafieldKey, ICourse } from 'app/shared/model/course.model';
import { useCountDownWorkingTime } from 'app/shared/hooks/useCountDownWorkingTime';
import { useHandleAttend } from 'app/pages/user/Course/CourseDetail/hooks/useHandleAttend';
import { useHandleLearn } from 'app/pages/user/Course/CourseDetail/hooks/useHandleLearn';
import { CourseEmployeeMetafieldKey, ICourseEmployee } from 'app/shared/model/course-employee.model';
import { CourseEmployeeStatusEnum } from 'app/shared/model/enumerations/course-employee-status-enum.model';
import { compareDate, formatDateTime } from 'app/shared/util/date-utils';
import { APP_TIME_DATE_FORMAT } from 'app/config/constants';
import { Warning } from '@phosphor-icons/react';
import { useNavigate } from 'react-router-dom';
import { UserRoutes } from 'app/pages/user/routes';
import { useCheckExamCanJoin } from 'app/pages/user/Course/CourseDetail/hooks/useCheckExamCanJoin';
import { useFetchExamEmployeeDetail } from 'app/pages/user/Course/CourseDetail/hooks/useFetchExamEmployeeDetail';
import { useFetchExamResult } from 'app/pages/user/Course/CourseDetail/hooks/useFetchExamResult';
import { ExamMetafieldKey } from 'app/shared/model/exam.model';
import { ExamEmployeeStatusEnum } from 'app/shared/model/enumerations/exam-employee-status-enum.model';

const CountDown = ({ totalTime, autoPlay, onTimeOut }: { totalTime: number; autoPlay: boolean; onTimeOut: () => void }) => {
  const { time, formatTime, setIsStopped } = useCountDownWorkingTime({
    autoPlay,
    workingTime: totalTime,
    pauseOnHidden: true,
    onTimeOut() {
      onTimeOut();
      setIsStopped(true);
    },
  });

  if (time === 0) {
    return (
      <Flex bg="#E6FCF5" gap="20px" py="8px" align="center" justify="center" style={{ borderRadius: '16px' }}>
        <Text color="#6B7280" fs="12px" fw="500">
          Thời gian học:
        </Text>
        <Text color="#12B886" fs="16px" fw="600">
          0:00
        </Text>
      </Flex>
    );
  }

  return (
    <>
      <Flex bg="#FFEECD" gap="20px" py="8px" align="center" justify="center" style={{ borderRadius: '16px' }}>
        <Text color="#6B7280" fs="12px" fw="500">
          Thời gian học:
        </Text>
        <Text color="#1F2A37" fs="16px" fw="600">
          {formatTime()}
        </Text>
      </Flex>
      {autoPlay && (
        <Alert
          variant="light"
          color="red"
          radius={16}
          title={
            <Text fz={22} mt={-5}>
              Lưu ý:
            </Text>
          }
          icon={
            <ThemeIcon color="red">
              <Warning color="white" size={16} />
            </ThemeIcon>
          }
        >
          Nếu bật tab khác, thời gian học sẽ không được tính. <br />
          Nếu thoát thời gian học sẽ được tính lại từ đầu.
        </Alert>
      )}
    </>
  );
};

const BtnJoinExam = ({ examId, requireAttend }: { examId: number; requireAttend: boolean }) => {
  const navigate = useNavigate();

  const { isLoading: isCheckingExamCanJoin, data: checkedExamCanJoin } = useCheckExamCanJoin({
    id: examId.toString(),
  });

  const { isLoading: isLoadingExamEmployee, data: examEmployee } = useFetchExamEmployeeDetail({
    id: examId.toString(),
    checkedExamCanJoin,
  });

  const { isLoading: isLoadingExamResult, data: examResult } = useFetchExamResult({
    id: examId.toString(),
    checkedExamCanJoin,
  });

  const maxNumberOfTest = useMemo(
    () => Number(examEmployee?.exam?.metafields?.filter(i => i.key === ExamMetafieldKey.max_number_of_test)?.[0]?.value || 0),
    [examEmployee?.exam?.metafields]
  );

  const outOfDate = useMemo(() => compareDate(examEmployee?.exam?.expireTime), [examEmployee?.exam?.expireTime]);

  const startOfDate = useMemo(() => compareDate(examEmployee?.exam?.applyTime), [examEmployee?.exam?.applyTime]);

  const showBtnJoinTest = useMemo(() => {
    return (
      checkedExamCanJoin &&
      Number(examResult?.numberOfTest) < maxNumberOfTest &&
      examResult?.status !== ExamEmployeeStatusEnum.PASS &&
      examResult?.status !== ExamEmployeeStatusEnum.EXPIRED &&
      !startOfDate &&
      outOfDate
    );
  }, [checkedExamCanJoin, examResult, maxNumberOfTest, startOfDate, outOfDate]);

  if (isLoadingExamEmployee || isCheckingExamCanJoin || isLoadingExamResult) {
    return <Skeleton radius="16px" height={35} />;
  }

  if (!showBtnJoinTest) return null;

  return (
    <Tooltip label="Cần điểm danh trước khi làm bài" disabled={!requireAttend}>
      <Button
        onClick={() => !requireAttend && navigate(`${UserRoutes.EXAM}/${examId}?isTesting=true`)}
        disabled={requireAttend}
        color="#2A2A86"
        variant="outline"
        radius="16px"
        size="md"
      >
        Làm bài
      </Button>
    </Tooltip>
  );
};

interface ICourseDetailColumnRightProps {
  course?: ICourse;
  courseEmployee?: ICourseEmployee;
  isLoading: boolean;
  autoPlayCountdown: boolean;
  onRefetch: () => any;
}
export const CourseDetailColumnRight = ({
  course,
  courseEmployee,
  autoPlayCountdown,
  isLoading,
  onRefetch,
}: ICourseDetailColumnRightProps) => {
  const { onAttend, attending } = useHandleAttend({
    courseId: course?.id,
    onSuccessFn: onRefetch,
  });

  const { onLearn, onLearning } = useHandleLearn({
    courseId: course?.id,
    autoPlayCountdown,
    onSuccessFn: onRefetch,
  });

  const miniumStudyTime = useMemo(
    () => Number(course?.metafields?.filter(i => i.key === CourseMetafieldKey.minimum_study_time)?.[0]?.value || 0),
    [course]
  );

  const learned = useMemo(() => courseEmployee?.status === CourseEmployeeStatusEnum.LEARNED, [courseEmployee]);

  const attendAt = useMemo(
    () => courseEmployee?.metafields?.filter(i => i.key === CourseEmployeeMetafieldKey.attend_at)?.[0]?.value,
    [courseEmployee]
  );

  const requireAttend = useMemo(
    () => course?.requireAttend && courseEmployee?.status !== CourseEmployeeStatusEnum.ATTENDED && !learned,
    [course?.requireAttend, courseEmployee?.status, learned]
  );

  const requireJoin = useMemo(() => course?.requireJoin && course?.meetingUrl, [course?.requireJoin, course?.meetingUrl]);

  const outOfDate = useMemo(() => compareDate(course?.expireTime), [course?.expireTime]);

  const startOfDate = useMemo(() => compareDate(course?.applyTime), [course?.applyTime]);

  const showBtn = useMemo(() => !startOfDate && outOfDate, [startOfDate, outOfDate]);

  const showCountdown = useMemo(() => {
    let res = false;
    if (miniumStudyTime > 0) res = true;
    if (course?.requireAttend) res = !requireAttend;
    return res && !learned;
  }, [miniumStudyTime, requireAttend, learned]);

  return (
    <Fragment>
      {isLoading ? (
        <Skeleton height={320} radius={16} />
      ) : (
        <Box bg="white" p="20px" className="course-content-box">
          <LoadingOverlay style={{ borderRadius: '8px' }} zIndex={10} visible={onLearning} loaderProps={{ children: <Loader /> }} />
          <Image src={course?.thumbUrl} fallbackSrc="../../../../../content/images/defaultBanner.png" radius="8px" alt="Norway" mb="20px" />
          <Flex direction="column" gap="20px">
            {attendAt && <Text color="#6B7280">Thời gian điểm danh: {formatDateTime(attendAt, APP_TIME_DATE_FORMAT)}</Text>}
            {showCountdown && (
              <>
                <Text color="#6B7280">Thời gian học tối thiểu: {miniumStudyTime} phút</Text>
                <CountDown autoPlay={autoPlayCountdown} totalTime={miniumStudyTime * 60} onTimeOut={onLearn} />
              </>
            )}
            {requireAttend && showBtn && (
              <Button
                onClick={() => !attending && onAttend()}
                disabled={attending}
                color="#2A2A86"
                variant="outline"
                radius="16px"
                size="md"
              >
                Điểm danh
              </Button>
            )}
            {requireJoin && showBtn && (
              <Tooltip label="Cần điểm danh trước khi vào học" disabled={!requireAttend}>
                <Button
                  onClick={() => !requireAttend && window.open(course?.meetingUrl, '_blank')}
                  disabled={requireAttend}
                  color="#2A2A86"
                  variant="filled"
                  radius="16px"
                  size="md"
                >
                  Vào học
                </Button>
              </Tooltip>
            )}
            {course?.examId && <BtnJoinExam examId={course.examId} requireAttend={!!requireAttend} />}
          </Flex>
        </Box>
      )}
    </Fragment>
  );
};
