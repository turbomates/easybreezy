import React, { useCallback, useEffect, useState } from "react";
import { useLocation, useParams } from "react-router";
import { useDispatch, useSelector } from "react-redux";
import { Card } from "antd";

import {
  EditProjectDescriptionRequest,
  EditProjectSlugRequest,
  EditProjectStatusRequest,
} from "ProjectModels";
import {
  changeProjectStatusAsync,
  editProjectDescriptionAsync,
  editProjectSlugAsync,
  fetchProjectAsync,
} from "../features/project/actions";
import {
  selectProject,
  selectProjectSlugFormErrors,
  selectProjectDescriptionFormErrors,
  selectIsLoadingProject,
} from "../features/project/selectors";
import { ProjectDescriptionForm } from "../features/project/components/Project/ProjectDescriptionForm";
import { ProjectStatusForm } from "../features/project/components/Project/ProjectStatusForm";
import { ProjectDescription } from "../features/project/components/Project/ProjectDescription";
import { ProjectSlug } from "../features/project/components/Project/ProjectSlug";
import { ProjectSlugForm } from "../features/project/components/Project/ProjectSlugForm";

export const ProjectPage: React.FC = () => {
  const dispatch = useDispatch();
  const location = useLocation();

  const { slug } = useParams<{ slug: string }>();

  const [isOpenProjectEditSlugForm, setIsOpenProjectEditSlugForm] = useState(
    false,
  );
  const [
    isOpenProjectDescriptionForm,
    setIsOpenProjectDescriptionForm,
  ] = useState(false);
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

  const editProjectSlug = useCallback(
    (form: EditProjectSlugRequest) => {
      dispatch(editProjectSlugAsync.request(form));
    },
    [dispatch],
  );

  const loading = useSelector(selectIsLoadingProject);
  const project = useSelector(selectProject);
  const descriptionFormErrors = useSelector(selectProjectDescriptionFormErrors);
  const slugFormErrors = useSelector(selectProjectSlugFormErrors);

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
            loading={loading}
          />
        )}
      </Card>

      <Card title="Description">
        {!isOpenProjectDescriptionForm && (
          <ProjectDescription
            onButtonClick={() => setIsOpenProjectDescriptionForm(true)}
            description={project?.description}
          />
        )}

        {isOpenProjectDescriptionForm && (
          <ProjectDescriptionForm
            edit={editProjectDescription}
            close={() => setIsOpenProjectDescriptionForm(false)}
            errors={descriptionFormErrors}
            project={project}
            loading={loading}
          />
        )}
      </Card>

      <Card title="Slug">
        {!isOpenProjectEditSlugForm && (
          <ProjectSlug
            openProjectSlugForm={() => setIsOpenProjectEditSlugForm(true)}
            slug={project.slug}
          />
        )}

        {!!project && isOpenProjectEditSlugForm && (
          <ProjectSlugForm
            edit={editProjectSlug}
            close={() => setIsOpenProjectEditSlugForm(false)}
            errors={slugFormErrors}
            project={project}
            loading={loading}
          />
        )}
      </Card>
    </div>
  );
};
