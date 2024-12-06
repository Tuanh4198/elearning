import { Flex, Input, StyleProp, Tooltip } from '@mantine/core';
import { Clock, WarningCircle } from '@phosphor-icons/react';
import React, { ReactNode, useEffect, useState } from 'react';
import InputMask from 'react-input-mask';

interface InputSecondProps {
  w: StyleProp<React.CSSProperties['width']>;
  defaultValue?: number;
  onChange: (value: number) => void;
  label: React.ReactNode;
  tooltipValue?: string;
  error?: ReactNode;
}

function convertToMinuteSecond(seconds) {
  const minutes = Math.floor(seconds / 60);
  const remainingSeconds = seconds % 60;
  const formattedMinutes = String(minutes).padStart(2, '0');
  const formattedSeconds = String(remainingSeconds).padStart(2, '0');
  return `${formattedMinutes}:${formattedSeconds}`;
}

export const InputSecond = ({ w, error, defaultValue, onChange, label, tooltipValue }: InputSecondProps) => {
  const [value, setValue] = useState<string | undefined>(defaultValue ? convertToMinuteSecond(defaultValue) : undefined);

  useEffect(() => {
    if (value && !value?.includes('_')) {
      const [minutes, seconds] = value.split(':');
      onChange(Number(minutes) * 60 + Number(seconds));
    }
  }, [value]);

  const onChangeValue = (e: any) => {
    setValue(e.target.value);
  };

  const onBlur = (e: React.FocusEvent<HTMLInputElement>) => {
    let [minutes, seconds] = e.target.value.split(':');
    if (!seconds || seconds === '__') {
      seconds = '00';
    } else if (seconds.startsWith('_')) {
      seconds = `0${seconds.replace('_', '')}`;
    } else if (seconds.endsWith('_')) {
      seconds = `${seconds.replace('_', '')}0`;
    }
    if (!minutes || minutes === '__') {
      minutes = '00';
    } else if (minutes.includes('_')) {
      minutes = `0${minutes.replace('_', '')}`;
    }
    setValue(`${minutes}:${seconds}`);
  };

  return (
    <Input.Wrapper
      w={w}
      className="input-second-wrapper"
      label={
        <Flex align="center" gap={5}>
          {label}
          {tooltipValue && (
            <Tooltip label={tooltipValue}>
              <WarningCircle size={16} weight="fill" color="#4B5563" />
            </Tooltip>
          )}
        </Flex>
      }
      error={error}
    >
      <Flex
        gap={12}
        direction="row"
        align="center"
        className="mantine-Input-wrapper mantine-Select-wrapper"
        style={{
          border: 'calc(0.0625rem* 1) solid #ced4da',
          borderRadius: 'calc(0.5rem* 1)',
          padding: '0px 12px',
          overflow: 'hidden',
          width: '100%',
        }}
      >
        <InputMask
          mask="99:99"
          placeholder="mm:ss"
          className="mantine-Input-input mantine-Select-input"
          style={{ border: 'none', height: 'calc(2.25rem * 1)', width: '100%', flex: 1 }}
          value={value}
          onChange={onChangeValue}
          onBlur={onBlur}
        />
        <Clock size={16} />
      </Flex>
    </Input.Wrapper>
  );
};
