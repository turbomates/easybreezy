import React from "react";
import { Form, Input, Button, Row, Col, Select } from "antd";
import { MinusCircleTwoTone, PlusCircleOutlined } from "@ant-design/icons";
import {
  EmployeeContact,
  ContactsForm as ContactsFormModel,
} from "HumanResourceModels";

import "./ContactsForm.scss";

interface Props {
  contacts: EmployeeContact[];
  canEdit: boolean;
  update: (contacts: ContactsFormModel) => any;
}

const rules = {
  type: {
    required: true,
    whitespace: true,
    message: "Please input type or delete the whole contact.",
  },
  value: {
    required: true,
    whitespace: true,
    message: "Please input value or delete the whole contact.",
  },
};

export const ContactsForm: React.FC<Props> = ({
  contacts,
  canEdit,
  update,
}) => {
  const onFinish = (values: any) => update(values);
  const [form] = Form.useForm();

  return (
    <Form
      form={form}
      onFinish={onFinish}
      initialValues={{ contacts }}
      className="contacts-form"
    >
      <Form.List name="contacts">
        {(fields, { add, remove }) => (
          <div>
            {fields.map((field) => (
              <Row key={field.key}>
                <Col className="contacts-form__col">
                  <Form.Item
                    name={[field.name, "type"]}
                    fieldKey={[field.fieldKey, "type"] as any}
                    validateTrigger={["onChange", "onBlur"]}
                    rules={[rules.type]}
                  >
                    <Select
                      placeholder="type"
                      disabled={!canEdit}
                      className="contact-input"
                    >
                      <Select.Option value="SKYPE">Skype</Select.Option>
                      <Select.Option value="PHONE">Phone</Select.Option>
                      <Select.Option value="EMAIL">Email</Select.Option>
                      <Select.Option value="TELEGRAM">Telegram</Select.Option>
                      <Select.Option value="SLACK">Slack</Select.Option>
                    </Select>
                  </Form.Item>
                </Col>
                <Col className="contacts-form__col">
                  <Form.Item
                    name={[field.name, "value"]}
                    fieldKey={[field.fieldKey, "value"] as any}
                    validateTrigger={["onChange", "onBlur"]}
                    rules={[rules.value]}
                  >
                    <Input
                      placeholder="value"
                      disabled={!canEdit}
                      className="contact-input"
                    />
                  </Form.Item>
                </Col>
                <Col flex="none">
                  {canEdit && (
                    <MinusCircleTwoTone
                      twoToneColor="#ff7875"
                      className="dynamic-delete-button"
                      onClick={() => remove(field.name)}
                    />
                  )}
                </Col>
              </Row>
            ))}
            {canEdit && (
              <Row justify="end">
                <Col>
                  <PlusCircleOutlined
                    onClick={() => add()}
                    className="dynamic-add-button"
                  />
                </Col>
              </Row>
            )}
          </div>
        )}
      </Form.List>

      {canEdit && (
        <Form.Item>
          <Button type="primary" htmlType="submit">
            Update contacts
          </Button>
        </Form.Item>
      )}
    </Form>
  );
};
