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
import { ProjectSlugForm } from "../features/project/components/Project/ProjectSlugForm";

import "./Project.scss";

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
              <ProjectStatusForm
                change={changeProjectStatus(project.slug)}
                project={project}
                loading={loading}
              />
            </List.Item>
          </List>
        </Card>
      </Col>
    </Row>
  );
};
