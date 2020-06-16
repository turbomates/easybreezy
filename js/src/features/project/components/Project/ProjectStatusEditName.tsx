import React, { useRef } from "react";
import { Button, Input } from "antd";
import { CheckCircleOutlined, CloseCircleOutlined } from "@ant-design/icons";

type Props = {
  field: any;
  fieldIndex: number;
  editInputRole: (fieldIndex: number, value: string) => void;
  closeInputRole: () => void;
};

const minLengthRoleName = 2;

export const ProjectStatusEditName: React.FC<Props> = ({
  field,
  editInputRole,
  closeInputRole,
  fieldIndex,
}) => {
  const inputRef = useRef<Input>(null);

  function edit() {
    if (inputRef.current?.state.value.length >= minLengthRoleName) {
      editInputRole(fieldIndex, inputRef.current!.state.value);
    }
  }

  return (
    <div className="role-form__input-wrapper">
      <div>
        <Input
          defaultValue={field.name}
          onPressEnter={edit}
          ref={inputRef}
          className="role-form__input"
        />
      </div>
      <div className="role-form__controls-btn">
        <Button onClick={edit} type="primary">
          <CheckCircleOutlined />
        </Button>
        <Button onClick={closeInputRole} type="danger">
          <CloseCircleOutlined />
        </Button>
      </div>
    </div>
  );
};
