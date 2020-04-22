import React, { useCallback, useEffect, useMemo, useState } from "react";
import { Button, Form, Input, Checkbox } from "antd";
import { EditOutlined, DeleteOutlined } from "@ant-design/icons";

import { useFormServerErrors } from "hooks/useFormServerErrors";
import { FormErrorMap } from "MyTypes";
import {
  Project,
  EditProjectRoleRequest,
  RemoveProjectRoleRequest,
  CreateProjectRoleRequest,
} from "ProjectModels";

import "./ProjectRoleForm.scss";
import { ProjectRoleFormTableWrapper } from "./ProjectRoleFormTableWrapper";

interface Props {
  project: Project;
  create: (form: CreateProjectRoleRequest) => void;
  edit: (form: EditProjectRoleRequest) => void;
  remove: (form: RemoveProjectRoleRequest) => void;
  errors: FormErrorMap;
  loading: boolean;
}

export const ProjectRoleForm: React.FC<Props> = ({
  errors,
  create,
  edit,
  remove,
  project,
  loading,
}) => {
  // TODO: ADD PERMISSIONS WHEN ADD ENDPOINT
  const permissions = ["testing", "building", "scripting", "deploy"];
  const [form] = Form.useForm();

  const [permissionIndexField, setPermissionIndexField] = useState(-1);

  const initialValues = useMemo(
    () => ({
      roles: project.roles,
    }),
    [project],
  );

  useFormServerErrors(form, errors, ["name", "description"]);

  const onFinishFailed = useCallback((errorInfo: any) => {
    console.log("onFinishFailed:", errorInfo);
  }, []);

  function removeRole(index: number) {
    remove({
      roleId: project.roles[index].id,
      slug: project.slug,
    });
  }

  function createRole(index: number, value: string) {
    const fields = form.getFieldValue("roles");
    create({
      roles: {
        permissions: [value],
        name: fields[index].name,
      },
      slug: project.slug,
    });
  }

  function editRole(index: number) {
    const fields = form.getFieldValue("roles");

    if (!fields[index].permissions) return;

    edit({
      roles: fields[index],
      slug: project.slug,
    });
  }

  function editPermission(index: number, newPermission: string) {
    const role = project.roles[index];

    const permissions = role.permissions.includes(newPermission)
      ? role.permissions.filter((permission) => permission !== newPermission)
      : [...role.permissions, newPermission];

    edit({
      roles: {
        ...role,
        permissions,
      },
      slug: project.slug,
    });
  }

  function toggleCheckbox(index: number, permission: string) {
    const role = project.roles[index];

    if (!role) {
      createRole(index, permission);
      return;
    }

    editPermission(index, permission);
  }

  function isChecked(value: string, index: number) {
    return project.roles[index]?.permissions.some(
      (permission) => permission === value,
    );
  }

  function isDisabled(index: number) {
    return !form.getFieldValue("roles")[index];
  }

  function isOpenInput(index: number) {
    if (!!project.roles[index]) {
      return permissionIndexField === index;
    }
    return true;
  }

  useEffect(() => {
    form.setFieldsValue({ roles: project.roles });
    setPermissionIndexField(-1);
  }, [project.roles, form]);

  return (
    <Form
      form={form}
      onFinishFailed={onFinishFailed}
      initialValues={initialValues}
      className="role-form"
    >
      <Form.List name="roles">
        {(fields, { add, remove }) => {
          return (
            <ProjectRoleFormTableWrapper permissions={permissions}>
              <tbody>
                {fields.map((field, fieldIndex) => {
                  return (
                    <tr key={fieldIndex}>
                      {isOpenInput(fieldIndex) ? (
                        <td>
                          <div className="role-form__input-wrapper">
                            <Form.Item
                              name={[field.name, "name"]}
                              rules={[
                                {
                                  required: true,
                                  message: "Please input role!",
                                },
                              ]}
                              noStyle={true}
                            >
                              <Input
                                onPressEnter={() => editRole(fieldIndex)}
                              />
                            </Form.Item>
                          </div>
                        </td>
                      ) : (
                        <td>
                          <div className="role-form__input-wrapper">
                            <div className="role-form__description">
                              {project.roles[fieldIndex]?.name}
                            </div>
                            <div className="role-form__controls-btn">
                              <Button
                                onClick={() =>
                                  setPermissionIndexField(fieldIndex)
                                }
                                type="primary"
                                loading={loading}
                                className="role-form__controls-btn"
                              >
                                <EditOutlined />
                              </Button>

                              <Button
                                onClick={() => removeRole(fieldIndex)}
                                type="danger"
                                loading={loading}
                                className="role-form__controls-btn"
                              >
                                <DeleteOutlined />
                              </Button>
                            </div>
                          </div>
                        </td>
                      )}

                      {permissions.map((permission, index) => (
                        <td key={index}>
                          <Form.Item noStyle={true} shouldUpdate>
                            {() => {
                              return (
                                <Checkbox
                                  onChange={() =>
                                    toggleCheckbox(fieldIndex, permission)
                                  }
                                  checked={isChecked(permission, fieldIndex)}
                                  disabled={isDisabled(fieldIndex)}
                                />
                              );
                            }}
                          </Form.Item>
                        </td>
                      ))}
                    </tr>
                  );
                })}
              </tbody>
              <tfoot>
                <tr>
                  <td>
                    <Button onClick={() => add()} type="primary">
                      Add role
                    </Button>
                  </td>
                </tr>
              </tfoot>
            </ProjectRoleFormTableWrapper>
          );
        }}
      </Form.List>
    </Form>
  );
};
