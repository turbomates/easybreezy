import React, { useCallback } from "react";
import { List, Tag, Tooltip } from "antd";
import {
  CheckCircleOutlined,
  CloseCircleOutlined,
  EditOutlined,
} from "@ant-design/icons";
import { Absence as AbsenceModel } from "HumanResourceModels";
import { getAbsenceTitle, getAbsenceDescription } from "./absence.helper";

interface Props {
  absence: AbsenceModel;
  canEdit: boolean;
  approve: (id: string) => void;
  remove: (id: string) => void;
  openUpdateModal: (id: string) => void;
}

export const AbsenceListItem: React.FC<Props> = ({
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

  const title = getAbsenceTitle(absence);
  const description = getAbsenceDescription(absence);

  const actions = [
    <Tooltip title="Dismiss">
      <CloseCircleOutlined onClick={handleRemove} />
    </Tooltip>,
  ];

  if (!absence.isApproved) {
    actions.push(
      <Tooltip title="Approve">
        <CheckCircleOutlined onClick={handleApprove} />
      </Tooltip>,
      <Tooltip title="Edit">
        <EditOutlined onClick={handleOpenUpdate} />
      </Tooltip>,
    );
  }

  return (
    <List.Item actions={canEdit ? actions : []}>
      <List.Item.Meta title={title} description={description} />
      <AbsenceStatus isApproved={absence.isApproved} />
    </List.Item>
  );
};

const AbsenceStatus = (props: { isApproved: boolean }) =>
  props.isApproved ? (
    <Tag color="green">approved</Tag>
  ) : (
    <Tag color="red">Not approved</Tag>
  );
