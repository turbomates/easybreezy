import { EmployeeShort } from "LocationModels";
import { Failure, Success } from "MyTypes";
import { api } from "./api";
import { Paging } from "MyTypes";

export const fetchEmployees = () =>
  api
    .get<Paging<EmployeeShort>>("/hr/employees")
    .then<Success<Paging<EmployeeShort>>>((resp) => ({
      success: true,
      data: resp.data,
    }))
    .catch<Failure>(() => ({ success: false, reason: "Error" }));
