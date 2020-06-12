import React, { useCallback, useEffect } from "react";
import { Button, Form, Input, Row } from "antd";
import formatISO from "date-fns/esm/formatISO";

import DatePicker from "../../../../components/antd/DatePicker";
import { getRequiredErrors } from "../../../../utils/errors";
import { EditHolidayForm } from "LocationModels";

type Props = {
  activeDateHolidayName: string;
  activeDate: Date;
  isWorkingDay: boolean;
  edit: (form: EditHolidayForm) => void;
  remove: (day: string) => void;
};

export const HolidayCalendarEditDate: React.FC<Props> = ({
  activeDate,
  activeDateHolidayName,
  isWorkingDay,
  edit,
  remove,
}) => {
  const [form] = Form.useForm();

  const onFinish = useCallback(
    (values: any) => {
      edit({
        name: values.name,
        isWorkingDay,
        day: formatISO(values.to, { representation: "date" }),
      });
    },
    [edit, isWorkingDay],
  );

  useEffect(() => {
    form.setFieldsValue({
      name: activeDateHolidayName,
      to: activeDate,
    });
  }, [activeDateHolidayName, activeDate, form]);

  return (
    <Form form={form} onFinish={onFinish} labelCol={{ span: 5 }}>
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
        <DatePicker
          disabledDate={(date) => date < new Date()}
          placeholder="To"
        />
      </Form.Item>
      <Form.Item wrapperCol={{ md: { offset: 5 } }}>
        <Row justify="space-between">
          <Button type="primary" htmlType="submit">
            Move
          </Button>
          <Button
            type="danger"
            onClick={() =>
              remove(formatISO(activeDate, { representation: "date" }))
            }
          >
            Remove
          </Button>
        </Row>
      </Form.Item>
    </Form>
  );
};
