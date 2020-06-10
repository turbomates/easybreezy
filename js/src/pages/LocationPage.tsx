import React, { useCallback, useEffect, useMemo } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useParams } from "react-router";

import {
  clearHolidaysAction,
  fetchCalendarsAsync,
  fetchHolidaysAsync,
  fetchLocationsAsync,
} from "../features/location/actions";
import {
  selectCalendars,
  selectHolidays,
  selectLocation,
} from "../features/location/selectors";
import { HolidayCalendar } from "../features/location/components/Holiday/HolidayCalendar";
import { withRule } from "../features/auth/components/withRule";
import { LocationHeader } from "../features/location/components/LocationHeader";

export const LocationPage: React.FC = withRule("GET:/api/hr/calendars")(() => {
  const dispatch = useDispatch();
  const { id } = useParams<{ id: string }>();

  const fetchLocations = useCallback(() => {
    dispatch(fetchLocationsAsync.request());
  }, [dispatch]);

  const fetchCalendars = useCallback(() => {
    dispatch(fetchCalendarsAsync.request());
  }, [dispatch]);

  const fetchHolidays = useCallback(
    (id: string) => {
      dispatch(fetchHolidaysAsync.request(id));
    },
    [dispatch],
  );

  const clearHolidays = useCallback(() => {
    dispatch(clearHolidaysAction());
  }, [dispatch]);

  const calendars = useSelector(selectCalendars);
  const holidays = useSelector(selectHolidays);
  const locations = useSelector(selectLocation);

  const location = useMemo(() => {
    return locations.items.find((location) => location.id === id);
  }, [locations, id]);

  useEffect(() => {
    fetchLocations();
  }, [fetchLocations]);

  useEffect(() => {
    fetchCalendars();
  }, [fetchCalendars]);

  useEffect(() => {
    const calendar = calendars.find((calendar) => calendar.location.id === id);

    if (!!calendar) {
      fetchHolidays(calendar.id);
    }
  }, [fetchHolidays, calendars, id]);

  useEffect(() => {
    return () => clearHolidays();
  }, [clearHolidays]);

  if (!location) return null;

  return (
    <>
      <LocationHeader
        location={location}
        hasCalendar={calendars.some((calendar) => calendar.location.id === id)}
      />
      <div className="content">
        <HolidayCalendar holidays={holidays} />
      </div>
    </>
  );
});
