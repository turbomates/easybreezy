import React, { useCallback, useEffect } from "react";
import { Card, Col, List, Modal, Row } from "antd";
import { PlusOutlined } from "@ant-design/icons";
import { useDispatch, useSelector } from "react-redux";
import { useParams } from "react-router";

import {
  addProjectTeamMemberAsync,
  changeProjectTeamStatusAsync,
  closeProjectTeamAddMemberFormAction,
  editProjectTeamMemberRoleAsync,
  fetchProjectAsync,
  fetchProjectTeamAsync,
  openProjectTeamAddMemberFormAction,
  removeProjectTeamMemberAsync,
} from "../features/project/actions";
import {
  getEmployees,
  getIsOpenTeamAddMemberForm,
  getProject,
  getProjectTeam,
} from "../features/project/selectors";
import {
  AddProjectTeamMemberRequest,
  ChangeProjectTeamStatusRequest,
  EditProjectTeamMemberRoleRequest,
  RemoveProjectTeamMemberRequest,
} from "ProjectModels";
import { TeamAddMemberForm } from "../features/project/components/Team/TeamAddMemberForm";
import { TeamMembersListItem } from "../features/project/components/Team/TeamMembersListItem";
import { fetchEmployeesAsync } from "../features/human-resouce/actions";
import { TeamChangeStatus } from "../features/project/components/Team/TeamChangeStatus";

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

  const editMemberRole = useCallback(
    (value: EditProjectTeamMemberRoleRequest) => {
      dispatch(editProjectTeamMemberRoleAsync.request(value));
    },
    [dispatch],
  );

  const removeMember = useCallback(
    (value: RemoveProjectTeamMemberRequest) => {
      dispatch(removeProjectTeamMemberAsync.request(value));
    },
    [dispatch],
  );

  const openAddMemberForm = useCallback(() => {
    dispatch(openProjectTeamAddMemberFormAction());
  }, [dispatch]);

  const closeAddMemberForm = useCallback(() => {
    dispatch(closeProjectTeamAddMemberFormAction());
  }, [dispatch]);

  const addMember = useCallback(
    (form: AddProjectTeamMemberRequest) => {
      dispatch(addProjectTeamMemberAsync.request(form));
    },
    [dispatch],
  );

  const changeStatus = useCallback(
    (value: ChangeProjectTeamStatusRequest) => {
      dispatch(changeProjectTeamStatusAsync.request(value));
    },
    [dispatch],
  );

  const fetchEmployees = useCallback(() => {
    dispatch(fetchEmployeesAsync.request());
  }, [dispatch]);

  const team = useSelector(getProjectTeam);
  const project = useSelector(getProject);
  const isOpenTeamAddMemberForm = useSelector(getIsOpenTeamAddMemberForm);
  const employees = useSelector(getEmployees);

  useEffect(() => {
    fetchProjectTeam(id);
  }, [id, fetchProjectTeam]);

  useEffect(() => {
    fetchProject(slug);
  }, [slug, fetchProject]);

  useEffect(() => {
    fetchEmployees();
  }, [fetchEmployees]);

  if (!project || project.slug !== slug) return null;

  if (!team || team.id !== id) return null;

  return (
    <Row gutter={10} className="content">
      <Col xl={14} lg={24} md={24} xs={24}>
        <Card
          title={team.name}
          extra={<PlusOutlined onClick={openAddMemberForm} />}
        >
          <List
            itemLayout="horizontal"
            dataSource={team.members}
            renderItem={(item) => (
              <TeamMembersListItem
                roles={project.roles}
                member={item}
                edit={editMemberRole}
                teamId={team.id}
                remove={removeMember}
              />
            )}
          />
        </Card>

        <Card>
          {team.status === "Active" ? (
            <TeamChangeStatus
              description="Access to tasks is closed. Can be restored when needed."
              type="danger"
              btnText="Deactivate team"
              onChange={() =>
                changeStatus({ teamId: team.id, status: "close" })
              }
            />
          ) : (
            <TeamChangeStatus
              description="Open access to development."
              type="primary"
              btnText="Activate team"
              onChange={() =>
                changeStatus({ teamId: team.id, status: "activate" })
              }
            />
          )}
        </Card>
      </Col>
      <Modal
        title="New team member"
        visible={isOpenTeamAddMemberForm}
        onCancel={closeAddMemberForm}
        footer={null}
        destroyOnClose={true}
      >
        <TeamAddMemberForm
          teamId={team.id}
          roles={project.roles}
          add={addMember}
          employees={employees}
        />
      </Modal>
    </Row>
  );
};
