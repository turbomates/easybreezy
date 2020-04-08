import { RootState } from "MyTypes";

export const location = (state: RootState) => state.humanLocation.location;

export const employee = (state: RootState) => state.humanLocation.employee;

export const isAssignLocationFormVisible = (state: RootState) =>
  state.humanLocation.employee.idToAssign !== null;

export const isEditEmployeeLocationFormVisible = (state: RootState) =>
  state.humanLocation.employeeLocation.employeeLocationToEdit !== null;

export const employeeLocation = (state: RootState) =>
  state.humanLocation.employeeLocation;
