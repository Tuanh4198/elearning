import { ActionIcon, Box, Button, Checkbox, Flex, Group, Modal, NumberInput, Radio, Text, Tooltip } from '@mantine/core';
import { UseFormReturnType } from '@mantine/form';
import { useDisclosure } from '@mantine/hooks';
import { Percent, Plus, Trash } from '@phosphor-icons/react';
import { ModalSelectQuizz } from 'app/pages/admin/Navigator/NavigatorUpsert/components/ModalSelectQuizz';
import { SelectQuizzCategory } from 'app/pages/admin/Navigator/NavigatorUpsert/components/SelectQuizzCategory';
import { groupQuizzPoolsByCategory } from 'app/pages/admin/Navigator/NavigatorUpsert/helper';
import { TCustomNodeData, TempCategory } from 'app/pages/admin/Navigator/NavigatorUpsert/type';
import { IQuizzPool, PoolStrategyEnum, QuizzPoolMetafieldKey } from 'app/shared/model/node.model';
import { IQuizzCategory } from 'app/shared/model/quizz-category.model';
import { IQuizz } from 'app/shared/model/quizz.model';
import { notiError } from 'app/shared/notifications';
import React, { Fragment, useEffect, useMemo, useState } from 'react';
import { v4 as uuidv4 } from 'uuid';

