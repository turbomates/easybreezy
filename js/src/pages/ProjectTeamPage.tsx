import React, { useCallback, useEffect } from "react";
import { Card, Col, List, Modal, Row } from "antd";
import { PlusOutlined } from "@ant-design/icons";
import { useDispatch, useSelector } from "react-redux";
import { useParams } from "react-router";

import {
  addProjectTeamMemberAsync,
  changeProjectTeamStatusAsync,
  closeProjectTeamNewMemberFormAction,
  editProjectTeamMemberRoleAsync,
  fetchProjectAsync,
  fetchProjectTeamAsync,
  openProjectTeamNewMemberFormAction,
  removeProjectTeamMemberAsync,
} from "../features/project/actions";
import {
  selectEmployeesSelectOptions,
  selectIsOpenNewTeamMemberForm,
  selectNewTeamMemberFormErrors,
  selectProject,
  selectProjectTeam,
} from "../features/project/selectors";
import {
  NewProjectTeamMemberRequest,
  ChangeProjectTeamStatusRequest,
  EditProjectTeamMemberRoleRequest,
  RemoveProjectTeamMemberRequest,
} from "ProjectModels";
import { TeamNewMemberForm } from "../features/project/components/Team/TeamNewMemberForm";
import { TeamMembersListItem } from "../features/project/components/Team/TeamMembersListItem";
import { fetchEmployeesAsync } from "../features/human-resouce/actions";
import { TeamChangeStatus } from "../features/project/components/Team/TeamChangeStatus";
import { Preloader } from "../features/app/components/Preloader";

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

  const openNewMemberForm = useCallback(() => {
    dispatch(openProjectTeamNewMemberFormAction());
  }, [dispatch]);

  const closeNewMemberForm = useCallback(() => {
    dispatch(closeProjectTeamNewMemberFormAction());
  }, [dispatch]);

  const addMember = useCallback(
    (form: NewProjectTeamMemberRequest) => {
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

  const team = useSelector(selectProjectTeam);
  const project = useSelector(selectProject);
  const isOpenTeamNewMemberForm = useSelector(selectIsOpenNewTeamMemberForm);
  const employeesSelectOptions = useSelector(selectEmployeesSelectOptions);
  const teamNewTeamMemberFormErrors = useSelector(
    selectNewTeamMemberFormErrors,
  );

  useEffect(() => {
    fetchProjectTeam(id);
  }, [id, fetchProjectTeam]);

  useEffect(() => {
    fetchProject(slug);
  }, [slug, fetchProject]);

  useEffect(() => {
    fetchEmployees();
  }, [fetchEmployees]);

  if (!project || project.slug !== slug) return <Preloader />;

  if (!team || team.id !== id) return <Preloader />;

  return (
    <Row gutter={10} className="content">
      <Col xl={14} lg={24} md={24} xs={24}>
        <Card
          title={team.name}
          extra={<PlusOutlined onClick={openNewMemberForm} />}
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
              description="Access to tasks will be closed. It can be restored if necessary."
              type="danger"
              buttonText="Deactivate team"
              onButtonClick={() =>
                changeStatus({ teamId: team.id, status: "close" })
              }
            />
          ) : (
            <TeamChangeStatus
              description="Access to development will be open."
              type="primary"
              buttonText="Activate team"
              onButtonClick={() =>
                changeStatus({ teamId: team.id, status: "activate" })
              }
            />
          )}
        </Card>
      </Col>
      <Modal
        title="New team member"
        visible={isOpenTeamNewMemberForm}
        onCancel={closeNewMemberForm}
        footer={null}
        destroyOnClose={true}
      >
        <TeamNewMemberForm
          teamId={team.id}
          roles={project.roles}
          add={addMember}
          errors={teamNewTeamMemberFormErrors}
          employeesSelectOptions={employeesSelectOptions}
        />
      </Modal>
    </Row>
  );
};
