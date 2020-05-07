import React from "react";
import { List } from "antd";
import { EmployeeNote } from "HumanResourceModels";

interface Props {
  notes: EmployeeNote[];
  canEdit: boolean;
}

export const Notes: React.FC<Props> = ({ notes, canEdit }) => {
  if (!notes.length) return null;

  return (
    <List
      dataSource={notes}
      renderItem={(note) => (
        <List.Item>
          <List.Item.Meta description={note.createdAt} />
          {note.text}
        </List.Item>
      )}
    />
  );
};
