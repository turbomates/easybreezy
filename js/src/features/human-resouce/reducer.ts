import { combineReducers } from "redux";
import { reducer as details } from "./details.reducer";
import { reducer as employees } from "./employee.reducer";

export default combineReducers({
  employees,
  details,
});
