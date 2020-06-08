import { api } from "./api";
import { Failure, Success } from "MyTypes";
import {
  Calendar,
  CalendarResponse,
  Holiday,
  HolidayResponse,
} from "LocationModels";

export const fetchCalendars = () =>
  api
    .get<CalendarResponse>("/hr/calendars")
    .then<Success<Calendar[]>>((resp) => ({
      success: true,
      data: resp.data.data.calendars,
    }))
    .catch<Failure>(() => ({ success: false, reason: "Error" }));

export const fetchHolidays = (id: string) =>
  api
    .get<HolidayResponse>(`/hr/calendars/holidays/${id}`)
    .then<Success<Holiday[]>>((resp) => ({
      success: true,
      data: resp.data.data.holidays,
    }))
    .catch<Failure>(() => ({ success: false, reason: "Error" }));

