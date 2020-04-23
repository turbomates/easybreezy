import React from "react";
import { Avatar, Select } from "antd";
import { UserOutlined } from "@ant-design/icons";
import Paragraph from "antd/lib/typography/Paragraph";
import parseISO from "date-fns/parseISO";
import formatISO from "date-fns/formatISO";
import {
  Employee,
  UpdateBirthdayRequestParams,
  SpecifySkillsRequestParams,
} from "HumanResourceModels";

import DatePicker from "components/antd/DatePicker";
import "./Profile.scss";

interface Props {
  employee: Employee;
  canEdit: boolean;
  updateBirthday: (data: UpdateBirthdayRequestParams) => void;
  specifySkills: (data: SpecifySkillsRequestParams) => any;
}

export const Profile: React.FC<Props> = ({
  employee,
  canEdit,
  updateBirthday,
  specifySkills,
}) => {
  const handleBirthdayChange = (value: Date | null) => {
    updateBirthday({
      userId: employee.userId,
      birthday: value ? formatISO(value, { representation: "date" }) : "",
    });
  };

  const handleSkillChange = (skills: string[]) => {
    specifySkills({ userId: employee.userId, skills });
  };

  return (
    <>
      <div className="user-profile">
        <Avatar
          className="user-profile__image"
          shape="square"
          size={150}
          icon={<UserOutlined />}
          src=""
        />
        <div className="user-profile__description">
          <h3>
            {employee.firstName} {employee.lastName}
          </h3>
          <h4>email</h4>

          <div className="user-profile__row">
            <Select
              mode="tags"
              style={{ width: "100%" }}
              placeholder="Enter your skill"
              onChange={handleSkillChange}
              defaultValue={employee.skills}
              disabled={!canEdit}
            />
          </div>
          <div className="user-profile__row">
            <label>Birthday</label>
            <DatePicker
              placeholder="Birthday"
              onChange={handleBirthdayChange}
              defaultValue={
                employee.birthday ? parseISO(employee.birthday) : new Date()
              }
              disabled={!canEdit}
            />
          </div>
        </div>
      </div>
      <Paragraph className="user-description" editable={canEdit}>
        {employee.bio || ""}
      </Paragraph>
    </>
  );
};
