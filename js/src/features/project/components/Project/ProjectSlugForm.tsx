import React, { useCallback, useMemo } from "react";
import { Button, Col, Form, Input, Row } from "antd";

import { useFormServerErrors } from "hooks/useFormServerErrors";
import { EditProjectSlugRequest, Project } from "ProjectModels";
import { FormErrorMap } from "MyTypes";
import {
  getMaxError,
  getMinError,
  getRequiredErrors,
  getUrlError,
} from "../../../../utils/errors";

type Props = {
  project: Project;
  edit: (form: EditProjectSlugRequest) => void;
  errors: FormErrorMap;
  loading: boolean;
};

export const ProjectSlugForm: React.FC<Props> = ({
  project,
  errors,
  edit,
  loading,
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
          ...getRequiredErrors(),
          getMinError(2),
          getMaxError(25),
          getUrlError(),
        ]}
      >
        <Input />
      </Form.Item>

      <Form.Item>
        <Row gutter={[16, 16]}>
          <Col>
            <Button type="primary" htmlType="submit" loading={loading}>
              Update
            </Button>
          </Col>
        </Row>
      </Form.Item>
    </Form>
  );
};
