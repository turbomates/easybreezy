import React, { useEffect } from "react";
import { Button, Form, Input } from "antd";

import DatePicker from "../../../../components/antd/DatePicker";
import { getRequiredErrors } from "../../../../utils/errors";

type Props = {
  activeDateHolidayName: string;
  activeDate: Date;
};

export const HolidayCalendarRemoveDate: React.FC<Props> = ({
  activeDateHolidayName,
  activeDate,
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
      <Form.Item label="Name" name="name">
        <Input style={{ width: 250 }} disabled={true} />
      </Form.Item>
      <Form.Item label="From" name="from" rules={[...getRequiredErrors()]}>
        <DatePicker />
      </Form.Item>
      <Form.Item wrapperCol={{ offset: 4 }}>
        <Button type="primary">Remove</Button>
      </Form.Item>
    </Form>
  );
};
