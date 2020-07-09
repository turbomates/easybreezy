import { combineReducers } from "redux";
import { connectRouter } from "connected-react-router";
import { History } from "history";

import { reducer as auth } from "../features/auth/reducer";
import humanResource from "../features/human-resouce/reducer";
import account from "../features/account/reducer";
import location from "../features/location/reducer";
import project from "../features/project/reducer";

const rootReducer = (history: History<any>) =>
  combineReducers({
    router: connectRouter(history),
    auth,
    humanResource,
    account,
    project,
    location,
  });

export default rootReducer;
