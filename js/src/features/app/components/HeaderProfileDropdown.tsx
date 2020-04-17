import React from "react";
import { Dropdown, Avatar } from "antd";
import { UserOutlined } from "@ant-design/icons";
import { HeaderProfileMenu } from "./HeaderProfileMenu";
import { HeaderProfileDropdownSkeleton } from "./HeaderProfileDropdownSkeleton";
import { Profile } from "AccountModules";

import "./HeaderProfileDropdown.css";

interface Props {
  profile: Profile | null;
  loading: boolean;
  logout: () => void;
}

export const HeaderProfileDropdown = (props: Props) => {
  const { profile, loading, logout } = props;

  if (loading || !profile) return <HeaderProfileDropdownSkeleton />;

  return (
    <Dropdown
      className="profile-dropdown"
      overlay={<HeaderProfileMenu userId={profile.id} logout={logout} />}
    >
      <div>
        <Avatar icon={<UserOutlined />} src="" />
        <div className="profile-info">
          <div className="profile-info__name">Firstname Lastname</div>
          <div className="profile-info__email">{profile.email}</div>
        </div>
      </div>
    </Dropdown>
  );
};
