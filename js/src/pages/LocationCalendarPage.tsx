import React, { useCallback, useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useParams } from "react-router";

import {
  clearHolidaysAction,
  fetchCalendarsAsync,
  fetchHolidaysAsync,
} from "../features/location/actions";
import {
  selectCalendars,
  selectHolidays,
} from "../features/location/selectors";
import { HolidayCalendar } from "../features/location/components/Holiday/HolidayCalendar";
import { withRule } from "../features/auth/components/withRule";

export const LocationCalendarPage: React.FC = withRule("GET:/api/hr/calendars")(() => {
  const dispatch = useDispatch();
  const { id } = useParams<{ id: string }>();

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

  return (
    <div className="content">
      <HolidayCalendar holidays={holidays} />
    </div>
  );
});
