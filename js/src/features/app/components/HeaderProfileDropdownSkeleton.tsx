import React from "react";
import { Avatar } from "antd";

import "./HeaderProfileDropdownSkeleton.css";

export const HeaderProfileDropdownSkeleton = () => {
  return (
    <div className="profile-dropdown">
      <Avatar icon="user" />
      <div className="profile-info">
        <div className="profile-info__name">
          <span className="placeholder" />
        </div>
        <div className="profile-info__email">
          <span className="placeholder" />
        </div>
      </div>
    </div>
  );
};
