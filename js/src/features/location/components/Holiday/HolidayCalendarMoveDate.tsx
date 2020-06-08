import React, { useEffect } from "react";
import { Button, Form, Input } from "antd";

import DatePicker from "../../../../components/antd/DatePicker";
import { getRequiredErrors } from "../../../../utils/errors";

type Props = {
  activeDateHolidayName: any;
  activeDate: Date;
};

export const HolidayCalendarMoveDate: React.FC<Props> = ({
  activeDate,
  activeDateHolidayName,
}) => {
  const [form] = Form.useForm();

  useEffect(() => {
    form.setFieldsValue({
      name: activeDateHolidayName,
      from: activeDate,
    });
  }, [activeDateHolidayName, activeDate, form]);

  return (
    <Form form={form} labelCol={{ span: 4 }}>
      <Form.Item label="Name" name="name" rules={[...getRequiredErrors()]}>
        <Input style={{ width: 250 }} />
      </Form.Item>
      <Form.Item label="From" name="from" rules={[...getRequiredErrors()]}>
        <DatePicker />
      </Form.Item>
      <Form.Item label="To" name="to" rules={[...getRequiredErrors()]}>
        <DatePicker />
      </Form.Item>
      <Form.Item wrapperCol={{ offset: 4 }}>
        <Button type="primary">Move</Button>
      </Form.Item>
    </Form>
  );
};
