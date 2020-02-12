import React from "react";
import { Card, List, Typography } from "antd";
import { UserDetails } from "HumanResourceModels";

import "./HumanResourceDetails.css";

const { Meta } = Card;

interface Props {
  user: UserDetails | null;
  loading: boolean;
}

export const HumanResourceDetails = (props: Props) => {
  const { loading, user } = props;

  return (
    <div className="content human-resource-details">
      <Card
        className="user-card"
        loading={loading}
        cover={user ? <img alt="example" src={user.avatar} /> : null}
      >
        {user && (
          <>
            <Meta title={user.username} description={user.description} />
            <List bordered={true} className="user-details-list">
              <List.Item>
                <Typography.Text>First Name</Typography.Text>
                <Typography.Text>{user.firstName}</Typography.Text>
              </List.Item>
              <List.Item>
                <Typography.Text>Last Name</Typography.Text>
                <Typography.Text>{user.lastName}</Typography.Text>
              </List.Item>
              <List.Item>
                <Typography.Text>phone</Typography.Text>
                <Typography.Text>{user.phone}</Typography.Text>
              </List.Item>
            </List>
          </>
        )}
      </Card>
    </div>
  );
};
