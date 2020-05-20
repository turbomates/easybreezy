import React, { useCallback } from "react";
import { Button, Form, Input } from "antd";

import { FormErrorMap } from "MyTypes";
import { useFormServerErrors } from "hooks/useFormServerErrors";
import { CreateProjectRequest } from "ProjectModels";
import { convertToSlug } from "../../helpers";
import {
  getMaxError,
  getMinError,
  getRequiredErrors,
  getUrlError,
} from "../../../../utils/errors";

type Props = {
  create: (form: CreateProjectRequest) => void;
  errors: FormErrorMap;
  loading: boolean;
};

export const NewProjectForm: React.FC<Props> = ({
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
        rules={[...getRequiredErrors(), getMinError(2), getMaxError(255)]}
      >
        <Input onChange={(event) => changeSlug(event.target.value)} />
      </Form.Item>

      <Form.Item
        label="Description"
        name="description"
        rules={[...getRequiredErrors()]}
      >
        <Input />
      </Form.Item>

      <Form.Item
        label="Slug"
        name="slug"
        rules={[...getRequiredErrors(), getUrlError()]}
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
