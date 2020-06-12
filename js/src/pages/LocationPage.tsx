import React, { useCallback, useEffect, useMemo } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useParams } from "react-router";

import {
  addHolidayAsync,
  editHolidayAsync,
  fetchCalendarsAsync,
  fetchHolidaysAsync,
  fetchLocationsAsync,
  removeHolidayAsync,
} from "../features/location/actions";
import {
  selectCalendars,
  selectHolidays,
  selectLocation,
} from "../features/location/selectors";
import { HolidayCalendar } from "../features/location/components/Holiday/HolidayCalendar";
import { withRule } from "../features/auth/components/withRule";
import { LocationHeader } from "../features/location/components/LocationHeader";
import { AddHolidayForm, EditHolidayForm } from "LocationModels";

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

  const addHoliday = useCallback(
    (calendarId: string) => (form: AddHolidayForm) =>
      dispatch(
        addHolidayAsync.request({
          calendarId,
          ...form,
        }),
      ),
    [dispatch],
  );

  const editHoliday = useCallback(
    (calendarId: string) => (form: EditHolidayForm) =>
      dispatch(
        editHolidayAsync.request({
          calendarId,
          ...form,
        }),
      ),
    [dispatch],
  );

  const removeHoliday = useCallback(
    (calendarId: string) => (day: string) =>
      dispatch(
        removeHolidayAsync.request({
          calendarId,
          day,
        }),
      ),
    [dispatch],
  );

  const calendars = useSelector(selectCalendars);
  const holidays = useSelector(selectHolidays);
  const locations = useSelector(selectLocation);

  const location = useMemo(
    () => locations.items.find((location) => location.id === id),
    [locations, id],
  );

  const calendarId = useMemo(
    () =>
      calendars.reduce<string>((calendarId, calendar) => {
        return calendar.location.id === id ? calendar.id : calendarId;
      }, ""),
    [calendars, id],
  );

  useEffect(() => {
    fetchLocations();
  }, [fetchLocations]);

  useEffect(() => {
    fetchCalendars();
  }, [fetchCalendars]);

  useEffect(() => {
    if (!!calendarId) {
      fetchHolidays(calendarId);
    }
  }, [fetchHolidays, calendarId]);

  if (!location) return null;

  return (
    <>
      <LocationHeader
        location={location}
        hasCalendar={!!calendarId}
        calendarId={calendarId}
      />

      <div className="content">
        {calendarId && (
          <HolidayCalendar
            holidays={holidays}
            remove={removeHoliday(calendarId)}
            edit={editHoliday(calendarId)}
            add={addHoliday(calendarId)}
          />
        )}
      </div>
    </>
  );
});
