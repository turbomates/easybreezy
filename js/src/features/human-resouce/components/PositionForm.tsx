import React from "react";
import { Form, Button, Input } from "antd";

interface Props {
  applyPosition: (text: string) => any;
}

export const PositionForm: React.FC<Props> = ({ applyPosition }) => {
  const [form] = Form.useForm();

  const handleFinish = (values: any) => {
    applyPosition(values.position);
    form.resetFields();
  };

  return (
    <Form form={form} onFinish={handleFinish}>
      <Form.Item name="position">
        <Input />
      </Form.Item>
      <Form.Item>
        <Button htmlType="submit" loading={false} type="primary">
          Add Position
        </Button>
      </Form.Item>
    </Form>
  );
};
