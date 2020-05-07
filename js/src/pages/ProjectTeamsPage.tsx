import React, { useCallback, useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useParams } from "react-router";
import { Card, Col, Row } from "antd";

import { fetchProjectAsync } from "../features/project/actions";
import { getProject } from "../features/project/selectors";
import { ProjectTeamsList } from "../features/project/components/ProjectTeamsList";

export const ProjectTeamsPage: React.FC = () => {
  const dispatch = useDispatch();
  const { slug } = useParams<{ slug: string }>();

  const fetchProject = useCallback(
    (slug: string) => {
      dispatch(fetchProjectAsync.request(slug));
    },
    [dispatch],
  );

  const project = useSelector(getProject);

  useEffect(() => {
    fetchProject(slug);
  }, [slug, fetchProject]);

  if (!project || slug !== project.slug) return null;

  return (
    <Row gutter={10} className="content">
      <Col lg={12} md={24} xs={24}>
        <Card title="Teams">
          <ProjectTeamsList teams={project.teams} slug={project.slug} />
        </Card>
      </Col>
    </Row>
  );
};
