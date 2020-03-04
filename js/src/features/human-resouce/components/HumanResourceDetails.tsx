import React, { useState } from "react";
import { Row, Col, Menu, Card } from "antd";
import { SettingOutlined, UserOutlined } from "@ant-design/icons";
import { UserDetails } from "HumanResourceModels";

import { Profile } from "./Profile";
import { Contacts } from "./Contacts";
import { Vacations } from "./Vacations";
import { Notes } from "./Notes";

import "./HumanResourceDetails.scss";

interface Props {
  user: UserDetails | null;
  loading: boolean;
  account: UserDetails | null;
}

export const HumanResourceDetails = (props: Props) => {
  const [selected, setSelected] = useState("general");
  const { loading, user, account } = props;

  const canEdit = (account && user && account.id === user.id) || false;
  const canSeeAdminStuff = canEdit;

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
          <UserOutlined />
          General info
        </Menu.Item>
        <Menu.Item key="settings">
          <SettingOutlined />
          Settings
        </Menu.Item>
      </Menu>
      <Row gutter={10} className="human-resource-details__grid content">
        <Col lg={12} md={24}>
          <Card
            className="human-resource-details__card user-details"
            loading={loading}
          >
            {user && <Profile user={user} canEdit={canEdit} />}
          </Card>
          <Card
            title="Positions"
            className="human-resource-details__card user-positions"
            loading={loading}
          ></Card>
          <Card
            title="Contacts"
            className="human-resource-details__card contacts"
            loading={loading}
          >
            <Contacts contacts={user?.contacts || []} canEdit={canEdit} />
          </Card>
        </Col>
        <Col lg={12} md={24}>
          <Card
            title="Vacations"
            className="human-resource-details__card vacations"
            loading={loading}
          >
            <Vacations vacations={user?.vacations || []} canEdit={canEdit} />
          </Card>
          <Card
            title="Sick Days"
            className="human-resource-details__card"
            loading={loading}
          >
            Card content
          </Card>
          {canSeeAdminStuff && (
            <Card
              title="Notes"
              className="human-resource-details__card"
              loading={loading}
            >
              <Notes notes={user?.notes || []} canEdit={canEdit} />
            </Card>
          )}
        </Col>
      </Row>
    </div>
  );
};
