import React, { useRef } from "react";
import { Button, Input } from "antd";
import { CheckCircleOutlined, CloseCircleOutlined } from "@ant-design/icons";

import { FormField } from "./ProjectRoleForm";

interface Props {
  field: FormField;
  fieldIndex: number;
  editInputRole: (fieldIndex: number, value: string) => void;
  closeInputRole: () => void;
}

export const ProjectRoleFormEditRoleName: React.FC<Props> = ({
  field,
  editInputRole,
  closeInputRole,
  fieldIndex,
}) => {
  const inputRef = useRef<Input>(null);

  return (
    <td>
      <div className="role-form__input-wrapper">
        <Input
          defaultValue={field.name}
          onPressEnter={() =>
            editInputRole(fieldIndex, inputRef.current!.state.value)
          }
          ref={inputRef}
          className="role-form__input"
        />
        <div className="role-form__controls-btn">
          <Button
            onClick={() =>
              editInputRole(fieldIndex, inputRef.current!.state.value)
            }
            type="primary"
          >
            <CheckCircleOutlined />
          </Button>
          <Button onClick={closeInputRole} type="danger">
            <CloseCircleOutlined />
          </Button>
        </div>
      </div>
    </td>
  );
};
