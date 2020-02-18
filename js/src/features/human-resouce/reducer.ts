import { combineReducers } from "redux";
import { reducer as vacations } from "./vacations.reducer";
import { reducer as details } from "./details.reducer";
import { reducer as profile } from "./profile.reducer";

export default combineReducers({
  vacations,
  details,
  profile,
});
