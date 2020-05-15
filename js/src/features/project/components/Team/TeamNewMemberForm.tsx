import React, { useCallback } from "react";
import { Button, Form, Select } from "antd";

import { NewProjectTeamMemberRequest, Role } from "ProjectModels";
import { Choice } from "MyTypes";

const { Option } = Select;

type Props = {
  teamId: string;
  roles: Role[];
  add: (form: NewProjectTeamMemberRequest) => void;
  employeesSelectOptions: Choice[];
}

const formItemLayout = {
  labelCol: { span: 4 },
};
const formTailLayout = {
  wrapperCol: { span: 8, offset: 4 },
};

export const TeamNewMemberForm: React.FC<Props> = ({
  teamId,
  roles,
  add,
  employeesSelectOptions,
}) => {
  const [form] = Form.useForm();

  const onFinish = useCallback(
    (values) =>
      add({
        user: values.user,
        role: values.role,
        teamId,
      }),
    [add, teamId],
  );

  return (
    <Form form={form} {...formItemLayout} onFinish={onFinish}>
      <Form.Item
        label="User"
        name="user"
        rules={[{ required: true, message: "Please input user" }]}
      >
        <Select
          showSearch
          filterOption={(input, option) => option?.children.includes(input)}
          optionFilterProp="children"
          notFoundContent={null}
          showArrow={false}
        >
          {employeesSelectOptions.map(({ label, value }) => (
            <Option value={value} key={value}>
              {label}
            </Option>
          ))}
        </Select>
      </Form.Item>

      <Form.Item
        label="Role"
        name="role"
        rules={[{ required: true, message: "Please input role" }]}
      >
        <Select>
          {roles.map((role) => (
            <Option value={role.id} key={role.id}>
              {role.name}
            </Option>
          ))}
        </Select>
      </Form.Item>

      <Form.Item {...formTailLayout}>
        <Button type="primary" htmlType="submit">
          Add
        </Button>
      </Form.Item>
    </Form>
  );
};
