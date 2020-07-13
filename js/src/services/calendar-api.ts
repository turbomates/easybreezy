import { api } from "./api";
import { Failure, FormFailure, Success } from "MyTypes";
import {
  AddHolidayRequest,
  Calendar,
  CalendarResponse,
  EditHolidayRequest,
  Holiday,
  HolidayResponse,
  ImportCalendarRequest,
  RemoveHolidayRequest,
} from "LocationModels";

export const fetchCalendars = () =>
  api
    .get<CalendarResponse>("/hr/calendars")
    .then<Success<Calendar[]>>((resp) => ({
      success: true,
      data: resp.data.data.calendars,
    }))
    .catch<Failure>(() => ({
      success: false,
      reason: "unexpected_server_error",
    }));

export const fetchHolidays = (id: string) =>
  api
    .get<HolidayResponse>(`/hr/calendars/holidays/${id}`)
    .then<Success<Holiday[]>>((resp) => ({
      success: true,
      data: resp.data.data.holidays,
    }))
    .catch<Failure>(() => ({
      success: false,
      reason: "unexpected_server_error",
    }));

export const importCalendar = (body: ImportCalendarRequest) =>
  api
    .post(`/hr/calendars`, body)
    .then<Success<null>>(() => ({
      success: true,
      data: null,
    }))
    .catch<Failure>(() => ({
      success: false,
      reason: "unexpected_server_error",
    }));

export const removeCalendar = (id: string) =>
  api
    .delete(`/hr/calendars/${id}`)
    .then<Success<null>>(() => ({
      success: true,
      data: null,
    }))
    .catch<Failure>(() => ({
      success: false,
      reason: "unexpected_server_error",
    }));

export const addHoliday = (body: AddHolidayRequest) =>
  api
    .post(`/hr/calendars/holidays`, body)
    .then<Success<null>>(() => ({
      success: true,
      data: null,
    }))
    .catch<FormFailure>((resp) => ({
      success: false,
      errors: resp?.response?.data?.errors || [],
    }));

export const editHoliday = ({ calendarId, ...body }: EditHolidayRequest) =>
  api
    .post(`/hr/calendars/holidays/${calendarId}`, body)
    .then<Success<null>>(() => ({
      success: true,
      data: null,
    }))
    .catch<FormFailure>((resp) => ({
      success: false,
      errors: resp?.response?.data?.errors || [],
    }));

export const removeHoliday = (body: RemoveHolidayRequest) =>
  api
    .delete(`/hr/calendars/holidays`, { data: body })
    .then<Success<null>>(() => ({
      success: true,
      data: null,
    }))
    .catch<Failure>(() => ({
      success: false,
      reason: "unexpected_server_error",
    }));
