import React, { useCallback, useMemo } from "react";
import { Form, Select } from "antd";

import { useFormServerErrors } from "hooks/useFormServerErrors";
import { FormErrorMap } from "MyTypes";
import { Project, EditProjectStatusRequest } from "ProjectModels";
import { switchProjectStatus } from "../helpers";

import "./ProjectStatusForm.scss";

const { Option } = Select;

type Props = {
  change: (form: EditProjectStatusRequest) => void;
  project: Project;
  errors: FormErrorMap;
  loading: boolean;
}

export const ProjectStatusForm: React.FC<Props> = ({
  errors,
  change,
  project,
  loading,
}) => {
  const [form] = Form.useForm();

  const initialValues = useMemo(() => ({ status: project.status }), [
    project.status,
  ]);

  useFormServerErrors(form, errors, ["status"]);

  const onFinish = useCallback(
    (values: any) => {
      change({
        slug: project.slug,
        statusType: switchProjectStatus(values),
      });
    },
    [project.slug, change],
  );

  const onFinishFailed = useCallback((errorInfo: any) => {
    console.log("onFinishFailed:", errorInfo);
  }, []);

  return (
    <Form
      form={form}
      labelCol={{ span: 2 }}
      onFinish={onFinish}
      onFinishFailed={onFinishFailed}
      initialValues={initialValues}
    >
      <Form.Item name="status" className="status-form__select">
        <Select
          onChange={onFinish}
          disabled={project.status === "Closed"}
          loading={loading}
        >
          <Option value="Active" className="status-form__option">
            <span>Activate</span>
            <div className="status-form__option-description">Description</div>
          </Option>
          <Option value="Closed">
            <span>Close</span>
            <div className="status-form__option-description">Description</div>
          </Option>
          <Option value="Suspended">
            <span>Suspend</span>
            <div className="status-form__option-description">Description</div>
          </Option>
        </Select>
      </Form.Item>
    </Form>
  );
};
