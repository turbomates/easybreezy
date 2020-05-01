import { Button, Input } from "antd";
import React from "react";
import { CheckCircleOutlined, CloseCircleOutlined } from "@ant-design/icons";

import { FormField } from "./ProjectRoleForm";

interface Props {
  field: FormField;
  fieldIndex: number;
  createRole: (fieldIndex: number, value: string) => void;
  editInputRole: (fieldIndex: number) => void;
  setIndexOfEditableInput: (number: number) => void;
  inputRef: React.RefObject<Input>;
}

export const ProjectRoleFormEditRoleName: React.FC<Props> = ({
  field,
  fieldIndex,
  createRole,
  editInputRole,
  setIndexOfEditableInput,
  inputRef,
}) => {
  return (
    <td>
      <div className="role-form__input-wrapper">
        <Input
          defaultValue={field.name}
          ref={inputRef}
          onChange={(event) => createRole(fieldIndex, event.target.value)}
          className="role-form__input"
        />
        {!!field.id && (
          <div className="role-form__controls-btn">
            <Button onClick={() => editInputRole(fieldIndex)} type="primary">
              <CheckCircleOutlined />
            </Button>
            <Button onClick={() => setIndexOfEditableInput(-1)} type="danger">
              <CloseCircleOutlined />
            </Button>
          </div>
        )}
      </div>
    </td>
  );
};
