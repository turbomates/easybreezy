import React, { useCallback, useMemo } from "react"
import { Button, Form, Input } from "antd";

import { useFormServerErrors } from "hooks/useFormServerErrors";
import { EditProjectDescriptionRequest, Project } from "ProjectModels"
import { FormErrorMap } from "MyTypes";

interface Props {
  project: Project;
  edit: (form: EditProjectDescriptionRequest) => void;
  errors: FormErrorMap;
  loading: boolean;
}

export const ProjectDescriptionForm: React.FC<Props> = ({project, errors, edit, loading}) => {
  const [form] = Form.useForm();

  const initialValues = useMemo(() => ({
    description: project.description
  }), [project.description])

  useFormServerErrors(form, errors, [
    "description",
  ]);

  const onFinish = useCallback((values: any) => {
    edit({
      slug: project.slug,
      description: values.description
    })
  }, [project.slug, edit])

  const onFinishFailed = useCallback((errorInfo: any) => {
    console.log("onFinishFailed:", errorInfo);
  }, []);

  return(
    <Form
      form={form}
      labelCol={{span: 8}}
      onFinish={onFinish}
      onFinishFailed={onFinishFailed}
      initialValues={initialValues}
    >
      <Form.Item
        label="Description"
        name="description"
        rules={[{required: true, message: "Please input Description!"}]}
      >
        <Input/>
      </Form.Item>

      <Form.Item>
        <Button type="primary" htmlType="submit" loading={loading}>
          Edit
        </Button>
      </Form.Item>
    </Form>
  )
}
