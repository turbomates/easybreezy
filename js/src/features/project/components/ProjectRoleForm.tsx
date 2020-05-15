import React, { useEffect, useMemo, useState } from "react";
import { Button, Checkbox } from "antd";
import omitBy from "lodash/fp/omitBy";

import {
  Project,
  EditProjectRoleRequest,
  RemoveProjectRoleRequest,
  CreateProjectRoleRequest,
  RolePermissions,
} from "ProjectModels";
import { ProjectRoleFormTableWrapper } from "./ProjectRoleFormTableWrapper";
import { ProjectRoleFormEditRoleName } from "./ProjectRoleFormEditRoleName";
import { ProjectRoleFormViewRoleName } from "./ProjectRoleFormViewRoleName";

import "./ProjectRoleForm.scss";

type Props = {
  project: Project;
  create: (form: CreateProjectRoleRequest) => void;
  edit: (form: EditProjectRoleRequest) => void;
  remove: (form: RemoveProjectRoleRequest) => void;
  rolePermissions: RolePermissions;
}

export type FormField = {
  id: string;
  name: string;
  permissions: string[];
}

type Form = {
  [key: string]: FormField;
}

const minLengthRoleName = 2;

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
      form[index].name.length < minLengthRoleName ||
      index === indexOfEditableInput
    );
  }

  function isOpenInput(index: number) {
    if (!!form[index].id) {
      return indexOfEditableInput === index;
    }
    return true;
  }

  function editInputRole(index: number, value: string) {
    const field = form[index];

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

  function removeFormField(index: number) {
    const fields = omitBy((field, key) => parseFloat(key) === index, form);

    setForm(fields);
  }

  function isAddBtnDisabled() {
    return Object.values(form).some((field) => !field.id);
  }

  function closeInputRole(index: number) {
    if (form[index].id) {
      return setIndexOfEditableInput(-1);
    }

    removeFormField(index);
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

      if (field.id) {
        edit(params);
      } else {
        create(params);
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
                <ProjectRoleFormEditRoleName
                  editInputRole={editInputRole}
                  field={field}
                  fieldIndex={fieldIndex}
                  closeInputRole={() => closeInputRole(fieldIndex)}
                />
              ) : (
                <ProjectRoleFormViewRoleName
                  name={field.name}
                  removeRole={() => removeRole(fieldIndex)}
                  openInputRole={() => setIndexOfEditableInput(fieldIndex)}
                />
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
            <Button
              onClick={addFormField}
              type="primary"
              disabled={isAddBtnDisabled()}
            >
              Add role
            </Button>
          </td>
        </tr>
      </tfoot>
    </ProjectRoleFormTableWrapper>
  );
};
