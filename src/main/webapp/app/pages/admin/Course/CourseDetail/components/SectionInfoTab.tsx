import React from 'react';
import { Button, Container, Flex, Grid, Image, Pill, Text } from '@mantine/core';
import { Trash } from '@phosphor-icons/react';

export const SectionInfoTab = () => {
  return (
    <Grid gutter="20px">
      <Grid.Col span={4}>
        <Container style={{ borderRadius: '8px' }} p={20} fluid size="responsive" bg="#fff" bd="1px solid #E5E7EB">
          <Image src={`https://picsum.photos/200`} radius={8} mb={20} />
          <Flex gap={12} my={20}>
            <Button flex={1} radius={4} color="#2A2A86" variant="outline" size="sm" fz={12} fw={400}>
              Chỉnh sửa khóa học
            </Button>
            <Button flex={1} radius={4} color="#FA5252" variant="outline" size="sm" fz={12} fw={400}>
              Kết thúc khóa học
            </Button>
            <Button px={15} radius={4} c="#FA5252" bg="#FFE3E3" variant="filled" size="sm">
              <Trash size={16} />
            </Button>
          </Flex>
          <Text c="#303030" fz={16} fw={600} mb={12}>
            Khoá học: Điền checklist before deployment
          </Text>
          <Text c="#1F2A37" fz={14} mb={12}>
            Kỹ năng chuyên môn
          </Text>
          <Text c="#6B7280" fz={14} mb={20}>
            17:00 15/04/2024 - 18:00 15/04/2023
          </Text>
          <Text c="#1F2A37" fz={14} mb={20}>
            Phòng đào tạo:-
          </Text>
          <Text c="#1F2A37" fz={14} mb={13}>
            Đối tượng
          </Text>
          <Flex gap={4} wrap="wrap" mb={20}>
            <Pill c="#6B7280" bg="#E5E7EB">
              PO
            </Pill>
            <Pill c="#6B7280" bg="#E5E7EB">
              DEV
            </Pill>
            <Pill c="#6B7280" bg="#E5E7EB">
              TESTER
            </Pill>
            <Pill c="#6B7280" bg="#E5E7EB">
              TO
            </Pill>
          </Flex>
          <Text c="#1F2A37" fz={14} mb={13}>
            Mô tả
          </Text>
          <Text c="#4B5563" fz={14}>
            Khoá học bán hàng là hành trình chinh phục nghệ thuật giao tiếp và thuyết phục. Tại đây, học viên được trang bị kiến thức vững
            chắc về kỹ năng bán hàng từ cơ bản đến nâng cao.
          </Text>
        </Container>
      </Grid.Col>
      <Grid.Col span={8}>
        <Text c="#303030" fw={600} mb={12}>
          Nội dung đào tạo
        </Text>
      </Grid.Col>
    </Grid>
  );
};
