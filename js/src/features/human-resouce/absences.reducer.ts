import { createReducer } from "typesafe-actions";
import { AbsencesMap, Absence } from "HumanResourceModels";
import {
  fetchAbsencesAsync,
  refetchMyAbsencesAsync,
  fetchMyAbsencesAsync,
  createAbsenceAsync,
  openAbsenceCreateForm,
  closeAbsenceCreateForm,
  openAbsenceUpdateModal,
  closeAbsenceUpdateModal,
} from "./actions";
import { combineReducers } from "redux";
import { FormErrorMap } from "MyTypes";
import { normalizeErrors } from "utils/error";

export type AllState = AbsencesMap;

const allInitialState: AbsencesMap = {};

export const all = createReducer<AllState>(allInitialState).handleAction(
  fetchAbsencesAsync.success,
  (state, action) => action.payload || {},
);

type MyState = {
  loading: boolean;
  items: Absence[];
  createFormVisible: boolean;
  absenceToUpdateId: string | null;
  errors: FormErrorMap;
};

const myInitialState = {
  loading: false,
  items: [],
  createFormVisible: false,
  absenceToUpdateId: null,
  errors: {},
};

export const my = createReducer<MyState>(myInitialState)
  .handleAction(fetchMyAbsencesAsync.request, (state, action) => ({
    ...state,
    loading: true,
  }))
  .handleAction(fetchMyAbsencesAsync.success, (state, action) => ({
    ...state,
    items: action.payload || [],
    loading: false,
  }))
  .handleAction(fetchMyAbsencesAsync.failure, (state, action) => ({
    ...state,
    items: [],
    loading: false,
  }))
  .handleAction(refetchMyAbsencesAsync.success, (state, action) => ({
    ...state,
    items: action.payload,
  }))
  .handleAction(refetchMyAbsencesAsync.failure, (state, action) => ({
    ...state,
    items: [],
  }))
  .handleAction(createAbsenceAsync.success, (state, action) => ({
    ...state,
    errors: {},
    createFormVisible: false,
  }))
  .handleAction(createAbsenceAsync.failure, (state, action) => ({
    ...state,
    errors: normalizeErrors(action.payload),
  }))
  .handleAction(openAbsenceCreateForm, (state, action) => ({
    ...state,
    createFormVisible: true,
  }))
  .handleAction(closeAbsenceCreateForm, (state, action) => ({
    ...state,
    createFormVisible: false,
  }))
  .handleAction(openAbsenceUpdateModal, (state, action) => ({
    ...state,
    absenceToUpdateId: action.payload,
  }))
  .handleAction(closeAbsenceUpdateModal, (state, action) => ({
    ...state,
    absenceToUpdateId: null,
  }));

export const reducer = combineReducers({
  all,
  my,
});
