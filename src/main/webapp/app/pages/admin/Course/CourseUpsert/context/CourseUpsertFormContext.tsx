import React, { createContext, ReactElement } from 'react';
import { FormErrors, useForm, UseFormReturnType } from '@mantine/form';
import { useChooseFile } from 'app/pages/admin/Course/CourseUpsert/hooks/useChooseFile';
import { FileWithPath } from '@mantine/dropzone';

export type IFormCourse = any;

export type TCourseUpsertFormContext = {
  form?: UseFormReturnType<IFormCourse, (values: IFormCourse) => IFormCourse>;
  errors?: FormErrors;
  setFiles: React.Dispatch<React.SetStateAction<FileWithPath[] | undefined>>;
  previewFiles?: React.JSX.Element[] | null;
};

export const CourseUpsertFormContext = createContext<TCourseUpsertFormContext>({
  form: undefined,
  errors: undefined,
  setFiles() {},
  previewFiles: undefined,
});

type TOkrInteractionContextProps = {
  initialValues: any;
  children: ReactElement;
};

export const CourseUpsertFormContextView = ({ initialValues, children }: TOkrInteractionContextProps) => {
  const { setFiles, previewFiles } = useChooseFile();

  const form = useForm<IFormCourse>({
    mode: 'controlled',
    initialValues,
    validate: {},
  });

  return (
    <CourseUpsertFormContext.Provider
      value={{
        form,
        errors: form.errors,
        setFiles,
        previewFiles,
      }}
    >
      {children}
    </CourseUpsertFormContext.Provider>
  );
};
