import { nodeContainerLabel } from 'app/pages/admin/Navigator/NavigatorUpsert/helper';
import { NodeType } from 'app/shared/model/enumerations/node-type-enum.model';
import { notiError, notiSuccess } from 'app/shared/notifications';
import axios from 'axios';
import React from 'react';
import { useState } from 'react';

export const useDeleteNode = () => {
  const [deletingNode, setDeletingNode] = useState(false);

  const deleteNode = async ({
    ids,
    nodeType,
    onSucceed,
    onFailed,
  }: {
    ids: (string | number)[];
    nodeType: NodeType;
    onSucceed: () => void;
    onFailed: () => void;
  }) => {
    setDeletingNode(true);
    try {
      for (const id of ids) {
        await axios.delete(`/api/nodes/${id}`);
      }
      onSucceed();
      notiSuccess({ message: `Xóa ${nodeContainerLabel[nodeType]} thành công` });
    } catch (error: any) {
      console.error('Delete error: ', error);
      const errMsg = error?.response?.data?.errors?.message?.[0];
      notiError({
        message: (
          <>
            Xóa {nodeContainerLabel[NodeType.ISLAND_AREA]} thất bại, vui lòng thử lại sau! <br />
            {errMsg}
          </>
        ),
      });
      onFailed();
    }
    setDeletingNode(false);
  };

  return {
    deletingNode,
    deleteNode,
  };
};
