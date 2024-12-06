import { useEffect, useRef, useState } from 'react';

export const useCountDownWorkingTime = ({
  autoPlay = true,
  pauseOnHidden,
  workingTime,
  onTimeOut,
}: {
  autoPlay: boolean;
  pauseOnHidden?: boolean;
  workingTime: number;
  onTimeOut: () => void;
}) => {
  const onTimeOutRef = useRef<() => void>();
  const [time, setTime] = useState(workingTime);
  const [playing, setPlaying] = useState<boolean>(true);
  const [isStopped, setIsStopped] = useState<boolean>(false);

  useEffect(() => {
    onTimeOutRef.current = onTimeOut;
  }, [onTimeOut]);

  useEffect(() => {
    if (!autoPlay) return;
    if (pauseOnHidden && !playing) return;
    if (isStopped) return;
    if (time <= 0) {
      if (onTimeOutRef.current) onTimeOutRef.current();
      return;
    }
    const timerId = setInterval(() => {
      setTime(prevTime => prevTime - 1);
    }, 1000);
    return () => clearInterval(timerId);
  }, [time, autoPlay, playing, isStopped]);

  useEffect(() => {
    if (!pauseOnHidden) return;
    const handleVisibilityChange = () => {
      if (document.visibilityState === 'hidden') {
        pauseOnHidden && setPlaying(false);
      } else {
        pauseOnHidden && setPlaying(true);
      }
    };
    document.addEventListener('visibilitychange', handleVisibilityChange);
    return () => {
      document.removeEventListener('visibilitychange', handleVisibilityChange);
    };
  }, []);

  const formatTime = () => {
    const min = Math.floor(time / 60);
    const sec = time % 60;
    return `${String(min).padStart(2, '0')}:${String(sec).padStart(2, '0')}`;
  };

  return {
    time,
    setTime,
    playing,
    setPlaying,
    isStopped,
    setIsStopped,
    formatTime,
  };
};
