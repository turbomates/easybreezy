import { reducer as info } from "./info.reducer";
import { reducer as rules } from "./rules.reducer";
import { combineReducers } from "redux";

export const reducer = combineReducers({
  info,
  rules,
});
