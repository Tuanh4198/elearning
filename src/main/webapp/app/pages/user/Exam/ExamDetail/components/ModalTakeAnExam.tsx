import React, { forwardRef, useImperativeHandle, useMemo, useRef } from 'react';
import { Box, Button, Flex, Loader, LoadingOverlay, Modal, Text } from '@mantine/core';
import { useDisclosure } from '@mantine/hooks';
import { ArrowLeft, ArrowRight } from '@phosphor-icons/react';
import { TakeAnExamCompleted } from 'app/pages/user/Exam/ExamDetail/components/TakeAnExamComplete';
import { TakeAnExamFailed } from 'app/pages/user/Exam/ExamDetail/components/TakeAnExamFailed';
import { TakeAnExamLimited } from 'app/pages/user/Exam/ExamDetail/components/TakeAnExamLimited';
import { useModalConfirmExam } from 'app/pages/user/Exam/ExamDetail/components/ModalConfirmExam';
import { ExamMetafieldKey, IExam } from 'app/shared/model/exam.model';
import { useCountDownWorkingTime } from 'app/shared/hooks/useCountDownWorkingTime';
import { IQuizz } from 'app/shared/model/quizz.model';
import { useEnterAnswer } from 'app/pages/user/Exam/ExamDetail/hooks/useEnterAnswer';
import { useSubmitFormTest } from 'app/pages/user/Exam/ExamDetail/hooks/useSubmitFormTest';
import { QueryObserverResult, RefetchOptions } from '@tanstack/react-query';
import { AxiosResponse } from 'axios';
import { ExamEmployeeStatusEnum } from 'app/shared/model/enumerations/exam-employee-status-enum.model';
import { IExamEmployeeResult } from 'app/shared/model/exam-employee-result.model';

const CountDown = forwardRef(
  (
    {
      totalTime,
      onTimeOut,
    }: {
      totalTime: number;
      onTimeOut: () => void;
    },
    ref
  ) => {
    const { formatTime, setPlaying, setTime, setIsStopped } = useCountDownWorkingTime({
      autoPlay: true,
      pauseOnHidden: true,
      workingTime: totalTime,
      onTimeOut,
    });

    useImperativeHandle(ref, () => ({
      onPause() {
        setPlaying(false);
      },
      onReset() {
        setPlaying(true);
        setIsStopped(false);
        setTime(totalTime);
      },
      onStopped() {
        setIsStopped(true);
      },
    }));

    return <>{formatTime()}</>;
  }
);

