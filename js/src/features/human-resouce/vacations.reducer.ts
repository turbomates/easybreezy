import { createReducer } from "typesafe-actions";

import { fetchVacationsAsync } from "./actions";
import { VacationMap } from "HumanResourceModels";

export type State = {
  items: VacationMap;
};

const initialState: State = {
  items: {},
};

export const reducer = createReducer<State>(initialState).handleAction(
  fetchVacationsAsync.success,
  (state, action) => ({
    ...state,
    items: action.payload,
  }),
);

