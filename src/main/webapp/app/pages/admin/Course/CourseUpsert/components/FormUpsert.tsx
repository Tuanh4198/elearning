import React, { useContext, useRef } from 'react';
import { CourseUpsertFormContext } from 'app/pages/admin/Course/CourseUpsert/context/CourseUpsertFormContext';
import {
  ActionIcon,
  Checkbox,
  Container,
  Grid,
  Group,
  NumberInput,
  Radio,
  Textarea,
  TextInput,
  Text,
  Flex,
  Button,
  Box,
  FileButton,
} from '@mantine/core';
import { AssignStrategyTitle } from 'app/shared/model/enumerations/assign-strategy-enum.model';
import { DateTimePicker } from '@mantine/dates';
import { Dropzone, IMAGE_MIME_TYPE } from '@mantine/dropzone';
import { ArrowsClockwise, CalendarBlank, Clock, Plus, Trash } from '@phosphor-icons/react';
import { notiError } from 'app/shared/notifications';

export const FormUpsert = () => {
  const startDateTime = useRef<HTMLInputElement>(null);
  const endDateTime = useRef<HTMLInputElement>(null);

  const { form, errors, previewFiles, setFiles } = useContext(CourseUpsertFormContext);

  return (
    <form onSubmit={form?.onSubmit(() => {})} className="modal-body">
      <Grid>
        <Grid.Col span={4}>
          <Container style={{ borderRadius: '8px' }} bd="1px solid #E5E7EB" p={20} fluid size="responsive" bg="#fff">
            {/*  */}
            <TextInput
              w="100%"
              label="Tên khóa học"
              placeholder="Nhập tên khóa học"
              c="#4B5563"
              mb={20}
              maxLength={255}
              withAsterisk
              {...form?.getInputProps('title')}
            />
            {/*  */}
            <Checkbox label="Khoá học bắt buộc" c="#4B5563" mb={20} {...form?.getInputProps('requireJoin')} />
            {/*  */}
            <Checkbox label="Yêu cầu điểm danh" c="#4B5563" mb={20} {...form?.getInputProps('requireAttend')} />
            {/*  */}
            <TextInput
              w="100%"
              label="Phòng đào tạo"
              placeholder="Nhập link phòng đào tạo (Nếu có)"
              c="#4B5563"
              mb={20}
              withAsterisk
              {...form?.getInputProps('meetingUrl')}
            />
            {/*  */}
            <DateTimePicker
              w="100%"
              label="Thời gian bắt đầu"
              placeholder="Chọn thời gian bắt đầu"
              c="#4B5563"
              mb={20}
              withAsterisk
              {...form?.getInputProps('')}
              rightSection={<CalendarBlank size={16} />}
              highlightToday
              withSeconds={false}
              timeInputProps={{
                ref: startDateTime,
                rightSection: (
                  <ActionIcon variant="subtle" onClick={() => startDateTime.current?.showPicker()}>
                    <Clock size={16} />
                  </ActionIcon>
                ),
              }}
            />
            {/*  */}
            <DateTimePicker
              w="100%"
              label="Thời gian kết thúc"
              placeholder="Chọn thời gian kết thúc"
              c="#4B5563"
              mb={20}
              {...form?.getInputProps('')}
              rightSection={<CalendarBlank size={16} />}
              highlightToday
              withSeconds={false}
              timeInputProps={{
                ref: endDateTime,
                rightSection: (
                  <ActionIcon variant="subtle" onClick={() => endDateTime.current?.showPicker()}>
                    <Clock size={16} />
                  </ActionIcon>
                ),
              }}
            />
            {/*  */}
            <NumberInput
              w="100%"
              label="Thời gian học bài tối thiểu"
              placeholder="Nhập số phút"
              c="#4B5563"
              mb={20}
              withAsterisk
              {...form?.getInputProps('')}
              rightSectionWidth={50}
              rightSection={<Text c="#9CA3AF">Phút</Text>}
              hideControls
            />
            {/*  */}
            <Radio.Group label="Đối tượng tham gia" c="#4B5563" mb={20} withAsterisk {...form?.getInputProps('assignStrategy')}>
              <Group justify="space-between" mt={12}>
                {Object.keys(AssignStrategyTitle).map((item, index) => (
                  <Radio key={index} value={item} label={AssignStrategyTitle[item]} />
                ))}
              </Group>
            </Radio.Group>
            {/*  */}
            <Textarea
              w="100%"
              label="Mô tả"
              placeholder="Mô tả khoá học"
              c="#4B5563"
              mb={20}
              rows={4}
              {...form?.getInputProps('description')}
            />
            {/*  */}
            {previewFiles && previewFiles?.length > 0 ? (
              <Box pos="relative">
                {previewFiles}
                <Box pos="absolute" top="50%" left="50%" style={{ transform: 'translate(-50%, -50%)' }}>
                  <Flex gap={8} about="center" justify="center">
                    <FileButton onChange={setFiles} multiple accept={IMAGE_MIME_TYPE.join(',')}>
                      {props => (
                        <Button p={12} variant="outline" bg="white" radius={8} size="md" {...props}>
                          <ArrowsClockwise size={12} />
                        </Button>
                      )}
                    </FileButton>
                    <Button p={12} variant="outline" color="#FA5252" bg="white" radius={8} size="md" onClick={() => setFiles(undefined)}>
                      <Trash size={12} />
                    </Button>
                  </Flex>
                </Box>
              </Box>
            ) : (
              <Dropzone
                bd="1px dashed #9CA3AF"
                bg="white"
                radius={8}
                p={previewFiles && previewFiles?.length > 0 ? 0 : 20}
                h={145}
                mah={145}
                maxSize={5 * 1024 ** 2}
                maxFiles={1}
                accept={IMAGE_MIME_TYPE}
                onDrop={setFiles}
                onReject={() => notiError({ message: 'Vui lòng chọn lại file khác!' })}
              >
                <Dropzone.Accept>
                  <Flex w="100%" direction="column" align="center" justify="center">
                    <Text fz={12} c="#2A2A86" mt={8} ta="center">
                      Thả file vào đây để chọn!
                    </Text>
                  </Flex>
                </Dropzone.Accept>
                <Dropzone.Reject>
                  <Flex w="100%" direction="column" align="center" justify="center">
                    <Text fz={12} c="#FA5252" mt={8} ta="center">
                      File không đúng định dạng hoặc dung lượng lớn hơn 5MB, <br />
                      vui lòng chọn lại file khác!
                    </Text>
                  </Flex>
                </Dropzone.Reject>
                <Dropzone.Idle>
                  <Flex w="100%" direction="column" align="center" justify="center">
                    <Button variant="outline" color="#2A2A86" size="md" rightSection={<Plus size={12} />}>
                      Thêm ảnh bìa
                    </Button>
                    <Text fz={12} c="#6B7280" mt={8} ta="center">
                      Kích thước tiêu chuẩn: 500x250px (2:1) <br />
                      Định dạng: jpg, jpeg, png <br />
                      Dung lượng tối đa: 5Mb
                    </Text>
                  </Flex>
                </Dropzone.Idle>
              </Dropzone>
            )}
          </Container>
        </Grid.Col>
        <Grid.Col span={8}></Grid.Col>
      </Grid>
    </form>
  );
};
