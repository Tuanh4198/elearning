import React, { useCallback, useMemo } from 'react';
import Highcharts from 'highcharts';
import HighchartsReact from 'highcharts-react-official';
import windbarb from 'highcharts/modules/windbarb';
import highchartsMore from 'highcharts/highcharts-more';
import { CapacityType, ICapacites } from 'app/pages/user/Home/hooks/useFetchEmployeeCapacity';
import { Box, Loader, LoadingOverlay } from '@mantine/core';

highchartsMore(Highcharts);
windbarb(Highcharts);

export const BlockCapacityChart = ({ isLoading, employeeCapacity }: { isLoading: boolean; employeeCapacity?: ICapacites[] }) => {
  const data = useMemo(() => {
    const specialty: ICapacites[] = [];
    const leadership: ICapacites[] = [];
    const coreCompetency: ICapacites[] = [];
    if (employeeCapacity && employeeCapacity.length > 0) {
      employeeCapacity.forEach(i => {
        switch (i.type) {
          case CapacityType.SPECIALTY:
            specialty.push(i);
            break;
          case CapacityType.LEADERSHIP:
            leadership.push(i);
            break;
          case CapacityType.CORE_COMPETENCY:
            coreCompetency.push(i);
            break;
          default:
            break;
        }
      });
      return [...specialty, ...leadership, ...coreCompetency];
    }
    return [];
  }, [employeeCapacity]);

  const chartData = useCallback(
    (type: CapacityType, value: 'real' | 'target') =>
      data?.map(c => {
        if (c.type === type) {
          return {
            y: value === 'real' ? c.realCapacity : c.targetCapacity - c.realCapacity,
            label: value === 'real' ? c.targetCapacity.toString() : undefined,
            states: value === 'real' ? undefined : { hover: { enabled: false } },
          };
        }
        return { y: undefined };
      }) || [],
    [data]
  );

  const options: Highcharts.Options = {
    chart: {
      polar: true,
      type: 'column',
      spacing: [20, 0, 20, 0],
      height: '120%',
    },
    title: {
      text: undefined,
    },
    pane: {
      size: '100%',
      innerSize: 100,
    },
    legend: {
      align: 'left',
      verticalAlign: 'bottom',
      y: 0,
      x: 0,
      width: '100%',
      layout: 'horizontal',
      padding: 0,
      useHTML: true,
      itemStyle: {
        fontSize: '12px',
        fontWeight: '500',
        height: 20,
      },
      labelFormatter() {
        if (Object.values(CapacityType).includes(this.name as CapacityType)) return '';
        return `<div style="position: relative; top: -5px;">${this.name}</div>`;
      },
      itemMarginTop: 12,
      symbolRadius: 4,
      symbolWidth: 20,
      symbolHeight: 20,
    },
    xAxis: {
      tickmarkPlacement: 'on',
      lineWidth: 0,
      title: {
        text: undefined,
      },
      labels: {
        enabled: false,
      },
      categories: data?.map(c => c.criteria),
      gridLineColor: 'rgba(15, 24, 35, 0.5)',
      gridLineDashStyle: 'Dash',
      gridLineWidth: 0,
      lineColor: '#C3FAE8',
    },
    yAxis: {
      min: 0,
      tickInterval: 1,
      endOnTick: false,
      showLastLabel: true,
      title: {
        text: undefined,
      },
      labels: {
        format: '{value}',
      },
      gridLineColor: 'rgba(15, 24, 35, 0.1)',
      gridLineDashStyle: 'Dash',
    },
    tooltip: {
      followPointer: true,
      backgroundColor: '#1F2A37',
      padding: 8,
      style: {
        borderRadius: 4,
        color: '#FFFFFF',
        fontSize: '12px',
      },
      useHTML: true,
      formatter(this: any) {
        const maxValue = this.point?.label;
        if (Object.values(CapacityType).includes(this.series.name)) return false;
        return `<div style="line-height: 16px; width: 130px; text-align: center;">
          <span style="white-space: pre-wrap;">${this.x}</span> <br />
          ${this.y}${maxValue ? `/${maxValue}` : ''}
        </div>`;
      },
    },
    plotOptions: {
      series: {
        stacking: 'normal',
        shadow: false,
        gapSize: 0,
        pointPlacement: 'on',
        pointRange: 2,
      },
    },
    series: [
      {
        name: CapacityType.LEADERSHIP,
        color: '#D0EBFF',
        type: 'column',
        data: chartData(CapacityType.LEADERSHIP, 'target'),
        enableMouseTracking: false,
        showInLegend: false,
      },
      {
        name: CapacityType.CORE_COMPETENCY,
        color: '#C3FAE8',
        type: 'column',
        data: chartData(CapacityType.CORE_COMPETENCY, 'target'),
        enableMouseTracking: false,
        showInLegend: false,
      },
      {
        name: CapacityType.SPECIALTY,
        color: '#FFEECD',
        type: 'column',
        data: chartData(CapacityType.SPECIALTY, 'target'),
        enableMouseTracking: false,
        showInLegend: false,
      },
      {
        name: 'Năng lực lãnh đạo',
        color: '#228BE6',
        type: 'column',
        data: chartData(CapacityType.LEADERSHIP, 'real'),
      },
      {
        name: 'Năng lực cốt lõi',
        color: '#12B886',
        colorAxis: '#C3FAE8',
        type: 'column',
        data: chartData(CapacityType.CORE_COMPETENCY, 'real'),
      },
      {
        name: 'Năng lực chuyên môn',
        color: '#FAB005',
        type: 'column',
        data: chartData(CapacityType.SPECIALTY, 'real'),
      },
    ],
  };

  return (
    <Box pos="relative">
      <LoadingOverlay style={{ borderRadius: '8px' }} zIndex={10} visible={isLoading} loaderProps={{ children: <Loader /> }} />
      <HighchartsReact highcharts={Highcharts} options={options} />
    </Box>
  );
};
