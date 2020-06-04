import React, { useCallback } from "react";
import { List, Tag, Tooltip } from "antd";
import {
  CheckCircleTwoTone,
  CloseCircleTwoTone,
  EditTwoTone,
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
      <CloseCircleTwoTone
        onClick={handleRemove}
        twoToneColor="#ff7875"
      />
    </Tooltip>,
  ];

  if (!absence.isApproved) {
    actions.unshift(
      <Tooltip title="Approve">
        <CheckCircleTwoTone
          onClick={handleApprove}
          twoToneColor="#52c41a"
        />
      </Tooltip>,
      <Tooltip title="Edit">
        <EditTwoTone onClick={handleOpenUpdate} />
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
