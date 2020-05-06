import React from "react";
import { Form, Button, Input } from "antd";
import { ApplySalaryForm } from "HumanResourceModels";
import TextArea from "antd/lib/input/TextArea";
import { FormErrorMap } from "MyTypes";
import { useFormServerErrors } from "hooks/useFormServerErrors";

interface Props {
  applySalary: (data: ApplySalaryForm) => any;
  errors: FormErrorMap;
}

export const SalaryForm: React.FC<Props> = ({ applySalary, errors }) => {
  const [form] = Form.useForm();

  useFormServerErrors(form, errors, ["amount", "comment"]);

  const handleFinish = (values: any) => applySalary(values);

  return (
    <Form form={form} onFinish={handleFinish}>
      <Form.Item
        name="amount"
        rules={[
          { required: true, message: "Please input amount!" },
          { pattern: /^\d+$/g, message: "Only digits are allowed!" },
        ]}
      >
        <Input placeholder="Enter amount" />
      </Form.Item>
      <Form.Item name="comment">
        <TextArea placeholder="Enter comment" />
      </Form.Item>
      <Form.Item>
        <Button htmlType="submit" loading={false} type="primary">
          Apply Salary
        </Button>
      </Form.Item>
    </Form>
  );
};
