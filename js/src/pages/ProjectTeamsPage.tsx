import React, { useCallback, useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useParams } from "react-router";
import { Card, Col, Modal, Row } from "antd";
import { PlusOutlined } from "@ant-design/icons";

import {
  closeProjectTeamCreateFormAction,
  createProjectTeamAsync,
  fetchProjectAsync,
  openProjectTeamCreateFormAction,
} from "../features/project/actions";
import {
  getErrors,
  getIsOpenProjectTeamCreateForm,
  getProject,
} from "../features/project/selectors";
import { ProjectTeamsList } from "../features/project/components/ProjectTeamsList";
import { ProjectTeamsCreateForm } from "../features/project/components/ProjectTeamsCreateForm";
import { CreateProjectTeamRequest } from "ProjectModels";

export const ProjectTeamsPage: React.FC = () => {
  const dispatch = useDispatch();
  const { slug } = useParams<{ slug: string }>();

  const fetchProject = useCallback(
    (slug: string) => {
      dispatch(fetchProjectAsync.request(slug));
    },
    [dispatch],
  );

  const createTeam = useCallback(
    (form: CreateProjectTeamRequest) => {
      dispatch(createProjectTeamAsync.request(form));
    },
    [dispatch],
  );

  const openProjectTeamCreateForm = useCallback(() => {
    dispatch(openProjectTeamCreateFormAction());
  }, [dispatch]);

  const closeProjectTeamCreateForm = useCallback(() => {
    dispatch(closeProjectTeamCreateFormAction());
  }, [dispatch]);

  const project = useSelector(getProject);
  const errors = useSelector(getErrors);
  const isOpenProjectTeamCreateForm = useSelector(
    getIsOpenProjectTeamCreateForm,
  );

  useEffect(() => {
    fetchProject(slug);
  }, [slug, fetchProject]);

  if (!project || slug !== project.slug) return null;

  return (
    <Row gutter={10} className="content">
      <Col lg={12} md={24} xs={24}>
        <Card
          title="Teams"
          extra={<PlusOutlined onClick={openProjectTeamCreateForm} />}
        >
          <ProjectTeamsList teams={project.teams} slug={project.slug} />
        </Card>
      </Col>

      <Modal
        title="Create team"
        visible={isOpenProjectTeamCreateForm}
        onCancel={closeProjectTeamCreateForm}
        footer={null}
      >
        <ProjectTeamsCreateForm
          projectId={project.id}
          errors={errors}
          create={createTeam}
        />
      </Modal>
    </Row>
  );
};
