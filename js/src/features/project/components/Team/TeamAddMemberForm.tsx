import React, { useCallback, useMemo, useState } from "react";
import { Button, Form, Select } from "antd";

import { AddProjectTeamMemberRequest, Role } from "ProjectModels";
import { EmployeeShort } from "HumanResourceModels";

interface Props {
  teamId: string;
  roles: Role[];
  add: (form: AddProjectTeamMemberRequest) => void;
  employees: EmployeeShort[];
}

const { Option } = Select;

export const TeamAddMemberForm: React.FC<Props> = ({
  teamId,
  roles,
  add,
  employees,
}) => {
  const [form] = Form.useForm();
  const [searchResult, setSearchResult] = useState<
    { label: string; value: string }[]
  >([]);

  const users = useMemo(
    () =>
      employees.map((emp) => ({
        value: emp.userId,
        label: `${emp.firstName} ${emp.lastName}`,
      })),
    [employees],
  );

  const onFinish = useCallback(
    (values) =>
      add({
        user: values.user,
        role: values.role,
        teamId,
      }),
    [add, teamId],
  );

  const onFinishFailed = useCallback((errorInfo: any) => {
    console.log("onFinishFailed:", errorInfo);
  }, []);

  const onSearch = (searchText: string) => {
    const name = searchText.trim();

    if (!name.length) {
      setSearchResult([]);
      return;
    }

    const result = users.filter((employee) => employee.label.includes(name));

    setSearchResult(result);
  };

  return (
    <Form
      form={form}
      labelCol={{ span: 8 }}
      onFinish={onFinish}
      onFinishFailed={onFinishFailed}
    >
      <Form.Item
        label="User"
        name="user"
        rules={[{ required: true, message: "Please input user!" }]}
      >
        <Select
          onSearch={onSearch}
          showSearch
          showArrow={false}
          filterOption={false}
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
        rules={[{ required: true, message: "Please input role!" }]}
      >
        <Select>
          {roles.map((role) => (
            <Option value={role.id} key={role.id}>
              {role.name}
            </Option>
          ))}
        </Select>
      </Form.Item>

      <Form.Item>
        <Button type="primary" htmlType="submit">
          Add
        </Button>
      </Form.Item>
    </Form>
  );
};
