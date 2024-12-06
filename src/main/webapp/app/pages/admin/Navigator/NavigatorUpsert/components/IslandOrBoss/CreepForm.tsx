import {
  Button,
  Image,
  Text,
  Flex,
  Switch,
  Textarea,
  ActionIcon,
  Radio,
  Divider,
  NumberInput,
  Modal,
  Select,
  ComboboxData,
  Box,
  Badge,
  TextInput,
} from '@mantine/core';
import { DateTimePicker } from '@mantine/dates';
import { useDisclosure } from '@mantine/hooks';
import { Trash, Clock, CalendarBlank, CaretDown, Plus } from '@phosphor-icons/react';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { InputSecond } from 'app/pages/admin/Navigator/NavigatorUpsert/components/InputSecond';
import { ModalSelectQuizzPool } from 'app/pages/admin/Navigator/NavigatorUpsert/components/ModalSelectQuizzPool';
import { ModalSelectDocument, typeVideo } from 'app/pages/admin/Navigator/NavigatorUpsert/components/ModalSelectDocument';
import { SelectPermission } from 'app/pages/admin/Navigator/NavigatorUpsert/components/SelectPermission';
import { NavigatorUpsertContext } from 'app/pages/admin/Navigator/NavigatorUpsert/context/NavigatorUpsertContext';
import {
  confirmDeleteLabel,
  errorDocument,
  errorPermission,
  errorQuizzPool,
  groupQuizzPoolsByCategory,
  nodeContainerLabel,
  PointStrategyTitle,
} from 'app/pages/admin/Navigator/NavigatorUpsert/helper';
import { useDeleteNode } from 'app/pages/admin/Navigator/NavigatorUpsert/hooks/useDeleteNode';
import { useModalConfirm } from 'app/pages/admin/Navigator/NavigatorUpsert/hooks/useModalConfirm';
import { UpdateNodeParams } from 'app/pages/admin/Navigator/NavigatorUpsert/hooks/useUpdateNode';
import { useUpsertCreep } from 'app/pages/admin/Navigator/NavigatorUpsert/hooks/useUpsertCreep';
import { TCustomNodeData } from 'app/pages/admin/Navigator/NavigatorUpsert/type';
import { DocumentMetafieldKey, IDocument } from 'app/shared/model/document.model';
import { NodeType } from 'app/shared/model/enumerations/node-type-enum.model';
import { IQuizzPool, PoolStrategyEnum, QuizzPoolMetafieldKey } from 'app/shared/model/node.model';
import React, { useContext, useMemo, useRef, useState } from 'react';
import { v4 as uuidv4 } from 'uuid';

interface CreepFormProps {
  onClose: () => void;
  initialValues?: TCustomNodeData;
  updatetingNode?: boolean;
  parentNodeId?: string;
  parentItemId?: string;
  parentNodeType?: NodeType;
  updateNode?: (props: UpdateNodeParams) => Promise<void>;
}

