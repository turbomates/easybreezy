import React, { useEffect, useCallback } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Card, Row, Col, Modal, List } from "antd";
import {
  fetchLocationsAsync,
  createLocationAsync,
  removeLocationAsync,
  fetchEmployeesAsync,
  assignLocationAsync,
  fetchEmployeeLocationsAsync,
  removeEmployeeLocationAsync,
  openLocationAssignForm,
  closeLocationAssignForm,
  openEmployeeLocationEditForm,
  closeEmployeeLocationEditForm,
  editEmployeeLocationAsync,
} from "features/human-location/actions";
import {
  location,
  employee,
  employeeLocation,
  isAssignLocationFormVisible,
  isEditEmployeeLocationFormVisible,
} from "features/human-location/selectors";
import { LocationsList } from "features/human-location/components/LocationsList";
import { LocationForm } from "features/human-location/components/LocationForm";
import { EmployeeListItem } from "features/human-location/components/EmployeeListItem";
import {
  LocationForm as LocationFormModel,
  AssignLocationForm,
  EmployeeLocation,
  EditEmployeeLocationData,
} from "LocationModels";
import { LocationAssignForm } from "features/human-location/components/LocationAssignForm";
import { EmployeeLocationEditForm } from "features/human-location/components/EmployeeLocationEditForm";

export const LocationsPage: React.FC = () => {
  const dispatch = useDispatch();

  useEffect(() => {
    dispatch(fetchLocationsAsync.request());
    dispatch(fetchEmployeesAsync.request());
    dispatch(fetchEmployeeLocationsAsync.request());
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const locations = useSelector(location);
  const employees = useSelector(employee);
  const employeeLocations = useSelector(employeeLocation);
  const isAssignVisible = useSelector(isAssignLocationFormVisible);
  const isEditVisible = useSelector(isEditEmployeeLocationFormVisible);

  const createLocation = useCallback(
    (form: LocationFormModel) => dispatch(createLocationAsync.request(form)),
    [dispatch],
  );

  const removeLocation = useCallback(
    (itemId: string) => dispatch(removeLocationAsync.request(itemId)),
    [dispatch],
  );

  const assignLocation = useCallback(
    (form: AssignLocationForm) => dispatch(assignLocationAsync.request(form)),
    [dispatch],
  );

  const editEmployeeLocation = useCallback(
    (data: EditEmployeeLocationData) =>
      dispatch(editEmployeeLocationAsync.request(data)),
    [dispatch],
  );

  const removeEmployeeLocation = useCallback(
    (id: string) => dispatch(removeEmployeeLocationAsync.request(id)),
    [dispatch],
  );

  const handleOpenAssignForm = useCallback(
    (id: string) => dispatch(openLocationAssignForm(id)),
    [dispatch],
  );

  const handleOpenEditForm = useCallback(
    (employeeLocation: EmployeeLocation) =>
      dispatch(openEmployeeLocationEditForm(employeeLocation)),
    [dispatch],
  );

  const handleCloseAssignForm = useCallback(
    () => dispatch(closeLocationAssignForm()),
    [dispatch],
  );

  const handleCloseEditForm = useCallback(
    () => dispatch(closeEmployeeLocationEditForm()),
    [dispatch],
  );

  return (
    <>
      <Row gutter={10} className="content">
        <Col lg={12} xs={24}>
          <Card title="Locations" loading={locations.loading}>
            <LocationForm
              create={createLocation}
              errors={locations.formErrors}
            />
            <LocationsList items={locations.items} remove={removeLocation} />
          </Card>
        </Col>
        <Col lg={12} xs={24}>
          <Card title="Employees">
            <List
              dataSource={employees.items}
              renderItem={(item) => (
                <EmployeeListItem
                  employee={item}
                  employeeLocations={employeeLocations.data[item.userId]}
                  openLocationAssignForm={handleOpenAssignForm}
                  openEmployeeLocationEditForm={handleOpenEditForm}
                  remove={removeEmployeeLocation}
                />
              )}
            />
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
            userId={employees.idToAssign!}
            assign={assignLocation}
            locations={locations.items}
            errors={employeeLocations.formErrors}
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
            employeeLocation={employeeLocations.employeeLocationToEdit!}
            edit={editEmployeeLocation}
            locations={locations.items}
            errors={employeeLocations.formErrors}
          />
        )}
      </Modal>
    </>
  );
};
