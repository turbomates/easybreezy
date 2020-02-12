import { createReducer } from "typesafe-actions";
import { UsersListing } from "HumanResourceModels";
import { fetchUsersAsync } from "./actions";

export type State = {
  loading: boolean;
} & UsersListing;

const initialState: State = {
  items: [],
  page: 1,
  perPage: 10,
  haveMore: false,
  loading: false,
};

export const reducer = createReducer<State>(initialState)
  .handleAction(fetchUsersAsync.request, (state, action) => ({
    ...state,
    loading: true,
  }))
  .handleAction(fetchUsersAsync.success, (state, action) => ({
    ...state,
    ...action.payload,
    loading: false,
  }))
  .handleAction(fetchUsersAsync.failure, (state, action) => initialState);
