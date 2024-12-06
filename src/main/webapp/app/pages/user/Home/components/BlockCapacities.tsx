import React, { useCallback, useMemo, useState } from 'react';
import { Accordion, Box, Flex, Progress, Skeleton, Tabs, Text, Tooltip } from '@mantine/core';
import { CapacityType, IBehaviorCapacities, ICapacites } from 'app/pages/user/Home/hooks/useFetchEmployeeCapacity';
import { Empty } from 'app/shared/components/Empty';

const Capacity = {
  [CapacityType.CORE_COMPETENCY]: {
    label: 'Năng lực\ncốt lõi',
    value: CapacityType.CORE_COMPETENCY,
    color: '#12B886',
  },
  [CapacityType.SPECIALTY]: {
    label: 'Năng lực\nchuyên môn',
    value: CapacityType.SPECIALTY,
    color: '#FAB005',
  },
  [CapacityType.LEADERSHIP]: {
    label: 'Năng lực\nlãnh đạo',
    value: CapacityType.LEADERSHIP,
    color: '#228BE6',
  },
};

export const BlockCapacities = ({
  isLoading,
  employeeCapacity,
  employeeBehaviorCapacity,
}: {
  isLoading: boolean;
  employeeCapacity?: ICapacites[];
  employeeBehaviorCapacity?: IBehaviorCapacities[];
}) => {
  const [activeTab, setActiveTab] = useState<CapacityType>(CapacityType.CORE_COMPETENCY);

  return (
    <Tabs value={activeTab} onChange={value => setActiveTab(value as CapacityType)}>
      <Tabs.List mb="20px">
        {Object.values(Capacity).map(item => (
          <Tabs.Tab bg={activeTab === item.value ? item.color : undefined} key={item.value} value={item.value}>
            <Text span={false} size="14px" fw={600} color={activeTab === item.value ? '#FCFCFD' : item.color}>
              {item.label}
            </Text>
          </Tabs.Tab>
        ))}
      </Tabs.List>
      {Object.values(Capacity).map(item => (
        <Tabs.Panel key={item.value} value={item.value}>
          <TabsPanelContent
            capacities={employeeCapacity}
            behaviorCapacities={employeeBehaviorCapacity}
            capacityType={item.value}
            isLoading={isLoading}
          />
        </Tabs.Panel>
      ))}
    </Tabs>
  );
};

const TabsPanelContent = ({
  capacities,
  behaviorCapacities,
  capacityType,
  isLoading,
}: {
  capacities?: ICapacites[];
  behaviorCapacities?: IBehaviorCapacities[];
  capacityType: CapacityType;
  isLoading: boolean;
}) => {
  const filterCapacitiesByType = useMemo(() => capacities?.filter(c => c.type === capacityType), [capacities, capacityType]);

  const filterBehaviorInCriteria = useCallback(
    (criteria: string) => behaviorCapacities?.filter(b => b.criteria === criteria) || [],
    [behaviorCapacities]
  );

  return (
    <Accordion variant="filled">
      {isLoading ? (
        <Flex direction="column" gap={12}>
          {Array.from({ length: 4 }).map((_, i) => (
            <Skeleton key={i} height={37} width="100%" />
          ))}
        </Flex>
      ) : filterCapacitiesByType && filterCapacitiesByType?.length > 0 ? (
        filterCapacitiesByType.map(c => (
          <Accordion.Item key={c.id} value={c.id.toString()} bd="none" bg="white">
            <Accordion.Control p={0}>
              <Flex align="center" justify="space-between" gap="12px" mr="12px">
                <Tooltip position="top-start" multiline maw="500px" label={c.criteria}>
                  <Text c="#1F2A37" size="14px" fw={500} lineClamp={1}>
                    {c.criteria}
                  </Text>
                </Tooltip>
                <Text c="#1F2A37" size="16px" fw={600} style={{ whiteSpace: 'nowrap' }}>
                  {c.realCapacity}/{c.targetCapacity}
                </Text>
              </Flex>
            </Accordion.Control>
            <Accordion.Panel p={0}>
              <Flex direction="column" gap="8px">
                {filterBehaviorInCriteria(c.criteria) && filterBehaviorInCriteria(c.criteria)?.length > 0 ? (
                  filterBehaviorInCriteria(c.criteria).map(b => {
                    return b.behavior ? (
                      <Flex key={b.id} direction="column" bd="1px solid #D0D5DD" p="12px" style={{ borderRadius: '8px' }}>
                        <Flex align="center" justify="space-between" gap="16px">
                          <Tooltip position="top-start" multiline maw="500px" label={b.behavior}>
                            <Text c="#1F2A37" size="14px" lineClamp={2}>
                              {b.behavior}
                            </Text>
                          </Tooltip>
                          <Text c="#1F2A37" size="16px" fw={600} style={{ whiteSpace: 'nowrap' }}>
                            {b.realCapacity}
                          </Text>
                        </Flex>
                        <Box pos="relative" mt="12px">
                          <Flex justify="space-evenly" pos="absolute" top={0} left={0} w="100%" h="100%" style={{ zIndex: 10 }}>
                            {Array.from({ length: b.targetCapacity - 1 }).map((_, i) => (
                              <div
                                key={i}
                                style={{
                                  height: '100%',
                                  background: 'white',
                                  width: '1px',
                                }}
                              />
                            ))}
                          </Flex>
                          <Progress value={(b.realCapacity * 100) / b.targetCapacity} color="#175CD3" size="md" />
                        </Box>
                      </Flex>
                    ) : null;
                  })
                ) : (
                  <Empty description="Không có dữ liệu" />
                )}
              </Flex>
            </Accordion.Panel>
          </Accordion.Item>
        ))
      ) : (
        <Empty description="Không có dữ liệu" />
      )}
    </Accordion>
  );
};
