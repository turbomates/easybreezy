import React from "react";
import { Form, Input, Button } from "antd";
import { PlusOutlined } from "@ant-design/icons";
import { UserContact } from "HumanResourceModels";
import { ContactIcon } from "./ContactIcon";

interface Props {
  contacts: UserContact[];
  canEdit: boolean;
}

export const Contacts: React.FC<Props> = ({ contacts, canEdit }) => (
  <>
    {contacts.length && (
      <Form layout="horizontal">
        {contacts.map(contact => (
          <Form.Item
            key={`${contact.type}${contact.value}`}
            labelCol={{ span: 5 }}
            wrapperCol={{ span: 8 }}
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
