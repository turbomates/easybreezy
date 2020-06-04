import React, { useCallback } from "react";
import { Form, Input, Button } from "antd";
import { LocationForm as LocationFormModel } from "LocationModels";
import { FormErrorMap } from "MyTypes";
import { useFormServerErrors } from "hooks/useFormServerErrors";

interface Props {
  create: (form: LocationFormModel) => void;
  errors: FormErrorMap;
}

const initialFormValues = { vacationDays: 0 };

export const LocationForm: React.FC<Props> = ({ create, errors }) => {
  const [form] = Form.useForm();

  useFormServerErrors(form, errors, ["name", "vacationDays"]);

  const onFinish = useCallback((values: any) => create(values), [create]);

  const onFinishFailed = useCallback(
    (errorInfo: any) => console.log("onFinishFailed:", errorInfo),
    [],
  );

  return (
    <Form
      name="createLocation"
      form={form}
      labelCol={{ span: 8 }}
      onFinish={onFinish}
      onFinishFailed={onFinishFailed}
      initialValues={initialFormValues}
    >
      <Form.Item
        label="Name"
        name="name"
        rules={[{ required: true, message: "Please input location name!" }]}
      >
        <Input />
      </Form.Item>

      <Form.Item
        label="Vacation Days"
        name="vacationDays"
        rules={[
          { required: true, message: "Please input your vacations dayss!" },
        ]}
      >
        <Input />
      </Form.Item>

      <Form.Item wrapperCol={{ offset: 8 }}>
        <Button type="primary" htmlType="submit" style={{ alignSelf: "right" }}>
          Create location
        </Button>
      </Form.Item>
    </Form>
  );
};
