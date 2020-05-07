import React from "react";
import { Form, Button, Input, InputNumber } from "antd";
import { ApplySalaryForm } from "HumanResourceModels";
import { FormErrorMap } from "MyTypes";
import { useFormServerErrors } from "hooks/useFormServerErrors";

interface Props {
  applySalary: (data: ApplySalaryForm) => void;
  errors: FormErrorMap;
}

export const SalaryForm: React.FC<Props> = ({ applySalary, errors }) => {
  const [form] = Form.useForm();

  useFormServerErrors(form, errors, ["amount", "comment"]);

  const handleFinish = (values: any) => applySalary(values);

  return (
    <Form form={form} onFinish={handleFinish} className="salary-form">
      <Form.Item
        name="amount"
        label="Amount"
        rules={[
          { required: true, message: "Please input amount!" },
          { pattern: /^\d+$/g, message: "Only digits are allowed!" },
        ]}
      >
        <InputNumber placeholder="Enter amount" width={100} />
      </Form.Item>
      <Form.Item name="comment" label="Comment">
        <Input.TextArea placeholder="Enter comment" />
      </Form.Item>
      <Form.Item>
        <Button htmlType="submit" loading={false} type="primary">
          Apply Salary
        </Button>
      </Form.Item>
    </Form>
  );
};
