import React, { useCallback, useEffect } from "react";
import { Card, Modal, List } from "antd";
import { PlusOutlined } from "@ant-design/icons";
import { AbsenceForm } from "./AbsenceForm";
import { AbsenceForm as AbsenceFormModel } from "HumanResourceModels";
import { useSelector, useDispatch } from "react-redux";
import {
  oneAbsences,
  employeeDetails,
  isUpdateAbsenceModalVisible,
  updateAbsenceFormInitialValues,
} from "../../selectors";
import {
  createAbsenceAsync,
  approveAbsenceAsync,
  fetchEmployeeAbsencesAsync,
  openAbsenceCreateModal,
  closeAbsenceCreateModal,
  removeAbsenceAsync,
  openAbsenceUpdateModal,
  closeAbsenceUpdateModal,
  updateAbsenceAsync,
} from "../../actions";
import { AbsenceListItem } from "./AbcenceListItem";

interface Props {
  canEdit: boolean;
  employeeId: string;
}

export const AbsencesCard: React.FC<Props> = ({ canEdit, employeeId }) => {
  const { employee } = useSelector(employeeDetails);
  const absences = useSelector(oneAbsences);
  const isUpdateVisible = useSelector(isUpdateAbsenceModalVisible);
  const initialValues = useSelector(updateAbsenceFormInitialValues);
  const dispatch = useDispatch();

  const create = useCallback(
    (data: AbsenceFormModel) => {
      if (employee)
        dispatch(
          createAbsenceAsync.request({
            userId: employee.userId,
            ...data,
          }),
        );
    },
    [dispatch, employee],
  );

  const update = useCallback(
    (form: AbsenceFormModel) => {
      dispatch(updateAbsenceAsync.request(form));
    },
    [dispatch],
  );

  const approve = useCallback(
    (absenceId: string) => dispatch(approveAbsenceAsync.request(absenceId)),
    [dispatch],
  );

  const remove = useCallback(
    (absenceId: string) => dispatch(removeAbsenceAsync.request(absenceId)),
    [dispatch],
  );

  const openCreateModal = useCallback(() => {
    dispatch(openAbsenceCreateModal());
  }, [dispatch]);

  const closeCreateModal = useCallback(() => {
    dispatch(closeAbsenceCreateModal());
  }, [dispatch]);

  const openUpdateModal = useCallback(
    (absenceId: string) => {
      dispatch(openAbsenceUpdateModal(absenceId));
    },
    [dispatch],
  );

  const closeUpdateModal = useCallback(() => {
    dispatch(closeAbsenceUpdateModal());
  }, [dispatch]);

  useEffect(() => {
    dispatch(fetchEmployeeAbsencesAsync.request(employeeId));
  }, [dispatch, employeeId]);

  return (
    <>
      <Card
        title="Absences"
        className="human-resource-details__card"
        loading={absences.loading}
        extra={canEdit ? <PlusOutlined onClick={openCreateModal} /> : null}
      >
        <List
          dataSource={absences.items}
          renderItem={(absence) => (
            <AbsenceListItem
              absence={absence}
              approve={approve}
              canEdit={canEdit}
              remove={remove}
              openUpdateModal={openUpdateModal}
            />
          )}
        />
      </Card>
      <Modal
        title="Update Absence"
        visible={isUpdateVisible}
        onCancel={closeUpdateModal}
        footer={null}
        destroyOnClose={true}
      >
        <AbsenceForm
          onSubmit={update}
          errors={absences.errors}
          mode="UPDATE"
          initialValues={initialValues}
        />
      </Modal>
      <Modal
        title="New Absence"
        visible={absences.isCreateModalVisible}
        onCancel={closeCreateModal}
        footer={null}
        destroyOnClose={true}
      >
        <AbsenceForm onSubmit={create} errors={absences.errors} mode="CREATE" />
      </Modal>
    </>
  );
};
