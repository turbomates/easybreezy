import { createReducer } from "typesafe-actions";
import { Location } from "LocationModels";
import { fetchLocationsAsync } from "./actions";

export type State = {
  items: Location[];
  loading: boolean;
};

const initialState: State = {
  items: [],
  loading: false,
};

export const reducer = createReducer<State>(initialState)
  .handleAction(fetchLocationsAsync.request, (state, action) => ({
    ...state,
    loading: true,
  }))
  .handleAction(fetchLocationsAsync.success, (state, action) => ({
    ...state,
    items: action.payload,
    loading: false,
  }))
  .handleAction(fetchLocationsAsync.failure, () => initialState);
