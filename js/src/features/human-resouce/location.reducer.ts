import { createReducer } from "typesafe-actions";
import { EmployeeLocation } from "LocationModels";
import {
  fetchEmployeeLocationsAsync,
  openEmployeeLocationEditForm,
  closeEmployeeLocationEditForm,
  editEmployeeLocationAsync,
  openLocationAssignForm,
  closeLocationAssignForm,
  assignLocationAsync,
} from "./actions";
import { FormErrorMap } from "MyTypes";
import { normalizeErrors } from "utils/errors";

export type State = {
  data: EmployeeLocation[];
  loading: boolean;
  formErrors: FormErrorMap;
  employeeLocationToEdit: EmployeeLocation | null;
  showAssignForm: boolean;
};

const initialState: State = {
  data: [],
  loading: false,
  formErrors: {},
  employeeLocationToEdit: null,
  showAssignForm: false,
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
  .handleAction(assignLocationAsync.success, (state, action) => ({
    ...state,
    formErrors: {},
    showAssignForm: false,
  }))
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
  }))
  .handleAction(openLocationAssignForm, (state, action) => ({
    ...state,
    showAssignForm: true,
  }))
  .handleAction(closeLocationAssignForm, (state, action) => ({
    ...state,
    showAssignForm: false,
  }));
