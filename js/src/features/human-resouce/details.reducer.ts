import { createReducer } from "typesafe-actions";
import { Employee } from "HumanResourceModels";
import { fetchEmployeeAsync } from "./actions";

export type State = {
  loading: boolean;
  employee: Employee | null;
};

const initialState: State = {
  loading: false,
  employee: null,
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
  .handleAction(fetchEmployeeAsync.failure, (state, action) => initialState);
