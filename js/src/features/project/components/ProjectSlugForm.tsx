import React, { useCallback, useMemo } from "react";
import { Button, Col, Form, Input, Row } from "antd";

import { useFormServerErrors } from "hooks/useFormServerErrors";
import { EditProjectSlugRequest, Project } from "ProjectModels";
import { FormErrorMap } from "MyTypes";

interface Props {
  project: Project;
  edit: (form: EditProjectSlugRequest) => void;
  close: () => void;
  errors: FormErrorMap;
  loading: boolean;
}

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

  const onFinishFailed = useCallback((errorInfo: any) => {
    console.log("onFinishFailed:", errorInfo);
  }, []);

  return (
    <Form
      form={form}
      onFinish={onFinish}
      onFinishFailed={onFinishFailed}
      initialValues={initialValues}
    >
      <Form.Item
        name="slug"
        rules={[
          { required: true, message: "Please input Slug!" },
          {
            pattern: /^[a-z0-9]+(?:[-_][a-z0-9]+)*$/,
            message: "Please input valid Slug!",
          },
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
