import React from "react";
import { Button } from "antd";
import { EditOutlined, DeleteOutlined } from "@ant-design/icons";

interface Props {
  name: string;
  removeRole: () => void;
  openInputRole: () => void;
}

export const ProjectRoleFormViewRoleName: React.FC<Props> = ({
  name,
  openInputRole,
  removeRole,
}) => {
  return (
    <td>
      <div className="role-form__input-wrapper">
        <div className="role-form__description">{name}</div>
        <div className="role-form__controls-btn">
          <Button
            onClick={openInputRole}
            type="primary"
            className="role-form__controls-btn"
          >
            <EditOutlined />
          </Button>

          <Button
            onClick={removeRole}
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
