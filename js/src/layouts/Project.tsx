import React, { useCallback, useEffect } from "react";
import { Layout } from "antd";
import { useSelector, useDispatch } from "react-redux";
import { useParams } from "react-router";

import { ProjectHeaderMenu } from "../features/project/components/ProjectHeaderMenu";
import { selectProject } from "../features/project/selectors";
import { fetchProjectAsync } from "../features/project/actions";

import "./Project.scss";

const { Content } = Layout;

export const Project: React.FC = ({ children }) => {
  const { slug } = useParams<{ slug: string }>();
  const dispatch = useDispatch();

  const fetchProject = useCallback(
    (slug: string) => {
      dispatch(fetchProjectAsync.request(slug));
    },
    [dispatch],
  );

  const project = useSelector(selectProject);

  useEffect(() => {
    fetchProject(slug);
  }, [fetchProject, slug]);

  if (!project || slug !== project.slug) return null;

  return (
    <Layout className="project-layout">
      <Layout>
        <ProjectHeaderMenu project={project} />
        <Content>{children}</Content>
      </Layout>
    </Layout>
  );
};
