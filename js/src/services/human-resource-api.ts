import { Failure, Success } from "MyTypes";
import { api } from "./api";
import { Paging } from "MyTypes";
import {
  EmployeeShort,
  EmployeeResponse,
  Employee,
  UpdateBirthdayRequestParams,
  ContactsForm,
  SpecifySkillsRequestParams,
  AddNoteRequestParams,
  ApplyPositionRequestParams,
  ApplySalaryRequestParams,
  AbsencesMap,
} from "HumanResourceModels";
import { ProfileResponse, Profile } from "AccountModules";

export const fetchEmployees = () =>
  api
    .get<Paging<EmployeeShort>>("/hr/employees")
    .then<Success<Paging<EmployeeShort>>>((resp) => ({
      success: true,
      data: resp.data,
    }))
    .catch<Failure>(() => ({ success: false, reason: "Error" }));

export const fetchEmployee = (id: string) =>
  api
    .get<EmployeeResponse>(`hr/employee/${id}`)
    .then<Success<Employee>>((resp) => ({
      success: true,
      data: resp.data.data,
    }))
    .catch<Failure>(() => ({ success: false, reason: "Error" }));

export const updateBirthday = ({
  userId,
  birthday,
}: UpdateBirthdayRequestParams) =>
  api
    .post(`hr/employee/${userId}/update-birthday`, { birthday })
    .then<Success<null>>(() => ({
      success: true,
      data: null,
    }))
    .catch<Failure>(() => ({ success: false, reason: "Error" }));

export const fetchProfile = () =>
  api
    .get<ProfileResponse>(`users/me`)
    .then<Success<Profile>>((resp) => ({
      success: true,
      data: resp.data.data,
    }))
    .catch<Failure>(() => ({ success: false, reason: "Error" }));

export const updateContacts = (contacts: ContactsForm) =>
  api
    .post("users/update-contacts", contacts)
    .then<Success<null>>(() => ({
      success: true,
      data: null,
    }))
    .catch<Failure>(() => ({ success: false, reason: "Error" }));

export const specifySkills = ({ userId, skills }: SpecifySkillsRequestParams) =>
  api
    .post(`hr/employee/${userId}/specify-skills`, { skills })
    .then<Success<null>>(() => ({
      success: true,
      data: null,
    }))
    .catch<Failure>(() => ({ success: false, reason: "Error" }));

export const addNote = ({ userId, text }: AddNoteRequestParams) =>
  api
    .post(`hr/employee/${userId}/write-note`, { text })
    .then<Success<null>>(() => ({
      success: true,
      data: null,
    }))
    .catch<Failure>(() => ({ success: false, reason: "Error" }));

export const applyPosition = ({
  userId,
  position,
}: ApplyPositionRequestParams) =>
  api
    .post(`hr/employee/${userId}/apply-position`, { position })
    .then<Success<null>>(() => ({
      success: true,
      data: null,
    }))
    .catch<Failure>(() => ({ success: false, reason: "Error" }));

export const applySalary = ({ userId, ...body }: ApplySalaryRequestParams) =>
  api
    .post(`hr/employee/${userId}/apply-salary`, body)
    .then<Success<null>>(() => ({
      success: true,
      data: null,
    }))
    .catch<Failure>(() => ({ success: false, reason: "Error" }));

export const fetchAbsences = () =>
  api
    .get("hr/absences")
    .then<Success<AbsencesMap>>((resp) => ({
      success: true,
      data: resp.data.data.absences,
    }))
    .catch<Failure>(() => ({ success: false, reason: "Error" }));
