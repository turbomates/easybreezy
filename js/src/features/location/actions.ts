import { createAction, createAsyncAction } from "typesafe-actions";
import { FormError } from "MyTypes";

import {
  AddHolidayRequest,
  Calendar,
  ChangeCalendar,
  EditHolidayRequest,
  Holiday,
  ImportCalendarRequest,
  Location,
  LocationForm,
  RemoveHolidayRequest,
} from "LocationModels";

export const fetchLocationsAsync = createAsyncAction(
  "FETCH_LOCATIONS_REQUEST",
  "FETCH_LOCATIONS_SUCCESS",
  "FETCH_LOCATIONS_FAILURE",
)<undefined, Location[], string>();

export const createLocationAsync = createAsyncAction(
  "CREATE_LOCATION_REQUEST",
  "CREATE_LOCATION_SUCCESS",
  "CREATE_LOCATION_FAILURE",
)<LocationForm, undefined, FormError[]>();

export const removeLocationAsync = createAsyncAction(
  "REMOVE_LOCATION_REQUEST",
  "REMOVE_LOCATION_SUCCESS",
  "REMOVE_LOCATION_FAILURE",
)<string, undefined, string>();

export const changeVisibilityLocationFormAction = createAction(
  "CHANGE_VISIBILITY_LOCATION_FORM",
)<boolean>();

export const fetchCalendarsAsync = createAsyncAction(
  "FETCH_CALENDARS_REQUEST",
  "FETCH_CALENDARS_SUCCESS",
  "FETCH_CALENDARS_FAILURE",
)<undefined, Calendar[], string>();

export const fetchHolidaysAsync = createAsyncAction(
  "FETCH_HOLIDAYS_REQUEST",
  "FETCH_HOLIDAYS_SUCCESS",
  "FETCH_HOLIDAYS_FAILURE",
)<string, Holiday[], string>();

export const importCalendarAsync = createAsyncAction(
  "IMPORT_CALENDAR_REQUEST",
  "IMPORT_CALENDAR_SUCCESS",
  "IMPORT_CALENDAR_FAILURE",
)<ImportCalendarRequest, undefined, string>();

export const removeCalendarAsync = createAsyncAction(
  "REMOVE_CALENDAR_REQUEST",
  "REMOVE_CALENDAR_SUCCESS",
  "REMOVE_CALENDAR_FAILURE",
)<string, undefined, string>();

export const changeCalendarAction = createAction("CHANGE_CALENDAR")<
  ChangeCalendar
>();

export const addHolidayAsync = createAsyncAction(
  "ADD_HOLIDAY_REQUEST",
  "ADD_HOLIDAY_SUCCESS",
  "ADD_HOLIDAY_FAILURE",
)<AddHolidayRequest, undefined, FormError[]>();

export const editHolidayAsync = createAsyncAction(
  "EDIT_HOLIDAY_REQUEST",
  "EDIT_HOLIDAY_SUCCESS",
  "EDIT_HOLIDAY_FAILURE",
)<EditHolidayRequest, undefined, FormError[]>();

export const removeHolidayAsync = createAsyncAction(
  "REMOVE_HOLIDAY_REQUEST",
  "REMOVE_HOLIDAY_SUCCESS",
  "REMOVE_HOLIDAY_FAILURE",
)<RemoveHolidayRequest, undefined, string>();

export const changeVisibilityImportCalendarAction = createAction(
  "CHANGE_VISIBILITY_IMPORT_CALENDAR",
)<boolean>();

export const changeVisibilityEditCalendarAction = createAction(
  "CHANGE_VISIBILITY_EDIT_CALENDAR",
)<boolean>();
