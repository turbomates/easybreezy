import { combineReducers } from "redux";
import { reducer as details } from "./details.reducer";
import { reducer as employees } from "./employee.reducer";
import { reducer as location } from "./location.reducer";
import { reducer as absences } from "./absences.reducer";

export default combineReducers({
  employees,
  details,
  location,
  absences,
});
