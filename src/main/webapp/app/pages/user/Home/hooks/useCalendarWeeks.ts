import { useEffect, useMemo, useRef, useState } from 'react';

export const dayOfWeeks = ['T2', 'T3', 'T4', 'T5', 'T6', 'T7', 'CN'];

export const getWeekDates = (date: Date = new Date()): Date[] => {
  const startOfWeek = new Date(date.setDate(date.getDate() - date.getDay() + 1));
  const weekDates: Date[] = [];
  for (let i = 0; i < 7; i++) {
    const currentDay = new Date(startOfWeek);
    currentDay.setDate(startOfWeek.getDate() + i);
    weekDates.push(currentDay);
  }
  return weekDates;
};

export const formatDate = (date: Date, fullYear?: boolean): string => {
  const day = String(date.getDate()).padStart(2, '0');
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const year = date.getFullYear();
  return fullYear ? `${day} Th${month} ${year}` : `${day}/${month}`;
};

export const useCalendarWeeks = () => {
  const currentDateIndex = useRef<number>(0);

  const [currentWeekDate, setCurrentWeekDate] = useState<Date>(new Date());
  const [selectedDate, setSelectedDate] = useState<Date>(new Date());

  const getNextWeek = (date: Date): Date => {
    const nextWeek = new Date(date);
    nextWeek.setDate(date.getDate() + 7);
    return nextWeek;
  };

  const getPreviousWeek = (date: Date): Date => {
    const previousWeek = new Date(date);
    previousWeek.setDate(date.getDate() - 7);
    return previousWeek;
  };

  const setNewSelectedDate = (value: Date) => {
    const currentWeeks = getWeekDates(value);
    setSelectedDate(currentWeeks[currentDateIndex.current]);
  };

  const showNextWeek = () => {
    const nextWeek = getNextWeek(currentWeekDate);
    setNewSelectedDate(nextWeek);
    setCurrentWeekDate(nextWeek);
  };

  const showPreviousWeek = () => {
    const previousWeek = getPreviousWeek(currentWeekDate);
    setNewSelectedDate(previousWeek);
    setCurrentWeekDate(previousWeek);
  };

  const currentWeekDates = getWeekDates(currentWeekDate);

  useEffect(() => {
    currentDateIndex.current = getWeekDates(currentWeekDate).findIndex(d => formatDate(new Date(), true) === formatDate(d, true));
  }, []);

  return {
    showPreviousWeek,
    showNextWeek,
    currentWeekDates,
    selectedDate,
    setSelectedDate,
  };
};
