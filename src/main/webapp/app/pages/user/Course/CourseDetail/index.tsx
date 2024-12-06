import React, { Fragment, useMemo, useState } from 'react';
import './styles.scss';
import { Text, Breadcrumbs, Grid } from '@mantine/core';
import { UserRoutes } from 'app/pages/user/routes';
import { Link, useParams } from 'react-router-dom';
import { useFetchCourseDetail } from 'app/pages/user/Course/CourseDetail/hooks/useFetchCourseDetail';
import { CourseDetailColumnLeft } from 'app/pages/user/Course/CourseDetail/components/CourseDetailColumnLeft';
import { CourseDetailColumnRight } from 'app/pages/user/Course/CourseDetail/components/CourseDetailColumnRight';
import { useFetchCourseEmployeeDetail } from 'app/pages/user/Course/CourseDetail/hooks/useFetchCourseEmployeeDetail';
import { CourseEmployeeStatusEnum } from 'app/shared/model/enumerations/course-employee-status-enum.model';

export const CourseDetail = () => {
  const { id } = useParams();

  const [autoPlayCountdown, setAutoPlayCountdown] = useState<boolean>(true);

  const { isLoading: isLoadingCourse, data: course } = useFetchCourseDetail({ id });

  const { isLoading: isLoadingCourseEmployee, data: courseEmployee, refetch: refetchCourseEmployee } = useFetchCourseEmployeeDetail({ id });

  const breadcrumbItems = useMemo(
    () => [
      { title: 'Khoá học', href: UserRoutes.COURSE },
      { title: 'Chi tiết khoá học', href: '#' },
    ],
    []
  );

  const learned = useMemo(() => {
    return courseEmployee?.status === CourseEmployeeStatusEnum.LEARNED;
  }, [courseEmployee]);

  const hasCountDown = useMemo(() => {
    return autoPlayCountdown && !learned;
  }, [autoPlayCountdown, learned]);

  return (
    <Fragment>
      <Breadcrumbs
        mb="20px"
        separatorMargin="5px"
        separator={
          <Text size="12px" color="#E5E7EB">
            /
          </Text>
        }
      >
        {breadcrumbItems.map((item, index) => (
          <Link to={item.href} key={index}>
            <Text fz="12px" color={breadcrumbItems.length - 1 === index ? '#2A2A86' : '#4B5563'}>
              {item.title}
            </Text>
          </Link>
        ))}
      </Breadcrumbs>
      <Grid gutter="20px">
        <Grid.Col span={8}>
          <CourseDetailColumnLeft
            isLoading={isLoadingCourse}
            course={course}
            courseEmployee={courseEmployee}
            autoPlayCountdown={hasCountDown}
            setAutoPlayCountdown={setAutoPlayCountdown}
          />
        </Grid.Col>
        <Grid.Col span={4}>
          <CourseDetailColumnRight
            isLoading={isLoadingCourse || isLoadingCourseEmployee}
            course={course}
            courseEmployee={courseEmployee}
            autoPlayCountdown={!hasCountDown}
            onRefetch={refetchCourseEmployee}
          />
        </Grid.Col>
      </Grid>
    </Fragment>
  );
};
