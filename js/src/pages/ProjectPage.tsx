import React, { useCallback, useEffect } from "react";
import { useLocation, useParams } from "react-router";
import { useDispatch, useSelector } from "react-redux";
import { Card } from "antd";

import {
  EditProjectDescriptionRequest,
  EditProjectStatusRequest,
} from "ProjectModels";
import {
  changeProjectStatusAsync,
  closeProjectDescriptionFormAction,
  editProjectDescriptionAsync,
  fetchProjectAsync,
  openProjectDescriptionFormAction,
} from "../features/project/actions";
import {
  getErrors,
  getProject,
  getIsLoading,
  getIsOpenProjectDescriptionForm,
} from "../features/project/selectors";
import { ProjectDescriptionForm } from "../features/project/components/ProjectDescriptionForm";
import { ProjectStatusForm } from "../features/project/components/ProjectStatusForm";
import { ProjectDescription } from "../features/project/components/ProjectDescription";

export const ProjectPage: React.FC = () => {
  const dispatch = useDispatch();
  const location = useLocation();

  const { slug } = useParams<{ slug: string }>();

  const fetchProject = useCallback(
    (slug: string) => {
      dispatch(fetchProjectAsync.request(slug));
    },
    [dispatch],
  );

  const editProjectDescription = useCallback(
    (form: EditProjectDescriptionRequest) => {
      dispatch(editProjectDescriptionAsync.request(form));
    },
    [dispatch],
  );

  const changeProjectStatus = useCallback(
    (form: EditProjectStatusRequest) => {
      dispatch(changeProjectStatusAsync.request(form));
    },
    [dispatch],
  );

  const openProjectDescriptionForm = useCallback(() => {
    dispatch(openProjectDescriptionFormAction());
  }, [dispatch]);

  const closeProjectDescriptionForm = useCallback(() => {
    dispatch(closeProjectDescriptionFormAction());
  }, [dispatch]);

  const errors = useSelector(getErrors);
  const loading = useSelector(getIsLoading);
  const project = useSelector(getProject);
  const isOpenProjectDescriptionForm = useSelector(
    getIsOpenProjectDescriptionForm,
  );

  useEffect(() => {
    fetchProject(slug);
  }, [location, slug, fetchProject]);

  if (!project || slug !== project.slug) return null;

  return (
    <div>
      <Card title="Name">{project?.name}</Card>

      <Card title="Status">
        {!!project && (
          <ProjectStatusForm
            change={changeProjectStatus}
            project={project}
            errors={errors}
            loading={loading}
          />
        )}
      </Card>

      <Card title="Description">
        {!isOpenProjectDescriptionForm && (
          <ProjectDescription
            openProjectDescriptionForm={openProjectDescriptionForm}
            description={project?.description}
          />
        )}

        {!!project && isOpenProjectDescriptionForm && (
          <ProjectDescriptionForm
            edit={editProjectDescription}
            close={closeProjectDescriptionForm}
            errors={errors}
            project={project}
            loading={loading}
          />
        )}
      </Card>
    </div>
  );
};
