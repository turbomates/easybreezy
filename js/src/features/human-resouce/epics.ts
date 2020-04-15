import { RootEpic } from "MyTypes";
import { from, of } from "rxjs";
import {
  filter,
  switchMap,
  map,
  catchError,
  debounceTime,
} from "rxjs/operators";
import { isActionOf } from "typesafe-actions";

import {
  fetchEmployeeAsync,
  fetchEmployeesAsync,
  updateEmployeeBirthdayAsync,
  updateContactsAsync,
  specifyEmployeeSkillsAsync,
  addEmployeeNoteAsync,
  applyEmployeePositionAsync,
  applyEmployeeSalaryAsync,
} from "./actions";

// export const fetchUsersEpic: RootEpic = (action$, state$, { api }) =>
//   action$.pipe(
//     filter(isActionOf(fetchUsersVacationsAsync.request)),
//     switchMap((action) =>
//       from(api.humanResource.fetchVacations()).pipe(
//         map((result) =>
//           result.success
//             ? fetchUsersVacationsAsync.success(result.data)
//             : fetchUsersVacationsAsync.failure(result.reason),
//         ),
//         catchError((message) => of(fetchUsersVacationsAsync.failure(message))),
//       ),
//     ),
//   );

export const fetchEmployeeEpic: RootEpic = (action$, state$, { api }) =>
  action$.pipe(
    filter(isActionOf(fetchEmployeeAsync.request)),
    switchMap((action) =>
      from(api.humanResource.fetchEmployee(action.payload)).pipe(
        map((result) =>
          result.success
            ? fetchEmployeeAsync.success(result.data)
            : fetchEmployeeAsync.failure(result.reason),
        ),
        catchError((message) => of(fetchEmployeeAsync.failure(message))),
      ),
    ),
  );

export const fetchEmployeesEpic: RootEpic = (action$, state$, { api }) =>
  action$.pipe(
    filter(isActionOf([fetchEmployeesAsync.request])),
    switchMap((action) =>
      from(api.humanResource.fetchEmployees()).pipe(
        map((result) =>
          result.success
            ? fetchEmployeesAsync.success(result.data)
            : fetchEmployeesAsync.failure(result.reason),
        ),
        catchError((message) => of(fetchEmployeesAsync.failure(message))),
      ),
    ),
  );

export const updateEmployeeBirthdayEpic: RootEpic = (
  action$,
  state$,
  { api },
) =>
  action$.pipe(
    filter(isActionOf(updateEmployeeBirthdayAsync.request)),
    debounceTime(500),
    switchMap((action, t) =>
      from(api.humanResource.updateBirthday(action.payload)).pipe(
        map((result) =>
          result.success
            ? updateEmployeeBirthdayAsync.success()
            : updateEmployeeBirthdayAsync.failure(result.reason),
        ),
        catchError((message) =>
          of(updateEmployeeBirthdayAsync.failure(message)),
        ),
      ),
    ),
  );

export const updateContactsEpic: RootEpic = (action$, state$, { api }) =>
  action$.pipe(
    filter(isActionOf(updateContactsAsync.request)),
    debounceTime(500),
    switchMap((action) =>
      from(api.humanResource.updateContacts(action.payload)).pipe(
        map((result) =>
          result.success
            ? updateContactsAsync.success()
            : updateContactsAsync.failure(result.reason),
        ),
        catchError((message) => of(updateContactsAsync.failure(message))),
      ),
    ),
  );

export const specifySkillsEpic: RootEpic = (action$, state$, { api }) =>
  action$.pipe(
    filter(isActionOf(specifyEmployeeSkillsAsync.request)),
    debounceTime(500),
    switchMap((action) =>
      from(api.humanResource.specifySkills(action.payload)).pipe(
        map((result) =>
          result.success
            ? specifyEmployeeSkillsAsync.success()
            : specifyEmployeeSkillsAsync.failure(result.reason),
        ),
        catchError((message) =>
          of(specifyEmployeeSkillsAsync.failure(message)),
        ),
      ),
    ),
  );

export const addEmployeeNoteEpic: RootEpic = (action$, state$, { api }) =>
  action$.pipe(
    filter(isActionOf(addEmployeeNoteAsync.request)),
    debounceTime(500),
    switchMap((action) =>
      from(api.humanResource.addNote(action.payload)).pipe(
        map((result) =>
          result.success
            ? addEmployeeNoteAsync.success()
            : addEmployeeNoteAsync.failure(result.reason),
        ),
        catchError((message) => of(addEmployeeNoteAsync.failure(message))),
      ),
    ),
  );

export const applyEmployeePositionEpic: RootEpic = (action$, state$, { api }) =>
  action$.pipe(
    filter(isActionOf(applyEmployeePositionAsync.request)),
    switchMap((action) =>
      from(api.humanResource.applyPosition(action.payload)).pipe(
        map((result) =>
          result.success
            ? applyEmployeePositionAsync.success()
            : applyEmployeePositionAsync.failure(result.reason),
        ),
        catchError((message) =>
          of(applyEmployeePositionAsync.failure(message)),
        ),
      ),
    ),
  );

export const applyEmployeeSalaryEpic: RootEpic = (action$, state$, { api }) =>
  action$.pipe(
    filter(isActionOf(applyEmployeeSalaryAsync.request)),
    switchMap((action) =>
      from(api.humanResource.applySalary(action.payload)).pipe(
        map((result) =>
          result.success
            ? applyEmployeeSalaryAsync.success()
            : applyEmployeeSalaryAsync.failure(result.reason),
        ),
        catchError((message) => of(applyEmployeeSalaryAsync.failure(message))),
      ),
    ),
  );

export const refetchEmployeeEpic: RootEpic = (action$, state$, { api }) =>
  action$.pipe(
    filter(
      isActionOf([
        addEmployeeNoteAsync.success,
        applyEmployeePositionAsync.success,
        applyEmployeeSalaryAsync.success,
      ]),
    ),
    switchMap(() =>
      from(
        api.humanResource.fetchEmployee(
          state$.value.humanResource.details.employee!.userId,
        ),
      ).pipe(
        map((result) =>
          result.success
            ? fetchEmployeeAsync.success(result.data)
            : fetchEmployeeAsync.failure(result.reason),
        ),
        catchError((message) => of(fetchEmployeeAsync.failure(message))),
      ),
    ),
  );
