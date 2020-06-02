import { RootState } from "MyTypes";

export const employees = (state: RootState) => state.humanResource.employees;

export const absences = (state: RootState) => state.humanResource.absences.all;

export const isUpdateAbsenceModalVisible = (state: RootState) =>
  state.humanResource.absences.one.absenceToUpdateId !== null;

export const updateAbsenceFormInitialValues = (state: RootState) => {
  const id = state.humanResource.absences.one.absenceToUpdateId;

  if (!id) return undefined;

  return state.humanResource.absences.one.items.find((item) => item.id === id);
};

export const oneAbsences = (state: RootState) =>
  state.humanResource.absences.one;

export const employeeDetails = (state: RootState) =>
  state.humanResource.details;

export const employeeLocations = (state: RootState) =>
  state.humanResource.location;

export const isAssignLocationFormVisible = (state: RootState) =>
  state.humanResource.location.showAssignForm;

export const isEditEmployeeLocationFormVisible = (state: RootState) =>
  state.humanResource.location.employeeLocationToEdit !== null;

export const selectVacations = (state: RootState) =>
  state.humanResource.vacations.items
