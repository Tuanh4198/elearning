import React, { Fragment, useCallback, useEffect, useMemo } from 'react';
import { Box, Button, Container, Flex, Loader, LoadingOverlay, Pill, Text, Tooltip } from '@mantine/core';
import { CaretCircleLeft, CaretCircleRight, ArrowDown } from '@phosphor-icons/react';
import { useCalendarWeeks, formatDate, dayOfWeeks } from 'app/pages/user/Home/hooks/useCalendarWeeks';
import moment from 'moment';
import { useFetchCourseEmployee } from 'app/pages/user/Home/hooks/useFetchCourseEmployee';
import { useFetchExamEmployee } from 'app/pages/user/Home/hooks/useFetchExamEmployee';
import { CourseEmployeeStatusEnum } from 'app/shared/model/enumerations/course-employee-status-enum.model';
import { Empty } from 'app/shared/components/Empty';
import { useToggle } from '@mantine/hooks';
import dayjs from 'dayjs';
import { ExamEmployeeStatusEnum } from 'app/shared/model/enumerations/exam-employee-status-enum.model';
import { Link } from 'react-router-dom';
import { UserRoutes } from 'app/pages/user/routes';

const startOfDayZ = (date: Date) => {
  const originalMoment = moment(date);
  const startOfDayLocal = originalMoment.startOf('day');
  const dateZ = startOfDayLocal.format('YYYY-MM-DDTHH:mm:ss.SSS[Z]');
  return dateZ;
};

const endOfDayZ = (date: Date) => {
  const originalMoment = moment(date);
  const startOfDayLocal = originalMoment.endOf('day');
  const dateZ = startOfDayLocal.format('YYYY-MM-DDTHH:mm:ss.SSS[Z]');
  return dateZ;
};

enum ISelectedDateBlockStatus {
  COMPLETED = 'COMPLETED',
  NOT_COMPLETED = 'NOT_COMPLETED',
}

enum ISelectedDateBlockType {
  COURSE = 'COURSE',
  EXAM = 'EXAM',
}

const statusMap = {
  [ISelectedDateBlockStatus.COMPLETED]: 'Đã hoàn thành',
  [ISelectedDateBlockStatus.NOT_COMPLETED]: 'Chưa hoàn thành',
};

interface ISelectedDateBlock {
  id?: number;
  applyTime: Date | null;
  expireTime: Date | null;
  from: string;
  to: string;
  status: ISelectedDateBlockStatus;
  type: ISelectedDateBlockType;
  title: string;
}

const BlockTimeItem = ({ item }: { item: ISelectedDateBlock }) => {
  const isCompleted = item.status === ISelectedDateBlockStatus.COMPLETED;

  return (
    <Flex gap="8px" className="time-item">
      <Flex
        bd={isCompleted ? '1px solid #12B886' : '1px solid #E5E7EB'}
        direction="column"
        px="12px"
        py="8px"
        gap="2px"
        align="center"
        justify="space-between"
        style={{ borderRadius: '8px' }}
      >
        <Text color="#4B5563" size="12px" fw={500}>
          {item.from}
        </Text>
        <ArrowDown size={10} color="#4B5563" />
        <Text color="#4B5563" size="12px" fw={500}>
          {item.to}
        </Text>
      </Flex>
      <Flex
        bd={isCompleted ? '1px solid #12B886' : '1px solid #E5E7EB'}
        direction="column"
        px="12px"
        py="8px"
        gap="4px"
        align="flex-start"
        justify="space-between"
        flex={1}
        style={{ borderRadius: '8px' }}
      >
        <Flex gap={8}>
          <Text size="12px" fw="500">
            <Pill size="xs" bg="#D4D4F0" c="#2A2A86">
              {item.type === ISelectedDateBlockType.COURSE ? 'Khóa học' : 'Kì thi'}
            </Pill>
          </Text>
          <Text size="12px" fw="500">
            <Pill size="xs" bg={isCompleted ? '#12B886' : '#FFE8CC'} c={isCompleted ? '#E6FCF5' : '#E8590C'}>
              {statusMap[item.status]}
            </Pill>
          </Text>
        </Flex>
        <Tooltip position="top-start" multiline maw={500} label={item.title}>
          <Text lineClamp={1} color="#1F2A37" size="14px">
            {item.title}
          </Text>
        </Tooltip>
      </Flex>
    </Flex>
  );
};