export const ModalSelectQuizzPool = ({
  onClose,
  quizzPools,
  setQuizzPools,
  initialValues,
  form,
}: {
  onClose: () => void;
  quizzPools: IQuizzPool[];
  setQuizzPools: React.Dispatch<React.SetStateAction<IQuizzPool[]>>;
  initialValues?: TCustomNodeData;
  form: UseFormReturnType<TCustomNodeData, (values: TCustomNodeData) => TCustomNodeData>;
}) => {
  const [tempQuizzPools, setTempQuizzPools] = useState<Array<IQuizzPool>>(quizzPools.length > 0 ? quizzPools : [{ id: uuidv4() }]);
  const [poolStrategy, setPoolStrategy] = useState<string>(
    form.getValues().examPoolStrategy != null ? (form.getValues().examPoolStrategy as string) : PoolStrategyEnum.WEIGHT
  );
  const [numberOfQuestion, setNumberOfQuestion] = useState<number | string | undefined>(
    form.getValues().examNumberOfQuestion != null ? form.getValues().examNumberOfQuestion?.toString() : undefined
  );
  const [examStrategy, setExamStrategy] = useState<boolean>(
    form.getValues().examStrategy != null ? (form.getValues().examStrategy as boolean) : false
  );

  const handleAddQuizzPools = (quizzPool: IQuizzPool) => {
    setTempQuizzPools(old => [...old, quizzPool]);
  };

  const handleChangeQuizzPool = (quizzPool: IQuizzPool) => {
    setTempQuizzPools(old =>
      old.map(i => {
        if (i.id === quizzPool.id) {
          return quizzPool;
        }
        return i;
      })
    );
  };

  const handleReplaceQuizzPools = (newQuizzPools: IQuizzPool[]) => {
    setTempQuizzPools(newQuizzPools);
  };

  const handleRemoveQuizzPool = (quizzPoolId: string) => {
    setTempQuizzPools(old => old.filter(o => o.id?.toString() !== quizzPoolId));
  };

  const onChangeExamNumberOfQuestion = (value: number | string) => {
    setNumberOfQuestion(Number(value));
  };

  const onChangeExamStrategy = (value: boolean) => {
    setExamStrategy(value);
  };

  const onChangePoolStrategy = (value: string) => {
    setPoolStrategy(value);
    setTempQuizzPools([{ id: uuidv4() }]);
    if (value === PoolStrategyEnum.MANUAL) {
      setNumberOfQuestion('');
    }
  };

  const onSubmit = () => {
    setQuizzPools(tempQuizzPools);
    form.setValues({
      examStrategy,
      examPoolStrategy: poolStrategy as PoolStrategyEnum,
      examNumberOfQuestion: Number(numberOfQuestion),
    });
    onClose();
  };

  const totalFrequencyRandomQuizz = useMemo(() => {
    let total = 0;
    tempQuizzPools.forEach(q => {
      const res = q.metafields?.find(m => m.key === QuizzPoolMetafieldKey.weight)?.value ?? 0;
      total += Number(res);
    });
    return total;
  }, [tempQuizzPools]);

  const isDisableSubmitBtn = useMemo(() => {
    if (poolStrategy === PoolStrategyEnum.WEIGHT) {
      return totalFrequencyRandomQuizz !== 100 || numberOfQuestion == null || numberOfQuestion === '';
    } else if (poolStrategy === PoolStrategyEnum.MANUAL) {
      return (
        tempQuizzPools.length <= 0 ||
        tempQuizzPools.some(t => t.categoryId == null || t.categoryId.toString() === '' || t.sourceId == null || t.sourceId === '')
      );
    } else return true;
  }, [poolStrategy, totalFrequencyRandomQuizz, numberOfQuestion, tempQuizzPools]);

  const disabledUpdate = useMemo(() => {
    return !!initialValues?.id;
  }, [initialValues]);

  return (
    <Fragment>
      <Box p="20px" bd="1px solid #E5E7EB" style={{ borderRadius: '16px' }}>
        <Flex direction="column" gap={16}>
          <Radio.Group value={poolStrategy} onChange={onChangePoolStrategy}>
            <Group>
              <Radio disabled={disabledUpdate} value={PoolStrategyEnum.WEIGHT} label={<Text fz={14}>Lấy ngẫu nhiên câu hỏi</Text>} />
              <Radio disabled={disabledUpdate} value={PoolStrategyEnum.MANUAL} label={<Text fz={14}>Tự chọn câu hỏi</Text>} />
            </Group>
          </Radio.Group>
          <Checkbox
            defaultChecked={examStrategy}
            label={<Text fz={14}>Giao ngẫu nhiên</Text>}
            onChange={e => onChangeExamStrategy(e.target.checked)}
          />
          {poolStrategy === PoolStrategyEnum.WEIGHT && (
            <Flex gap={16} align="center">
              <Flex align="center">
                <Text fz={14}>Tổng số câu hỏi được giao</Text>
                <Text c="#FA5252">*</Text>:{' '}
              </Flex>
              <NumberInput hideControls placeholder="Số câu" value={numberOfQuestion} onChange={onChangeExamNumberOfQuestion} />
            </Flex>
          )}
          <Flex direction="column" gap={16}>
            {poolStrategy === PoolStrategyEnum.WEIGHT ? (
              <PoolStrategyWeight
                disabledUpdate={disabledUpdate}
                totalFrequencyRandomQuizz={totalFrequencyRandomQuizz}
                handleAddQuizzPools={handleAddQuizzPools}
                handleChangeQuizzPool={handleChangeQuizzPool}
                handleRemoveQuizzPool={handleRemoveQuizzPool}
                tempQuizzPools={tempQuizzPools}
              />
            ) : (
              <PoolStrategyManual
                disabledUpdate={disabledUpdate}
                handleReplaceQuizzPools={handleReplaceQuizzPools}
                tempQuizzPools={tempQuizzPools}
              />
            )}
          </Flex>
        </Flex>
      </Box>
      <Flex w="100%" justify="flex-end" mt={24} gap={15}>
        <Button onClick={onClose} variant="outline">
          Hủy
        </Button>
        <Button disabled={isDisableSubmitBtn} onClick={onSubmit}>
          Xác nhận
        </Button>
      </Flex>
    </Fragment>
  );
};

interface IPoolStrategyWeight {
  disabledUpdate: boolean;
  totalFrequencyRandomQuizz?: number;
  tempQuizzPools: IQuizzPool[];
  handleAddQuizzPools: (quizzPool: IQuizzPool) => void;
  handleChangeQuizzPool: (quizzPools: IQuizzPool) => void;
  handleRemoveQuizzPool: (quizzPoolId: string) => void;
}

