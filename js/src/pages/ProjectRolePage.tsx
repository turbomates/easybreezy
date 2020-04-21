import React, { useCallback, useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useLocation, useParams } from "react-router";
import { Card } from "antd";

import { ProjectRoleForm } from "../features/project/components/ProjectRoleForm";
import {
  CreateProjectRoleRequest,
  EditProjectRoleRequest,
  RemoveProjectRoleRequest,
} from "ProjectModels";
import {
  clearStateAction,
  createProjectRoleAsync,
  editProjectRoleAsync,
  fetchProjectAsync,
  removeProjectRoleAsync,
} from "../features/project/actions";
import {
  getErrors,
  getProject,
  getIsLoading,
} from "../features/project/selectors";
import { useClearState } from "../hooks/useClearState";

export const ProjectRolePage: React.FC = () => {
  const dispatch = useDispatch();
  const location = useLocation();
  const { slug } = useParams<{ slug: string }>();

  const fetchProject = useCallback(
    (slug: string) => {
      dispatch(fetchProjectAsync.request(slug));
    },
    [dispatch],
  );

  const createProjectRole = useCallback(
    (form: CreateProjectRoleRequest) => {
      dispatch(createProjectRoleAsync.request(form));
    },
    [dispatch],
  );

  const editProjectRole = useCallback(
    (form: EditProjectRoleRequest) => {
      dispatch(editProjectRoleAsync.request(form));
    },
    [dispatch],
  );

  const removeProjectRole = useCallback(
    (form: RemoveProjectRoleRequest) => {
      dispatch(removeProjectRoleAsync.request(form));
    },
    [dispatch],
  );

  const errors = useSelector(getErrors);
  const loading = useSelector(getIsLoading);
  const project = useSelector(getProject);

  useEffect(() => {
    fetchProject(slug);
  }, [location, slug, fetchProject]);

  useClearState(clearStateAction);

  if (!project) return null;

  return (
    <Card title="Role">
      <ProjectRoleForm
        project={project}
        errors={errors}
        loading={loading}
        edit={editProjectRole}
        create={createProjectRole}
        remove={removeProjectRole}
      />
    </Card>
  );
};
