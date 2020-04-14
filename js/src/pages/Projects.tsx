import React, { useCallback } from "react"
import { useDispatch, useSelector } from "react-redux"
import { Modal } from "antd"

import { CreateProjectForm } from "../features/project/components/ProjectCreateForm"
import { ProjectRolesForm } from "../features/project/components/ProjectRolesForm"
import { ProjectStatusForm } from "../features/project/components/ProjectStatusForm"
import { ProjectDescriptionForm } from "../features/project/components/ProjectDescriptionForm"
import {
  changeProjectStatusAsync,
  closeProjectCreateForm,
  closeProjectDescriptionForm,
  closeProjectRoleForm,
  closeProjectStatusForm,
  createProjectAsync,
  createProjectRoleAsync,
  editProjectDescriptionAsync,
  editProjectRoleAsync,
  fetchProjectAsync,
  openProjectCreateForm,
  openProjectDescriptionForm,
  openProjectRoleForm,
  openProjectStatusForm,
  removeProjectRoleAsync
} from "../features/project/actions"

import {
  isOpenProjectCreateForm,
  isOpenProjectRoleForm,
  isOpenProjectStatusForm,
  isLoading,
  getErrors,
  isOpenProjectDescriptionForm,
  getProject
} from "../features/project/selectors"
import {
  EditProjectDescriptionRequest,
  CreateProjectRequest,
  EditProjectStatusRequest,
  EditProjectRoleRequest, RemoveProjectRoleRequest
} from "ProjectModels"

export const ProjectsPage = () => {
  const dispatch = useDispatch()

  const fetchProject = useCallback((slug: string) => {
    dispatch(fetchProjectAsync.request(slug))
  }, [dispatch])

  const createProject = useCallback((form: CreateProjectRequest) => {
    dispatch(createProjectAsync.request(form))
  }, [dispatch])

  const changeProjectStatus = useCallback((form: EditProjectStatusRequest) => {
    dispatch(changeProjectStatusAsync.request(form))
  }, [dispatch])

  const editProjectDescription = useCallback((form: EditProjectDescriptionRequest) => {
    dispatch(editProjectDescriptionAsync.request(form))
  }, [dispatch])

  const createProjectRole = useCallback((form: EditProjectRoleRequest) => {
    dispatch(createProjectRoleAsync.request(form))
  }, [dispatch])

  const editProjectRole = useCallback((form: EditProjectRoleRequest) => {
    dispatch(editProjectRoleAsync.request(form))
  }, [dispatch])

  const removeProjectRole = useCallback((form: RemoveProjectRoleRequest) => {
    dispatch(removeProjectRoleAsync.request(form))
  }, [dispatch])

  const openCreateForm = useCallback(() => dispatch(openProjectCreateForm()), [dispatch])
  const closeCreateForm = useCallback(() => dispatch(closeProjectCreateForm()), [dispatch])
  const openRoleForm = useCallback(() => dispatch(openProjectRoleForm()), [dispatch])
  const closeRoleForm = useCallback(() => dispatch(closeProjectRoleForm()), [dispatch])
  const openStatusForm = useCallback(() => dispatch(openProjectStatusForm()), [dispatch])
  const closeStatusForm = useCallback(() => dispatch(closeProjectStatusForm()), [dispatch])
  const openDescriptionForm = useCallback(() => dispatch(openProjectDescriptionForm()), [dispatch])
  const closeDescriptionForm = useCallback(() => dispatch(closeProjectDescriptionForm()), [dispatch])

  const errors = useSelector(getErrors)
  const loading = useSelector(isLoading)
  const isOpenCreateForm = useSelector(isOpenProjectCreateForm)
  const isOpenRoleForm = useSelector(isOpenProjectRoleForm)
  const isOpenDescriptionForm = useSelector(isOpenProjectDescriptionForm)
  const isOpenStatusForm = useSelector(isOpenProjectStatusForm)
  const project = useSelector(getProject)

  return (
    <div>
      {isOpenCreateForm && <Modal
          title="Create project"
          visible={isOpenCreateForm}
          onCancel={closeCreateForm}
          footer={null}
      >
          <CreateProjectForm
              create={createProject}
              errors={errors}
              loading={loading}/>
      </Modal>}

      {isOpenRoleForm && !!project && <Modal
          title="Role"
          visible={isOpenRoleForm}
          onCancel={closeRoleForm}
          footer={null}
      >
          <ProjectRolesForm
              create={createProjectRole}
              edit={editProjectRole}
              remove={removeProjectRole}
              errors={errors}
              project={project}
              loading={loading}
          />
      </Modal>}

      {isOpenStatusForm && !!project && <Modal
          title="Status"
          visible={isOpenStatusForm}
          onCancel={closeStatusForm}
          footer={null}
      >
          <ProjectStatusForm
              change={changeProjectStatus}
              errors={errors} project={project}
              loading={loading}
          />
      </Modal>}

      {isOpenDescriptionForm && !!project && <Modal
          title="Status"
          visible={isOpenDescriptionForm}
          onCancel={closeDescriptionForm}
          footer={null}
      >
          <ProjectDescriptionForm
              edit={editProjectDescription}
              errors={errors}
              project={project}
              loading={loading}
          />
      </Modal>}

    </div>
  )
}
