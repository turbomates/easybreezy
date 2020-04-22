import {
  UserVacations,
  Employee,
  EmployeeShort,
  UpdateBirthdayRequestParams,
  ContactsForm,
  SpecifySkillsRequestParams,
  AddNoteRequestParams,
  ApplyPositionRequestParams,
  ApplySalaryRequestParams,
} from "HumanResourceModels";
import { createAsyncAction, createAction } from "typesafe-actions";
import { Paging, FormError } from "MyTypes";
import {
  AssignLocationForm,
  EditEmployeeLocationData,
  EmployeeLocation,
} from "LocationModels";

export const fetchUsersVacationsAsync = createAsyncAction(
  "FETCH_USERS_VACATIONS_REQUEST",
  "FETCH_USERS_VACATIONS_SUCCESS",
  "FETCH_USERS_VACATIONS_FAILURE",
)<undefined, UserVacations[], string>();

export const fetchEmployeeAsync = createAsyncAction(
  "FETCH_USER_DETAILS_REQUEST",
  "FETCH_USER_DETAILS_SUCCESS",
  "FETCH_USER_DETAILS_FAILURE",
)<string, Employee, string>();

export const fetchEmployeesAsync = createAsyncAction(
  "FETCH_EMPLOYEES_REQUEST",
  "FETCH_EMPLOYEES_SUCCESS",
  "FETCH_EMPLOYEES_FAILURE",
)<undefined, Paging<EmployeeShort>, string>();

export const updateEmployeeBirthdayAsync = createAsyncAction(
  "UPDATE_EMPLOYEE_BIRTHDAY_REQUEST",
  "UPDATE_EMPLOYEE_BIRTHDAY_SUCCESS",
  "UPDATE_EMPLOYEE_BIRTHDAY_FAILURE",
)<UpdateBirthdayRequestParams, undefined, string>();

export const updateContactsAsync = createAsyncAction(
  "UPDATE_CONTACTS_REQUEST",
  "UPDATE_CONTACTS_SUCCESS",
  "UPDATE_CONTACTS_FAILURE",
)<ContactsForm, undefined, string>();

export const specifyEmployeeSkillsAsync = createAsyncAction(
  "SPECIFY_EMPLOYEE_SKILLS_REQUEST",
  "SPECIFY_EMPLOYEE_SKILLS_SUCCESS",
  "SPECIFY_EMPLOYEE_SKILLS_FAILURE",
)<SpecifySkillsRequestParams, undefined, string>();

export const addEmployeeNoteAsync = createAsyncAction(
  "ADD_EMPLOYEE_NOTE_REQUEST",
  "ADD_EMPLOYEE_NOTE_SUCCESS",
  "ADD_EMPLOYEE_NOTE_FAILURE",
)<AddNoteRequestParams, undefined, string>();

export const applyEmployeePositionAsync = createAsyncAction(
  "APPLY_EMPLOYEE_POSITION_REQUEST",
  "APPLY_EMPLOYEE_POSITION_SUCCESS",
  "APPLY_EMPLOYEE_POSITION_FAILURE",
)<ApplyPositionRequestParams, undefined, string>();

export const applyEmployeeSalaryAsync = createAsyncAction(
  "APPLY_EMPLOYEE_SALARY_REQUEST",
  "APPLY_EMPLOYEE_SALARY_SUCCESS",
  "APPLY_EMPLOYEE_SALARY_FAILURE",
)<ApplySalaryRequestParams, undefined, string>();

export const assignLocationAsync = createAsyncAction(
  "ASSIGN_LOCATION_REQUEST",
  "ASSIGN_LOCATION_SUCCESS",
  "ASSIGN_LOCATION_FAILURE",
)<AssignLocationForm, undefined, FormError[]>();

export const fetchEmployeeLocationsAsync = createAsyncAction(
  "FETCH_EMPLOYEE_LOCATIONS_REQUEST",
  "FETCH_EMPLOYEE_LOCATIONS_SUCCESS",
  "FETCH_EMPLOYEE_LOCATIONS_FAILURE",
)<string, EmployeeLocation[], string>();

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
)();

export const closeLocationAssignForm = createAction(
  "CLOSE_LOCATIONS_ASSIGN_FORM",
)();

export const openEmployeeLocationEditForm = createAction(
  "OPEN_EMPLOYEE_LOCATION_EDIT_FORM",
)<EmployeeLocation>();

export const closeEmployeeLocationEditForm = createAction(
  "CLOSE_EMPLOYEE_LOCATION_EDIT_FORM",
)();
