import React, { Fragment, useMemo } from 'react';
import { Text, Box, Pill, Skeleton, Container, Spoiler, Flex, Button } from '@mantine/core';
import { formatDateTime } from 'app/shared/util/date-utils';
import { APP_TIME_DATE_FORMAT } from 'app/config/constants';
import { IExam } from 'app/shared/model/exam.model';
import { useFetchCourseDetail } from 'app/pages/user/Course/CourseDetail/hooks/useFetchCourseDetail';
import { Empty } from 'app/shared/components/Empty';
import { DocumentPreviewer } from 'app/shared/components/DocumentPreviewer';
import { DocumentMetafieldKey, DocumentMetafieldType } from 'app/shared/model/document.model';
import { useFetchCourseEmployeeDetail } from 'app/pages/user/Exam/ExamDetail/hooks/useFetchCourseEmployeeDetail';
import { CourseEmployeeStatusEnum } from 'app/shared/model/enumerations/course-employee-status-enum.model';
import { useNavigate } from 'react-router-dom';
import { UserRoutes } from 'app/pages/user/routes';

interface IExamDetailColumnLeftProps {
  exam?: IExam;
  isLoading: boolean;
}
export const ExamDetailColumnLeft = ({ exam, isLoading }: IExamDetailColumnLeftProps) => {
  const navigate = useNavigate();

  const { isLoading: isLoadingCourseDetail, data: courseDetail } = useFetchCourseDetail({ id: exam?.courseId?.toString() });

  const { isLoading: isLoadingCourseEmployee, data: courseEmployee } = useFetchCourseEmployeeDetail({ id: exam?.courseId?.toString() });

  const renderDocuments = useMemo(() => {
    const learned = courseEmployee?.status === CourseEmployeeStatusEnum.LEARNED;
    if (!courseEmployee || learned) {
      return courseDetail && courseDetail?.documents && courseDetail?.documents?.length > 0 ? (
        courseDetail.documents.map(d => {
          const mimeType = d?.metafields?.filter(i => i.key === DocumentMetafieldKey.mime_type)?.[0]?.value;
          const type = d?.metafields?.filter(i => i.key === DocumentMetafieldKey.type)?.[0]?.value;
          return (
            <Fragment key={d.id}>
              {type === DocumentMetafieldType.web ? <DocumentPreviewer fileUrl={d.content} mimeType={mimeType} /> : null}
            </Fragment>
          );
        })
      ) : (
        <Container py={30}>
          <Empty description="Không có dữ liệu kiến thức" />
        </Container>
      );
    } else {
      return (
        <Container py={30}>
          <Flex direction="column" align="center" justify="center">
            <Empty description="Bạn cần tham gia khóa học trước khi làm bài kiểm tra!" />
            <Button mt={15} variant="outline" onClick={() => navigate(`${UserRoutes.COURSE}/${exam?.courseId}`)}>
              Tham gia khóa học
            </Button>
          </Flex>
        </Container>
      );
    }
  }, [courseDetail, courseEmployee]);

  return (
    <Fragment>
      {isLoading ? (
        <>
          <Skeleton height={100} radius={16} mb="20px" />
          <Skeleton height={200} radius={16} />
        </>
      ) : (
        <>
          <Box bg="white" p="20px" mb="20px" className="exam-description-box">
            {exam?.category?.title && (
              <Text size="14px" fw="500">
                <Pill size="xl" bg="#C3FAE8" style={{ color: '#099268' }}>
                  {exam?.category?.title}
                </Pill>
              </Text>
            )}
            <Text color="black" size="20px" fw="700" mt="20px">
              {exam?.title}
            </Text>
            <Text color="#6B7280" mt="20px">
              {formatDateTime(exam?.applyTime, APP_TIME_DATE_FORMAT)}
              {' - '}
              {exam?.expireTime ? formatDateTime(exam?.expireTime, APP_TIME_DATE_FORMAT) : 'Không có hạn'}
            </Text>
            <Spoiler color="black" mt="20px" maxHeight={150} showLabel="Xem thêm" hideLabel="Ân bớt">
              {exam?.description}
            </Spoiler>
          </Box>
          <Box bg="white" p="20px" className="exam-content-box">
            <Text color="#1F2A37" size="20px" fw="600" mb="20px">
              Nội dung kiến thức
            </Text>
            {isLoadingCourseDetail && isLoadingCourseEmployee ? <Skeleton height={100} radius={16} mb="20px" /> : renderDocuments}
          </Box>
        </>
      )}
    </Fragment>
  );
};
