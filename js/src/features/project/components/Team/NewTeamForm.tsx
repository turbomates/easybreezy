import React, { useCallback } from "react";
import { Button, Form, Input } from "antd";

import { useFormServerErrors } from "../../../../hooks/useFormServerErrors";
import { CreateProjectTeamRequest } from "ProjectModels";
import { FormErrorMap } from "MyTypes";

type Props = {
  projectId: string;
  errors: FormErrorMap;
  create: (form: CreateProjectTeamRequest) => void;
}

export const NewTeamForm: React.FC<Props> = ({
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

  return (
    <Form form={form} labelCol={{ span: 8 }} onFinish={onFinish}>
      <Form.Item
        name="name"
        rules={[
          { required: true, message: "Please input name" },
          { min: 2, message: "Name should be at least 2 characters long" },
          { max: 25, message: "Name must be no more than 25 characters" },
        ]}
      >
        <Input name="name" placeholder="Name" />
      </Form.Item>

      <Form.Item>
        <Button type="primary" htmlType="submit">
          Create
        </Button>
      </Form.Item>
    </Form>
  );
};
