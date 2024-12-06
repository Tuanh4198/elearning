import {
  Button,
  Image,
  Text,
  Flex,
  Switch,
  Textarea,
  TextInput,
  ActionIcon,
  Divider,
  Radio,
  Modal,
  Input,
  Box,
  Select,
  ComboboxData,
} from '@mantine/core';
import { DateTimePicker } from '@mantine/dates';
import { useDisclosure } from '@mantine/hooks';
import { Trash, Clock, CalendarBlank, Plus, CaretDown } from '@phosphor-icons/react';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { SelectPermission } from 'app/pages/admin/Navigator/NavigatorUpsert/components/SelectPermission';
import { TCustomNodeData } from 'app/pages/admin/Navigator/NavigatorUpsert/type';
import React, { useContext, useMemo, useRef, useState } from 'react';
import { v4 as uuidv4 } from 'uuid';
import { InputSecond } from 'app/pages/admin/Navigator/NavigatorUpsert/components/InputSecond';
import { ModalSelectDocument, typeVideo } from 'app/pages/admin/Navigator/NavigatorUpsert/components/ModalSelectDocument';
import {
  confirmDeleteLabel,
  defaultRule,
  errorDocument,
  errorPermission,
  getValueInCondition,
  nodeContainerLabel,
} from 'app/pages/admin/Navigator/NavigatorUpsert/helper';
import { useDeleteNode } from 'app/pages/admin/Navigator/NavigatorUpsert/hooks/useDeleteNode';
import { useModalConfirm } from 'app/pages/admin/Navigator/NavigatorUpsert/hooks/useModalConfirm';
import { NodeType } from 'app/shared/model/enumerations/node-type-enum.model';
import { NavigatorUpsertContext } from 'app/pages/admin/Navigator/NavigatorUpsert/context/NavigatorUpsertContext';
import { useUpsertIsland } from 'app/pages/admin/Navigator/NavigatorUpsert/hooks/useUpsertIsland';
import { UpdateNodeParams } from 'app/pages/admin/Navigator/NavigatorUpsert/hooks/useUpdateNode';
import { DocumentMetafieldKey, IDocument } from 'app/shared/model/document.model';
import { NodeRuleNamespace } from 'app/shared/model/node.model';

interface IslandFormProps {
  onClose: () => void;
  initialValues?: TCustomNodeData;
  updatetingNode?: boolean;
  parentNodeId?: string;
  parentItemId?: string;
  parentNodeType?: NodeType;
  updateNode?: (props: UpdateNodeParams) => Promise<void>;
}

const now = new Date();
const minTime = `${String(now.getHours()).padStart(2, '0')}:${String(now.getMinutes()).padStart(2, '0')}`;

