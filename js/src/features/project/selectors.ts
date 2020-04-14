import { RootState } from "MyTypes";

export const getProject = (state: RootState) => state.projectReducer.project

export const isOpenProjectCreateForm = (state: RootState) => state.projectReducer.isOpenCreateForm

export const isOpenProjectRoleForm = (state: RootState) => state.projectReducer.isOpenRoleForm

export const isOpenProjectStatusForm = (state: RootState) => state.projectReducer.isOpenStatusForm

export const isOpenProjectDescriptionForm = (state: RootState) => state.projectReducer.isOpenDescriptionForm

export const isLoading = (state: RootState) => state.projectReducer.loading

export const getErrors = (state: RootState) => state.projectReducer.errors

