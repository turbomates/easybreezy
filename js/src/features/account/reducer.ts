import { createReducer } from "typesafe-actions";
import { UserDetails } from "HumanResourceModels";
import { fetchProfileAsync } from "./actions";

export type State = {
  loading: boolean;
  profile: UserDetails | null;
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
