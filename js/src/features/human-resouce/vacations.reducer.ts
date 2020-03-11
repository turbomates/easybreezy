import { createReducer } from "typesafe-actions";
import { UserVacations } from "HumanResourceModels";
import { fetchUsersVacationsAsync } from "./actions";

export type State = {
  items: UserVacations[];
  loading: boolean;
};

const initialState: State = {
  items: [],
  loading: false,
};

export const reducer = createReducer<State>(initialState)
  .handleAction(fetchUsersVacationsAsync.request, (state, action) => ({
    ...state,
    loading: true,
  }))
  .handleAction(fetchUsersVacationsAsync.success, (state, action) => ({
    ...state,
    items: action.payload,
    loading: false,
  }))
  .handleAction(
    fetchUsersVacationsAsync.failure,
    (state, action) => initialState,
  );