export const CreepForm = ({
  onClose,
  initialValues,
  updatetingNode,
  parentNodeId,
  parentItemId,
  parentNodeType,
  updateNode,
}: CreepFormProps) => {
  const { nodesRef, creatingNode, refetchNodes } = useContext(NavigatorUpsertContext);

  const openedDateTime = useRef<HTMLInputElement>(null);
  const closedDateTime = useRef<HTMLInputElement>(null);
  const selectDepartmentRef = useRef<any>();
  const selectedPositionsRef = useRef<string[] | undefined>(initialValues?.roles);

  const [endTime, setEndtime] = useState<Date | undefined>(initialValues?.endTime);
  const [filePermission, setFilePermission] = useState<File | null>(null);
  const [employeeCodes, setEmployeeCodes] = useState<string[] | undefined>(initialValues?.employeeCodes);
  const [quizzPools, setQuizzPools] = useState<Array<IQuizzPool>>(initialValues?.examQuizzPools ? initialValues?.examQuizzPools : []);
  const [poolStrategy, setPoolStrategy] = useState<string>(
    initialValues?.examPoolStrategy ? initialValues?.examPoolStrategy : PoolStrategyEnum.WEIGHT
  );
  const [documents, setDocuments] = useState<Array<IDocument>>(
    initialValues?.examDocuments && initialValues?.examDocuments?.length > 0 ? initialValues?.examDocuments : [{ id: uuidv4() }]
  );

  const [openedModalSelectQuestion, { open: openModalSelectQuestion, close: closeModalSelectQuestion }] = useDisclosure(false);

  const { form, onSubmit, parentRootId, setParentRootId, nodeContainerParent, isAssignAllEmployee, toggleIsAssignAllEmployee } =
    useUpsertCreep({
      initialValues,
      parentNodeId,
      parentItemId,
      parentNodeType,
      updateNode,
      closeDrawer: onClose,
    });

  form.watch('endTime', ({ value }) => {
    setEndtime(value);
  });

  form.watch('examPoolStrategy', ({ value }) => value && setPoolStrategy(value));

  const handleChangeDocuments = (document: IDocument) => {
    setDocuments(old =>
      old.map(i => {
        if (i.id === document.id) {
          return document;
        }
        return i;
      })
    );
  };

  const handleRemoveDocument = (documentId: string) => {
    setDocuments(old => old.filter(o => o.id?.toString() !== documentId));
  };

  const handleChangeWorkingTime = (value: number) => {
    form.setFieldValue('examWorkingTime', value);
  };

  const parentRootOption = (): ComboboxData =>
    nodesRef.current
      .filter(n => n.type === NodeType.ISLAND_AREA)
      .map(n => ({
        label: n.label || '',
        value: n.id.toString(),
      }));

  const { deletingNode, deleteNode } = useDeleteNode();

  const { closeModal, openModal, modalConfirm } = useModalConfirm({
    loading: deletingNode,
    title: `Xóa ${nodeContainerLabel[NodeType.CREEP]}`,
    content: confirmDeleteLabel({ nodeType: nodeContainerLabel[NodeType.CREEP], nodeName: initialValues?.label }),
    onOk() {
      if (initialValues && initialValues.id) {
        const withDeleteNodeContainer = nodeContainerParent?.data.items && nodeContainerParent?.data.items?.length <= 1;
        const deletedIds = withDeleteNodeContainer ? [initialValues.id, nodeContainerParent.id] : [initialValues.id];
        deleteNode({
          ids: deletedIds,
          nodeType: initialValues?.type || NodeType.CREEP,
          onSucceed() {
            refetchNodes();
            closeModal();
            onClose();
          },
          onFailed() {
            closeModal();
          },
        });
      }
    },
  });

  const showQuizzPool = useMemo(() => {
    if (!quizzPools || quizzPools.length <= 0) return null;
    if (poolStrategy === PoolStrategyEnum.WEIGHT) {
      const quizzPoolsValid = quizzPools.filter(q => q.categoryId != null);
      return (
        quizzPoolsValid && (
          <Flex wrap="wrap" gap={12} w="100%">
            {quizzPoolsValid.map(c => {
              const weight = c.metafields?.find(m => m.key === QuizzPoolMetafieldKey.weight)?.value;
              return (
                <Badge h={24} m={0} key={c.categoryId} bg={'#D4D4F0'} c={'#1E1E73'}>
                  <Flex align="center">
                    <Text maw={100} lineClamp={1}>
                      {c.categoryName}
                    </Text>
                    {' - '}
                    <Text>{weight ?? 0}%</Text>
                  </Flex>
                </Badge>
              );
            })}
          </Flex>
        )
      );
    } else if (poolStrategy === PoolStrategyEnum.MANUAL) {
      const categoryValid = groupQuizzPoolsByCategory(quizzPools).filter(c => c.categoryId != null && c.categoryName != null);
      return (
        categoryValid && (
          <Flex wrap="wrap" gap={12} w="100%">
            {categoryValid.map(c => (
              <Badge h={24} m={0} key={c.tempId} bg={'#D4D4F0'} c={'#1E1E73'}>
                <Flex align="center">
                  <Text maw={100} lineClamp={1}>
                    {c.categoryName}
                  </Text>
                  {' - '}
                  <Text>{c.tempQuizzPools.length} câu</Text>
                </Flex>
              </Badge>
            ))}
          </Flex>
        )
      );
    }
    return null;
  }, [quizzPools, poolStrategy]);

  return (
    <form
      className="form-island-or-creep"
      onSubmit={form.onSubmit((value: TCustomNodeData) => {
        onSubmit({
          value,
          employeeCodes,
          selectedPositions: selectedPositionsRef.current,
          selectedDepartments: selectDepartmentRef.current?.getCheckedNodes(),
          documents,
          quizzPools,
        });
      })}
    >
      <Flex direction="column" align="flex-start" gap={20}>
        <TextInput style={{ display: 'none' }} key={form.key('examRules')} {...form.getInputProps('examRules')} />
        <TextInput style={{ display: 'none' }} key={form.key('examNumberOfQuestion')} {...form.getInputProps('examNumberOfQuestion')} />
        <TextInput style={{ display: 'none' }} key={form.key('examMaxNumberOfTest')} {...form.getInputProps('examMaxNumberOfTest')} />
        <TextInput style={{ display: 'none' }} key={form.key('examWorkingTime')} {...form.getInputProps('examWorkingTime')} />
        <TextInput style={{ display: 'none' }} key={form.key('examPointStrategy')} {...form.getInputProps('examPointStrategy')} />
        <TextInput style={{ display: 'none' }} key={form.key('examMinPointToPass')} {...form.getInputProps('examMinPointToPass')} />
        <TextInput style={{ display: 'none' }} key={form.key('examDescription')} {...form.getInputProps('examDescription')} />
        <TextInput
          w="100%"
          maxLength={50}
          withAsterisk
          label="Tên Đảo"
          placeholder="Nhập tên"
          className="input-node-title"
          key={form.key('label')}
          {...form.getInputProps('label')}
        />
        <Textarea
          w="100%"
          maxLength={256}
          withAsterisk
          label="Mô tả"
          placeholder="Nhập mô tả"
          key={form.key('description')}
          {...form.getInputProps('description')}
          rows={4}
          resize="none"
        />
        <Select
          w="100%"
          withAsterisk
          allowDeselect={false}
          disabled={!!initialValues?.id}
          searchable
          onChange={setParentRootId}
          value={parentRootId}
          data={parentRootOption()}
          label="Quần đảo"
          placeholder="Chọn quần đảo"
          rightSection={<CaretDown size={16} />}
        />
        <Switch
          label="Bắt buộc làm"
          defaultChecked={initialValues?.examRequireJoin}
          key={form.key('examRequireJoin')}
          {...form.getInputProps('examRequireJoin')}
        />
        <Radio.Group
          label="Chọn hình ảnh quái"
          withAsterisk
          className="group-radio-island-or-creep"
          w="100%"
          key={form.key('thumbUrl')}
          {...form.getInputProps('thumbUrl')}
        >
          <Flex mt="xs" justify="space-between" w="100%">
            <Radio value="1" label={<Image src={'../../../../content/images/Creep_1.jpg'} />} />
            <Radio value="2" label={<Image src={'../../../../content/images/Creep_2.jpg'} />} />
            <Radio value="3" label={<Image src={'../../../../content/images/Creep_3.jpg'} />} />
          </Flex>
        </Radio.Group>
        <DateTimePicker
          w="100%"
          withAsterisk
          highlightToday
          withSeconds={false}
          label="Thời gian mở"
          placeholder="Chọn thời gian"
          valueFormat={APP_DATE_FORMAT}
          minDate={new Date()}
          rightSection={<CalendarBlank size={16} />}
          timeInputProps={{
            ref: openedDateTime,
            rightSection: (
              <ActionIcon variant="subtle" onClick={() => openedDateTime.current?.showPicker()}>
                <Clock size={16} />
              </ActionIcon>
            ),
          }}
          key={form.key('startTime')}
          {...form.getInputProps('startTime')}
        />
        <DateTimePicker
          w="100%"
          clearable
          highlightToday
          withSeconds={false}
          label="Thời gian đóng"
          placeholder="Chọn thời gian"
          valueFormat={APP_DATE_FORMAT}
          minDate={new Date()}
          rightSection={endTime ? undefined : <CalendarBlank size={16} />}
          timeInputProps={{
            ref: closedDateTime,
            rightSection: (
              <ActionIcon variant="subtle" onClick={() => closedDateTime.current?.showPicker()}>
                <Clock size={16} />
              </ActionIcon>
            ),
          }}
          key={form.key('endTime')}
          {...form.getInputProps('endTime')}
        />
        <InputSecond
          w="100%"
          defaultValue={initialValues?.examWorkingTime}
          onChange={handleChangeWorkingTime}
          label={
            <>
              Thời gian làm bài<Text c="#fa5252">*</Text>
            </>
          }
          error={form.errors?.['examWorkingTime']}
        />
        <Divider />
        <Flex gap={3}>
          <Text c="black" fz={14}>
            Nội dung đào tạo
          </Text>
        </Flex>
        {documents.map(d => (
          <DocumentItem
            key={d.id}
            documentsLength={documents.length}
            document={d}
            handleChangeDocuments={handleChangeDocuments}
            handleRemoveDocument={handleRemoveDocument}
          />
        ))}
        {form.errors?.[errorDocument] && <Text c="#FA5252">{form.errors?.[errorDocument]}</Text>}
        <Button
          onClick={() => {
            setDocuments(oldValues => [...oldValues, { content: '', id: uuidv4() }]);
          }}
          variant="outline"
          color="#111928"
          leftSection={<Plus size={16} />}
        >
          Thêm nội dung
        </Button>
        <Divider />
        <Text c="black" fz={14}>
          Danh sách câu hỏi
        </Text>
        {showQuizzPool}
        {form.errors?.[errorQuizzPool] && <Text c="#FA5252">{form.errors?.[errorQuizzPool]}</Text>}
        <Button onClick={openModalSelectQuestion} variant="outline" color="#111928">
          Thiết lập
        </Button>
        <Select
          w="100%"
          withAsterisk
          allowDeselect={false}
          label="Cách chấm điểm"
          data={Object.entries(PointStrategyTitle).map(([key, value]) => ({
            value: key,
            label: value,
          }))}
          key={form.key('examPointStrategy')}
          {...form.getInputProps('examPointStrategy')}
        />
        <Modal
          centered
          size="815px"
          opened={openedModalSelectQuestion}
          onClose={closeModalSelectQuestion}
          title={
            <Text fw={600} fz={16} c="#111928">
              Thêm danh sách câu hỏi
            </Text>
          }
          styles={{ content: { overflow: 'unset !important' } }}
        >
          <ModalSelectQuizzPool
            form={form}
            initialValues={initialValues}
            quizzPools={quizzPools}
            onClose={closeModalSelectQuestion}
            setQuizzPools={setQuizzPools}
          />
        </Modal>
        <Flex gap={12}>
          <NumberInput
            flex={1}
            label="Điểm tối thiểu"
            placeholder="Nhập"
            hideControls
            withAsterisk
            key={form.key('examMinPointToPass')}
            {...form.getInputProps('examMinPointToPass')}
          />
          <NumberInput
            flex={1}
            label="Số lần làm tối đa"
            placeholder="Nhập"
            hideControls
            key={form.key('examMaxNumberOfTest')}
            {...form.getInputProps('examMaxNumberOfTest')}
          />
        </Flex>
        <Divider />
        <Flex gap={3}>
          <Text c="black" fz={14}>
            Tuỳ chọn giao bài
          </Text>
          <Text c="#fa5252">*</Text>
        </Flex>
        <Switch defaultChecked={isAssignAllEmployee} onChange={toggleIsAssignAllEmployee} label="Giao bài cho tất cả nhân sự" />
        {!isAssignAllEmployee && (
          <SelectPermission
            form={form}
            employeeCodes={employeeCodes}
            setEmployeeCodes={setEmployeeCodes}
            filePermission={filePermission}
            setFilePermission={setFilePermission}
            selectDepartmentRef={selectDepartmentRef}
            selectDepartments={initialValues?.departments}
            selectedPositionsRef={selectedPositionsRef}
          />
        )}
        {form.errors?.[errorPermission] && (
          <Text c="#FA5252" mt="sm">
            {form.errors?.[errorPermission]}
          </Text>
        )}
        {initialValues ? (
          <Flex gap={12} justify="flex-end" w="100%">
            {modalConfirm}
            <Button loading={deletingNode} disabled={updatetingNode} onClick={openModal} variant="outline" color="#FA5252">
              <Trash size={16} />
            </Button>
            <Button loading={updatetingNode} type="submit" variant="outline" color="#111928">
              Chỉnh sửa
            </Button>
          </Flex>
        ) : (
          <Flex gap={12} justify="flex-end" w="100%">
            <Button disabled={creatingNode} onClick={onClose} variant="outline" color="#111928">
              Hủy
            </Button>
            <Button loading={creatingNode} type="submit">
              Tạo
            </Button>
          </Flex>
        )}
      </Flex>
    </form>
  );
};

