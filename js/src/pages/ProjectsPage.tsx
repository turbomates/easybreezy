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
  selectCreateProjectFormErrors,
  selectIsLoadingProjects,
} from "../features/project/selectors";
import { ProjectList } from "../features/project/components/Project/ProjectList";
import { CreateProjectForm } from "../features/project/components/Project/ProjectCreateForm";

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

  const createFormErrors = useSelector(selectCreateProjectFormErrors);
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
          <CreateProjectForm
            create={createProject}
            errors={createFormErrors}
            loading={loading}
          />
        </Modal>
      )}
    </div>
  );
};
