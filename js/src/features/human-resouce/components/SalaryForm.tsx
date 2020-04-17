import React from "react";
import { Form, Button, Input } from "antd";
import { ApplySalaryForm } from "HumanResourceModels";
import TextArea from "antd/lib/input/TextArea";

interface Props {
  applySalary: (data: ApplySalaryForm) => any;
}

export const SalaryForm: React.FC<Props> = ({ applySalary }) => {
  const [form] = Form.useForm();

  const handleFinish = (values: any) => {
    applySalary(values);
    form.resetFields();
  };

  return (
    <Form form={form} onFinish={handleFinish}>
      <Form.Item name="amount">
        <Input />
      </Form.Item>
      <Form.Item name="comment">
        <TextArea />
      </Form.Item>
      <Form.Item>
        <Button htmlType="submit" loading={false} type="primary">
          Apply Salary
        </Button>
      </Form.Item>
    </Form>
  );
};
