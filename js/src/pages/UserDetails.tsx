import React, { useEffect, useCallback, useState } from "react";
import { useParams } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";
import { SettingOutlined, UserOutlined } from "@ant-design/icons";
import { Row, Col, Menu, Card } from "antd";
import {
  fetchEmployeeAsync,
  updateEmployeeBirthdayAsync,
  updateContactsAsync,
  specifyEmployeeSkillsAsync,
} from "../features/human-resouce/actions";
import { authUserId } from "features/auth/selectors";
import { isAdmin as isAdminSelector } from "features/account/selectors";
import { employeeDetails } from "../features/human-resouce/selectors";
import {
  UpdateBirthdayRequestParams,
  ContactsForm as ContactsFormModel,
  SpecifySkillsRequestParams,
} from "HumanResourceModels";
import { Profile } from "features/human-resouce/components/Profile";
import { ContactsForm } from "features/human-resouce/components/ContactsForm";
import { AbsencesCard } from "features/human-resouce/components/Absence/AbsencesCard";
import { NotesCard } from "features/human-resouce/components/Note/NotesCard";
import { SalariesCard } from "features/human-resouce/components/Salary/SalariesCard";
import { EmployeeLocationsCard } from "features/human-resouce/components/EmployeeLocation/EmployeeLocationsCard";
import { PositionsCard } from "features/human-resouce/components/Position/PositionsCard";

import "./UserDetails.scss";

export const UserDetailsPage: React.FC = () => {
  const [selected, setSelected] = useState("general");
  const { id } = useParams<{ id: string }>();
  const details = useSelector(employeeDetails);
  const { employee, loading } = details;
  const isAdmin = useSelector(isAdminSelector);
  const authId = useSelector(authUserId);

  const dispatch = useDispatch();

  const canEdit = authId === id;

  const load = useCallback(() => {
    dispatch(fetchEmployeeAsync.request(id));
  }, [dispatch, id]);

  const updateBirthday = useCallback(
    (data: UpdateBirthdayRequestParams) =>
      dispatch(updateEmployeeBirthdayAsync.request(data)),
    [dispatch],
  );

  const updateContacts = useCallback(
    (data: ContactsFormModel) => dispatch(updateContactsAsync.request(data)),
    [dispatch],
  );

  const specifySkills = useCallback(
    (data: SpecifySkillsRequestParams) =>
      dispatch(specifyEmployeeSkillsAsync.request(data)),
    [dispatch],
  );

  useEffect(() => {
    load();
  }, [id, load]);

  return (
    <div className="human-resource-details">
      <Menu
        onClick={({ key }) => {
          setSelected(key);
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
        <Col lg={12} md={24} xs={24}>
          <Card className="human-resource-details__card" loading={loading}>
            {employee && (
              <Profile
                employee={employee}
                canEdit={canEdit}
                updateBirthday={updateBirthday}
                specifySkills={specifySkills}
              />
            )}
          </Card>
          <EmployeeLocationsCard employeeId={id} canEdit={canEdit} />
          <PositionsCard canEdit={canEdit} details={details} />
          <Card
            title="Contacts"
            className="human-resource-details__card"
            loading={loading}
          >
            <ContactsForm
              contacts={employee?.contacts || []}
              canEdit={canEdit}
              update={updateContacts}
            />
          </Card>
        </Col>
        <Col lg={12} md={24} xs={24}>
          {(isAdmin || canEdit) && (
            <SalariesCard canEdit={canEdit} details={details} />
          )}
          <NotesCard canEdit={canEdit} details={details} />
          <AbsencesCard canEdit={canEdit} employeeId={id} />
        </Col>
      </Row>
    </div>
  );
};
