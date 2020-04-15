import React from "react";
import { Form, Button } from "antd";
import TextArea from "antd/lib/input/TextArea";

interface Props {
  addNote: (text: string) => any;
}

export const NoteForm: React.FC<Props> = ({ addNote }) => {
  const [form] = Form.useForm();

  const handleFinish = (values: any) => {
    addNote(values.text);
    form.resetFields();
  };

  return (
    <Form form={form} onFinish={handleFinish}>
      <Form.Item name="text">
        <TextArea rows={4} />
      </Form.Item>
      <Form.Item>
        <Button htmlType="submit" loading={false} type="primary">
          Add Note
        </Button>
      </Form.Item>
    </Form>
  );
};
