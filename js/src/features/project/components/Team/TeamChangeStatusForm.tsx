import React, { useCallback, useMemo } from "react";
import { Form, Select } from "antd";

import { ChangeProjectTeamStatusRequest } from "ProjectModels";
import { useFormServerErrors } from "../../../../hooks/useFormServerErrors";
import { switchProjectTeamStatus } from "../../helpers";
import { FormErrorMap } from "MyTypes";

interface Props {
  status: string;
  teamId: string;
  change: (form: ChangeProjectTeamStatusRequest) => void;
  errors: FormErrorMap;
}

const { Option } = Select;

export const TeamChangeStatusForm: React.FC<Props> = ({
  teamId,
  status,
  change,
  errors,
}) => {
  const initialValues = useMemo(() => ({ status }), [status]);
  const [form] = Form.useForm();

  const onFinish = useCallback(
    (values: any) => {
      change({
        status: switchProjectTeamStatus(values),
        teamId,
      });
    },
    [change, teamId],
  );

  const onFinishFailed = useCallback((errorInfo: any) => {
    console.log("onFinishFailed:", errorInfo);
  }, []);

  useFormServerErrors(form, errors, ["status"]);

  return (
    <Form
      form={form}
      labelCol={{ span: 2 }}
      onFinish={onFinish}
      onFinishFailed={onFinishFailed}
      initialValues={initialValues}
    >
      <Form.Item name="status">
        <Select onChange={onFinish}>
          <Option value="Active">
            <span>Activate</span>
          </Option>
          <Option value="Closed">
            <span>Close</span>
          </Option>
        </Select>
      </Form.Item>
    </Form>
  );
};
