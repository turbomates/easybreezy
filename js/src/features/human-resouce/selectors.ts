import { RootState } from "MyTypes";

export const employees = (state: RootState) => state.humanResource.employees;

export const calendarGroups = (state: RootState) =>
  state.humanResource.employees.items.map((item) => ({
    id: item.userId,
    title: item.userId,
    item,
  }));

export const employeeDetails = (state: RootState) =>
  state.humanResource.details;

export const employeeLocations = (state: RootState) =>
  state.humanResource.location;

export const isAssignLocationFormVisible = (state: RootState) =>
  state.humanResource.location.showAssignForm;

export const isEditEmployeeLocationFormVisible = (state: RootState) =>
  state.humanResource.location.employeeLocationToEdit !== null;
