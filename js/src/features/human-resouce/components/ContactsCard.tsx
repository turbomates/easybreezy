import React from "react";
import { Card, Form, Input, Button } from "antd";
import {
  SkypeOutlined,
  PhoneOutlined,
  ContactsOutlined,
  PlusOutlined,
  MailOutlined,
} from "@ant-design/icons";
import { UserContact } from "HumanResourceModels";

interface Props {
  contacts: UserContact[];
  loading: boolean;
  canEdit: boolean;
}

const getContactIcon = (type: string) => {
  switch (type) {
    case "phone":
      return <PhoneOutlined />;
    case "skype":
      return <SkypeOutlined />;
    case "email":
      return <MailOutlined />;

    default:
      return <ContactsOutlined />;
  }
};

const formItemLayout = {
  labelCol: {
    span: 5,
  },
  wrapperCol: {
    span: 8,
  },
};

export const ContactsCard = (props: Props) => {
  const { loading, contacts, canEdit } = props;

  return (
    <Card
      title="Contacts"
      bordered={false}
      className="human-resource-details__card contacts"
      loading={loading}
    >
      {contacts.length && (
        <Form name="global_state" layout="horizontal">
          {contacts.map(contact => (
            <Form.Item
              {...formItemLayout}
              label={
                <span>
                  {getContactIcon(contact.type)} {contact.type}
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
    </Card>
  );
};
