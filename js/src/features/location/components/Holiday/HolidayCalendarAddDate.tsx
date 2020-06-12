import React, { useCallback, useEffect } from "react";
import { Button, Checkbox, Form, Input } from "antd";
import formatISO from "date-fns/esm/formatISO";

import { getRequiredErrors } from "../../../../utils/errors";
import { AddHolidayForm } from "LocationModels";

type Props = {
  activeDate: Date;
  add: (form: AddHolidayForm) => void;
};

export const HolidayCalendarAddDate: React.FC<Props> = ({
  activeDate,
  add,
}) => {
  const [form] = Form.useForm();

  useEffect(() => {
    form.setFieldsValue({
      ...form.getFieldsValue(),
      to: activeDate,
    });
  }, [activeDate, form]);

  const onFinish = useCallback(
    (values: any) => {
      add({
        ...values,
        day: formatISO(activeDate, { representation: "date" }),
      });
    },
    [add, activeDate],
  );

  return (
    <Form
      form={form}
      onFinish={onFinish}
      initialValues={{ isWorkingDay: false }}
      labelCol={{ span: 5 }}
    >
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
        wrapperCol={{ md: { offset: 5 } }}
      >
        <Checkbox>Working day</Checkbox>
      </Form.Item>
      <Form.Item wrapperCol={{ md: { offset: 5 } }}>
        <Button type="primary" htmlType="submit">
          Add
        </Button>
      </Form.Item>
    </Form>
  );
};
