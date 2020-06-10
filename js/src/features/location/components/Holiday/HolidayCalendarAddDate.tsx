import React, { useEffect } from "react";
import { Button, Checkbox, Form, Input } from "antd";

import { getRequiredErrors } from "../../../../utils/errors";

type Props = {
  activeDate: Date;
};

export const HolidayCalendarAddDate: React.FC<Props> = ({ activeDate }) => {
  const [form] = Form.useForm();

  useEffect(() => {
    form.setFieldsValue({
      ...form.getFieldsValue(),
      to: activeDate,
    });
  }, [activeDate, form]);

  return (
    <Form form={form} labelCol={{ span: 5 }}>
      <Form.Item shouldUpdate noStyle={true}>
        {({ getFieldValue }) => {
          return (
            <Form.Item
              label={getFieldValue("isWorkingDay") ? "Comment" : "Name"}
              name="name"
              required={false}
              rules={[...getRequiredErrors()]}
            >
              <Input style={{ width: 250 }} />
            </Form.Item>
          );
        }}
      </Form.Item>

      <Form.Item
        name="isWorkingDay"
        valuePropName="checked"
        wrapperCol={{ offset: 5 }}
      >
        <Checkbox>Working day</Checkbox>
      </Form.Item>
      <Form.Item wrapperCol={{ offset: 5 }}>
        <Button type="primary">Add</Button>
      </Form.Item>
    </Form>
  );
};
