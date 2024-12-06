import React, { Fragment, useMemo } from 'react';
import { AspectRatio, Box, Button, Container, Flex, Overlay, Pill, Skeleton, Spoiler, Text, Tooltip } from '@mantine/core';
import { ICourse } from 'app/shared/model/course.model';
import { compareDate, formatDateTime } from 'app/shared/util/date-utils';
import { APP_TIME_DATE_FORMAT } from 'app/config/constants';
import { DocumentPreviewer } from 'app/shared/components/DocumentPreviewer';
import { Empty } from 'app/shared/components/Empty';
import { ICourseEmployee } from 'app/shared/model/course-employee.model';
import { CourseEmployeeStatusEnum } from 'app/shared/model/enumerations/course-employee-status-enum.model';
import { DocumentMetafieldKey, DocumentMetafieldType } from 'app/shared/model/document.model';

interface ICourseDetailColumnLeftProps {
  course?: ICourse;
  courseEmployee?: ICourseEmployee;
  isLoading: boolean;
  autoPlayCountdown: boolean;
  setAutoPlayCountdown: React.Dispatch<React.SetStateAction<boolean>>;
}
export const CourseDetailColumnLeft = ({
  autoPlayCountdown,
  course,
  courseEmployee,
  isLoading,
  setAutoPlayCountdown,
}: ICourseDetailColumnLeftProps) => {
  const requireAttend = useMemo(
    () => course?.requireAttend && courseEmployee?.status !== CourseEmployeeStatusEnum.ATTENDED,
    [course?.requireAttend, courseEmployee?.status]
  );

  const outOfDate = useMemo(() => compareDate(course?.expireTime), [course?.expireTime]);

  const startOfDate = useMemo(() => compareDate(course?.applyTime), [course?.applyTime]);

  const showBtn = useMemo(() => !startOfDate && outOfDate, [startOfDate, outOfDate]);

  return (
    <Fragment>
      {isLoading ? (
        <>
          <Skeleton height={100} radius={16} mb="20px" />
          <Skeleton height={200} radius={16} />
        </>
      ) : (
        <>
          <Box bg="white" p="20px" mb="20px" className="course-description-box">
            {course?.category?.title && (
              <Text size="14px" fw="500">
                <Pill size="xl" bg="#C3FAE8" style={{ color: '#099268' }}>
                  {course?.category?.title}
                </Pill>
              </Text>
            )}
            <Text color="black" size="20px" fw="700" mt="20px">
              {course?.title}
            </Text>
            <Text color="#6B7280" mt="20px">
              {formatDateTime(course?.applyTime, APP_TIME_DATE_FORMAT)}
              {' - '}
              {course?.expireTime ? formatDateTime(course?.expireTime, APP_TIME_DATE_FORMAT) : 'Không có hạn'}
            </Text>
            <Spoiler color="black" mt="20px" maxHeight={150} showLabel="Xem thêm" hideLabel="Ân bớt">
              {course?.description}
            </Spoiler>
          </Box>
          <Box bg="white" p="20px" className="course-content-box">
            <Text color="#1F2A37" size="20px" fw="600" mb="20px">
              Nội dung khoá học
            </Text>
            {startOfDate ? (
              <Container py={30}>
                <Empty description="Khóa học sắp diễn ra" />
              </Container>
            ) : course && course?.documents && course?.documents?.length > 0 ? (
              <AspectRatio ratio={16 / 9} w="100%" pos="relative">
                <Flex direction="column" gap={15}>
                  {course.documents.map(d => {
                    const mimeType = d?.metafields?.filter(i => i.key === DocumentMetafieldKey.mime_type)?.[0]?.value;
                    const type = d?.metafields?.filter(i => i.key === DocumentMetafieldKey.type)?.[0]?.value;
                    return (
                      <Fragment key={d.id}>
                        {type === DocumentMetafieldType.web ? <DocumentPreviewer fileUrl={d.content} mimeType={mimeType} /> : null}
                      </Fragment>
                    );
                  })}
                </Flex>
                {autoPlayCountdown && outOfDate && (
                  <Overlay color="#000" backgroundOpacity={0.35} blur={5} h="100%">
                    <Flex align="flex-start" justify="center" pt={30} w="100%" h="100%">
                      {courseEmployee && showBtn && (
                        <Tooltip label="Cần điểm danh trước khi Xem tài liệu học" disabled={!requireAttend}>
                          <Button
                            disabled={requireAttend}
                            variant="outline"
                            bg="white"
                            onClick={() => !requireAttend && setAutoPlayCountdown(!autoPlayCountdown)}
                          >
                            Xem chi tiết
                          </Button>
                        </Tooltip>
                      )}
                    </Flex>
                  </Overlay>
                )}
              </AspectRatio>
            ) : (
              <Container py={30}>
                <Empty description="Không có dữ liệu kiến thức" />
              </Container>
            )}
          </Box>
        </>
      )}
    </Fragment>
  );
};
