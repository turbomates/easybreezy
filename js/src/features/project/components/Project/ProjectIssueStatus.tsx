import React, { useEffect } from "react";
import { Button, Form, Input, Row, Col } from "antd";
import { CheckCircleOutlined, DeleteOutlined } from "@ant-design/icons";

import { ProjectStatuses } from "ProjectModels";
import {
  getMaxError,
  getMinError,
  getRequiredErrors,
  getUniqError,
} from "../../../../utils/errors";

import "./ProjectIssueStatus.scss";

type Props = {
  statuses: ProjectStatuses[];
  change: (name: string, statusId: string) => void;
  add: (name: string) => void;
  remove: (statusId: string) => void;
};

export const ProjectIssueStatus: React.FC<Props> = ({
  statuses,
  change,
  add,
  remove,
}) => {
  const [form] = Form.useForm();

  useEffect(() => {
    if (form && statuses.length > 0) {
      form.setFieldsValue({ statuses });
    } else if (form) {
      form.setFieldsValue({ statuses: [{ name: "" }] });
    }
  }, [statuses, form]);

  function onChange(index: number) {
    const field = form.getFieldValue(["statuses", index]);
    if (field.id) {
      change(field.name, field.id);
    } else {
      add(field.name);
    }
  }

  function onRemove(index: number) {
    const field = form.getFieldValue(["statuses", index]);

    if (field?.id) {
      remove(field.id);
    } else {
      const fields = form.getFieldValue("statuses");

      form.setFieldsValue({
        statuses: fields.filter(
          (field: { name?: string; id?: string }) => field?.id,
        ),
      });
    }
  }

  function isDisabled() {
    const fields = form.getFieldValue("statuses");

    return !form.getFieldValue(["statuses", fields?.length - 1, "id"]);
  }

  return (
    <Form form={form} className="issue-status">
      <Form.List name="statuses">
        {(fields, { add }) => (
          <div>
            {fields.map((field) => (
              <Row key={field.key}>
                <Col className="issue-status__input">
                  <Form.Item
                    name={[field.name, "name"]}
                    fieldKey={[field.fieldKey, "name"] as any}
                    rules={[
                      ...getRequiredErrors(),
                      getMinError(2),
                      getMaxError(25),
                      ({ getFieldValue }) =>
                        getUniqError(getFieldValue(["statuses"]), "name"),
                    ]}
                  >
                    <Input onPressEnter={() => onChange(field.fieldKey)} />
                  </Form.Item>
                </Col>
                <Col className="issue-status__btn">
                  <Button
                    onClick={() => onChange(field.fieldKey)}
                    type="primary"
                  >
                    <CheckCircleOutlined />
                  </Button>
                </Col>
                <Col className="issue-status__btn">
                  <Button
                    onClick={() => onRemove(field.fieldKey)}
                    type="danger"
                  >
                    <DeleteOutlined />
                  </Button>
                </Col>
              </Row>
            ))}

            <Form.Item>
              <Button
                type="primary"
                onClick={() => add()}
                disabled={isDisabled()}
              >
                Add
              </Button>
            </Form.Item>
          </div>
        )}
      </Form.List>
    </Form>
  );
};
