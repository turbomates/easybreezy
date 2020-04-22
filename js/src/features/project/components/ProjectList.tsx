import React, { useEffect } from "react";
import { Button, Card, List } from "antd";
import { parse, stringify } from "query-string";
import { useHistory, useLocation } from "react-router";
import { NavLink } from "react-router-dom";

import { Paging } from "components/Paging";
import { Paging as IPaging } from "MyTypes";
import { ProjectList as IProjectList, ProjectsRequest } from "ProjectModels";
import "../project.scss";

interface Props {
  projects: IPaging<IProjectList> | null;
  fetchProjects: (params: ProjectsRequest) => void;
  openCreateForm: () => void;
}

export const ProjectList: React.FC<Props> = ({
  projects,
  fetchProjects,
  openCreateForm,
}: Props) => {
  const history = useHistory();
  const location = useLocation();

  useEffect(() => {
    const params = parse(location.search) as ProjectsRequest;
    fetchProjects(params);
  }, [location, fetchProjects]);

  function onChange({ page, perPage }: { page: number; perPage: number }) {
    history.push(
      `?${stringify({
        currentPage: page,
        pageSize: perPage,
      })}`,
    );
  }

  if (projects === null) return null;

  return (
    <>
      <Button
        onClick={openCreateForm}
        type="primary"
        className="project__create-btn"
      >
        Create project
      </Button>

      <Card title="Projects">
        <List
          dataSource={projects.data}
          renderItem={(project) => (
            <List.Item>
              <List.Item.Meta
                title={
                  <NavLink to={`/projects/${project.slug}`}>
                    {project.name}
                  </NavLink>
                }
                description={project.description}
              />
            </List.Item>
          )}
        />
      </Card>

      <Paging
        haveMore={projects.hasMore}
        page={projects.currentPage}
        perPage={projects.pageSize}
        onChange={onChange}
      />
    </>
  );
};
