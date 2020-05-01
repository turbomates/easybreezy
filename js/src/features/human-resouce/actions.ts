import {
  Employee,
  EmployeeShort,
  UpdateBirthdayRequestParams,
  ContactsForm,
  SpecifySkillsRequestParams,
  AddNoteRequestParams,
  ApplyPositionRequestParams,
  ApplySalaryRequestParams,
  AbsencesMap,
  Absence,
  CreateAbsenceData,
  AbsenceForm,
} from "HumanResourceModels";
import { createAsyncAction, createAction } from "typesafe-actions";
import { Paging, FormError } from "MyTypes";
import {
  AssignLocationForm,
  EditEmployeeLocationData,
  EmployeeLocation,
} from "LocationModels";

export const fetchAbsencesAsync = createAsyncAction(
  "FETCH_ABSENCES_REQUEST",
  "FETCH_ABSENCES_SUCCESS",
  "FETCH_ABSENCES_FAILURE",
)<undefined, AbsencesMap, string>();

export const fetchMyAbsencesAsync = createAsyncAction(
  "FETCH_MY_ABSENCES_REQUEST",
  "FETCH_MY_ABSENCES_SUCCESS",
  "FETCH_MY_ABSENCES_FAILURE",
)<undefined, Absence[], string>();

export const refetchMyAbsencesAsync = createAsyncAction(
  "REFETCH_MY_ABSENCES_REQUEST",
  "REFETCH_MY_ABSENCES_SUCCESS",
  "REFETCH_MY_ABSENCES_FAILURE",
)<undefined, Absence[], string>();

export const createAbsenceAsync = createAsyncAction(
  "CREATE_ABSENCE_REQUEST",
  "CREATE_ABSENCE_SUCCESS",
  "CREATE_ABSENCE_FAILURE",
)<CreateAbsenceData, undefined, FormError[]>();

export const updateAbsenceAsync = createAsyncAction(
  "UPDATE_ABSENCE_REQUEST",
  "UPDATE_ABSENCE_SUCCESS",
  "UPDATE_ABSENCE_FAILURE",
)<AbsenceForm, undefined, FormError[]>();

export const removeAbsenceAsync = createAsyncAction(
  "REMOVE_ABSENCE_REQUEST",
  "REMOVE_ABSENCE_SUCCESS",
  "REMOVE_ABSENCE_FAILURE",
)<string, undefined, string>();

export const approveAbsenceAsync = createAsyncAction(
  "APPROVE_ABSENCE_REQUEST",
  "APPROVE_ABSENCE_SUCCESS",
  "APPROVE_ABSENCE_FAILURE",
)<string, undefined, string>();

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

export const openAbsenceUpdateModal = createAction("OPEN_ABSENCE_UPDATE_MODAL")<
  string
>();

export const closeAbsenceUpdateModal = createAction(
  "CLOSE_ABSENCE_UPDATE_MODAL",
)();

export const openAbsenceCreateForm = createAction("OPEN_ABSENCE_CREATE_FORM")();

export const closeAbsenceCreateForm = createAction(
  "CLOSE_ABSENCE_CREATE_FORM",
)();