const PoolStrategyWeight = ({
  disabledUpdate,
  totalFrequencyRandomQuizz,
  tempQuizzPools,
  handleAddQuizzPools,
  handleChangeQuizzPool,
  handleRemoveQuizzPool,
}: IPoolStrategyWeight) => {
  const categoriesId = useMemo(() => tempQuizzPools.filter(t => t.sourceId != null).map(t => t.sourceId), [tempQuizzPools]);

  return (
    <Fragment>
      {tempQuizzPools.map(q => {
        const defaultCategory =
          q.sourceId != null && q.categoryName != null
            ? {
                id: Number(q.sourceId),
                title: q.categoryName,
              }
            : undefined;

        return (
          <Flex key={q.id} gap={16} align="center">
            <Box flex={1}>
              <SelectQuizzCategory
                disabled={disabledUpdate}
                allowClear={false}
                defaultSelected={defaultCategory}
                onSelectedItem={(_, data?: IQuizzCategory) => {
                  const newQ = structuredClone(q) as IQuizzPool;
                  if (categoriesId.includes(data?.id)) {
                    notiError({ message: 'Danh mục này đã được chọn, vui lòng chọn danh mục khác' });
                    handleChangeQuizzPool({
                      ...newQ,
                      sourceId: undefined,
                      categoryName: undefined,
                      metafields:
                        q.metafields && q.metafields?.length > 0
                          ? q.metafields?.map(m => {
                              if (m.key === QuizzPoolMetafieldKey.weight) return { ...m, value: '' };
                              return m;
                            })
                          : undefined,
                    });
                    return;
                  }
                  handleChangeQuizzPool({ ...newQ, sourceId: data?.id, categoryId: data?.id, categoryName: data?.title });
                }}
              />
            </Box>
            <NumberInput
              value={q.metafields?.find(m => m.key === QuizzPoolMetafieldKey.weight)?.value}
              onChange={value => {
                const newQ = structuredClone(q) as IQuizzPool;
                handleChangeQuizzPool({
                  ...newQ,
                  metafields:
                    newQ.metafields && newQ.metafields?.length > 0
                      ? newQ.metafields?.map(m => {
                          if (m.key === QuizzPoolMetafieldKey.weight) return { ...m, value: value.toString() };
                          return m;
                        })
                      : [
                          {
                            key: QuizzPoolMetafieldKey.weight,
                            value: value.toString(),
                          },
                        ],
                });
              }}
              disabled={q.sourceId == null || disabledUpdate}
              w={150}
              hideControls
              placeholder="Tần suất"
              rightSection={<Percent size={16} />}
            />
            {!disabledUpdate && (
              <ActionIcon onClick={() => q.id != null && handleRemoveQuizzPool(q.id.toString())} variant="transparent" c="#111928" mb="2px">
                <Trash size={20} />
              </ActionIcon>
            )}
          </Flex>
        );
      })}
      <Flex gap={16} align="center">
        <Flex justify="flex-start" flex={1}>
          <Button
            disabled={disabledUpdate}
            onClick={() => handleAddQuizzPools({ id: uuidv4() })}
            variant="outline"
            color="#4B5563"
            rightSection={<Plus size={16} />}
          >
            Thêm Danh mục
          </Button>
        </Flex>
        <Box w={150} ta="right">
          <Text
            c={(totalFrequencyRandomQuizz ?? 0) < 100 ? '#000000' : (totalFrequencyRandomQuizz ?? 0) > 100 ? '#fa5252' : '#12B886'}
            fz={14}
          >
            {totalFrequencyRandomQuizz ?? 0}%
          </Text>
        </Box>
        <ActionIcon style={{ opacity: '0' }} variant="transparent" c="#111928" mb="2px">
          <Trash size={20} />
        </ActionIcon>
      </Flex>
    </Fragment>
  );
};

interface IPoolStrategyManual {
  disabledUpdate: boolean;
  tempQuizzPools: IQuizzPool[];
  handleReplaceQuizzPools: (newQuizzPools: IQuizzPool[]) => void;
}

const PoolStrategyManual = ({ disabledUpdate, tempQuizzPools, handleReplaceQuizzPools }: IPoolStrategyManual) => {
  const [categories, setCategories] = useState<TempCategory[]>(
    tempQuizzPools.length > 0
      ? groupQuizzPoolsByCategory(tempQuizzPools)
      : [{ tempId: uuidv4(), categoryId: undefined, categoryName: undefined, tempQuizzPools: [] }]
  );

  const handleChangeCategory = (newCategory: TempCategory) => {
    setCategories(old =>
      old.map(o => {
        if (o.tempId === newCategory.tempId) {
          return newCategory;
        }
        return o;
      })
    );
  };

  const handleAddCategory = () => {
    setCategories(old => [...old, { tempId: uuidv4(), categoryId: undefined, categoryName: undefined, tempQuizzPools: [] }]);
  };

  const handleRemoveCategory = (tempCategoryId: string) => {
    setCategories(old => old.filter(o => o.tempId !== tempCategoryId));
  };

  const categoriesId = useMemo(() => categories.filter(t => t.categoryId != null).map(t => t.categoryId), [categories]);

  useEffect(() => {
    const newTempQuizzPools = categories
      .map(c => {
        if (c.tempQuizzPools.length > 0) return c.tempQuizzPools;
        else
          return [
            {
              id: uuidv4(),
              categoryName: c.categoryName,
              categoryId: c.categoryId,
              sourceId: undefined,
            },
          ];
      })
      .flat();
    handleReplaceQuizzPools(newTempQuizzPools);
  }, [categories]);

  return (
    <Fragment>
      {categories.map(c => (
        <PoolStrategyManualItem
          item={c}
          key={c.tempId}
          disabledUpdate={disabledUpdate}
          categoriesId={categoriesId}
          handleChangeCategory={handleChangeCategory}
          handleRemoveCategory={handleRemoveCategory}
        />
      ))}
      <Flex justify="flex-start">
        <Button disabled={disabledUpdate} onClick={handleAddCategory} variant="outline" color="#4B5563" rightSection={<Plus size={16} />}>
          Thêm Danh mục
        </Button>
      </Flex>
    </Fragment>
  );
};

