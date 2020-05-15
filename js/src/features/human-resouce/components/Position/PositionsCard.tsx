import React, { useCallback } from "react";
import { Card, Modal } from "antd";
import { PlusOutlined } from "@ant-design/icons";
import { useDispatch } from "react-redux";
import {
  openAddPositionModal,
  closeAddPositionModal,
  applyEmployeePositionAsync,
} from "../../actions";
import { State as Details } from "../../details.reducer";
import { PositionForm } from "./PositionForm";
import { Positions } from "./Positions";

interface Props {
  canEdit: boolean;
  details: Details;
}

export const PositionsCard: React.FC<Props> = ({ canEdit, details }) => {
  const dispatch = useDispatch();
  const { employee, loading, errors, isAddPositionModalVisible } = details;

  const applyPosition = useCallback(
    (position: string) => {
      if (employee) {
        dispatch(
          applyEmployeePositionAsync.request({
            userId: employee.userId,
            position,
          }),
        );
      }
    },
    [dispatch, employee],
  );

  const openAddModal = useCallback(() => {
    dispatch(openAddPositionModal());
  }, [dispatch]);

  const closeAddModal = useCallback(() => {
    dispatch(closeAddPositionModal());
  }, [dispatch]);

  return (
    <>
      <Card
        title="Positions"
        className="human-resource-details__card"
        loading={loading}
        extra={canEdit ? <PlusOutlined onClick={openAddModal} /> : null}
      >
        {(employee?.positions?.length && (
          <Positions positions={employee.positions} />
        )) ||
          null}
      </Card>
      {canEdit && (
        <Modal
          title="Add Position"
          visible={isAddPositionModalVisible}
          onCancel={closeAddModal}
          footer={null}
          destroyOnClose={true}
        >
          <PositionForm errors={errors} applyPosition={applyPosition} />
        </Modal>
      )}
    </>
  );
};
