import React from "react";
import { Form, Button, Input, Select } from "antd";
import DatePicker from "components/antd/DatePicker";
import { AbsenceForm as AbsenceFormModel, Absence } from "HumanResourceModels";
import { FormErrorMap } from "MyTypes";
import { serializeForm, deserializeForm } from "./absence.helper";
import { useFormServerErrors } from "hooks/useFormServerErrors";

const { RangePicker } = DatePicker;

interface Props {
  mode: "CREATE" | "UPDATE";
  onSubmit: (form: AbsenceFormModel) => void;
  errors: FormErrorMap;
  initialValues?: Absence;
}

export const AbsenceForm: React.FC<Props> = (props) => {
  const { onSubmit, mode, errors } = props;
  const [form] = Form.useForm();

  const initialValues = props.initialValues
    ? deserializeForm(props.initialValues)
    : {};
  const handleFinish = (values: any) => onSubmit(serializeForm(values));

  useFormServerErrors(form, errors, ["range", "reason", "comment"]);

  return (
    <Form form={form} onFinish={handleFinish} initialValues={initialValues}>
      <Form.Item
        name="range"
        label="Range"
        labelCol={{ span: 4 }}
        rules={[
          { required: true, message: "Please input start and end dates!" },
        ]}
      >
        <RangePicker />
      </Form.Item>

      <Form.Item
        name="reason"
        label="Reason"
        labelCol={{ span: 4 }}
        rules={[{ required: true, message: "Please input reason!" }]}
      >
        <Select placeholder="Select Reason">
          <Select.Option value="VACATION">Vacation</Select.Option>
          <Select.Option value="DAYON">Day on</Select.Option>
          <Select.Option value="SICK">Sick</Select.Option>
          <Select.Option value="PERSONAL">Personal</Select.Option>
        </Select>
      </Form.Item>

      <Form.Item name="comment" label="Comment" labelCol={{ span: 4 }}>
        <Input.TextArea placeholder="Enter Comment" />
      </Form.Item>

      <Form.Item wrapperCol={{ offset: 4 }}>
        <Button htmlType="submit" loading={false} type="primary">
          <ButtonTitle mode={mode} />
        </Button>
      </Form.Item>
    </Form>
  );
};

function ButtonTitle(props: { mode: "CREATE" | "UPDATE" }) {
  switch (props.mode) {
    case "UPDATE":
      return <span>Save</span>;
    case "CREATE":
    default:
      return <span>Add</span>;
  }
}
