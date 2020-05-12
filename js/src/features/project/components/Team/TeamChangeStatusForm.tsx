import React, { useCallback, useMemo } from "react";
import { Form, Select } from "antd";

import { ProjectTeamStatusRequest } from "ProjectModels";
import { useFormServerErrors } from "../../../../hooks/useFormServerErrors";
import { switchProjectTeamStatus } from "../../helpers";
import { FormErrorMap } from "MyTypes";

const { Option } = Select;

type Props = {
  status: string;
  onChange: (status: ProjectTeamStatusRequest) => void;
  errors: FormErrorMap;
};

export const TeamChangeStatusForm: React.FC<Props> = ({
  status,
  onChange,
  errors,
}) => {
  const initialValues = useMemo(() => ({ status }), [status]);
  const [form] = Form.useForm();

  const onFinish = useCallback(
    (values: any) => onChange(switchProjectTeamStatus(values)),
    [onChange],
  );

  useFormServerErrors(form, errors, ["status"]);

  return (
    <Form
      form={form}
      labelCol={{ span: 2 }}
      onFinish={onFinish}
      initialValues={initialValues}
    >
      <Form.Item name="status" className="status-form__select">
        <Select onChange={onFinish}>
          <Option value="Active" className="status-form__option">
            <span>Activate</span>
          </Option>
          <Option value="Closed" className="status-form__option">
            <span>Close</span>
          </Option>
        </Select>
      </Form.Item>
    </Form>
  );
};
