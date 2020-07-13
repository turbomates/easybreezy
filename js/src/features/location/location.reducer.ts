import { createReducer } from "typesafe-actions";

import { Location } from "LocationModels";
import { FormErrorMap } from "MyTypes";
import {
  fetchLocationsAsync,
  createLocationAsync,
  changeVisibilityLocationFormAction,
} from "./actions";
import { normalizeErrors } from "utils/errors";

export type State = {
  items: Location[];
  loading: boolean;
  isVisibleLocationForm: boolean;
  formErrors: FormErrorMap;
};

const initialState: State = {
  items: [],
  loading: false,
  isVisibleLocationForm: false,
  formErrors: {},
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
  .handleAction(fetchLocationsAsync.failure, () => initialState)
  .handleAction(createLocationAsync.failure, (state, action) => ({
    ...state,
    formErrors: normalizeErrors(action.payload),
  }))
  .handleAction(changeVisibilityLocationFormAction, (state, action) => ({
    ...state,
    isVisibleLocationForm: action.payload,
  }));
