import React from "react";
import { List } from "antd";
import { Absence as AbsenceModel } from "HumanResourceModels";
import { Absence } from "./Abcence";

interface Props {
  absences: AbsenceModel[];
  canEdit: boolean;
  approve: (id: string) => any;
  remove: (id: string) => any;
  openUpdateModal: (id: string) => any;
}

export const Absences: React.FC<Props> = ({
  absences,
  canEdit,
  approve,
  remove,
  openUpdateModal,
}) => {
  if (!absences.length) return null;

  return (
    <List
      dataSource={absences}
      renderItem={(absence) => (
        <Absence
          absence={absence}
          approve={approve}
          canEdit={canEdit}
          remove={remove}
          openUpdateModal={openUpdateModal}
        />
      )}
    />
  );
};