interface DocumentProps {
  document: IDocument;
  documentsLength: number;
  handleChangeDocuments: (document: IDocument) => void;
  handleRemoveDocument: (documentId: string) => void;
}

const DocumentItem = ({ document, documentsLength, handleChangeDocuments, handleRemoveDocument }: DocumentProps) => {
  const [openedModalSelectQuestion, { open: openModalSelectQuestion, close: closeModalSelectQuestion }] = useDisclosure();

  const onChooseDocument = (selectedDocument: IDocument) => {
    handleChangeDocuments({
      ...document,
      content: selectedDocument.content,
      type: selectedDocument.type,
      name: selectedDocument.name,
      metafields: [
        {
          key: DocumentMetafieldKey.mime_type,
          value: selectedDocument.type,
        },
      ],
    });
  };

  return (
    <Flex gap={12} align="center" w="100%">
      <Box
        flex={1}
        variant="outline"
        onClick={openModalSelectQuestion}
        c="#111928"
        bd="1px solid #E5E7EB"
        style={{
          textOverflow: 'ellipsis',
          overflow: 'hidden',
          whiteSpace: 'nowrap',
          cursor: 'pointer',
          borderRadius: 'calc(0.5rem* 1)',
          padding: '5px 12px',
          textAlign: 'center',
        }}
      >
        {document.type && document.content ? document.name : 'Chọn nội dung'}
      </Box>
      <Modal
        centered
        size="815px"
        opened={openedModalSelectQuestion}
        onClose={closeModalSelectQuestion}
        title="Nội dung đào tạo"
        styles={{ content: { overflow: 'unset !important' } }}
      >
        <ModalSelectDocument
          onClose={closeModalSelectQuestion}
          onOk={onChooseDocument}
          linkValueDefault={document.type === typeVideo ? document.content : undefined}
          selectedDocumentContentDefault={document.type !== typeVideo ? document.content : undefined}
        />
      </Modal>
      {documentsLength > 1 && (
        <ActionIcon onClick={() => handleRemoveDocument(document.id?.toString() ?? '')} variant="transparent" c="#111928" mb="2px">
          <Trash size={20} />
        </ActionIcon>
      )}
    </Flex>
  );
};
