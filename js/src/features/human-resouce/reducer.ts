import { combineReducers } from "redux";
import { reducer as list } from "./list.reducer";
import { reducer as details } from "./details.reducer";
import { reducer as profile } from "./profile.reducer";

export default combineReducers({
  list,
  details,
  profile,
});
