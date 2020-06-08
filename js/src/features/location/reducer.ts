import { combineReducers } from "redux";
import { reducer as location } from "./location.reducer";
import { reducer as holiday } from "./holiday.reducer";

export default combineReducers({
  location,
  holiday,
});
