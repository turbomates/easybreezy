import React from "react";
import { Form, Input, Button } from "antd";
import { PlusOutlined } from "@ant-design/icons";
import { UserContact } from "HumanResourceModels";
import { ContactIcon } from "./ContactIcon";

interface Props {
  contacts: UserContact[];
  canEdit: boolean;
}

const LABEL_COL = { span: 5 };
const WRAPPER_COL = { span: 8 };

export const Contacts = (props: Props) => {
  const { contacts, canEdit } = props;

  return (
    <>
      {contacts.length && (
        <Form layout="horizontal">
          {contacts.map(contact => (
            <Form.Item
              key={`${contact.type}${contact.value}`}
              labelCol={LABEL_COL}
              wrapperCol={WRAPPER_COL}
              label={
                <span>
                  <ContactIcon type={contact.type} /> {contact.type}
                </span>
              }
            >
              <Input value={contact.value} disabled={!canEdit} />
            </Form.Item>
          ))}
        </Form>
      )}
      {canEdit && (
        <Button onClick={() => {}}>
          <PlusOutlined /> Add Contact
        </Button>
      )}
    </>
  );
};
