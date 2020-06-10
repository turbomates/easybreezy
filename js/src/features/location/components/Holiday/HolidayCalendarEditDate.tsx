import React, { useEffect } from "react";
import { Button, Form, Input } from "antd";

import DatePicker from "../../../../components/antd/DatePicker";
import { getRequiredErrors } from "../../../../utils/errors";

type Props = {
  activeDateHolidayName: string;
  activeDate: Date;
  isWorkingDay: boolean;
};

export const HolidayCalendarEditDate: React.FC<Props> = ({
  activeDate,
  activeDateHolidayName,
  isWorkingDay,
}) => {
  const [form] = Form.useForm();

  useEffect(() => {
    form.setFieldsValue({
      name: activeDateHolidayName,
      to: activeDate,
    });
  }, [activeDateHolidayName, activeDate, form]);

  return (
    <Form form={form} labelCol={{ span: 5 }}>
      <Form.Item
        label={isWorkingDay ? "Comment" : "Name"}
        name="name"
        required={false}
        rules={[...getRequiredErrors()]}
      >
        <Input style={{ width: 250 }} />
      </Form.Item>
      <Form.Item
        label="To"
        name="to"
        required={false}
        rules={[...getRequiredErrors(false)]}
      >
        <DatePicker placeholder="To" />
      </Form.Item>
      <Form.Item wrapperCol={{ offset: 5 }}>
        <Button type="primary">Move</Button>
        <Button type="danger" style={{ float: "right" }}>
          Remove
        </Button>
      </Form.Item>
    </Form>
  );
};
