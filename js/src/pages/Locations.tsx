import React, { useEffect, useCallback } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Card, Row, Col } from "antd";
import {
  fetchLocationsAsync,
  createLocationAsync,
  removeLocationAsync,
} from "features/human-location/actions";
import { location } from "features/human-location/selectors";
import { LocationsList } from "features/human-location/components/LocationsList";
import { LocationForm } from "features/human-location/components/LocationForm";
import { LocationForm as LocationFormModel } from "LocationModels";

export const LocationsPage: React.FC = () => {
  const dispatch = useDispatch();

  useEffect(() => {
    dispatch(fetchLocationsAsync.request());
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

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
};
