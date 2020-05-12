import React, { useCallback, useMemo } from "react";
import { Button, Col, Form, Input, Row } from "antd";

import { useFormServerErrors } from "hooks/useFormServerErrors";
import { EditProjectDescriptionRequest, Project } from "ProjectModels";
import { FormErrorMap } from "MyTypes";

type Props = {
  project: Project;
  edit: (form: EditProjectDescriptionRequest) => void;
  close: () => void;
  errors: FormErrorMap;
  loading: boolean;
}

export const ProjectDescriptionForm: React.FC<Props> = ({
  project,
  errors,
  edit,
  loading,
  close,
}) => {
  const [form] = Form.useForm();

  const initialValues = useMemo(
    () => ({
      description: project.description,
    }),
    [project.description],
  );

  useFormServerErrors(form, errors, ["description"]);

  const onFinish = useCallback(
    (values: any) => {
      edit({
        slug: project.slug,
        description: values.description,
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
        name="description"
        rules={[
          {
            required: true,
            message: "Please input description!",
          },
        ]}
      >
        <Input.TextArea autoSize />
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
