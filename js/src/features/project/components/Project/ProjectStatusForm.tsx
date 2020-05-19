import React, { useCallback, useMemo } from "react";
import { Form, Select } from "antd";

import { Project, EditProjectStatusRequest } from "ProjectModels";
import { switchProjectStatus } from "../../helpers";

import "./ProjectStatusForm.scss";

const { Option } = Select;

type Props = {
  change: (form: EditProjectStatusRequest) => void;
  project: Project;
  loading: boolean;
};

export const ProjectStatusForm: React.FC<Props> = ({
  change,
  project,
  loading,
}) => {
  const [form] = Form.useForm();

  const initialValues = useMemo(() => ({ status: project.status }), [
    project.status,
  ]);

  const onFinish = useCallback(
    (values: any) => {
      change({
        slug: project.slug,
        statusType: switchProjectStatus(values),
      });
    },
    [project.slug, change],
  );

  return (
    <Form
      form={form}
      labelCol={{ span: 2 }}
      onFinish={onFinish}
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
