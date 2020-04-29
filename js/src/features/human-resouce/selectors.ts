import { RootState } from "MyTypes";

export const employees = (state: RootState) => state.humanResource.employees;

export const absences = (state: RootState) => state.humanResource.absences.all;

export const isUpdateAbsenceModalVisible = (state: RootState) =>
  state.humanResource.absences.my.absenceToUpdateId !== null;

export const updateAbsenceFormInitialValues = (state: RootState) => {
  const id = state.humanResource.absences.my.absenceToUpdateId;

  if (!id) return null;

  return state.humanResource.absences.my.items.find((item) => item.id === id);
};

export const myAbsences = (state: RootState) => state.humanResource.absences.my;

export const employeeDetails = (state: RootState) =>
  state.humanResource.details;

export const employeeLocations = (state: RootState) =>
  state.humanResource.location;

export const isAssignLocationFormVisible = (state: RootState) =>
  state.humanResource.location.showAssignForm;

export const isEditEmployeeLocationFormVisible = (state: RootState) =>
  state.humanResource.location.employeeLocationToEdit !== null;
