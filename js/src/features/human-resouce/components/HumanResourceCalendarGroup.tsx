import React from "react";
import { Avatar } from "antd";
import { UserOutlined } from "@ant-design/icons";
import { Link } from "react-router-dom";

import "./HumanResourceCalendarGroup.scss";

interface Props {
  firstName: string | null;
  lastName: string | null;
  avatar: string | null;
  id: string;
}

export const HumanResourceCalendarGroup: React.FC<Props> = (props) => {
  const name =
    props.firstName && props.lastName
      ? `${props.firstName} ${props.lastName}`
      : props.id;

  return (
    <Link to={`/users/${props.id}`} className="human-resource-calendar-group">
      <Avatar icon={<UserOutlined />} src={props.avatar || ""} />{" "}
      <span className="title">{name}</span>
    </Link>
  );
};
