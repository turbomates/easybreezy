import React from "react";
import { Dropdown, Avatar } from "antd";
import { HeaderProfileMenu } from "./HeaderProfileMenu";
import { UserDetails } from "HumanResourceModels";
import { HeaderProfileDropdownSkeleton } from "./HeaderProfileDropdownSkeleton";

import "./HeaderProfileDropdown.css";

interface Props {
  profile: UserDetails | null;
  loading: boolean;
}

export const HeaderProfileDropdown = (props: Props) => {
  const { profile, loading } = props;
  if (loading || !profile) return <HeaderProfileDropdownSkeleton />;

  return (
    <Dropdown
      className="profile-dropdown"
      overlay={<HeaderProfileMenu userId={profile.id} />}
    >
      <div>
        <Avatar icon="user" src={profile.avatar} />
        <div className="profile-info">
          <div className="profile-info__name">
            {profile.firstName} {profile.lastName}
          </div>
          <div className="profile-info__email">{profile.username}</div>
        </div>
      </div>
    </Dropdown>
  );
};
