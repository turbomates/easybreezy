import { FormError } from "MyTypes";
import {
  Location,
  LocationForm,
  AssignLocationForm,
  EditEmployeeLocationData,
  EmployeeLocationsMap,
  EmployeeLocation,
} from "LocationModels";
import { createAsyncAction, createAction } from "typesafe-actions";

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

export const assignLocationAsync = createAsyncAction(
  "ASSIGN_LOCATION_REQUEST",
  "ASSIGN_LOCATION_SUCCESS",
  "ASSIGN_LOCATION_FAILURE",
)<AssignLocationForm, undefined, FormError[]>();

export const fetchEmployeeLocationsAsync = createAsyncAction(
  "FETCH_EMPLOYEE_LOCATIONS_REQUEST",
  "FETCH_EMPLOYEE_LOCATIONS_SUCCESS",
  "FETCH_EMPLOYEE_LOCATIONS_FAILURE",
)<undefined, EmployeeLocationsMap, string>();

export const editEmployeeLocationAsync = createAsyncAction(
  "EDIT_EMPLOYEE_LOCATION_REQUEST",
  "EDIT_EMPLOYEE_LOCATION_SUCCESS",
  "EDIT_EMPLOYEE_LOCATION_FAILURE",
)<EditEmployeeLocationData, undefined, FormError[]>();

export const removeEmployeeLocationAsync = createAsyncAction(
  "REMOVE_EMPLOYEE_LOCATION_REQUEST",
  "REMOVE_EMPLOYEE_LOCATION_SUCCESS",
  "REMOVE_EMPLOYEE_LOCATION_FAILURE",
)<string, undefined, string>();

export const openLocationAssignForm = createAction(
  "OPEN_LOCATIONS_ASSIGN_FORM",
)<string | null>();

export const closeLocationAssignForm = createAction(
  "CLOSE_LOCATIONS_ASSIGN_FORM",
)();

export const openEmployeeLocationEditForm = createAction(
  "OPEN_EMPLOYEE_LOCATION_EDIT_FORM",
)<EmployeeLocation>();

export const closeEmployeeLocationEditForm = createAction(
  "CLOSE_EMPLOYEE_LOCATION_EDIT_FORM",
)();
