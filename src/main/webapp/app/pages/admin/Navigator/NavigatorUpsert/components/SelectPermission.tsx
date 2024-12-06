import { Flex, Input, Button, CloseButton, Text, Paper, Box, ScrollArea, Pill, Divider, Loader, Grid, Skeleton } from '@mantine/core';
import { Dropzone, MS_EXCEL_MIME_TYPE } from '@mantine/dropzone';
import { UseFormReturnType } from '@mantine/form';
import { DownloadSimple, UploadSimple } from '@phosphor-icons/react';
import { apiPositionUrl, SelectPosition } from 'app/pages/admin/Navigator/NavigatorUpsert/components/SelectPosition';
import { TreeSelectDepartment } from 'app/pages/admin/Navigator/NavigatorUpsert/components/TreeSelectDepartment';
import { regEmployeeCode } from 'app/pages/admin/Navigator/NavigatorUpsert/helper';
import { TCustomNodeData } from 'app/pages/admin/Navigator/NavigatorUpsert/type';
import { IPosition } from 'app/shared/model/position.model';
import { notiError } from 'app/shared/notifications';
import { exportToExcel } from 'app/shared/util/export-xlxs-file';
import { ASC } from 'app/shared/util/pagination.constants';
import axios from 'axios';
import React, { Fragment, memo, useEffect, useRef, useState } from 'react';
import * as XLSX from 'xlsx';

interface SelectPermissionProps {
  form: UseFormReturnType<TCustomNodeData, (values: TCustomNodeData) => TCustomNodeData>;
  employeeCodes?: string[];
  setEmployeeCodes?: React.Dispatch<React.SetStateAction<string[] | undefined>>;
  filePermission: File | null;
  setFilePermission: React.Dispatch<React.SetStateAction<File | null>>;
  selectDepartmentRef: React.MutableRefObject<any>;
  selectDepartments?: string[];
  selectedPositionsRef: React.MutableRefObject<string[] | undefined>;
}

export const SelectPermission = ({
  form,
  employeeCodes,
  setEmployeeCodes,
  filePermission,
  setFilePermission,
  selectDepartmentRef,
  selectDepartments,
  selectedPositionsRef,
}: SelectPermissionProps) => {
  const selectPositionRef = useRef<any>(null);
  const defaultSelectedRef = useRef<IPosition[]>([]);

  const [fetchingDefaultValue, setFetchingDefaultValue] = useState(false);

  useEffect(() => {
    if (selectedPositionsRef.current != null && selectedPositionsRef.current.length > 0 && defaultSelectedRef.current.length <= 0) {
      const fetchDefaultValue = async (codes: string[]) => {
        setFetchingDefaultValue(true);
        const requestUrl = `${apiPositionUrl}${`?page=0&size=${codes.length}&sort=id,${ASC}`}${
          codes && codes.length > 0 ? `&code.in=${codes}` : ''
        }`;
        const res = await axios.get<IPosition[]>(requestUrl);
        defaultSelectedRef.current = res.data;
        setFetchingDefaultValue(false);
      };
      fetchDefaultValue(selectedPositionsRef.current);
    }
  }, []);

  const onPositionChange = (code?: string) => {
    if (!code) return;
    let newValue = [...(selectedPositionsRef.current ?? [])];
    if (newValue?.filter(i => i === code)?.length) {
      newValue = newValue?.filter(i => i !== code);
    } else {
      if (code) {
        newValue?.push(code);
      }
    }
    selectedPositionsRef.current = newValue;
  };

  if (fetchingDefaultValue) {
    return (
      <Flex direction="column" gap={10}>
        {Array.from({ length: 4 }).map((_, i) => (
          <Skeleton key={i} w="100%" h="20px" radius="8px" />
        ))}
      </Flex>
    );
  }

  return (
    <Flex direction="column" gap={20} w="100%">
      <TreeSelectDepartment ref={selectDepartmentRef} defaultSelecteds={selectDepartments} />
      <SelectPosition
        ref={selectPositionRef}
        defaultSelecteds={defaultSelectedRef.current}
        onSelectedItem={onPositionChange}
        label="Vị trí làm việc"
      />
      <Flex direction="column" gap={4}>
        <Input.Label>Danh sách tải lên</Input.Label>
        <DropzoneFilePermission
          employeeCodes={employeeCodes}
          setEmployeeCodes={setEmployeeCodes}
          filePermission={filePermission}
          setFilePermission={setFilePermission}
        />
      </Flex>
    </Flex>
  );
};

