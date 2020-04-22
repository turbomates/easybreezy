import { combineReducers } from "redux";
import { reducer as project } from "./project.reducer";
import { reducer as projects } from "./projects.reducer";

export default combineReducers({
  project,
  projects,
});