export const IslandForm = ({
  onClose,
  initialValues,
  updatetingNode,
  parentNodeId,
  parentItemId,
  parentNodeType,
  updateNode,
}: IslandFormProps) => {
  const { nodesRef, creatingNode, refetchNodes } = useContext(NavigatorUpsertContext);

  const openedDateTime = useRef<HTMLInputElement>(null);
  const closedDateTime = useRef<HTMLInputElement>(null);
  const selectDepartmentRef = useRef<any>();
  const selectedPositionsRef = useRef<string[] | undefined>(initialValues?.roles);

  const [endTime, setEndtime] = useState<Date | undefined>(initialValues?.endTime);
  const [filePermission, setFilePermission] = useState<File | null>(null);
  const [employeeCodes, setEmployeeCodes] = useState<string[] | undefined>(initialValues?.employeeCodes);
  const [documents, setDocuments] = useState<Array<IDocument>>(
    initialValues?.courseDocuments ? initialValues?.courseDocuments : [{ id: uuidv4() }]
  );

  const {
    form,
    onSubmit,
    parentRootId,
    setParentRootId,
    nodeContainerParent,
    isUseZoom,
    toggleIsUseZoom,
    isAssignAllEmployee,
    toggleIsAssignAllEmployee,
  } = useUpsertIsland({
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

  const handleRemoveDocuments = (documentId: string) => {
    setDocuments(old => old.filter(o => o.id?.toString() !== documentId.toString()));
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
    title: `Xóa ${nodeContainerLabel[NodeType.ISLAND]}`,
    content: confirmDeleteLabel({ nodeType: nodeContainerLabel[NodeType.ISLAND], nodeName: initialValues?.label }),
    onOk() {
      if (initialValues && initialValues.id) {
        const withDeleteNodeContainer = nodeContainerParent?.data.items && nodeContainerParent?.data.items?.length <= 1;
        const deletedIds = withDeleteNodeContainer ? [initialValues.id, nodeContainerParent.id] : [initialValues.id];
        deleteNode({
          ids: deletedIds,
          nodeType: initialValues?.type || NodeType.ISLAND,
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

  return (
    <form
      className="form-island-or-creep"
      onSubmit={form.onSubmit((value: TCustomNodeData) =>
        onSubmit({
          value,
          documents,
          employeeCodes,
          selectedPositions: selectedPositionsRef.current,
          selectedDepartments: selectDepartmentRef.current?.getCheckedNodes(),
        })
      )}
    >
      <Flex direction="column" align="flex-start" gap={20}>
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
          label="Bắt buộc học"
          defaultChecked={initialValues?.courseRequireJoin}
          key={form.key('courseRequireJoin')}
          {...form.getInputProps('courseRequireJoin')}
        />
        <Switch
          label="Cần điểm danh"
          defaultChecked={initialValues?.courseRequireAttend}
          key={form.key('courseRequireAttend')}
          {...form.getInputProps('courseRequireAttend')}
        />
        <Switch label="Học qua zoom" checked={isUseZoom} onChange={toggleIsUseZoom} />
        <TextInput
          display={isUseZoom ? '' : 'none'}
          w="100%"
          maxLength={50}
          withAsterisk
          label="Link zoom"
          placeholder="Nhập link zoom"
          key={form.key('courseMeetingUrl')}
          {...form.getInputProps('courseMeetingUrl')}
        />
        <TextInput
          display={isUseZoom ? '' : 'none'}
          w="100%"
          maxLength={50}
          label="Mật khẩu"
          placeholder="Nhập mật khẩu"
          key={form.key('courseMeetingPassword')}
          {...form.getInputProps('courseMeetingPassword')}
        />
        <Radio.Group
          label="Chọn hình ảnh đảo"
          withAsterisk
          className="group-radio-island-or-creep"
          w="100%"
          key={form.key('thumbUrl')}
          {...form.getInputProps('thumbUrl')}
        >
          <Flex mb="5" justify="space-between" w="100%">
            <Radio value="1" label={<Image src={'../../../../content/images/Island_1.svg'} />} />
            <Radio value="2" label={<Image src={'../../../../content/images/Island_2.svg'} />} />
            <Radio value="3" label={<Image src={'../../../../content/images/Island_3.svg'} />} />
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
            minTime,
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
            minTime,
          }}
          key={form.key('endTime')}
          {...form.getInputProps('endTime')}
        />
        <Divider />
        <Flex gap={3}>
          <Text c="black" fz={14}>
            Nội dung đào tạo
          </Text>
          <Text c="#fa5252">*</Text>
        </Flex>
        {documents.map(c => (
          <Document
            key={c.id}
            document={c}
            documentsLength={documents.length}
            handleChangeDocuments={handleChangeDocuments}
            handleRemoveDocument={handleRemoveDocuments}
          />
        ))}
        {form.errors?.[errorDocument] && <Text c="#FA5252">{form.errors?.[errorDocument]}</Text>}
        <Button
          onClick={() => setDocuments(oldValues => [...oldValues, { id: uuidv4() }])}
          variant="outline"
          color="#111928"
          leftSection={<Plus size={16} />}
        >
          Thêm nội dung
        </Button>
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
  documentsLength: number;
  document: IDocument;
  handleChangeDocuments: (document: IDocument) => void;
  handleRemoveDocument: (documentId: string) => void;
}

const Document = ({ documentsLength, document, handleChangeDocuments, handleRemoveDocument }: DocumentProps) => {
  const [openedModalSelectQuestion, { open: openModalSelectQuestion, close: closeModalSelectQuestion }] = useDisclosure();

  const timeValue = useMemo(() => {
    const conditionDocument = document.rules?.find(r => r.namespace === NodeRuleNamespace.DOCUMENT)?.condition;
    return conditionDocument ? getValueInCondition({ condition: conditionDocument }) : undefined;
  }, [document.rules]);

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

  const onChangeTime = (time: number) => {
    handleChangeDocuments({
      ...document,
      rules: document.rules
        ? document.rules.map(r => ({
            ...r,
            condition: `input.studyTime == '${time}'`,
          }))
        : [
            {
              ...defaultRule,
              namespace: NodeRuleNamespace.DOCUMENT,
              condition: `input.studyTime == '${time}'`,
            },
          ],
    });
  };

  return (
    <Flex gap={12} align="flex-end" w="100%" key={document.id}>
      <Flex direction="column" style={{ width: `calc(100% - ${documentsLength > 1 ? 150 : 120}px)` }}>
        <Input.Label>Nội dung</Input.Label>
        <Box
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
      </Flex>
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
      <InputSecond
        w={120}
        defaultValue={timeValue ? Number(timeValue) : undefined}
        onChange={onChangeTime}
        label="Học trong"
        tooltipValue="Thời gian học tối thiểu"
      />
      {documentsLength > 1 && (
        <ActionIcon onClick={() => handleRemoveDocument(document.id?.toString() ?? '')} variant="transparent" c="#111928" mb="2px">
          <Trash size={20} />
        </ActionIcon>
      )}
    </Flex>
  );
};