interface PoolStrategyManualItemProps {
  item: TempCategory;
  disabledUpdate: boolean;
  categoriesId: (number | undefined)[];
  handleChangeCategory;
  handleRemoveCategory;
}

const PoolStrategyManualItem = ({
  item,
  disabledUpdate,
  categoriesId,
  handleChangeCategory,
  handleRemoveCategory,
}: PoolStrategyManualItemProps) => {
  const [openedModalSelectQuestion, { open: openModalSelectQuestion, close: closeModalSelectQuestion }] = useDisclosure(false);

  const totalQuizz = item.tempQuizzPools.length;
  const tempQuizzId = item.tempQuizzPools.map(t => t.sourceId).filter(t => t != null);
  const defaultCategory =
    item.categoryId != null && item.categoryName != null
      ? {
          id: item.categoryId,
          title: item.categoryName,
        }
      : undefined;

  return (
    <Flex key={item.tempId} gap={16} align="center">
      <Box flex={1}>
        <SelectQuizzCategory
          allowClear={false}
          disabled={disabledUpdate}
          defaultSelected={defaultCategory}
          onSelectedItem={(_, data?: IQuizzCategory) => {
            if (categoriesId.includes(data?.id)) {
              notiError({ message: 'Danh mục này đã được chọn, vui lòng chọn danh mục khác' });
              handleChangeCategory({
                ...item,
                categoryId: undefined,
                categoryName: undefined,
                tempQuizzPools: [],
              });
              return;
            }
            handleChangeCategory({
              ...item,
              categoryId: data?.id,
              categoryName: data?.title,
              tempQuizzPools: [],
            });
          }}
        />
      </Box>
      <Button
        w={250}
        onClick={openModalSelectQuestion}
        variant={totalQuizz > 0 ? 'default' : 'outline'}
        color={totalQuizz > 0 ? '#ffffff' : '#111928'}
        disabled={item.categoryId == null}
      >
        {totalQuizz > 0 ? `Đã chọn ${totalQuizz} câu` : 'Chọn câu hỏi'}
      </Button>
      {item.categoryId && item.categoryName && (
        <Modal
          centered
          size="1120px"
          opened={openedModalSelectQuestion}
          closeOnEscape={false}
          title={
            <Tooltip label={item.categoryName}>
              <Text c="#111928" fw={600} lineClamp={1}>
                {item.categoryName}
              </Text>
            </Tooltip>
          }
          onClose={closeModalSelectQuestion}
          styles={{ content: { overflow: 'unset !important' } }}
        >
          {openedModalSelectQuestion && (
            <ModalSelectQuizz
              disabledUpdate={disabledUpdate}
              onClose={closeModalSelectQuestion}
              onSubmit={(quizzs: IQuizz[]) =>
                handleChangeCategory({
                  ...item,
                  tempQuizzPools: quizzs.map(q => ({
                    categoryId: item.categoryId,
                    categoryName: item.categoryName,
                    sourceId: q.id,
                  })),
                })
              }
              category={defaultCategory}
              defaultSelectedIds={tempQuizzId as (string | number)[]}
            />
          )}
        </Modal>
      )}
      {!disabledUpdate && (
        <ActionIcon onClick={() => handleRemoveCategory(item.tempId)} variant="transparent" c="#111928" mb="2px">
          <Trash size={20} />
        </ActionIcon>
      )}
    </Flex>
  );
};
