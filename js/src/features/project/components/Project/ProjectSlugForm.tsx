import React, { useCallback, useMemo } from "react";
import { Button, Col, Form, Input, Row } from "antd";

import { useFormServerErrors } from "hooks/useFormServerErrors";
import { EditProjectSlugRequest, Project } from "ProjectModels";
import { FormErrorMap } from "MyTypes";

type Props = {
  project: Project;
  edit: (form: EditProjectSlugRequest) => void;
  close: () => void;
  errors: FormErrorMap;
  loading: boolean;
};

export const ProjectSlugForm: React.FC<Props> = ({
  project,
  errors,
  edit,
  loading,
  close,
}) => {
  const [form] = Form.useForm();

  const initialValues = useMemo(
    () => ({
      slug: project.slug,
    }),
    [project.slug],
  );

  useFormServerErrors(form, errors, ["slug"]);

  const onFinish = useCallback(
    (values: any) => {
      edit({
        slug: project.slug,
        newSlug: values.slug,
      });
    },
    [project.slug, edit],
  );

  return (
    <Form form={form} onFinish={onFinish} initialValues={initialValues}>
      <Form.Item
        name="slug"
        rules={[
          { required: true, message: "Please input slug" },
          {
            pattern: /^[a-z0-9]+(?:[-_][a-z0-9]+)*$/,
            message: "Please input valid slug",
          },
          { min: 2, message: "Name should be at least 2 characters long" },
          { max: 25, message: "Name must be no more than 25 characters" },
        ]}
      >
        <Input />
      </Form.Item>

      <Form.Item>
        <Row gutter={[16, 16]}>
          <Col>
            <Button type="primary" htmlType="submit" loading={loading}>
              Save
            </Button>
          </Col>
          <Col>
            <Button type="danger" onClick={close} loading={loading}>
              Cancel
            </Button>
          </Col>
        </Row>
      </Form.Item>
    </Form>
  );
};
