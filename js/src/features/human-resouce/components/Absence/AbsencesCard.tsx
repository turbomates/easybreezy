import React, { useCallback, useEffect } from "react";
import { Card, Divider, Modal, List } from "antd";
import { AbsenceForm } from "./AbsenceForm";
import { AbsenceForm as AbsenceFormModel } from "HumanResourceModels";
import { useSelector, useDispatch } from "react-redux";
import {
  myAbsences,
  employeeDetails,
  isUpdateAbsenceModalVisible,
  updateAbsenceFormInitialValues,
} from "../../selectors";
import {
  createAbsenceAsync,
  approveAbsenceAsync,
  fetchMyAbsencesAsync,
  openAbsenceCreateForm,
  closeAbsenceCreateForm,
  removeAbsenceAsync,
  openAbsenceUpdateModal,
  closeAbsenceUpdateModal,
  updateAbsenceAsync,
} from "../../actions";
import { AbsenceListItem } from "./AbcenceListItem";

interface Props {
  canEdit: boolean;
}

export const AbsencesCard: React.FC<Props> = ({ canEdit }) => {
  const { employee } = useSelector(employeeDetails);
  const absences = useSelector(myAbsences);
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

  const toggle = useCallback(() => {
    absences.createFormVisible
      ? dispatch(closeAbsenceCreateForm())
      : dispatch(openAbsenceCreateForm());
  }, [dispatch, absences.createFormVisible]);

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
    dispatch(fetchMyAbsencesAsync.request());
  }, [dispatch]);

  return (
    <>
      <Card
        title="Absences"
        className="human-resource-details__card"
        loading={absences.loading}
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
        {canEdit && (
          <Divider>
            <span onClick={toggle}>Create new</span>
          </Divider>
        )}
        {canEdit && absences.createFormVisible && (
          <AbsenceForm
            onSubmit={create}
            errors={absences.errors}
            mode="CREATE"
          />
        )}
      </Card>
      <Modal
        title="Update absence"
        visible={isUpdateVisible}
        onCancel={closeUpdateModal}
        footer={null}
      >
        {isUpdateVisible && (
          <AbsenceForm
            onSubmit={update}
            errors={absences.errors}
            mode="UPDATE"
            initialValues={initialValues}
          />
        )}
      </Modal>
    </>
  );
};