interface DropzoneFilePermissionProps {
  filePermission: File | null;
  setFilePermission: React.Dispatch<React.SetStateAction<File | null>>;
  employeeCodes?: string[];
  setEmployeeCodes?: React.Dispatch<React.SetStateAction<string[] | undefined>>;
}

const DropzoneFilePermission = memo(
  ({ employeeCodes, setEmployeeCodes, filePermission, setFilePermission }: DropzoneFilePermissionProps) => {
    const template = [{ EMPLOYEE_CODE: 'YD12345' }];

    const handleExportTemplate = () => {
      exportToExcel(template, 'mau_nhan_su');
    };

    const handleFileChange = files => {
      const file = files[0];
      if (file) {
        const reader = new FileReader();
        reader.onload = (e: any) => {
          try {
            const data = new Uint8Array(e.target.result);
            const workbook = XLSX.read(data, { type: 'array' });
            const sheetName = workbook.SheetNames[0];
            const worksheet = workbook.Sheets[sheetName];
            const json = XLSX.utils.sheet_to_json(worksheet);
            const _employeeCodes = json.map((i: any) => i?.EMPLOYEE_CODE);
            if (_employeeCodes.length > 0) {
              const employeeCodesNotMatchReg = _employeeCodes?.filter(i => i == null || !regEmployeeCode.test(i));
              if (employeeCodesNotMatchReg?.length > 0) {
                notiError({ message: `Các mã sau: ${employeeCodesNotMatchReg.join(', ')} sai định dạng, vui lòng kiểm tra lại!` });
                return;
              }
            } else {
              notiError({ message: 'File không có dữ liệu, vui lòng chọn file khác!' });
              return;
            }
            setEmployeeCodes && setEmployeeCodes(_employeeCodes);
            setFilePermission(file);
          } catch (err) {
            console.error('Invalid JSON file: ' + err);
          }
        };
        reader.onerror = err => {
          console.error('Error reading file: ' + err);
        };
        reader.readAsArrayBuffer(file);
      }
    };

    return (
      <Paper radius="8px" className="block-download-upload-file" bd={`1px dashed #E5E7EB`} p="12px">
        {filePermission ? (
          <Flex h={54} gap={5} p={12} style={{ borderRadius: '8px' }}>
            <Text flex={1} c="#1F2A37" fw={600} lineClamp={1}>
              {filePermission.name}
            </Text>
            <CloseButton
              onClick={() => {
                setFilePermission(null);
                setEmployeeCodes && setEmployeeCodes([]);
              }}
            />
          </Flex>
        ) : (
          <Flex gap="8px">
            <Box flex={1}>
              <Dropzone
                p={12}
                maxFiles={1}
                bd="none"
                onDrop={handleFileChange}
                accept={MS_EXCEL_MIME_TYPE}
                onReject={() => notiError({ message: 'Vui lòng chọn lại file khác!' })}
              >
                <Button w="100%" color="#4B5563" variant="transparent" leftSection={<UploadSimple size={16} />}>
                  Tải lên
                </Button>
              </Dropzone>
            </Box>
            <Box flex={1}>
              <Button
                onClick={handleExportTemplate}
                w="100%"
                h="100%"
                color={'#4B5563'}
                variant="subtle"
                leftSection={<DownloadSimple size={16} />}
              >
                File mẫu
              </Button>
            </Box>
          </Flex>
        )}
        {employeeCodes && (
          <>
            <Divider my="sm" />
            <ScrollArea.Autosize mah={200} scrollbars="y">
              <Flex gap={8} wrap="wrap">
                {employeeCodes?.map(item => (
                  <Fragment key={item}>
                    {item ? (
                      // <Pill onRemove={() => {}} withRemoveButton c="#1E1E73" fw={500} fz="14px" radius="24px" bg="#D4D4F0">
                      <Pill c="#1E1E73" fw={500} fz="14px" radius="24px" bg="#D4D4F0">
                        {item}
                      </Pill>
                    ) : null}
                  </Fragment>
                ))}
              </Flex>
            </ScrollArea.Autosize>
          </>
        )}
      </Paper>
    );
  }
);
