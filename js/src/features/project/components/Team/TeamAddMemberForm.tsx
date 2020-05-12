import React, { useCallback, useState } from "react";
import { Button, Form, Select } from "antd";

import { AddProjectTeamMemberRequest, Role } from "ProjectModels";
import { Choice } from "MyTypes";

const { Option } = Select;

type Props = {
  teamId: string;
  roles: Role[];
  add: (form: AddProjectTeamMemberRequest) => void;
  employees: Choice[];
}

const formItemLayout = {
  labelCol: { span: 4 },
};
const formTailLayout = {
  wrapperCol: { span: 8, offset: 4 },
};

export const TeamAddMemberForm: React.FC<Props> = ({
  teamId,
  roles,
  add,
  employees,
}) => {
  const [form] = Form.useForm();
  const [searchResult, setSearchResult] = useState<Choice[]>([]);

  const onFinish = useCallback(
    (values) =>
      add({
        user: values.user,
        role: values.role,
        teamId,
      }),
    [add, teamId],
  );

  const onSearch = (searchText: string) => {
    const name = searchText.trim();

    if (!name.length) {
      setSearchResult([]);
      return;
    }

    setSearchResult(
      employees.filter((employee) => employee.label.includes(name)),
    );
  };

  return (
    <Form form={form} {...formItemLayout} onFinish={onFinish}>
      <Form.Item
        label="User"
        name="user"
        rules={[{ required: true, message: "Please input user" }]}
      >
        <Select
          showSearch
          onSearch={onSearch}
          filterOption={false}
          notFoundContent={null}
          showArrow={false}
        >
          {searchResult.map(({ label, value }) => (
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
