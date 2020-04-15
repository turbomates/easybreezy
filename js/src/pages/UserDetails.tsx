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
  addEmployeeNoteAsync,
  applyEmployeePositionAsync,
  applyEmployeeSalaryAsync,
} from "../features/human-resouce/actions";
import {
  UpdateBirthdayRequestParams,
  ContactsForm as ContactsFormModel,
  SpecifySkillsRequestParams,
  ApplySalaryForm,
} from "HumanResourceModels";
import { authUserId } from "features/auth/selectors";
import { employeeDetails } from "../features/human-resouce/selectors";
import { Profile } from "features/human-resouce/components/Profile";
import { ContactsForm } from "features/human-resouce/components/ContactsForm";
import { Notes } from "features/human-resouce/components/Notes";
import { NoteForm } from "features/human-resouce/components/NoteForm";
import { Positions } from "features/human-resouce/components/Positions";
import { PositionForm } from "features/human-resouce/components/PositionForm";
import { Salaries } from "features/human-resouce/components/Salaries";
import { SalaryForm } from "features/human-resouce/components/SalaryForm";

import "./UserDetails.scss";

export const UserDetailsPage: React.FC = () => {
  const [selected, setSelected] = useState("general");
  const { id } = useParams<{ id: string }>();
  const { employee, loading } = useSelector(employeeDetails);
  const authId = useSelector(authUserId);
  const dispatch = useDispatch();

  const canEdit = authId === id;
  const canSeeAdminStuff = canEdit;

  const load = useCallback(
    (id: string) => dispatch(fetchEmployeeAsync.request(id)),
    [dispatch],
  );

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

  const addNote = useCallback(
    (text: string) => {
      if (employee) {
        dispatch(
          addEmployeeNoteAsync.request({
            userId: employee.userId,
            text,
          }),
        );
      }
    },
    [dispatch, employee],
  );

  const applyPosition = useCallback(
    (position: string) => {
      if (employee) {
        dispatch(
          applyEmployeePositionAsync.request({
            userId: employee.userId,
            position,
          }),
        );
      }
    },
    [dispatch, employee],
  );

  const applySalary = useCallback(
    (form: ApplySalaryForm) => {
      if (employee) {
        dispatch(
          applyEmployeeSalaryAsync.request({
            userId: employee.userId,
            ...form,
          }),
        );
      }
    },
    [dispatch, employee],
  );

  useEffect(() => {
    load(id);
  }, [id, load]);

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
            {employee && (
              <Profile
                employee={employee}
                canEdit={canEdit}
                updateBirthday={updateBirthday}
                specifySkills={specifySkills}
              />
            )}
          </Card>
          <Card
            title="Locations"
            className="human-resource-details__card user-locations"
            loading={loading}
          ></Card>
          <Card
            title="Positions"
            className="human-resource-details__card user-positions"
            loading={loading}
          >
            {(employee?.positions?.length && (
              <Positions positions={employee.positions} />
            )) ||
              null}
            {canEdit && <PositionForm applyPosition={applyPosition} />}
          </Card>
          <Card
            title="Contacts"
            className="human-resource-details__card contacts"
            loading={loading}
          >
            <ContactsForm
              contacts={employee?.contacts || []}
              canEdit={canEdit}
              update={updateContacts}
            />
          </Card>
        </Col>
        <Col lg={12} md={24}>
          {canSeeAdminStuff && (
            <Card
              title="Salaries"
              className="human-resource-details__card"
              loading={loading}
            >
              {(employee?.salaries?.length && (
                <Salaries salaries={employee?.salaries || []} />
              )) ||
                null}
              {canEdit && <SalaryForm applySalary={applySalary} />}
            </Card>
          )}

          <Card
            title="Notes"
            className="human-resource-details__card"
            loading={loading}
          >
            <Notes notes={employee?.notes || []} canEdit={false} />
            {canEdit && <NoteForm addNote={addNote} />}
          </Card>
        </Col>
      </Row>
    </div>
  );
};
