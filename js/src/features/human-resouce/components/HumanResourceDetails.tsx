import React, { useState } from "react";
import {
  Card,
  List,
  Typography,
  Row,
  Col,
  Menu,
  Icon,
  DatePicker,
  Avatar,
} from "antd";
import moment from "moment";
import { UserDetails } from "HumanResourceModels";

import "./HumanResourceDetails.css";

const { RangePicker } = DatePicker;
const { Meta } = Card;

interface Props {
  user: UserDetails | null;
  loading: boolean;
}

export const HumanResourceDetails = (props: Props) => {
  const [selected, setSelected] = useState("general");
  const { loading, user } = props;

  return (
    <div className="human-resource-details">
      <Menu
        onClick={(e: any) => {
          console.log(e);
          setSelected(e.key);
        }}
        selectedKeys={[selected]}
        mode="horizontal"
      >
        <Menu.Item key="general">
          <Icon type="person" />
          General info
        </Menu.Item>
        <Menu.Item key="settings">
          <Icon type="setting" />
          Settings
        </Menu.Item>
      </Menu>
      <Row gutter={10} className="human-resource-details__grid">
        <Col span={12}>
          <Card className="user-card" loading={loading}>
            {user && (
              <>
                <Meta
                  title={user.username}
                  description={`${user.firstName} ${user.lastName}`}
                  avatar={
                    <Avatar
                      shape="square"
                      size={150}
                      icon="user"
                      src={user.avatar}
                    />
                  }
                />
                {user.description}
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
        </Col>
        <Col span={12}>
          <Card
            title="Vacations"
            bordered={false}
            className="user-card"
            loading={loading}
          >
            {user?.vacations.map((vacation, index) => (
              <span key={index} className="vacation-item">
                <RangePicker
                  defaultValue={[moment(vacation.from), moment(vacation.to)]}
                  format="DD/MM/YYYY"
                />
                <span className="vacation-item-description">
                  {vacation.description}
                </span>
              </span>
            ))}
          </Card>
          <Card title="Sick Days" bordered={false} className="user-card">
            Card content
          </Card>
          <Card title="Notes" bordered={false} className="user-card">
            Card content
          </Card>
        </Col>
      </Row>
    </div>
  );
};
