import React, { useCallback } from "react";
import { Button, Form, Input } from "antd";

import { useFormServerErrors } from "../../../hooks/useFormServerErrors";
import { CreateProjectTeamRequest } from "ProjectModels";
import { FormErrorMap } from "MyTypes";

interface Props {
  projectId: string;
  errors: FormErrorMap;
  create: (form: CreateProjectTeamRequest) => void;
}

export const ProjectTeamsCreateForm: React.FC<Props> = ({
  projectId,
  errors,
  create,
}) => {
  const [form] = Form.useForm();

  useFormServerErrors(form, errors, ["name"]);

  const onFinish = useCallback(
    (values: any) => {
      create({
        name: values.name,
        project: projectId,
      });
    },
    [create, projectId],
  );

  const onFinishFailed = useCallback((errorInfo: any) => {
    console.log("onFinishFailed:", errorInfo);
  }, []);

  return (
    <Form
      form={form}
      labelCol={{ span: 8 }}
      onFinish={onFinish}
      onFinishFailed={onFinishFailed}
    >
      <Form.Item
        label="Name"
        name="name"
        rules={[{ required: true, message: "Please input name!" }]}
      >
        <Input name="name" />
      </Form.Item>

      <Form.Item>
        <Button type="primary" htmlType="submit">
          Create
        </Button>
      </Form.Item>
    </Form>
  );
};
