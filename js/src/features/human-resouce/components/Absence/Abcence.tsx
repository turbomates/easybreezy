import React, { useCallback } from "react";
import { List, Tag, Tooltip } from "antd";
import {
  CheckCircleOutlined,
  CloseCircleOutlined,
  EditOutlined,
} from "@ant-design/icons";
import { Absence as AbsenceModel } from "HumanResourceModels";

interface Props {
  absence: AbsenceModel;
  canEdit: boolean;
  approve: (id: string) => any;
  remove: (id: string) => any;
  openUpdateModal: (id: string) => any;
}

export const Absence: React.FC<Props> = ({
  absence,
  canEdit,
  approve,
  remove,
  openUpdateModal,
}) => {
  const handleApprove = useCallback(() => {
    if (!absence.isApproved) {
      approve(absence.id);
    }
  }, [absence, approve]);

  const handleRemove = useCallback(() => {
    remove(absence.id);
  }, [absence, remove]);

  const handleOpenUpdate = useCallback(() => {
    openUpdateModal(absence.id);
  }, [absence, openUpdateModal]);

  const title = `${absence.startedAt} ${absence.endedAt}`;
  const description = `${absence.reason}: ${absence.comment}`;
  const content = absence.isApproved ? (
    <Tag color="green">approved</Tag>
  ) : (
    <Tag color="red">Not approved</Tag>
  );

  const actions = [
    <Tooltip title="Remove">
      <CloseCircleOutlined onClick={handleRemove} />
    </Tooltip>,
    <Tooltip title="Edit">
      <EditOutlined onClick={handleOpenUpdate} />
    </Tooltip>,
  ];

  if (!absence.isApproved) {
    actions.push(
      <Tooltip title="Approve">
        <CheckCircleOutlined
          onClick={handleApprove}
          disabled={!absence.isApproved}
        />
      </Tooltip>,
    );
  }

  return (
    <List.Item actions={actions}>
      <List.Item.Meta title={title} description={description} />
      {content}
    </List.Item>
  );
};
