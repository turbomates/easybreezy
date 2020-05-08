import React, { useCallback, useEffect } from "react";
import { Card, Col, Row } from "antd";
import { PlusOutlined } from "@ant-design/icons";
import { useDispatch, useSelector } from "react-redux";
import { useParams } from "react-router";

import { TeamMembersList } from "../features/project/components/Team/TeamMembersList";
import {
  fetchProjectAsync,
  fetchProjectTeamAsync,
} from "../features/project/actions";
import { getProject, getProjectTeam } from "../features/project/selectors";

export const ProjectTeamPage: React.FC = () => {
  const dispatch = useDispatch();
  const { id, slug } = useParams<{ id: string; slug: string }>();

  const fetchProjectTeam = useCallback(
    (id: string) => {
      dispatch(fetchProjectTeamAsync.request(id));
    },
    [dispatch],
  );

  const fetchProject = useCallback(
    (slug: string) => {
      dispatch(fetchProjectAsync.request(slug));
    },
    [dispatch],
  );

  const team = useSelector(getProjectTeam);
  const project = useSelector(getProject);

  useEffect(() => {
    fetchProjectTeam(id);
  }, [id, fetchProjectTeam]);

  useEffect(() => {
    fetchProject(slug);
  }, [slug, fetchProject]);

  if (!project || project.slug !== slug) return null;

  if (!team || team.id !== id) return null;

  return (
    <Row gutter={10} className="content">
      <Col lg={12} md={24} xs={24}>
        <Card title="Team" extra={<PlusOutlined />}>
          <TeamMembersList members={team.members} roles={project.roles} />
        </Card>
      </Col>
    </Row>
  );
};
