import React, { useState } from "react";
import { Row, Col, Menu } from "antd";
import { SettingOutlined, UserOutlined } from "@ant-design/icons";
import { UserDetails } from "HumanResourceModels";

import { ProfileCard } from "./ProfileCard";
import { PositionsCard } from "./PositionsCard";
import { ContactsCard } from "./ContactsCard";
import { VacationsCard } from "./VacationsCard";
import { SickDaysCart } from "./SickDaysCart";
import { NotesCart } from "./NotesCart";

import "./HumanResourceDetails.scss";

interface Props {
  user: UserDetails | null;
  loading: boolean;
  account: UserDetails | null;
}

export const HumanResourceDetails = (props: Props) => {
  const [selected, setSelected] = useState("general");
  const { loading, user } = props;

  const canEdit = props.account?.id === props.user?.id;
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
      <Row gutter={10} className="human-resource-details__grid">
        <Col lg={12} md={24}>
          <ProfileCard user={user} loading={loading} canEdit={canEdit} />
          <PositionsCard user={user} loading={loading} canEdit={canEdit} />
          <ContactsCard
            contacts={user?.contacts || []}
            loading={loading}
            canEdit={canEdit}
          />
        </Col>
        <Col lg={12} md={24}>
          <VacationsCard
            vacations={user?.vacations || []}
            loading={loading}
            canEdit={canEdit}
          />
          <SickDaysCart sickDays={[]} loading={loading} canEdit={canEdit} />
          {canSeeAdminStuff && (
            <NotesCart
              notes={user?.notes || []}
              loading={loading}
              canEdit={canEdit}
            />
          )}
        </Col>
      </Row>
    </div>
  );
};
