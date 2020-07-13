import React, { useCallback, useEffect } from "react";
import { Card, Modal } from "antd";
import { PlusOutlined } from "@ant-design/icons";
import { useSelector, useDispatch } from "react-redux";
import { selectLocation } from "../../../location/selectors";
import {
  employeeLocations,
  isEditEmployeeLocationFormVisible,
  isAssignLocationFormVisible,
} from "../../selectors";
import {
  fetchEmployeeLocationsAsync,
  editEmployeeLocationAsync,
  assignLocationAsync,
  openEmployeeLocationEditForm,
  closeEmployeeLocationEditForm,
  openLocationAssignForm,
  closeLocationAssignForm,
} from "../../actions";
import { EmployeeLocationList } from "./EmployeeLocationList";
import {
  EditEmployeeLocationData,
  AssignLocationForm,
  EmployeeLocation,
} from "LocationModels";
import { LocationAssignForm } from "./LocationAssignForm";
import { EmployeeLocationEditForm } from "./EmployeeLocationEditForm";
import { fetchLocationsAsync } from "features/location/actions";

import "./EmployeeLocationsCard.scss";

interface Props {
  canEdit: boolean;
  employeeId: string;
}

export const EmployeeLocationsCard: React.FC<Props> = ({
  canEdit,
  employeeId,
}) => {
  const location = useSelector(selectLocation);
  const employeeLocation = useSelector(employeeLocations);
  const isEditVisible = useSelector(isEditEmployeeLocationFormVisible);
  const isAssignVisible = useSelector(isAssignLocationFormVisible);
  const dispatch = useDispatch();

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
    dispatch(fetchEmployeeLocationsAsync.request(employeeId));
  }, [employeeId, dispatch]);

  useEffect(() => {
    dispatch(fetchLocationsAsync.request());
  }, [dispatch]);

  return (
    <>
      <Card
        title="Locations"
        className="human-resource-details__card employee-location-card"
        loading={employeeLocation.loading}
        extra={canEdit ? <PlusOutlined onClick={handleOpenAssignForm} /> : null}
      >
        <EmployeeLocationList
          employeeLocations={employeeLocation.data}
          openEmployeeLocationEditForm={handleOpenEditForm}
        />
      </Card>
      <Modal
        title="Assign location"
        visible={isAssignVisible}
        onCancel={handleCloseAssignForm}
        footer={null}
        destroyOnClose={true}
      >
        <LocationAssignForm
          assign={assignEmployeeLocation}
          locations={location.items}
          errors={employeeLocation.formErrors}
        />
      </Modal>
      <Modal
        title="Edit employee location"
        visible={isEditVisible}
        onCancel={handleCloseEditForm}
        footer={null}
        destroyOnClose={true}
      >
        <EmployeeLocationEditForm
          employeeLocation={employeeLocation.employeeLocationToEdit!}
          edit={editEmployeeLocation}
          locations={location.items}
          errors={employeeLocation.formErrors}
        />
      </Modal>
    </>
  );
};
