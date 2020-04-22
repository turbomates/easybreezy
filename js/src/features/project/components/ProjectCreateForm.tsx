import React, { useCallback } from "react";
import { Button, Form, Input } from "antd";

import { FormErrorMap } from "MyTypes";
import { useFormServerErrors } from "hooks/useFormServerErrors";
import { CreateProjectRequest } from "ProjectModels";

interface Props {
  create: (form: CreateProjectRequest) => void;
  errors: FormErrorMap;
  loading: boolean;
}

export const CreateProjectForm: React.FC<Props> = ({
  errors,
  create,
  loading,
}) => {
  const [form] = Form.useForm();

  useFormServerErrors(form, errors, ["name", "description"]);

  const onFinish = useCallback(
    (values: any) => {
      create(values);
    },
    [create],
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
        rules={[{ required: true, message: "Please input Name!" }]}
      >
        <Input />
      </Form.Item>

      <Form.Item
        label="Description"
        name="description"
        rules={[{ required: true, message: "Please input Description!" }]}
      >
        <Input />
      </Form.Item>

      <Form.Item>
        <Button type="primary" htmlType="submit" loading={loading}>
          Create
        </Button>
      </Form.Item>
    </Form>
  );
};
