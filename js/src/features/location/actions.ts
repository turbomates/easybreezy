import { createAction, createAsyncAction } from "typesafe-actions";
import { FormError } from "MyTypes";

import { Calendar, Holiday, Location, LocationForm } from "LocationModels";

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

export const clearHolidaysAction = createAction("CLEAR_HOLIDAYS")();
