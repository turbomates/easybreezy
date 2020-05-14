import { createReducer } from "typesafe-actions";
import { fetchRulesAsync } from "./actions";
import { RulesMap } from "AuthModels";

type State =
  | { status: "initial" }
  | { status: "loading" }
  | { status: "loaded"; map: RulesMap }
  | { status: "error" };

export const reducer = createReducer<State>({ status: "initial" })
  .handleAction(fetchRulesAsync.request, () => ({
    status: "loading",
  }))
  .handleAction(fetchRulesAsync.success, (_, action) => ({
    status: "loaded",
    map: action.payload,
  }))
  .handleAction(fetchRulesAsync.failure, () => ({
    status: "error",
  }));
