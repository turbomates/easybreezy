import React from "react";
import { Card, Form } from "antd";
import { UserNote } from "HumanResourceModels";
import Paragraph from "antd/lib/typography/Paragraph";

interface Props {
  notes: UserNote[];
  loading: boolean;
  canEdit: boolean;
}

export const NotesCart = (props: Props) => {
  const { notes, loading, canEdit } = props;

  return (
    <Card
      title="Notes"
      bordered={false}
      className="human-resource-details__card"
      loading={loading}
    >
      {notes.length && (
        <Form name="global_state" layout="vertical">
          {notes.map(note => (
            <Form.Item label={<span>{note.createdAt}</span>}>
              <Paragraph editable={canEdit}>{note.text}</Paragraph>
            </Form.Item>
          ))}
        </Form>
      )}
    </Card>
  );
};
