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
  getErrors,
  getProject,
  getIsLoading,
} from "../features/project/selectors";
import { ProjectDescriptionForm } from "../features/project/components/ProjectDescriptionForm";
import { ProjectStatusForm } from "../features/project/components/ProjectStatusForm";
import { ProjectDescription } from "../features/project/components/ProjectDescription";
import { ProjectSlug } from "../features/project/components/ProjectSlug";
import { ProjectSlugForm } from "../features/project/components/ProjectSlugForm";

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

  const errors = useSelector(getErrors);
  const loading = useSelector(getIsLoading);
  const project = useSelector(getProject);

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
            openProjectDescriptionForm={() =>
              setIsOpenProjectDescriptionForm(true)
            }
            description={project?.description}
          />
        )}

        {isOpenProjectDescriptionForm && (
          <ProjectDescriptionForm
            edit={editProjectDescription}
            closeProjectDescriptionForm={() =>
              setIsOpenProjectDescriptionForm(false)
            }
            errors={errors}
            project={project}
            loading={loading}
          />
        )}
      </Card>

      <Card title="Slug">
        {!isOpenProjectEditSlugForm && (
          <ProjectSlug
            openProjectSlugForm={() => setIsOpenProjectEditSlugForm(true)}
            slug={project?.slug}
          />
        )}

        {!!project && isOpenProjectEditSlugForm && (
          <ProjectSlugForm
            edit={editProjectSlug}
            closeProjectSlugForm={() => setIsOpenProjectEditSlugForm(false)}
            errors={errors}
            project={project}
            loading={loading}
          />
        )}
      </Card>
    </div>
  );
};
