import { combineReducers } from "redux";
import { reducer as location } from "./location.reducer";
import { reducer as employee } from "./employee.reducer";
import { reducer as employeeLocation } from "./employee-location.reducer";

export const reducer = combineReducers({
  location,
  employee,
  employeeLocation,
});
