import React, { useCallback, useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useParams } from "react-router";
import { Card } from "antd";

import { ProjectRoleForm } from "../features/project/components/ProjectRoleForm";
import {
  CreateProjectRoleRequest,
  EditProjectRoleRequest,
  RemoveProjectRoleRequest,
} from "ProjectModels";
import {
  createProjectRoleAsync,
  editProjectRoleAsync,
  fetchProjectAsync,
  fetchProjectRoleAsync,
  removeProjectRoleAsync,
} from "../features/project/actions";
import {
  getProject,
  getRolePermissions,
} from "../features/project/selectors";

export const ProjectRolePage: React.FC = () => {
  const dispatch = useDispatch();
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

  const fetchRolePermissions = useCallback(() => {
    dispatch(fetchProjectRoleAsync.request());
  }, [dispatch]);

  const project = useSelector(getProject);
  const rolePermissions = useSelector(getRolePermissions);

  useEffect(() => {
    fetchProject(slug);
    fetchRolePermissions();
  }, [slug, fetchProject, fetchRolePermissions]);

  if (!project || slug !== project.slug) return null;

  return (
    <Card title="Role">
      <ProjectRoleForm
        project={project}
        edit={editProjectRole}
        create={createProjectRole}
        remove={removeProjectRole}
        rolePermissions={rolePermissions}
      />
    </Card>
  );
};
