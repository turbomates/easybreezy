import { createReducer } from "typesafe-actions";
import { UserDetails } from "HumanResourceModels";
import { fetchProfileAsync } from "./actions";

export type State = {
  loading: boolean;
  data: UserDetails | null;
};

const initialState: State = {
  loading: false,
  data: null,
};

export const reducer = createReducer<State>(initialState)
  .handleAction(fetchProfileAsync.request, (state, action) => ({
    ...state,
    loading: true,
  }))
  .handleAction(fetchProfileAsync.success, (state, action) => ({
    ...state,
    loading: false,
    data: action.payload,
  }))
  .handleAction(fetchProfileAsync.failure, (state, action) => initialState);
