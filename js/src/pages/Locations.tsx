import React, { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Spin } from "antd";
import { LoadingOutlined } from "@ant-design/icons";
import {
  fetchLocationsAsync,
  createLocationAsync,
  removeLocationAsync,
} from "features/location/actions";
import { location } from "features/location/selectors";
import { LocationsList } from "features/location/components/LocationsList";
import { LocationForm } from "features/location/components/LocationForm";

export const LocationsPage: React.FC = () => {
  const dispatch = useDispatch();

  useEffect(() => {
    dispatch(fetchLocationsAsync.request());
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const { loading, items } = useSelector(location);

  const createLocation = (value: string) =>
    dispatch(createLocationAsync.request(value));

  const removeLocation = (itemId: string) =>
    dispatch(removeLocationAsync.request(itemId));

  if (loading) return <Spin indicator={<LoadingOutlined spin />} />;

  return (
    <>
      <h1>Locations</h1>
      <LocationForm create={createLocation} />
      <LocationsList items={items} remove={removeLocation} />
    </>
  );
};
