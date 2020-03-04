import React from "react";
import {
  SkypeOutlined,
  PhoneOutlined,
  ContactsOutlined,
  MailOutlined,
} from "@ant-design/icons";

interface Props {
  type: string;
}

export const ContactIcon = (props: Props) => {
  switch (props.type) {
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
