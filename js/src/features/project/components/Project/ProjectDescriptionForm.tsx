import React, { useCallback, useMemo } from "react";
import { Button, Col, Form, Input } from "antd";

import { useFormServerErrors } from "hooks/useFormServerErrors";
import { EditProjectDescriptionRequest, Project } from "ProjectModels";
import { FormErrorMap } from "MyTypes";
import { getRequiredErrors } from "../../../../utils/errors";

type Props = {
  project: Project;
  edit: (form: EditProjectDescriptionRequest) => void;
  errors: FormErrorMap;
  loading: boolean;
};

export const ProjectDescriptionForm: React.FC<Props> = ({
  project,
  errors,
  edit,
  loading,
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

  return (
    <Form
      form={form}
      onFinish={onFinish}
      initialValues={initialValues}
      style={{ width: "100%" }}
    >
      <Form.Item name="description" rules={[...getRequiredErrors()]}>
        <Input.TextArea autoSize />
      </Form.Item>

      <Form.Item>
        <Col>
          <Button type="primary" htmlType="submit" loading={loading}>
            Update
          </Button>
        </Col>
      </Form.Item>
    </Form>
  );
};