const isValidDate = (selectedDate: Date, applyTime: Date, expireTime: Date) => {
  let isValid = false;
  if (!expireTime) {
    if (isSameDate(selectedDate, applyTime)) {
      isValid = true;
    }
  } else {
    if (dayjs(selectedDate) >= dayjs(applyTime) && dayjs(selectedDate) <= dayjs(expireTime)) {
      isValid = true;
    }
  }
  return isValid;
};

const isSameDate = (date1: Date, date2: Date) => {
  if (!date1 || !date2) return false;
  return date1.getFullYear() === date2.getFullYear() && date1.getMonth() === date2.getMonth() && date1.getDate() === date2.getDate();
};

const getFromTo = (selectedDate: Date, applyTime: Date, expireTime: Date) => {
  const formatTime = (date: Date) => {
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    return `${hours}:${minutes}`;
  };

  let from = '00:00';
  let to = '23:59';
  if (isSameDate(selectedDate, applyTime)) {
    from = formatTime(applyTime);
  }

  if (isSameDate(selectedDate, expireTime)) {
    to = formatTime(expireTime);
  }

  return { from, to };
};

const showLimitItems = 10;

export const BlockTimetable = () => {
  const [value, toggle] = useToggle();

  const { showPreviousWeek, showNextWeek, currentWeekDates, selectedDate, setSelectedDate } = useCalendarWeeks();

  const startOfWeek = useMemo(() => {
    if (!currentWeekDates || currentWeekDates.length === 0) return null;
    const dayZ = startOfDayZ(currentWeekDates[0]);
    return dayZ;
  }, [currentWeekDates]);

  const endOfWeek = useMemo(() => {
    if (!currentWeekDates || currentWeekDates.length === 0) return null;
    const dayZ = endOfDayZ(currentWeekDates[currentWeekDates.length - 1]);
    return dayZ;
  }, [currentWeekDates]);

  useEffect(() => {
    refetchCourses();
    refetchExams();
  }, [startOfWeek, endOfWeek]);

  const {
    isLoading: isLoadingCourses,
    data: courses,
    refetch: refetchCourses,
  } = useFetchCourseEmployee({ from: startOfWeek, to: endOfWeek, status: null });

  const {
    isLoading: isLoadingExams,
    data: exams,
    refetch: refetchExams,
  } = useFetchExamEmployee({ from: startOfWeek, to: endOfWeek, status: null });

  const selectedDateData = useCallback(
    (date: Date): ISelectedDateBlock[] => {
      const result: ISelectedDateBlock[] = [];
      courses?.forEach(item => {
        if (!item?.course || !item.course.applyTime) return;
        const applyTime = dayjs(item.course.applyTime).toDate();
        let expireTime;
        if (item.course.expireTime) {
          expireTime = dayjs(item.course.expireTime).toDate();
        }
        const isValid = isValidDate(date, applyTime, expireTime);
        if (isValid) {
          const { from, to } = getFromTo(date, applyTime, expireTime);
          result.push({
            id: item.course.id,
            title: item?.course?.title || '',
            from,
            to,
            type: ISelectedDateBlockType.COURSE,
            applyTime: applyTime || null,
            expireTime: expireTime || null,
            status:
              item?.status === CourseEmployeeStatusEnum.LEARNED
                ? ISelectedDateBlockStatus.COMPLETED
                : ISelectedDateBlockStatus.NOT_COMPLETED,
          });
        }
      });
      exams?.forEach(item => {
        if (!item?.exam || !item.exam.applyTime) return;
        const applyTime = dayjs(item.exam.applyTime).toDate();
        let expireTime;
        if (item.exam.expireTime) {
          expireTime = dayjs(item.exam.expireTime).toDate();
        }
        const isValid = isValidDate(date, applyTime, expireTime);
        if (isValid) {
          const { from, to } = getFromTo(date, applyTime, expireTime);
          result.push({
            id: item.exam.id,
            title: item?.exam?.title || '',
            from,
            to,
            type: ISelectedDateBlockType.EXAM,
            applyTime: applyTime || null,
            expireTime: expireTime || null,
            status:
              item?.status === ExamEmployeeStatusEnum.PASS ? ISelectedDateBlockStatus.COMPLETED : ISelectedDateBlockStatus.NOT_COMPLETED,
          });
        }
      });
      return result.sort((a, b) => {
        if (dayjs(a.applyTime) < dayjs(b.applyTime)) return -1;
        return 1;
      });
    },
    [courses, exams]
  );

  return (
    <Box pos="relative" bg="white" p="20px" className="dashboard-block">
      <LoadingOverlay
        style={{ borderRadius: '8px' }}
        zIndex={10}
        visible={isLoadingCourses || isLoadingExams}
        loaderProps={{ children: <Loader /> }}
      />
      <Text color="#1F2A37" size="20px" fw="700" mb="12px">
        Thời khoá biểu
      </Text>
      <Flex align="center" justify="space-between" mb="12px">
        <Button onClick={showPreviousWeek} radius={500} variant="subtle" color="#111928" p={0} size="sm">
          <CaretCircleLeft size={24} />
        </Button>
        <Text color="#6B7280" size="14px" fw={500}>
          {formatDate(currentWeekDates[0], true)} - {formatDate(currentWeekDates[currentWeekDates.length - 1], true)}
        </Text>
        <Button onClick={showNextWeek} radius={500} variant="subtle" color="#111928" p={0} size="sm">
          <CaretCircleRight size={24} />
        </Button>
      </Flex>
      <Flex align="center" justify="space-between" gap="6px" mb="12px">
        {currentWeekDates.map((day, index) => {
          const dayFormat = formatDate(day);
          const selectedDateFormat = formatDate(selectedDate);
          return (
            <Flex
              className={`day-item ${selectedDateData(day) && selectedDateData(day)?.length > 0 && 'active'}`}
              bg={selectedDateFormat === dayFormat ? '#1F2A37' : 'white'}
              bd="1px solid #E5E7EB"
              onClick={() => setSelectedDate(day)}
              key={dayFormat}
              direction="column"
              align="center"
              justify="center"
              flex={1}
              px="12px"
              py="10px"
              gap="8px"
            >
              <Text color={selectedDateFormat === dayFormat ? 'white' : '#1F2A37'} size="14px" fw="500">
                {dayOfWeeks[index]}
              </Text>
              <Text color={selectedDateFormat === dayFormat ? 'white' : '#9CA3AF'} size="12px" fw="500">
                {dayFormat}
              </Text>
            </Flex>
          );
        })}
      </Flex>
      <Flex direction="column" gap="8px">
        {selectedDateData(selectedDate) && selectedDateData(selectedDate)?.length > 0 ? (
          selectedDateData(selectedDate)
            .slice(0, value ? undefined : showLimitItems)
            .map(item => (
              <Link
                to={item.type === ISelectedDateBlockType.COURSE ? `${UserRoutes.COURSE}/${item.id}` : `${UserRoutes.EXAM}/${item.id}`}
                key={item.id}
              >
                <BlockTimeItem item={item} />
              </Link>
            ))
        ) : (
          <Container py={30}>
            <Empty description="Không có lịch nào" />
          </Container>
        )}
        {selectedDateData(selectedDate) && selectedDateData(selectedDate)?.length > showLimitItems && (
          <Button onClick={() => toggle()} variant="outline" mt={10}>
            {value ? 'Ẩn bớt' : 'Xem thêm'}
          </Button>
        )}
      </Flex>
    </Box>
  );
};
