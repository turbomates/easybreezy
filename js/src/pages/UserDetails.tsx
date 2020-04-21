import React, { useEffect, useCallback, useState } from "react";
import { useParams } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";
import { SettingOutlined, UserOutlined } from "@ant-design/icons";
import { Row, Col, Menu, Card, Modal, Button } from "antd";
import {
  fetchEmployeeAsync,
  updateEmployeeBirthdayAsync,
  updateContactsAsync,
  specifyEmployeeSkillsAsync,
  addEmployeeNoteAsync,
  applyEmployeePositionAsync,
  applyEmployeeSalaryAsync,
  fetchEmployeeLocationsAsync,
  editEmployeeLocationAsync,
  closeEmployeeLocationEditForm,
  openEmployeeLocationEditForm,
  openLocationAssignForm,
  closeLocationAssignForm,
  assignLocationAsync,
} from "../features/human-resouce/actions";
import { fetchLocationsAsync } from "features/location/actions";
import { authUserId } from "features/auth/selectors";
import { isAdmin as isAdminSelector } from "features/account/selectors";
import {
  employeeDetails,
  employeeLocations,
  isEditEmployeeLocationFormVisible,
  isAssignLocationFormVisible,
} from "../features/human-resouce/selectors";
import { location as locationSelector } from "../features/location/selectors";
import {
  UpdateBirthdayRequestParams,
  ContactsForm as ContactsFormModel,
  SpecifySkillsRequestParams,
  ApplySalaryForm,
} from "HumanResourceModels";
import {
  EditEmployeeLocationData,
  EmployeeLocation,
  AssignLocationForm,
} from "LocationModels";
import { Profile } from "features/human-resouce/components/Profile";
import { ContactsForm } from "features/human-resouce/components/ContactsForm";
import { Notes } from "features/human-resouce/components/Notes";
import { NoteForm } from "features/human-resouce/components/NoteForm";
import { Positions } from "features/human-resouce/components/Positions";
import { PositionForm } from "features/human-resouce/components/PositionForm";
import { Salaries } from "features/human-resouce/components/Salaries";
import { SalaryForm } from "features/human-resouce/components/SalaryForm";
import { EmployeeLocationList } from "features/human-resouce/components/EmployeeLocationList";
import { EmployeeLocationEditForm } from "features/human-resouce/components/EmployeeLocationEditForm";
import { LocationAssignForm } from "features/human-resouce/components/LocationAssignForm";

import "./UserDetails.scss";

export const UserDetailsPage: React.FC = () => {
  const [selected, setSelected] = useState("general");
  const { id } = useParams<{ id: string }>();
  const { employee, loading } = useSelector(employeeDetails);
  const location = useSelector(locationSelector);
  const employeeLocation = useSelector(employeeLocations);
  const isAdmin = useSelector(isAdminSelector);
  const authId = useSelector(authUserId);
  const isEditVisible = useSelector(isEditEmployeeLocationFormVisible);
  const isAssignVisible = useSelector(isAssignLocationFormVisible);
  const dispatch = useDispatch();

  const canEdit = authId === id;

  const load = useCallback(() => {
    dispatch(fetchEmployeeAsync.request(id));
  }, [dispatch, id]);

  const loadLocations = useCallback(() => {
    dispatch(fetchEmployeeLocationsAsync.request(id));
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

  const editEmployeeLocation = useCallback(
    (data: EditEmployeeLocationData) =>
      dispatch(editEmployeeLocationAsync.request(data)),
    [dispatch],
  );

  const assignEmployeeLocation = useCallback(
    (data: AssignLocationForm) => dispatch(assignLocationAsync.request(data)),
    [dispatch],
  );

  const handleOpenEditForm = useCallback(
    (employeeLocation: EmployeeLocation) =>
      dispatch(openEmployeeLocationEditForm(employeeLocation)),
    [dispatch],
  );

  const handleCloseEditForm = useCallback(
    () => dispatch(closeEmployeeLocationEditForm()),
    [dispatch],
  );

  const handleOpenAssignForm = useCallback(
    () => dispatch(openLocationAssignForm()),
    [dispatch],
  );

  const handleCloseAssignForm = useCallback(
    () => dispatch(closeLocationAssignForm()),
    [dispatch],
  );

  useEffect(() => {
    load();
  }, [id, load]);

  useEffect(() => {
    loadLocations();
  }, [id, loadLocations]);

  useEffect(() => {
    dispatch(fetchLocationsAsync.request());
  }, [dispatch]);

  return (
    <div className="human-resource-details">
      <Menu
        onClick={(e: any) => {
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
          <Card
            title="Locations"
            className="human-resource-details__card"
            loading={loading}
          >
            <EmployeeLocationList
              employeeLocations={employeeLocation.data}
              openEmployeeLocationEditForm={handleOpenEditForm}
              // TODO refactor when back ready
              remove={() => {}}
            />
            <Button onClick={handleOpenAssignForm}>Assign new location</Button>
          </Card>
          <Card
            title="Positions"
            className="human-resource-details__card"
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
        <Col lg={12} md={24}>
          {(isAdmin || canEdit) && (
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
      <Modal
        title="Assign location"
        visible={isAssignVisible}
        onCancel={handleCloseAssignForm}
        footer={null}
      >
        {isAssignVisible && (
          <LocationAssignForm
            assign={assignEmployeeLocation}
            locations={location.items}
            errors={employeeLocation.formErrors}
          />
        )}
      </Modal>
      <Modal
        title="Edit employee location"
        visible={isEditVisible}
        onCancel={handleCloseEditForm}
        footer={null}
      >
        {isEditVisible && (
          <EmployeeLocationEditForm
            employeeLocation={employeeLocation.employeeLocationToEdit!}
            edit={editEmployeeLocation}
            locations={location.items}
            errors={employeeLocation.formErrors}
          />
        )}
      </Modal>
    </div>
  );
};
