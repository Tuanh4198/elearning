import React from 'react';
import { Card, Flex, Image, Skeleton, Text } from '@mantine/core';
import { Link } from 'react-router-dom';
import { UserRoutes } from 'app/pages/user/routes';
import { IExam } from 'app/shared/model/exam.model';
import { APP_TIME_DATE_FORMAT } from 'app/config/constants';
import { formatDateTime } from 'app/shared/util/date-utils';

interface ExamCardProps {
  exam?: IExam;
  loading?: boolean;
  canEnjoy?: boolean;
}
export const ExamCard = ({ exam, loading, canEnjoy }: ExamCardProps) => {
  if (loading) {
    return (
      <Card padding="12px" radius="16px" withBorder className="exam-card-item">
        <Skeleton height={120} radius="8px" mb="16px" />
        <Skeleton height={22} mb="8px" />
        <Skeleton height={12} mb="8px" />
      </Card>
    );
  }

  return (
    <Link to={`${UserRoutes.EXAM}${canEnjoy ? '/employee/' : '/'}${exam?.id}`}>
      <Card padding="12px" radius="16px" withBorder className="exam-card-item">
        <Flex style={{ height: '120px' }} align="center" justify="center" mb="16px" p={0}>
          <Image
            w="100%"
            h="100%"
            flex="none"
            src={exam?.thumbUrl}
            fallbackSrc="../../../../../../content/images/defaultBanner.png"
            fit="cover"
            radius="8px"
          />
        </Flex>
        <Text color="#1F2A37" mb="8px" fw={500} lineClamp={3}>
          {exam?.title}
        </Text>
        <Text color="#6B7280" size="12px">
          {exam?.expireTime ? formatDateTime(exam?.expireTime, APP_TIME_DATE_FORMAT) : 'Không có hạn'}
        </Text>
      </Card>
    </Link>
  );
};
