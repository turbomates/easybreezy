import { Button } from "antd";
import React from "react";
import { EditOutlined, DeleteOutlined } from "@ant-design/icons";

interface Props {
  name: string;
  fieldIndex: number;
  removeRole: (fieldIndex: number) => void;
  setIndexOfEditableInput: (number: number) => void;
}

export const ProjectRoleFormViewRoleName: React.FC<Props> = ({
  name,
  fieldIndex,
  setIndexOfEditableInput,
  removeRole,
}) => {
  return (
    <td>
      <div className="role-form__input-wrapper">
        <div className="role-form__description">{name}</div>
        <div className="role-form__controls-btn">
          <Button
            onClick={() => setIndexOfEditableInput(fieldIndex)}
            type="primary"
            className="role-form__controls-btn"
          >
            <EditOutlined />
          </Button>

          <Button
            onClick={() => removeRole(fieldIndex)}
            type="danger"
            className="role-form__controls-btn"
          >
            <DeleteOutlined />
          </Button>
        </div>
      </div>
    </td>
  );
};
