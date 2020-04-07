import { createReducer } from "typesafe-actions";
import { User } from "AuthModels";
import { signInAsync, signOutAsync, checkAuth } from "./actions";

type State =
  | { status: "initial" }
  | { status: "checking" }
  | { status: "authorizing" }
  | { status: "authorized"; user: User }
  | { status: "unauthorized"; reason?: string };

const reducer = createReducer<State>({ status: "initial" })
  .handleAction(checkAuth.request, (state, action) => ({
    status: "checking",
  }))
  .handleAction(checkAuth.success, (state, action) => ({
    status: "authorized",
    user: action.payload,
  }))
  .handleAction(checkAuth.failure, (state, action) => ({
    status: "unauthorized",
  }))
  .handleAction(signInAsync.request, (state, action) => ({
    status: "authorizing",
  }))
  .handleAction(signInAsync.success, (state, action) => ({
    status: "authorized",
    user: action.payload,
  }))
  .handleAction(signInAsync.failure, (state, action) => ({
    status: "unauthorized",
    reason: action.payload,
  }))
  .handleAction(signOutAsync.request, (state, action) => ({
    ...state,
  }))
  .handleAction(signOutAsync.success, (state, action) => ({
    status: "unauthorized",
  }))
  .handleAction(signOutAsync.failure, (state, action) => ({
    status: "unauthorized",
  }));

export default reducer;
