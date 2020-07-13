import React, { useEffect, useCallback } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Card, Row, Col, Modal } from "antd";
import { PlusOutlined } from "@ant-design/icons";

import {
  fetchLocationsAsync,
  createLocationAsync,
  removeLocationAsync,
  changeVisibilityLocationFormAction,
} from "features/location/actions";
import {
  selectIsOpenCreateLocationForm,
  selectLocation,
} from "features/location/selectors";
import { LocationsList } from "features/location/components/Location/LocationsList";
import { LocationForm } from "features/location/components/Location/LocationForm";
import { LocationForm as LocationFormModel } from "LocationModels";
import { withRule } from "../features/auth/components/withRule";

export const LocationsPage: React.FC = withRule("GET:/api/hr/locations")(() => {
  const dispatch = useDispatch();

  useEffect(() => {
    dispatch(fetchLocationsAsync.request());
  }, [dispatch]);

  const locations = useSelector(selectLocation);
  const isVisibleLocationForm = useSelector(selectIsOpenCreateLocationForm);

  const createLocation = useCallback(
    (form: LocationFormModel) => dispatch(createLocationAsync.request(form)),
    [dispatch],
  );

  const removeLocation = useCallback(
    (itemId: string) => dispatch(removeLocationAsync.request(itemId)),
    [dispatch],
  );

  const changeVisibilityLocationForm = useCallback(
    (visibility: boolean) =>
      dispatch(changeVisibilityLocationFormAction(visibility)),
    [dispatch],
  );

  return (
    <>
      <Row gutter={10} className="content">
        <Col lg={12} md={24} xs={24}>
          <Card
            title="Locations"
            extra={
              <PlusOutlined
                onClick={() => changeVisibilityLocationForm(true)}
              />
            }
          >
            <LocationsList items={locations.items} remove={removeLocation} />
          </Card>
        </Col>

        <Modal
          title="New location"
          visible={isVisibleLocationForm}
          onCancel={() => changeVisibilityLocationForm(false)}
          footer={null}
          destroyOnClose={true}
        >
          <LocationForm create={createLocation} errors={locations.formErrors} />
        </Modal>
      </Row>
    </>
  );
});
