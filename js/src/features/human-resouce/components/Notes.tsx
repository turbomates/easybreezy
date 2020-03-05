import React from "react";
import { Form } from "antd";
import { UserNote } from "HumanResourceModels";
import Paragraph from "antd/lib/typography/Paragraph";

interface Props {
  notes: UserNote[];
  canEdit: boolean;
}

export const Notes: React.FC<Props> = ({ notes, canEdit }) => {
  if (notes.length) return null;

  return (
    <Form layout="vertical">
      {notes.map(note => (
        <Form.Item key={note.id} label={<span>{note.createdAt}</span>}>
          <Paragraph editable={canEdit}>{note.text}</Paragraph>
        </Form.Item>
      ))}
    </Form>
  );
};
