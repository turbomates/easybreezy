import { createReducer } from "typesafe-actions";
import { UserDetails } from "HumanResourceModels";
import { fetchUserDetailsAsync } from "./actions";

export type State = {
  loading: boolean;
  user: UserDetails | null;
};

const initialState: State = {
  loading: false,
  user: null,
};

export const reducer = createReducer<State>(initialState)
  .handleAction(fetchUserDetailsAsync.request, (state, action) => ({
    ...state,
    loading: true,
  }))
  .handleAction(fetchUserDetailsAsync.success, (state, action) => ({
    ...state,
    loading: false,
    user: action.payload,
  }))
  .handleAction(fetchUserDetailsAsync.failure, (state, action) => initialState);
