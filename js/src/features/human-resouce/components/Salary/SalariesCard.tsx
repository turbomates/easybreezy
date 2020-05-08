import React, { useCallback } from "react";
import { Card, Modal } from "antd";
import { PlusOutlined } from "@ant-design/icons";
import { useDispatch } from "react-redux";
import {
  applyEmployeeSalaryAsync,
  openApplySalaryModal,
  closeApplySalaryModal,
} from "../../actions";
import { State as Details } from "../../details.reducer";
import { Salaries } from "./Salaries";
import { SalaryForm } from "./SalaryForm";
import { ApplySalaryForm } from "HumanResourceModels";

import "./SalaryCard.scss";

interface Props {
  canEdit: boolean;
  details: Details;
}

export const SalariesCard: React.FC<Props> = ({ canEdit, details }) => {
  const dispatch = useDispatch();
  const { employee, loading, errors, isApplySalaryModalVisible } = details;

  const applySalary = useCallback(
    (form: ApplySalaryForm) => {
      if (employee) {
        dispatch(
          applyEmployeeSalaryAsync.request({
            userId: employee.userId,
            ...form,
          }),
        );
      }
    },
    [dispatch, employee],
  );

  const openCreateModal = useCallback(() => {
    dispatch(openApplySalaryModal());
  }, [dispatch]);

  const closeCreateModal = useCallback(() => {
    dispatch(closeApplySalaryModal());
  }, [dispatch]);

  return (
    <>
      <Card
        title="Salaries"
        className="human-resource-details__card"
        loading={loading}
        extra={canEdit ? <PlusOutlined onClick={openCreateModal} /> : null}
      >
        {(employee?.salaries?.length && (
          <Salaries salaries={employee?.salaries} />
        )) ||
          null}
      </Card>
      <Modal
        title="Apply Salary"
        visible={isApplySalaryModalVisible}
        onCancel={closeCreateModal}
        footer={null}
        destroyOnClose={true}
      >
        <SalaryForm errors={errors} applySalary={applySalary} />
      </Modal>
    </>
  );
};
