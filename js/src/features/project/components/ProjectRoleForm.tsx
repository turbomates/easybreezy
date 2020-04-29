import React, { useEffect, useMemo, useRef, useState } from "react";
import { Button, Input, Checkbox } from "antd";
import {
  EditOutlined,
  DeleteOutlined,
  CheckCircleOutlined,
  CloseCircleOutlined,
} from "@ant-design/icons";

import {
  Project,
  EditProjectRoleRequest,
  RemoveProjectRoleRequest,
  CreateProjectRoleRequest,
  RolePermissions,
} from "ProjectModels";
import { ProjectRoleFormTableWrapper } from "./ProjectRoleFormTableWrapper";

import "./ProjectRoleForm.scss";

interface Props {
  project: Project;
  create: (form: CreateProjectRoleRequest) => void;
  edit: (form: EditProjectRoleRequest) => void;
  remove: (form: RemoveProjectRoleRequest) => void;
  rolePermissions: RolePermissions;
}

interface FormField {
  id: string;
  name: string;
  permissions: string[];
}

interface Form {
  [key: string]: FormField;
}

const minLengthRoleName = 3;

export const ProjectRoleForm: React.FC<Props> = ({
  create,
  edit,
  remove,
  project,
  rolePermissions,
}) => {
  const initialForm = useMemo(
    () =>
      project.roles.reduce(
        (acc, role, index) => ({
          ...acc,
          [index]: role,
        }),
        {},
      ),
    [project.roles],
  );

  const [indexOfEditableInput, setIndexOfEditableInput] = useState(-1);
  const [indexOfEditableField, setIndexOfEditableField] = useState(-1);
  const [form, setForm] = useState<Form>(initialForm);

  const inputRef = useRef<Input>(null);

  function removeRole(index: number) {
    remove({
      roleId: project.roles[index].id,
      slug: project.slug,
    });
  }

  function toggleCheckbox(index: number, newPermission: string) {
    setIndexOfEditableField(index);

    const field = form[index];

    const permissions = field.permissions.includes(newPermission)
      ? field.permissions.filter((permission) => permission !== newPermission)
      : [...field.permissions, newPermission];

    setForm({
      ...form,
      [index]: {
        ...field,
        permissions: permissions,
      },
    });
  }

  function isChecked(value: string, index: number) {
    return form[index]?.permissions.some((permission) => permission === value);
  }

  function isDisabled(index: number) {
    return (
      form[index].name.length < minLengthRoleName || indexOfEditableInput !== -1
    );
  }

  function isOpenInput(index: number) {
    if (!!form[index].id) {
      return indexOfEditableInput === index;
    }
    return true;
  }

  function editInputRole(index: number) {
    const field = form[index];
    const value = inputRef.current!.state.value;

    setIndexOfEditableField(index);
    setIndexOfEditableInput(-1);

    setForm({
      ...form,
      [index]: {
        ...field,
        name: value,
      },
    });
  }

  function createRole(index: number, value: string) {
    const field = form[index];

    if (!!field.id) return;

    setForm({
      ...form,
      [index]: {
        ...field,
        name: value,
      },
    });
  }

  function addFormField() {
    const key = Object.keys(form).length;

    setForm({
      ...form,
      [key]: {
        name: "",
        permissions: [],
      },
    });
  }

  useEffect(() => {
    setForm(initialForm);
    setIndexOfEditableInput(-1);
    setIndexOfEditableField(-1);
  }, [initialForm]);

  useEffect(() => {
    if (indexOfEditableField !== -1 && indexOfEditableInput === -1) {
      const field = form[indexOfEditableField];
      const params = {
        roles: field,
        slug: project.slug,
      };

      if (!field.id && field.permissions.length > 0) {
        create(params);
      } else if (field.id) {
        edit(params);
      }

      setIndexOfEditableInput(-1);
      setIndexOfEditableField(-1);
    }
  }, [
    form,
    indexOfEditableField,
    create,
    edit,
    project.slug,
    indexOfEditableInput,
  ]);

  return (
    <ProjectRoleFormTableWrapper permissions={rolePermissions}>
      <tbody>
        {Object.values(form).map((field, fieldIndex) => {
          return (
            <tr key={fieldIndex}>
              {isOpenInput(fieldIndex) ? (
                <td>
                  <div className="role-form__input-wrapper">
                    <Input
                      defaultValue={field.name}
                      ref={inputRef}
                      onChange={(event) =>
                        createRole(fieldIndex, event.target.value)
                      }
                      className="role-form__input"
                    />
                    {!!field.id && (
                      <div className="role-form__controls-btn">
                        <Button
                          onClick={() => editInputRole(fieldIndex)}
                          type="primary"
                        >
                          <CheckCircleOutlined />
                        </Button>
                        <Button
                          onClick={() => setIndexOfEditableInput(-1)}
                          type="danger"
                        >
                          <CloseCircleOutlined />
                        </Button>
                      </div>
                    )}
                  </div>
                </td>
              ) : (
                <td>
                  <div className="role-form__input-wrapper">
                    <div className="role-form__description">{field.name}</div>
                    <div className="role-form__controls-btn">
                      <Button
                        onClick={() => setIndexOfEditableInput(fieldIndex)}
                        type="primary"
                        className="role-form__controls-btn"
                      >
                        <EditOutlined />
                      </Button>

                      <Button
                        onClick={() => removeRole(fieldIndex)}
                        type="danger"
                        className="role-form__controls-btn"
                      >
                        <DeleteOutlined />
                      </Button>
                    </div>
                  </div>
                </td>
              )}
              {rolePermissions.map((permission, index) => (
                <td key={index}>
                  <Checkbox
                    onChange={() => toggleCheckbox(fieldIndex, permission)}
                    checked={isChecked(permission, fieldIndex)}
                    disabled={isDisabled(fieldIndex)}
                  />
                </td>
              ))}
            </tr>
          );
        })}
      </tbody>
      <tfoot>
        <tr>
          <td>
            <Button onClick={addFormField} type="primary">
              Add role
            </Button>
          </td>
        </tr>
      </tfoot>
    </ProjectRoleFormTableWrapper>
  );
};
