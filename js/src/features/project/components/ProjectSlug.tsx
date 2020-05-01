import React from "react";
import { Button } from "antd";

interface Props {
  slug?: string;
  openProjectSlugForm: (open: boolean) => void;
}

export const ProjectSlug: React.FC<Props> = ({ slug, openProjectSlugForm }) => {
  return (
    <div>
      <p>{slug}</p>
      <Button type="primary" onClick={() => openProjectSlugForm(true)}>
        Edit
      </Button>
    </div>
  );
};
