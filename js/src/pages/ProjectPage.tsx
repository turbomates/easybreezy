import React, { useCallback, useEffect } from "react";
import { useLocation, useParams } from "react-router";
import { useDispatch, useSelector } from "react-redux";
import { Card, Col, List, Row } from "antd";

import {
  EditProjectDescriptionRequest,
  EditProjectSlugRequest,
  ProjectStatusTypeRequest,
} from "ProjectModels";
import {
  addProjectIssueStatusAsync,
  changeProjectIssueStatusAsync,
  changeProjectStatusAsync,
  editProjectDescriptionAsync,
  editProjectSlugAsync,
  fetchProjectAsync,
  removeProjectIssueStatusAsync,
} from "../features/project/actions";
import {
  selectProject,
  selectProjectSlugFormErrors,
  selectProjectDescriptionFormErrors,
  selectIsLoadingProject,
} from "../features/project/selectors";
import { ProjectDescriptionForm } from "../features/project/components/Project/ProjectDescriptionForm";
import { ProjectStatusDangerZone } from "../features/project/components/Project/ProjectStatusDangerZone";
import { ProjectSlugForm } from "../features/project/components/Project/ProjectSlugForm";

import "./Project.scss";
import { ProjectIssueStatus } from "../features/project/components/Project/ProjectIssueStatus";

export const ProjectPage: React.FC = () => {
  const dispatch = useDispatch();
  const location = useLocation();
  const { slug } = useParams<{ slug: string }>();

  const loading = useSelector(selectIsLoadingProject);
  const project = useSelector(selectProject);
  const descriptionFormErrors = useSelector(selectProjectDescriptionFormErrors);
  const slugFormErrors = useSelector(selectProjectSlugFormErrors);

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
    (slug: string) => {
      return (statusType: ProjectStatusTypeRequest) => {
        dispatch(
          changeProjectStatusAsync.request({
            slug,
            statusType,
          }),
        );
      };
    },
    [dispatch],
  );

  const editProjectSlug = useCallback(
    (form: EditProjectSlugRequest) => {
      dispatch(editProjectSlugAsync.request(form));
    },
    [dispatch],
  );

  const addProjectIssueStatus = useCallback(
    (slug: string) => {
      return (name: string) =>
        dispatch(
          addProjectIssueStatusAsync.request({
            slug,
            name,
          }),
        );
    },
    [dispatch],
  );

  const changeProjectIssueStatus = useCallback(
    (slug: string) => {
      return (name: string, statusId: string) =>
        dispatch(
          changeProjectIssueStatusAsync.request({
            slug,
            statusId,
            name,
          }),
        );
    },
    [dispatch],
  );

  const removeProjectIssueStatus = useCallback(
    (slug: string) => {
      return (statusId: string) =>
        dispatch(
          removeProjectIssueStatusAsync.request({
            slug,
            statusId,
          }),
        );
    },
    [dispatch],
  );

  useEffect(() => {
    fetchProject(slug);
  }, [location, slug, fetchProject]);

  if (!project || slug !== project.slug) return null;

  return (
    <Row className="content" gutter={24}>
      <Col lg={12} md={24} xs={24}>
        <Card>
          <List>
            <List.Item className="list-item">
              <List.Item.Meta title="Issue statuses" />
              <ProjectIssueStatus
                statuses={project.statuses}
                change={changeProjectIssueStatus(project.slug)}
                add={addProjectIssueStatus(project.slug)}
                remove={removeProjectIssueStatus(project.slug)}
              />
            </List.Item>

            <List.Item className="list-item">
              <List.Item.Meta title="Description" />
              <ProjectDescriptionForm
                edit={editProjectDescription}
                errors={descriptionFormErrors}
                project={project}
                loading={loading}
              />
            </List.Item>

            <List.Item className="list-item">
              <List.Item.Meta title="Slug" />
              <ProjectSlugForm
                edit={editProjectSlug}
                errors={slugFormErrors}
                project={project}
                loading={loading}
              />
            </List.Item>

            <List.Item className="list-item">
              <ProjectStatusDangerZone
                change={changeProjectStatus(project.slug)}
                project={project}
              />
            </List.Item>
          </List>
        </Card>
      </Col>
    </Row>
  );
};
