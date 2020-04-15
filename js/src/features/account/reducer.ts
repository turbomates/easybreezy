import { createReducer } from "typesafe-actions";
import { fetchProfileAsync } from "./actions";
import { Profile } from "AccountModules";

export type State = {
  loading: boolean;
  profile: Profile | null;
};

const initialState: State = {
  loading: false,
  profile: null,
};

export default createReducer<State>(initialState)
  .handleAction(fetchProfileAsync.request, (state, action) => ({
    ...state,
    loading: true,
  }))
  .handleAction(fetchProfileAsync.success, (state, action) => ({
    ...state,
    loading: false,
    profile: action.payload,
  }))
  .handleAction(fetchProfileAsync.failure, (state, action) => initialState);
