import React, { useCallback } from "react";
import { Button, Form, Input } from "antd";

import { FormErrorMap } from "MyTypes";
import { useFormServerErrors } from "hooks/useFormServerErrors";
import { CreateProjectRequest } from "ProjectModels";
import { convertToSlug } from "../../helpers";

type Props = {
  create: (form: CreateProjectRequest) => void;
  errors: FormErrorMap;
  loading: boolean;
};

export const CreateProjectForm: React.FC<Props> = ({
  errors,
  create,
  loading,
}) => {
  const [form] = Form.useForm();

  useFormServerErrors(form, errors, ["name", "description", "slug"]);

  const onFinish = useCallback(
    (values: any) => {
      create({
        description: values.description,
        name: values.name,
        slug: values.slug,
      });
    },
    [create],
  );

  function changeSlug(value: string) {
    if (!form.isFieldTouched("slug")) {
      form.setFields([
        {
          value: convertToSlug(value),
          name: "slug",
          touched: false,
        },
      ]);
    }
  }

  return (
    <Form form={form} labelCol={{ span: 8 }} onFinish={onFinish}>
      <Form.Item
        label="Name"
        name="name"
        rules={[
          { required: true, message: "Please input name" },
          { min: 2, message: "Name should be at least 2 characters long" },
          { max: 255, message: "Name must be no more than 255 characters" },
          { whitespace: true, message: "Please input name" },
        ]}
      >
        <Input onChange={(event) => changeSlug(event.target.value)} />
      </Form.Item>

      <Form.Item
        label="Description"
        name="description"
        rules={[
          { required: true, message: "Please input description" },
          { whitespace: true, message: "Please input description" },
        ]}
      >
        <Input />
      </Form.Item>

      <Form.Item
        label="Slug"
        name="slug"
        rules={[
          { required: true, message: "Please input slug" },
          {
            pattern: /^[a-z0-9]+(?:[-_][a-z0-9]+)*$/,
            message: "Please input valid slug",
          },
        ]}
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