interface IModalTakeAnExam {
  exam?: IExam;
  quizzs?: IQuizz[];
  onSubmitSuccess: (options?: RefetchOptions) => Promise<QueryObserverResult<AxiosResponse<IExamEmployeeResult, any> | undefined, Error>>;
}
interface IModalTakeAnExamBase {
  close: () => void;
}
const ModalTakeAnExam = ({ exam, quizzs, close, onSubmitSuccess }: IModalTakeAnExamBase & IModalTakeAnExam) => {
  const countDownRef = useRef<any>();

  const maxNumberOfTest = useMemo(
    () => Number(exam?.metafields?.filter(i => i.key === ExamMetafieldKey.max_number_of_test)?.[0]?.value || 0),
    [exam]
  );

  const totalTime = useMemo(
    () => Number(exam?.metafields?.filter(i => i.key === ExamMetafieldKey.working_time)?.[0]?.value || 0),
    [exam?.metafields]
  );

  const { answers, setAnswer, answersRef, renderFormAnswer } = useEnterAnswer();

  const { onSubmit, submiting, result, mutation, handleSetStartAt } = useSubmitFormTest({
    examId: exam?.id,
    answersRef,
    quizzs,
    totalTime,
    onSuccess: onSubmitSuccess,
  });

  const { onOpenModalConfirmExam: onOpenModalConfirmClose, renderModalConfirmExam: renderModalConfirmClose } = useModalConfirmExam({
    content: (
      <>
        Kết quả sẽ không được ghi nhận do <br /> bài thi chưa được hoàn thành. <br /> Bạn xác nhận thoát?
      </>
    ),
    okTitle: 'Xác nhận',
    onClickOk: close,
    hasClose: true,
  });

  const { onOpenModalConfirmExam: onOpenModalConfirmTimeout, renderModalConfirmExam: renderModalConfirmTimeout } = useModalConfirmExam({
    content: (
      <>
        Thời gian làm bài đã hết. <br /> Bạn vui lòng nộp bài nhé
      </>
    ),
    okTitle: 'Nộp bài',
    onClickOk: close,
    hasClose: false,
  });

  const { onOpenModalConfirmExam: onOpenModalConfirmSubmit, renderModalConfirmExam: renderModalConfirmSubmit } = useModalConfirmExam({
    content: 'Bạn chắc chắn muốn nộp bài?',
    okTitle: 'Nộp bài',
    onClickOk() {
      countDownRef?.current?.onStopped && countDownRef?.current?.onStopped();
      onSubmit();
    },
    hasClose: true,
  });

  const totalQuizz = useMemo(() => quizzs?.length, [quizzs]);

  const totalQuizzAnswered = useMemo(() => Object.values(answers).filter(it => it.answer.trim())?.length, [answers]);

  const renderResult = useMemo(() => {
    if (result?.status === ExamEmployeeStatusEnum.PASS) {
      return <TakeAnExamCompleted exam={exam} result={result} />;
    } else if (result?.status === ExamEmployeeStatusEnum.NOT_PASS) {
      if (result?.numberOfTest && result?.numberOfTest >= maxNumberOfTest) {
        return <TakeAnExamLimited exam={exam} result={result} />;
      }
      return (
        <TakeAnExamFailed
          onReWork={() => {
            countDownRef?.current?.onReset && countDownRef?.current?.onReset();
            handleSetStartAt();
            mutation.reset();
            setAnswer({});
          }}
          exam={exam}
          result={result}
        />
      );
    }
  }, [result]);

  return (
    <Box pos="relative">
      <LoadingOverlay zIndex={10} visible={submiting} loaderProps={{ children: <Loader /> }} />
      <div className="header-form">
        <Flex align="center" justify="flex-start" gap="8px" p="20px" mb="20px" bg="#ffffff">
          <Button
            variant="subtle"
            color="#111928"
            p="0"
            onClick={() => {
              if (result) {
                close();
              } else {
                onOpenModalConfirmClose();
              }
            }}
          >
            <ArrowLeft size={30} />
          </Button>
          <Text color="#000000" size="20px" fw="700">
            {exam?.title}
          </Text>
        </Flex>
        {!result && (
          <Flex pr="5px" className="quizz-action" align="center" mb="20px" justify="space-between">
            <Flex bg="white" w="270px" style={{ borderRadius: '16px', border: '2px solid #E5E7EB' }}>
              <Flex
                style={{ borderRight: '0.5px solid #E5E7EB' }}
                flex={1}
                px="20px"
                py="8px"
                align="center"
                justify="center"
                direction="column"
              >
                <Text color="#6B7280" size="12px" fw="500" mb="2px">
                  Số câu đã làm
                </Text>
                <Text color="#1F2A37" fw="600">
                  {totalQuizzAnswered}/{totalQuizz}
                </Text>
              </Flex>
              <Flex
                style={{ borderLeft: '0.5px solid #E5E7EB' }}
                flex={1}
                px="20px"
                py="8px"
                align="center"
                justify="center"
                direction="column"
              >
                <Text color="#6B7280" size="12px" fw="500" mb="2px">
                  Thời gian
                </Text>
                <Text color="#1F2A37" fw="600">
                  <CountDown
                    ref={countDownRef}
                    totalTime={Number(totalTime) * 60}
                    onTimeOut={() => {
                      onSubmit(true);
                      onOpenModalConfirmTimeout();
                    }}
                  />
                </Text>
              </Flex>
            </Flex>
            <Button
              variant="outline"
              color="#1F2A37"
              bg="white"
              radius="45px"
              size="lg"
              disabled={totalQuizz !== totalQuizzAnswered || !!result}
              onClick={onOpenModalConfirmSubmit}
              rightSection={<ArrowRight size={14} />}
            >
              Nộp bài
            </Button>
          </Flex>
        )}
      </div>
      <div className="body-form">
        {result ? (
          renderResult
        ) : (
          <Flex className="quizz-wrapper" direction="column" gap="20px" mb="20px" m="auto">
            {quizzs?.map(quizz => (
              <Flex key={quizz.id} className="quizz-item" direction="column" p="20px" gap="16px">
                <Text color="#1F2A37" size="16px" fw="600">
                  {quizz.content}
                </Text>
                {renderFormAnswer(quizz)}
              </Flex>
            ))}
          </Flex>
        )}
      </div>
      {renderModalConfirmTimeout()}
      {renderModalConfirmClose()}
      {renderModalConfirmSubmit()}
    </Box>
  );
};

export const useModalTakeAnExam = () => {
  const [opened, { open, close }] = useDisclosure(false);

  return {
    openedModalTakeAnExam: opened,
    onOpenModalTakeAnExam: open,
    renderModalTakeAnExam: (props: IModalTakeAnExam) => (
      <Modal
        closeOnEscape={false}
        opened={opened}
        onClose={close}
        title={undefined}
        fullScreen
        radius={0}
        closeButtonProps={{ hidden: true }}
        className={`exam-modal modal-take-an-exam ${opened}`}
      >
        {opened && <ModalTakeAnExam close={close} {...props} />}
      </Modal>
    ),
  };
};
