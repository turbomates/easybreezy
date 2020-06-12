import React, { useCallback } from "react";
import { Form, Input, Button, InputNumber } from "antd";

import { LocationForm as LocationFormModel } from "LocationModels";
import { FormErrorMap } from "MyTypes";
import { useFormServerErrors } from "hooks/useFormServerErrors";
import { getRequiredErrors } from "../../../../utils/errors";

interface Props {
  create: (form: LocationFormModel) => void;
  errors: FormErrorMap;
}

const initialFormValues = { vacationDays: 0 };

export const LocationForm: React.FC<Props> = ({ create, errors }) => {
  const [form] = Form.useForm();

  useFormServerErrors(form, errors, ["name", "vacationDays"]);

  const onFinish = useCallback(
    (values: any) => {
      create(values);
    },
    [create],
  );

  return (
    <Form
      name="createLocation"
      form={form}
      labelCol={{ span: 8 }}
      onFinish={onFinish}
      initialValues={initialFormValues}
    >
      <Form.Item label="Name" name="name" rules={[...getRequiredErrors(false)]}>
        <Input />
      </Form.Item>

      <Form.Item
        label="Vacation Days"
        name="vacationDays"
        rules={[...getRequiredErrors(false)]}
      >
        <InputNumber min={24} />
      </Form.Item>

      <Form.Item wrapperCol={{ md: { offset: 8 } }}>
        <Button type="primary" htmlType="submit" style={{ alignSelf: "right" }}>
          Create location
        </Button>
      </Form.Item>
    </Form>
  );
};
