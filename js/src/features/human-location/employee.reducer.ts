import { createReducer } from "typesafe-actions";
import { EmployeeShort } from "LocationModels";
import { fetchEmployeesAsync, selectEmployeeSync } from "./actions";

export type State = {
  items: EmployeeShort[];
  currentPage: number;
  pageSize: number;
  hasMore: boolean;
  loading: boolean;
  idToAssign: string | null;
};

const initialState: State = {
  items: [],
  currentPage: 1,
  pageSize: 30,
  hasMore: false,
  loading: false,
  idToAssign: null,
};

export const reducer = createReducer<State>(initialState)
  .handleAction(fetchEmployeesAsync.request, (state, action) => ({
    ...state,
    loading: true,
  }))
  .handleAction(
    fetchEmployeesAsync.success,
    (state, { payload: { data, ...rest } }) => ({
      ...state,
      ...rest,
      items: data,
      loading: false,
    }),
  )
  .handleAction(fetchEmployeesAsync.failure, () => initialState)
  .handleAction(selectEmployeeSync, (state, action) => ({
    ...state,
    idToAssign: action.payload,
  }));
