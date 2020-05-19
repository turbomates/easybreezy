import { RootEpic } from "MyTypes";
import { catchError, filter, map, mergeMap, switchMap } from "rxjs/operators";
import { from, of } from "rxjs";
import { isActionOf } from "typesafe-actions";
import { push } from "connected-react-router";

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
  editProjectSlugAsync,
  createProjectTeamAsync,
  closeProjectTeamCreateFormAction,
  fetchProjectTeamAsync,
  editProjectTeamMemberRoleAsync,
  removeProjectTeamMemberAsync,
  changeProjectTeamStatusAsync,
  addProjectTeamMemberAsync,
  closeProjectTeamNewMemberFormAction,
} from "./actions";
import { notificationError } from "../notification/actions";
import { SOMETHING_WENT_WRONG } from "../../constants";

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
                push({ pathname: `/projects/${action.payload.slug}` }),
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
            : [notificationError(SOMETHING_WENT_WRONG)],
        ),
        catchError(() => of(notificationError(SOMETHING_WENT_WRONG))),
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
            : [notificationError(SOMETHING_WENT_WRONG)],
        ),
        catchError(() => of(notificationError(SOMETHING_WENT_WRONG))),
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
            : fetchProjectRoleAsync.failure(result.reason),
        ),
        catchError((message) => of(fetchProjectRoleAsync.failure(message))),
      ),
    ),
  );

export const editProjectSlug: RootEpic = (action$, state$, { api }) =>
  action$.pipe(
    filter(isActionOf(editProjectSlugAsync.request)),
    switchMap((action) =>
      from(api.project.editSlug(action.payload)).pipe(
        mergeMap((result) =>
          result.success
            ? [
                editProjectSlugAsync.success(),
                push({ pathname: `/projects/${action.payload.newSlug}` }),
              ]
            : [editProjectSlugAsync.failure(result.errors)],
        ),
        catchError((message) => of(editProjectSlugAsync.failure(message))),
      ),
    ),
  );

export const createProjectTeam: RootEpic = (action$, state$, { api }) =>
  action$.pipe(
    filter(isActionOf(createProjectTeamAsync.request)),
    switchMap((action) =>
      from(api.team.createTeam(action.payload)).pipe(
        mergeMap((result) =>
          result.success
            ? [
                createProjectTeamAsync.success(),
                closeProjectTeamCreateFormAction(),
                fetchProjectAsync.request(
                  state$.value.project.project.data!.slug,
                ),
              ]
            : [createProjectTeamAsync.failure(result.errors)],
        ),
        catchError((message) => of(createProjectTeamAsync.failure(message))),
      ),
    ),
  );

export const fetchProjectTeam: RootEpic = (action$, state$, { api }) =>
  action$.pipe(
    filter(isActionOf(fetchProjectTeamAsync.request)),
    switchMap((action) =>
      from(api.team.fetchTeam(action.payload)).pipe(
        map((result) =>
          result.success
            ? fetchProjectTeamAsync.success(result.data.data)
            : fetchProjectTeamAsync.failure(result.errors),
        ),
        catchError((message) => of(fetchProjectTeamAsync.failure(message))),
      ),
    ),
  );

export const editProjectTeamMemberRole: RootEpic = (action$, state$, { api }) =>
  action$.pipe(
    filter(isActionOf(editProjectTeamMemberRoleAsync.request)),
    switchMap((action) =>
      from(api.team.editMemberRole(action.payload)).pipe(
        map((result) =>
          result.success
            ? editProjectTeamMemberRoleAsync.success()
            : notificationError(SOMETHING_WENT_WRONG),
        ),
        catchError(() => of(notificationError(SOMETHING_WENT_WRONG))),
      ),
    ),
  );

export const removeProjectTeamMember: RootEpic = (action$, state$, { api }) =>
  action$.pipe(
    filter(isActionOf(removeProjectTeamMemberAsync.request)),
    switchMap((action) =>
      from(api.team.removeMember(action.payload)).pipe(
        mergeMap((result) =>
          result.success
            ? [
                removeProjectTeamMemberAsync.success(),
                fetchProjectTeamAsync.request(action.payload.teamId),
              ]
            : [notificationError(SOMETHING_WENT_WRONG)],
        ),
        catchError(() => of(notificationError(SOMETHING_WENT_WRONG))),
      ),
    ),
  );

export const changeProjectTeamStatus: RootEpic = (action$, state$, { api }) =>
  action$.pipe(
    filter(isActionOf(changeProjectTeamStatusAsync.request)),
    switchMap((action) =>
      from(api.team.changeStatus(action.payload)).pipe(
        mergeMap((result) =>
          result.success
            ? [
                changeProjectTeamStatusAsync.success(),
                fetchProjectTeamAsync.request(action.payload.teamId),
              ]
            : [notificationError(SOMETHING_WENT_WRONG)],
        ),
        catchError(() => of(notificationError(SOMETHING_WENT_WRONG))),
      ),
    ),
  );

export const addProjectTeamMember: RootEpic = (action$, state$, { api }) =>
  action$.pipe(
    filter(isActionOf(addProjectTeamMemberAsync.request)),
    switchMap((action) =>
      from(api.team.addMember(action.payload)).pipe(
        mergeMap((result) =>
          result.success
            ? [
                addProjectTeamMemberAsync.success(),
                fetchProjectTeamAsync.request(action.payload.teamId),
                closeProjectTeamNewMemberFormAction(),
              ]
            : [addProjectTeamMemberAsync.failure(result.errors)],
        ),
        catchError((message) => of(addProjectTeamMemberAsync.failure(message))),
      ),
    ),
  );
