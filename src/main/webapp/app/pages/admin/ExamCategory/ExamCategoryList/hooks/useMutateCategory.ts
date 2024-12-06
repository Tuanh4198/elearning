import { useMutation } from '@tanstack/react-query';
import { notiError, notiSuccess } from 'app/shared/notifications';
import { addCategories, deleteCategory, ICategoryRequest, updateCategory } from 'app/pages/admin/QuizzCategory/QuizzCategoryList/apis';

interface IMutateCategory {
  onSuccess: () => void;
  onFailed: () => void;
}

export const useMutateCategory = (params: IMutateCategory) => {
  const addMutation = useMutation({
    mutationFn: addCategories,
    onSuccess() {
      notiSuccess({ message: 'Tạo danh mục thành công' });
      params.onSuccess();
    },
    onError(error) {
      console.error('Error posting data:', error);
      notiError({ message: 'Tạo danh mục thất bại!' });
      params.onFailed();
    },
  });

  const onAdd = (request: ICategoryRequest) => {
    addMutation.mutate(request);
  };

  const updateMutation = useMutation({
    mutationFn: updateCategory,
    onSuccess() {
      notiSuccess({ message: 'Cập nhật danh mục thành công' });
      params.onSuccess();
    },
    onError(error) {
      console.error('Error posting data:', error);
      notiError({ message: 'Cập nhật danh mục thất bại!' });
      params.onFailed();
    },
  });

  const onUpdate = (request: ICategoryRequest) => {
    updateMutation.mutate(request);
  };

  const deleteMutation = useMutation({
    mutationFn: deleteCategory,
    onSuccess() {
      notiSuccess({ message: 'Xóa danh mục thành công' });
      params.onSuccess();
    },
    onError(error) {
      console.error('Error posting data:', error);
      notiError({ message: 'Xóa danh mục thất bại!' });
      params.onFailed();
    },
  });

  const onDelete = (id?: number) => {
    if (id != null) {
      deleteMutation.mutate(id);
    }
  };

  return {
    onAdd,
    adding: addMutation.isPending,
    onUpdate,
    updating: updateMutation.isPending,
    onDelete,
    deleting: deleteMutation.isPending,
  };
};
