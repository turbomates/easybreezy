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
  selectIsOpenNewTeamForm,
  selectNewTeamFormErrors,
  selectProject,
} from "../features/project/selectors";
import { TeamList } from "../features/project/components/Team/TeamList";
import { NewTeamForm } from "../features/project/components/Team/NewTeamForm";
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

  const project = useSelector(selectProject);
  const newTeamMemberFormErrors = useSelector(selectNewTeamFormErrors);
  const isOpenNewTeamForm = useSelector(selectIsOpenNewTeamForm);

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
          <TeamList teams={project.teams} slug={project.slug} />
        </Card>
      </Col>

      <Modal
        title="New team"
        visible={isOpenNewTeamForm}
        onCancel={closeProjectTeamCreateForm}
        footer={null}
        destroyOnClose={true}
      >
        <NewTeamForm
          projectId={project.id}
          errors={newTeamMemberFormErrors}
          create={createTeam}
        />
      </Modal>
    </Row>
  );
};
