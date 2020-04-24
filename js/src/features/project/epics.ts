import { RootEpic } from "MyTypes";
import { catchError, filter, map, mergeMap, switchMap } from "rxjs/operators";
import { from, of } from "rxjs";
import { isActionOf } from "typesafe-actions";

import {
  changeProjectStatusAsync,
  createProjectAsync,
  editProjectDescriptionAsync,
  fetchProjectAsync,
  fetchProjectsAsync,
  editProjectRoleAsync,
  removeProjectRoleAsync,
  createProjectRoleAsync,
  fetchProjectRoleAsync,
} from "./actions";
import { push } from "connected-react-router"

export const fetchProjectsEpic: RootEpic = (action$, state$, { api }) =>
  action$.pipe(
    filter(isActionOf(fetchProjectsAsync.request)),
    switchMap((action) =>
      from(api.project.fetchProjects(action.payload)).pipe(
        map((result) =>
          result.success
            ? fetchProjectsAsync.success(result.data)
            : fetchProjectsAsync.failure(result.reason),
        ),
        catchError((message) => of(fetchProjectsAsync.failure(message))),
      ),
    ),
  );

export const fetchProjectEpic: RootEpic = (action$, state$, { api }) =>
  action$.pipe(
    filter(isActionOf(fetchProjectAsync.request)),
    switchMap((action) =>
      from(api.project.fetchProject(action.payload)).pipe(
        map((result) =>
          result.success
            ? fetchProjectAsync.success(result.data.data)
            : fetchProjectAsync.failure(result.reason),
        ),
        catchError((message) => of(fetchProjectAsync.failure(message))),
      ),
    ),
  );

export const createProject: RootEpic = (action$, state$, { api }) =>
  action$.pipe(
    filter(isActionOf(createProjectAsync.request)),
    switchMap((action) =>
      from(api.project.createProject(action.payload)).pipe(
        mergeMap((result) =>
          result.success
            ? [
                createProjectAsync.success(),
                push({pathname: `/projects/${action.payload.slug}`})
              ]
            : [createProjectAsync.failure(result.errors)],
        ),
        catchError((message) => of(createProjectAsync.failure(message))),
      ),
    ),
  );

export const changeProjectStatus: RootEpic = (action$, state$, { api }) =>
  action$.pipe(
    filter(isActionOf(changeProjectStatusAsync.request)),
    switchMap((action) =>
      from(api.project.editStatus(action.payload)).pipe(
        mergeMap((result) =>
          result.success
            ? [
                changeProjectStatusAsync.success(),
                fetchProjectAsync.request(action.payload.slug),
              ]
            : [changeProjectStatusAsync.failure(result.errors)],
        ),
        catchError((message) => of(changeProjectStatusAsync.failure(message))),
      ),
    ),
  );

export const editProjectDescription: RootEpic = (action$, state$, { api }) =>
  action$.pipe(
    filter(isActionOf(editProjectDescriptionAsync.request)),
    switchMap((action) =>
      from(api.project.editDescription(action.payload)).pipe(
        map((result) =>
          result.success
            ? editProjectDescriptionAsync.success()
            : editProjectDescriptionAsync.failure(result.errors),
        ),
        catchError((message) =>
          of(editProjectDescriptionAsync.failure(message)),
        ),
      ),
    ),
  );

export const createProjectRole: RootEpic = (action$, state$, { api }) =>
  action$.pipe(
    filter(isActionOf(createProjectRoleAsync.request)),
    switchMap((action) =>
      from(api.project.createRole(action.payload)).pipe(
        mergeMap((result) =>
          result.success
            ? [
                createProjectRoleAsync.success(),
                fetchProjectAsync.request(action.payload.slug),
              ]
            : [createProjectRoleAsync.failure(result.errors)],
        ),
        catchError((message) => of(createProjectRoleAsync.failure(message))),
      ),
    ),
  );

export const editProjectRole: RootEpic = (action$, state$, { api }) =>
  action$.pipe(
    filter(isActionOf(editProjectRoleAsync.request)),
    switchMap((action) =>
      from(api.project.editRole(action.payload)).pipe(
        map((result) =>
          result.success
            ? editProjectRoleAsync.success()
            : editProjectRoleAsync.failure(result.errors),
        ),
        catchError((message) => of(editProjectRoleAsync.failure(message))),
      ),
    ),
  );

export const removeProjectRole: RootEpic = (action$, state$, { api }) =>
  action$.pipe(
    filter(isActionOf(removeProjectRoleAsync.request)),
    switchMap((action) =>
      from(api.project.removeRole(action.payload)).pipe(
        mergeMap((result) =>
          result.success
            ? [
                removeProjectRoleAsync.success(),
                fetchProjectAsync.request(action.payload.slug),
              ]
            : [removeProjectRoleAsync.failure(result.errors)],
        ),
        catchError((message) => of(removeProjectRoleAsync.failure(message))),
      ),
    ),
  );

export const fetchProjectRolePermissionsEpic: RootEpic = (
  action$,
  state$,
  { api },
) =>
  action$.pipe(
    filter(isActionOf(fetchProjectRoleAsync.request)),
    switchMap((action) =>
      from(api.project.fetchRolePermissions()).pipe(
        map((result) =>
          result.success
            ? fetchProjectRoleAsync.success(result.data)
            : fetchProjectRoleAsync.failure(result.errors),
        ),
        catchError((message) => of(fetchProjectRoleAsync.failure(message))),
      ),
    ),
  );
