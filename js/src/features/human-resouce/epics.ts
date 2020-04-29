import { RootEpic } from "MyTypes";
import { from, of } from "rxjs";
import {
  filter,
  switchMap,
  map,
  catchError,
  debounceTime,
  mergeMap,
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
  assignLocationAsync,
  fetchEmployeeLocationsAsync,
  removeEmployeeLocationAsync,
  editEmployeeLocationAsync,
  fetchAbsencesAsync,
  fetchMyAbsencesAsync,
  approveAbsenceAsync,
  createAbsenceAsync,
  refetchMyAbsencesAsync,
  removeAbsenceAsync,
  updateAbsenceAsync,
  closeAbsenceUpdateModal,
} from "./actions";

export const fetchAbsencesEpic: RootEpic = (action$, state$, { api }) =>
  action$.pipe(
    filter(isActionOf(fetchAbsencesAsync.request)),
    switchMap((action) =>
      from(api.humanResource.fetchAbsences()).pipe(
        map((result) =>
          result.success
            ? fetchAbsencesAsync.success(result.data)
            : fetchAbsencesAsync.failure(result.reason),
        ),
        catchError((message) => of(fetchAbsencesAsync.failure(message))),
      ),
    ),
  );

export const fetchMyAbsencesEpic: RootEpic = (action$, state$, { api }) =>
  action$.pipe(
    filter(isActionOf(fetchMyAbsencesAsync.request)),
    switchMap(() =>
      from(api.humanResource.fetchMyAbsences()).pipe(
        map((result) =>
          result.success
            ? fetchMyAbsencesAsync.success(result.data)
            : fetchMyAbsencesAsync.failure(result.reason),
        ),
      ),
    ),
  );

export const refetchMyAbsencesEpic: RootEpic = (action$, state$, { api }) =>
  action$.pipe(
    filter(isActionOf(refetchMyAbsencesAsync.request)),
    switchMap(() =>
      from(api.humanResource.fetchMyAbsences()).pipe(
        map((result) =>
          result.success
            ? refetchMyAbsencesAsync.success(result.data)
            : refetchMyAbsencesAsync.failure(result.reason),
        ),
      ),
    ),
  );

export const approveAbsenceEpic: RootEpic = (action$, state$, { api }) =>
  action$.pipe(
    filter(isActionOf(approveAbsenceAsync.request)),
    switchMap((action) =>
      from(api.humanResource.approveAbsence(action.payload)).pipe(
        map((result) =>
          result.success
            ? approveAbsenceAsync.success()
            : approveAbsenceAsync.failure(result.reason),
        ),
      ),
    ),
  );

export const createAbsenceEpic: RootEpic = (action$, state$, { api }) =>
  action$.pipe(
    filter(isActionOf(createAbsenceAsync.request)),
    switchMap((action) =>
      from(api.humanResource.createAbsence(action.payload)).pipe(
        map((result) =>
          result.success
            ? createAbsenceAsync.success()
            : createAbsenceAsync.failure(result.errors),
        ),
      ),
    ),
  );

export const updateAbsenceEpic: RootEpic = (action$, state$, { api }) =>
  action$.pipe(
    filter(isActionOf(updateAbsenceAsync.request)),
    switchMap((action) =>
      from(
        api.humanResource.updateAbsence({
          form: action.payload,
          absenceId: state$.value.humanResource.absences.my.absenceToUpdateId!,
        }),
      ).pipe(
        mergeMap((result) =>
          result.success
            ? of(updateAbsenceAsync.success(), closeAbsenceUpdateModal())
            : of(updateAbsenceAsync.failure(result.errors)),
        ),
      ),
    ),
  );

export const removeAbsenceEpic: RootEpic = (action$, state$, { api }) =>
  action$.pipe(
    filter(isActionOf(removeAbsenceAsync.request)),
    switchMap((action) =>
      from(api.humanResource.removeAbsence(action.payload)).pipe(
        map((result) =>
          result.success
            ? removeAbsenceAsync.success()
            : removeAbsenceAsync.failure(result.reason),
        ),
      ),
    ),
  );

export const refetchMyAbsenceEpic: RootEpic = (action$, state$, { api }) =>
  action$.pipe(
    filter(
      isActionOf([
        approveAbsenceAsync.success,
        createAbsenceAsync.success,
        removeAbsenceAsync.success,
        updateAbsenceAsync.success,
      ]),
    ),
    switchMap(() => of(refetchMyAbsencesAsync.request())),
  );

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

export const removeEmployeeLocationEpic: RootEpic = (action$, _, { api }) =>
  action$.pipe(
    filter(isActionOf(removeEmployeeLocationAsync.request)),
    switchMap((action) =>
      from(api.location.removeUserLocation(action.payload)).pipe(
        map((result) =>
          result.success
            ? removeEmployeeLocationAsync.success()
            : removeEmployeeLocationAsync.failure(result.reason),
        ),
      ),
    ),
  );

export const assignLocationEpic: RootEpic = (action$, state$, { api }) =>
  action$.pipe(
    filter(isActionOf(assignLocationAsync.request)),
    switchMap((action) =>
      from(
        api.location.assignUserLocations({
          ...action.payload,
          userId: state$.value.humanResource.details.employee?.userId!,
        }),
      ).pipe(
        map((result) =>
          result.success
            ? assignLocationAsync.success()
            : assignLocationAsync.failure(result.errors),
        ),
      ),
    ),
  );

export const editEmployeeLocationEpic: RootEpic = (action$, state$, { api }) =>
  action$.pipe(
    filter(isActionOf(editEmployeeLocationAsync.request)),
    switchMap((action) =>
      from(api.location.editUserLocation(action.payload)).pipe(
        map((result) =>
          result.success
            ? editEmployeeLocationAsync.success()
            : editEmployeeLocationAsync.failure(result.errors),
        ),
      ),
    ),
  );

export const fetchEmployeeLocationsEpic: RootEpic = (action$, _, { api }) =>
  action$.pipe(
    filter(isActionOf([fetchEmployeeLocationsAsync.request])),
    switchMap((action) =>
      from(api.location.fetchUserLocations(action.payload)).pipe(
        map((result) =>
          result.success
            ? fetchEmployeeLocationsAsync.success(result.data)
            : fetchEmployeeLocationsAsync.failure(result.reason),
        ),
      ),
    ),
  );

export const refetchEmployeeLocationsEpic: RootEpic = (action$, state$) =>
  action$.pipe(
    filter(
      isActionOf([
        assignLocationAsync.success,
        removeEmployeeLocationAsync.success,
        editEmployeeLocationAsync.success,
      ]),
    ),
    switchMap(() =>
      of(
        fetchEmployeeLocationsAsync.request(
          state$.value.humanResource.details.employee?.userId!,
        ),
      ),
    ),
  );
