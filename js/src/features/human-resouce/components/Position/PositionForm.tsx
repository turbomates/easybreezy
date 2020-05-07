import React from "react";
import { Form, Button, Input } from "antd";
import { FormErrorMap } from "MyTypes";
import { useFormServerErrors } from "hooks/useFormServerErrors";

interface Props {
  errors: FormErrorMap;
  applyPosition: (text: string) => void;
}

export const PositionForm: React.FC<Props> = ({ errors, applyPosition }) => {
  const [form] = Form.useForm();

  useFormServerErrors(form, errors, ["position"]);

  const handleFinish = (values: any) => applyPosition(values.position);

  return (
    <Form form={form} onFinish={handleFinish}>
      <Form.Item
        name="position"
        rules={[{ required: true, message: "Please input position dates!" }]}
      >
        <Input placeholder="Enter position" />
      </Form.Item>
      <Form.Item>
        <Button htmlType="submit" loading={false} type="primary">
          Add Position
        </Button>
      </Form.Item>
    </Form>
  );
};
