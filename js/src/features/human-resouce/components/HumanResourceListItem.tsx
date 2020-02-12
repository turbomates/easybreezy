import React from "react";
import { useHistory } from "react-router";
import { List, Avatar } from "antd";
import { User } from "HumanResourceModels";

export const HumanResourceListItem = (props: { item: User }) => {
  const history = useHistory();

  return (
    <List.Item onClick={() => history.push(`/users/${props.item.id}`)}>
      <List.Item.Meta
        avatar={<Avatar src={props.item.avatar} />}
        title={props.item.username}
        description="test user description"
      />
    </List.Item>
  );
};
