import React from "react";
import { Form, Button } from "antd";
import TextArea from "antd/lib/input/TextArea";
import { FormErrorMap } from "MyTypes";
import { useFormServerErrors } from "hooks/useFormServerErrors";

interface Props {
  addNote: (text: string) => any;
  errors: FormErrorMap;
}

const TEXT_LENGTH = 256;

export const NoteForm: React.FC<Props> = ({ addNote, errors }) => {
  const [form] = Form.useForm();

  useFormServerErrors(form, errors, ["text"]);

  const handleFinish = (values: any) => addNote(values.text);

  return (
    <Form form={form} onFinish={handleFinish}>
      <Form.Item
        name="text"
        rules={[
          { required: true, message: "Please input message!" },
          {
            max: TEXT_LENGTH,
            message: `Note text is too long. Max length is ${TEXT_LENGTH}`,
          },
        ]}
      >
        <TextArea rows={4} placeholder="Enter note text" />
      </Form.Item>
      <Form.Item>
        <Button htmlType="submit" loading={false} type="primary">
          Add Note
        </Button>
      </Form.Item>
    </Form>
  );
};
