import React, { useEffect } from "react";
import { Button, Form, Input } from "antd";

import DatePicker from "../../../../components/antd/DatePicker";
import { getRequiredErrors } from "../../../../utils/errors";

type Props = {
  activeDateHolidayName: any;
  activeDate: Date;
};

export const HolidayCalendarAddDate: React.FC<Props> = ({
  activeDate,
  activeDateHolidayName,
}) => {
  const [form] = Form.useForm();

  useEffect(() => {
    form.setFieldsValue({
      to: activeDate,
    });
  }, [activeDate, form]);

  return (
    <Form form={form} labelCol={{ span: 4 }}>
      <Form.Item label="Name" name="name" rules={[...getRequiredErrors()]}>
        <Input style={{ width: 250 }} />
      </Form.Item>
      <Form.Item label="To" name="to" rules={[...getRequiredErrors()]}>
        <DatePicker />
      </Form.Item>
      <Form.Item wrapperCol={{ offset: 4 }} rules={[...getRequiredErrors()]}>
        <Button type="primary">Create</Button>
      </Form.Item>
    </Form>
  );
};
