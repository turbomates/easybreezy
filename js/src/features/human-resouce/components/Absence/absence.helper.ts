import { AbsenceForm, Absence } from "HumanResourceModels";
import parseISO from "date-fns/esm/parseISO";
import formatISO from "date-fns/esm/formatISO";

export const deserializeForm = (form: Absence) => {
  const startedAt = parseISO(form.startedAt);
  const endedAt = parseISO(form.endedAt);

  const range = [startedAt, endedAt];

  return {
    range,
    reason: form.reason,
    comment: form.comment,
  };
};

export const serializeForm = (form: any): AbsenceForm => ({
  startedAt: formatISO(form.range[0], { representation: "date" }),
  endedAt: formatISO(form.range[1], { representation: "date" }),
  reason: form.reason,
  comment: form.comment,
});

export const getAbsenceTitle = (absence: Absence) =>
  `${absence.startedAt} ${absence.endedAt}`;

export const getAbsenceDescription = (absence: Absence) =>
  `${absence.reason}: ${absence.comment}`;
