import React, { useCallback } from "react";
import { Form, Input, Button, Upload, InputNumber } from "antd";
import { UploadOutlined } from "@ant-design/icons";

import { LocationForm as LocationFormModel } from "LocationModels";
import { FormErrorMap } from "MyTypes";
import { useFormServerErrors } from "hooks/useFormServerErrors";
import { getRequiredErrors } from "../../../../utils/errors";

interface Props {
  create: (form: LocationFormModel) => void;
  errors: FormErrorMap;
}

const initialFormValues = { vacationDays: 0 };

export const LocationForm: React.FC<Props> = ({ create, errors }) => {
  const [form] = Form.useForm();

  useFormServerErrors(form, errors, ["name", "vacationDays"]);

  const onFinish = useCallback(
    (values: any) => {
      create(values);
    },
    [create],
  );

  return (
    <Form
      name="createLocation"
      form={form}
      labelCol={{ span: 8 }}
      onFinish={onFinish}
      initialValues={initialFormValues}
    >
      <Form.Item label="Name" name="name" rules={[...getRequiredErrors(false)]}>
        <Input />
      </Form.Item>

      <Form.Item
        label="Vacation Days"
        name="vacationDays"
        rules={[...getRequiredErrors(false)]}
      >
        <InputNumber min={24} />
      </Form.Item>

      <Form.Item
        wrapperCol={{ offset: 8 }}
        // name="calendar"
        valuePropName="fileList"
        getValueFromEvent={(value) => {
          if (value.fileList.length !== 0) {
            return [value.fileList[value.fileList.length - 1]];
          } else {
            return value.fileList;
          }
        }}
      >
        <Upload beforeUpload={() => false}>
          <Button>
            <UploadOutlined />
            Upload calendar
          </Button>
        </Upload>
      </Form.Item>

      <Form.Item wrapperCol={{ offset: 8 }}>
        <Button type="primary" htmlType="submit" style={{ alignSelf: "right" }}>
          Create location
        </Button>
      </Form.Item>
    </Form>
  );
};
