import React, { useCallback } from "react";
import { Card, Modal } from "antd";
import { PlusOutlined } from "@ant-design/icons";
import { useDispatch } from "react-redux";
import {
  addEmployeeNoteAsync,
  openCreateNoteModal,
  closeCreateNoteModal,
} from "../../actions";
import { State as Details } from "../../details.reducer";
import { NoteForm } from "./NoteForm";
import { Notes } from "./Notes";

interface Props {
  canEdit: boolean;
  details: Details;
}

export const NotesCard: React.FC<Props> = ({ canEdit, details }) => {
  const dispatch = useDispatch();
  const { employee, loading, isCreateNoteModalVisible, errors } = details;

  const addNote = useCallback(
    (text: string) => {
      if (employee) {
        dispatch(
          addEmployeeNoteAsync.request({
            userId: employee.userId,
            text,
          }),
        );
      }
    },
    [dispatch, employee],
  );

  const openCreateModal = useCallback(() => {
    dispatch(openCreateNoteModal());
  }, [dispatch]);

  const closeCreateModal = useCallback(() => {
    dispatch(closeCreateNoteModal());
  }, [dispatch]);

  return (
    <>
      <Card
        title="Notes"
        className="human-resource-details__card"
        loading={loading}
        extra={canEdit ? <PlusOutlined onClick={openCreateModal} /> : null}
      >
        {canEdit && <Notes notes={employee?.notes || []} canEdit={false} />}
      </Card>
      <Modal
        title="Add Note"
        visible={isCreateNoteModalVisible}
        onCancel={closeCreateModal}
        footer={null}
      >
        <NoteForm addNote={addNote} errors={errors} />
      </Modal>
    </>
  );
};
