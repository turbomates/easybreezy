import { createReducer } from "typesafe-actions";
import { AbsencesMap, Absence } from "HumanResourceModels";
import {
  fetchAbsencesAsync,
  fetchEmployeeAbsencesAsync,
  createAbsenceAsync,
  openAbsenceCreateModal,
  closeAbsenceCreateModal,
  openAbsenceUpdateModal,
  closeAbsenceUpdateModal,
  updateAbsenceAsync,
} from "./actions";
import { combineReducers } from "redux";
import { FormErrorMap } from "MyTypes";
import { normalizeErrors, fillErrors } from "utils/errors";

export type AllState = AbsencesMap;

const allInitialState: AbsencesMap = {};

export const all = createReducer<AllState>(allInitialState).handleAction(
  fetchAbsencesAsync.success,
  (state, action) => action.payload || {},
);

type OneState = {
  loading: boolean;
  items: Absence[];
  isCreateModalVisible: boolean;
  absenceToUpdateId: string | null;
  errors: FormErrorMap;
};

const oneInitialState = {
  loading: false,
  items: [],
  isCreateModalVisible: false,
  absenceToUpdateId: null,
  errors: {},
};

export const one = createReducer<OneState>(oneInitialState)
  .handleAction(fetchEmployeeAbsencesAsync.request, (state, action) => ({
    ...state,
    loading: true,
  }))
  .handleAction(fetchEmployeeAbsencesAsync.success, (state, action) => ({
    ...state,
    items: action.payload || [],
    loading: false,
  }))
  .handleAction(fetchEmployeeAbsencesAsync.failure, (state, action) => ({
    ...state,
    items: [],
    loading: false,
  }))
  .handleAction(createAbsenceAsync.success, (state, action) => ({
    ...state,
    errors: {},
    isCreateModalVisible: false,
  }))
  .handleAction(createAbsenceAsync.failure, (state, action) => ({
    ...state,
    errors: action.payload.length
      ? normalizeErrors(action.payload) // TODO move start and end to range
      : fillErrors(
          ["range", "reason", "comment"],
          "Something went wrong. Please check the field",
        ),
  }))
  .handleAction(updateAbsenceAsync.failure, (state, action) => ({
    ...state,
    errors: action.payload.length
      ? normalizeErrors(action.payload) // TODO move start and end to range
      : fillErrors(
          ["range", "reason", "comment"],
          "Something went wrong. Please check the field",
        ),
  }))
  .handleAction(openAbsenceCreateModal, (state, action) => ({
    ...state,
    isCreateModalVisible: true,
  }))
  .handleAction(closeAbsenceCreateModal, (state, action) => ({
    ...state,
    errors: {},
    isCreateModalVisible: false,
  }))
  .handleAction(openAbsenceUpdateModal, (state, action) => ({
    ...state,
    absenceToUpdateId: action.payload,
  }))
  .handleAction(closeAbsenceUpdateModal, (state, action) => ({
    ...state,
    errors: {},
    absenceToUpdateId: null,
  }));

export const reducer = combineReducers({
  all,
  one,
});
