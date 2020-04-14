import React, { useCallback, useMemo } from "react";
import { Button, Form, Radio } from "antd";

import { useFormServerErrors } from "hooks/useFormServerErrors";
import { FormErrorMap } from "MyTypes";
import { Project, EditProjectStatusRequest } from "ProjectModels"

interface Props {
  change: (form: EditProjectStatusRequest) => void
  project: Project
  errors: FormErrorMap
  loading: boolean
}

export const ProjectStatusForm: React.FC<Props> = ({errors, change, project, loading}) => {
  const [form] = Form.useForm();

  const initialValues = useMemo(() => ({status: project.status}), [project.status]);

  useFormServerErrors(form, errors, [
    "status",
  ]);

  const onFinish = useCallback((values: any) => {
    let status = '';

    switch (values.status) {
      case "Active":
        status = "activate";
        break;
      case "Closed":
        status = "close";
        break;
      case "Suspended":
        status = "suspend";
        break
    }

    change({name: project.name, statusType: status})
  }, [project.name, change]);

  const onFinishFailed = useCallback((errorInfo: any) => {
    console.log("onFinishFailed:", errorInfo);
  }, []);

  return (
    <Form
      form={form}
      labelCol={{span: 2}}
      onFinish={onFinish}
      onFinishFailed={onFinishFailed}
      initialValues={initialValues}
    >
      <Form.Item
        name="status"
      >
        <Radio.Group buttonStyle="solid">
          <Radio.Button value="Active">Activate</Radio.Button>
          <Radio.Button value="Closed">Close</Radio.Button>
          <Radio.Button value="Suspended">Suspend</Radio.Button>
        </Radio.Group>
      </Form.Item>

      <Form.Item>
        <Button type="primary" htmlType="submit" loading={loading}>
          Edit
        </Button>
      </Form.Item>
    </Form>
  )
};
