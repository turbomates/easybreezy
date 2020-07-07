import { combineReducers } from "redux";
import { reducer as locations } from "./location.reducer";
import { reducer as holiday } from "./holiday.reducer";

export default combineReducers({
  locations,
  holiday,
});
