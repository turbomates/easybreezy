import React, { useCallback, useEffect } from "react";
import { Card, Col, Modal, Row } from "antd";
import { PlusOutlined } from "@ant-design/icons";
import { useDispatch, useSelector } from "react-redux";
import { useParams } from "react-router";

import { TeamMembersList } from "../features/project/components/Team/TeamMembersList";
import {
  addProjectTeamMemberAsync,
  changeProjectTeamStatusAsync,
  closeProjectTeamAddMemberFormAction,
  editProjectTeamMemberRoleAsync,
  fetchEmployeesAsync,
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
  getTeamErrors,
} from "../features/project/selectors";
import {
  AddProjectTeamMemberRequest,
  ChangeProjectTeamStatusRequest,
  EditProjectTeamMemberRoleRequest,
  RemoveProjectTeamMemberRequest,
} from "ProjectModels";
import { TeamAddMemberForm } from "../features/project/components/Team/TeamAddMemberForm";
import { TeamChangeStatusForm } from "../features/project/components/Team/TeamChangeStatusForm";

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
    (form: ChangeProjectTeamStatusRequest) => {
      dispatch(changeProjectTeamStatusAsync.request(form));
    },
    [dispatch],
  );

  const fetchEmployees = useCallback(() => {
    dispatch(fetchEmployeesAsync.request());
  }, [dispatch]);

  const team = useSelector(getProjectTeam);
  const project = useSelector(getProject);
  const isOpenTeamAddMemberForm = useSelector(getIsOpenTeamAddMemberForm);
  const errors = useSelector(getTeamErrors);
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
      <Col lg={12} md={24} xs={24}>
        <Card title="Team" extra={<PlusOutlined onClick={openAddMemberForm} />}>
          <TeamMembersList
            members={team.members}
            roles={project.roles}
            edit={editMemberRole}
            teamId={team.id}
            remove={removeMember}
          />
        </Card>
      </Col>

      <Col lg={12} md={24} xs={24}>
        <Card title="Status">
          <TeamChangeStatusForm
            status={team.status}
            teamId={team.id}
            change={changeStatus}
            errors={errors}
          />
        </Card>
      </Col>

      <Modal
        title="Add member"
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
