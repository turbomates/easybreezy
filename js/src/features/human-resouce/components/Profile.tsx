import React from "react";
import { DatePicker, Avatar, Tag, Input } from "antd";
import { PlusOutlined, UserOutlined } from "@ant-design/icons";
import Paragraph from "antd/lib/typography/Paragraph";
import moment from "moment";
import { UserDetails } from "HumanResourceModels";

interface Props {
  user: UserDetails;
  canEdit: boolean;
}

export const Profile: React.FC<Props> = ({ user, canEdit }) => (
  <>
    <div className="user-details__short-info user-profile">
      <Avatar
        className="user-profile__image"
        shape="square"
        size={150}
        icon={<UserOutlined />}
        src={user.avatar}
      />
      <div className="user-profile__description">
        <h3>
          {user.firstName} {user.lastName}
        </h3>
        <h4>{user.username}</h4>

        <div className="user-profile__row">
          {user.skills.map(skill => (
            <Tag key={skill} closable={true}>
              {skill}
            </Tag>
          ))}
          {canEdit && (
            <Tag className="site-tag-plus">
              <PlusOutlined /> Add Skill
            </Tag>
          )}
        </div>
        <div className="user-profile__row">
          <label>Birthday</label>
          <DatePicker
            placeholder="Birthday"
            value={moment(user.birthday)}
            disabled={!canEdit}
          />
        </div>
        <div className="user-profile__row">
          <label>Location</label>
          <Input placeholder="Location" value="Minsk" disabled={!canEdit} />
        </div>
      </div>
    </div>
    <Paragraph className="user-description" editable={canEdit}>
      {user.description}
    </Paragraph>
  </>
);
