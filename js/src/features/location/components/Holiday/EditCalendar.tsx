import React, { useCallback } from "react";
import { Button, Form, Row, Upload } from "antd";
import { InboxOutlined } from "@ant-design/icons";

import { convertFileToBase64 } from "../../helpers";
import { getCalendarError, getRequiredErrors } from "../../../../utils/errors";

const { Dragger } = Upload;

type Props = {
  remove: () => void;
  change: (encodeCalendar: string) => void;
};

export const EditCalendar: React.FC<Props> = ({ change, remove }) => {
  const [form] = Form.useForm();

  const onFinish = useCallback(
    (values: any) => {
      convertFileToBase64(
        values.calendar[0].originFileObj,
      ).then((encodeCalendar) => change(encodeCalendar));
    },
    [change],
  );

  return (
    <Form
      form={form}
      onFinish={onFinish}
      layout="vertical"
      hideRequiredMark={true}
    >
      <Form.Item
        name="calendar"
        label="Calendar file (.ics)"
        valuePropName="fileList"
        getValueFromEvent={(value) => {
          if (value.fileList.length !== 0) {
            return [value.fileList[value.fileList.length - 1]];
          } else {
            return value.fileList;
          }
        }}
        rules={[...getRequiredErrors(false), getCalendarError()]}
      >
        <Dragger beforeUpload={() => false}>
          <p className="ant-upload-drag-icon">
            <InboxOutlined />
          </p>
          <p className="ant-upload-text">Click or drag file to this area</p>
        </Dragger>
      </Form.Item>

      <Form.Item>
        <Row justify="space-between">
          <Button type="primary" htmlType="submit">
            Save
          </Button>

          <Button type="danger" onClick={remove}>
            Remove
          </Button>
        </Row>
      </Form.Item>
    </Form>
  );
};
