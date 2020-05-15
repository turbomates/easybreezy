import React, { useEffect, useCallback } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Card, Row, Col } from "antd";
import {
  fetchLocationsAsync,
  createLocationAsync,
  removeLocationAsync,
} from "features/location/actions";
import { location } from "features/location/selectors";
import { LocationsList } from "features/location/components/LocationsList";
import { LocationForm } from "features/location/components/LocationForm";
import { LocationForm as LocationFormModel } from "LocationModels";
import { withRule } from "../features/auth/components/withRule";

export const LocationsPage: React.FC = withRule("GET:/api/hr/locations")(() => {
  const dispatch = useDispatch();

  useEffect(() => {
    dispatch(fetchLocationsAsync.request());
  }, [dispatch]);

  const locations = useSelector(location);

  const createLocation = useCallback(
    (form: LocationFormModel) => dispatch(createLocationAsync.request(form)),
    [dispatch],
  );

  const removeLocation = useCallback(
    (itemId: string) => dispatch(removeLocationAsync.request(itemId)),
    [dispatch],
  );

  return (
    <>
      <Row gutter={10} className="content">
        <Col lg={12} md={24} xs={24}>
          <Card title="Locations" loading={locations.loading}>
            <LocationForm
              create={createLocation}
              errors={locations.formErrors}
            />
            <LocationsList items={locations.items} remove={removeLocation} />
          </Card>
        </Col>
      </Row>
    </>
  );
});
