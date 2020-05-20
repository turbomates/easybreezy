import React, { useCallback } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Modal } from "antd";

import { CreateProjectRequest, ProjectsListQuery } from "ProjectModels";
import {
  closeProjectCreateForm,
  createProjectAsync,
  fetchProjectsAsync,
  openProjectCreateForm,
} from "../features/project/actions";
import {
  selectIsOpenProjectCreateForm,
  selectProjects,
  selectNewProjectFormErrors,
  selectIsLoadingProjects,
} from "../features/project/selectors";
import { ProjectList } from "../features/project/components/Project/ProjectList";
import { NewProjectForm } from "../features/project/components/Project/NewProjectForm";

export const ProjectsPage: React.FC = () => {
  const dispatch = useDispatch();

  const fetchProjects = useCallback(
    (params: ProjectsListQuery) => {
      dispatch(fetchProjectsAsync.request(params));
    },
    [dispatch],
  );

  const createProject = useCallback(
    (form: CreateProjectRequest) => {
      dispatch(createProjectAsync.request(form));
    },
    [dispatch],
  );

  const openCreateForm = useCallback(() => dispatch(openProjectCreateForm()), [
    dispatch,
  ]);

  const closeCreateForm = useCallback(
    () => dispatch(closeProjectCreateForm()),
    [dispatch],
  );

  const newFormErrors = useSelector(selectNewProjectFormErrors);
  const loading = useSelector(selectIsLoadingProjects);
  const isOpenCreateForm = useSelector(selectIsOpenProjectCreateForm);
  const projects = useSelector(selectProjects);

  return (
    <div style={{ padding: 8 }}>
      <ProjectList
        projects={projects}
        fetchProjects={fetchProjects}
        openCreateForm={openCreateForm}
      />

      {openCreateForm && (
        <Modal
          title="Create project"
          visible={isOpenCreateForm}
          onCancel={closeCreateForm}
          footer={null}
          destroyOnClose={true}
        >
          <NewProjectForm
            create={createProject}
            errors={newFormErrors}
            loading={loading}
          />
        </Modal>
      )}
    </div>
  );
};
