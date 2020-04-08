import { createReducer } from "typesafe-actions";
import { EmployeeLocationsMap, EmployeeLocation } from "LocationModels";
import {
  fetchEmployeeLocationsAsync,
  assignLocationAsync,
  openEmployeeLocationEditForm,
  closeEmployeeLocationEditForm,
  editEmployeeLocationAsync,
} from "./actions";
import { FormErrorMap } from "MyTypes";
import { normalizeErrors } from "utils/error";

export type State = {
  data: EmployeeLocationsMap;
  loading: boolean;
  formErrors: FormErrorMap;
  employeeLocationToEdit: EmployeeLocation | null;
};

const initialState: State = {
  data: {},
  loading: false,
  formErrors: {},
  employeeLocationToEdit: null,
};

export const reducer = createReducer<State>(initialState)
  .handleAction(fetchEmployeeLocationsAsync.request, (state, action) => ({
    ...state,
    loading: true,
  }))
  .handleAction(fetchEmployeeLocationsAsync.success, (state, action) => ({
    ...state,
    data: action.payload,
    loading: false,
  }))
  .handleAction(fetchEmployeeLocationsAsync.failure, () => initialState)
  .handleAction(assignLocationAsync.failure, (state, action) => ({
    ...state,
    formErrors: normalizeErrors(action.payload),
  }))
  .handleAction(openEmployeeLocationEditForm, (state, action) => ({
    ...state,
    formErrors: {},
    employeeLocationToEdit: action.payload,
  }))
  .handleAction(closeEmployeeLocationEditForm, (state, action) => ({
    ...state,
    formErrors: {},
    employeeLocationToEdit: null,
  }))
  .handleAction(editEmployeeLocationAsync.success, (state, action) => ({
    ...state,
    employeeLocationToEdit: null,
    formErrors: {},
  }))
  .handleAction(editEmployeeLocationAsync.failure, (state, action) => ({
    ...state,
    formErrors: normalizeErrors(action.payload),
  }));
