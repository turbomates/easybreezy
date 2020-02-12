import { combineReducers } from "redux";
import { connectRouter } from "connected-react-router";
import { History } from "history";

import auth from "../features/auth/reducer";
import humanResource from "../features/human-resouce/reducer";
import account from "../features/account/reducer";

const rootReducer = (history: History<any>) =>
  combineReducers({
    router: connectRouter(history),
    auth,
    humanResource,
    account,
  });

export default rootReducer;