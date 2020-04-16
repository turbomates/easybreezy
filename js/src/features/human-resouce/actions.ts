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
import { createAsyncAction } from "typesafe-actions";
import { Paging } from "MyTypes";

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
