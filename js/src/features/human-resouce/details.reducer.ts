import { createReducer } from "typesafe-actions";
import { Employee } from "HumanResourceModels";
import { FormErrorMap } from "MyTypes";
import {
  fetchEmployeeAsync,
  openCreateNoteModal,
  closeCreateNoteModal,
  openApplySalaryModal,
  closeApplySalaryModal,
  openAddPositionModal,
  closeAddPositionModal,
  addEmployeeNoteAsync,
  applyEmployeeSalaryAsync,
  applyEmployeePositionAsync,
} from "./actions";
import { normalizeErrors, fillErrors } from "utils/error";

export type State = {
  loading: boolean;
  employee: Employee | null;
  errors: FormErrorMap;
  isCreateNoteModalVisible: boolean;
  isApplySalaryModalVisible: boolean;
  isAddPositionModalVisible: boolean;
};

const initialState: State = {
  loading: false,
  employee: null,
  errors: {},
  isCreateNoteModalVisible: false,
  isApplySalaryModalVisible: false,
  isAddPositionModalVisible: false,
};

export const reducer = createReducer<State>(initialState)
  .handleAction(fetchEmployeeAsync.request, (state, action) => ({
    ...state,
    loading: true,
  }))
  .handleAction(fetchEmployeeAsync.success, (state, action) => ({
    ...state,
    loading: false,
    employee: action.payload,
  }))
  .handleAction(fetchEmployeeAsync.failure, (state, action) => initialState)
  .handleAction(addEmployeeNoteAsync.failure, (state, action) => ({
    ...state,
    errors: action.payload.length
      ? normalizeErrors(action.payload)
      : fillErrors(["text"], "Something went wrong. Please check the field"),
  }))
  .handleAction(applyEmployeeSalaryAsync.failure, (state, action) => ({
    ...state,
    errors: action.payload.length
      ? normalizeErrors(action.payload)
      : fillErrors(
          ["amount", "comment"],
          "Something went wrong. Please check the field",
        ),
  }))
  .handleAction(applyEmployeePositionAsync.failure, (state, action) => ({
    ...state,
    errors: action.payload.length
      ? normalizeErrors(action.payload)
      : fillErrors(
          ["position"],
          "Something went wrong. Please check the field",
        ),
  }))
  .handleAction(openCreateNoteModal, (state, action) => ({
    ...state,
    isCreateNoteModalVisible: true,
  }))
  .handleAction(closeCreateNoteModal, (state, action) => ({
    ...state,
    errors: {},
    isCreateNoteModalVisible: false,
  }))
  .handleAction(openApplySalaryModal, (state, action) => ({
    ...state,
    isApplySalaryModalVisible: true,
  }))
  .handleAction(closeApplySalaryModal, (state, action) => ({
    ...state,
    errors: {},
    isApplySalaryModalVisible: false,
  }))
  .handleAction(openAddPositionModal, (state, action) => ({
    ...state,
    isAddPositionModalVisible: true,
  }))
  .handleAction(closeAddPositionModal, (state, action) => ({
    ...state,
    errors: {},
    isAddPositionModalVisible: false,
  }));
