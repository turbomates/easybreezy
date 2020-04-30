import { createReducer } from "typesafe-actions";
import { AbsencesMap } from "HumanResourceModels";
import { fetchAbsencesAsync } from "./actions";

export type State = AbsencesMap;

const initialState: State = {};

export const reducer = createReducer<State>(initialState).handleAction(
  fetchAbsencesAsync.success,
  (state, action) => action.payload || {},
);
